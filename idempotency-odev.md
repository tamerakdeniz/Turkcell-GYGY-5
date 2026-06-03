# Idempotency - Araştırma Ödevi

> Aynı işlemi birden fazla kez denediğimizde sistemin sonucu bozmadan aynı kalmasını sağlayan yaklaşımdır. Özellikle ödeme, sipariş, rezervasyon ve API retry senaryolarında kritik hale gelir.

---

## 1. Idempotency Nedir?

Idempotency, bir işlemin **bir kez çalışması ile birden fazla kez çalışmasının sistemde aynı sonucu üretmesi** demektir.

Basit örnek:

```text
DELETE /users/5
```

Bu istek ilk çalıştığında kullanıcıyı siler. Aynı istek tekrar gelirse kullanıcı zaten silinmiştir; sonuç yine "kullanıcı artık yok" durumudur. Bu yüzden `DELETE` genelde idempotent kabul edilir.

Ama şu istek idempotent değildir:

```text
POST /orders
```

Aynı sipariş isteği iki kez gelirse iki ayrı sipariş oluşabilir. Kullanıcı tek kez "Satın al" butonuna bastığını sanırken sistemde iki sipariş oluşması ciddi bir problemdir.

---

## 2. Günlük Hayattan Örnek

Bir kafede kasiyere "bir kahve" dedin. Kasiyer duymadığı için tekrar sordun, sen de aynı cümleyi tekrar ettin.

- İyi sistem: "Bu müşteri zaten bir kahve söyledi" der ve tek kahve hazırlar.
- Kötü sistem: Her tekrarını yeni sipariş sanır ve iki kahve hazırlar.

Idempotency, yazılımda bu "aynı isteği tekrar ettim ama yeni işlem oluşmasın" mantığıdır.

---

## 3. Neden Gerekli?

Gerçek sistemlerde aynı istek birden fazla kez gelebilir:

- Kullanıcının butona arka arkaya basması
- Mobil internetin kopup tekrar denemesi
- Tarayıcının isteği tekrar göndermesi
- Backend servisinin timeout alıp retry yapması
- Mesaj kuyruğundaki mesajın tekrar tüketilmesi

Bu durumlar normaldir. Önemli olan sistemin bu tekrarları güvenli yönetmesidir.

---

## 4. HTTP Metodlarında Idempotency

| Metod | Idempotent mi? | Açıklama |
|---|:---:|---|
| `GET` | Evet | Veri okur, sistem durumunu değiştirmez. |
| `PUT` | Evet | Kaynağı aynı veriyle tekrar güncellemek sonucu değiştirmez. |
| `DELETE` | Evet | Kaynak zaten silindiyse tekrar silme sonucu değiştirmez. |
| `POST` | Genelde hayır | Her çağrıda yeni kaynak oluşturabilir. |
| `PATCH` | Duruma bağlı | `stok = 10` idempotent olabilir, `stok = stok - 1` değildir. |

Önemli nokta: Bir endpoint'in HTTP metoduna bakmak tek başına yeterli değildir. Asıl karar, endpoint'in sistem durumunu nasıl değiştirdiğine göre verilir.

---

## 5. Yazılımda Nasıl Sağlanır?

### 5.1 Idempotency Key Kullanımı

Client, her kritik işlem için benzersiz bir anahtar gönderir:

```http
POST /payments
Idempotency-Key: 8f2b4e1d-9a7c-4c51-91f5-0a771c1a4d21
Content-Type: application/json

{
  "orderId": 42,
  "amount": 500
}
```

Backend bu anahtarı kaydeder. Aynı key tekrar gelirse yeni ödeme oluşturmak yerine önceki sonucu döner.

Akış:

1. İlk istek gelir.
2. `Idempotency-Key` veritabanında yoksa işlem yapılır.
3. İşlem sonucu key ile birlikte saklanır.
4. Aynı key tekrar gelirse işlem tekrar yapılmaz, saklanan sonuç döner.

### 5.2 Unique Constraint Kullanımı

Veritabanı seviyesinde tekrarları engellemek en güvenli yöntemlerden biridir.

```sql
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    idempotency_key VARCHAR(100) NOT NULL UNIQUE,
    amount NUMERIC(10, 2) NOT NULL,
    status VARCHAR(30) NOT NULL
);
```

Uygulama hata yapsa bile DB aynı `idempotency_key` ile ikinci kaydı kabul etmez.

### 5.3 Doğal Idempotency

Bazı işlemler zaten doğal olarak idempotent tasarlanabilir:

```http
PUT /users/10/email

{
  "email": "ali@example.com"
}
```

Bu istek 1 kez de çalışsa 5 kez de çalışsa kullanıcının e-postası aynı değere set edilir.

Ama şu işlem idempotent değildir:

```http
POST /wallets/10/increase

{
  "amount": 100
}
```

Her tekrar cüzdan bakiyesini 100 TL daha artırır.

---

## 6. Spring Boot Tarzı Basit Örnek

```java
@PostMapping("/payments")
public ResponseEntity<PaymentResponse> createPayment(
        @RequestHeader("Idempotency-Key") String idempotencyKey,
        @RequestBody CreatePaymentRequest request) {

    Optional<Payment> existingPayment =
            paymentRepository.findByIdempotencyKey(idempotencyKey);

    if (existingPayment.isPresent()) {
        return ResponseEntity.ok(toResponse(existingPayment.get()));
    }

    Payment payment = new Payment();
    payment.setOrderId(request.orderId());
    payment.setAmount(request.amount());
    payment.setIdempotencyKey(idempotencyKey);
    payment.setStatus("SUCCESS");

    Payment savedPayment = paymentRepository.save(payment);
    return ResponseEntity.status(201).body(toResponse(savedPayment));
}
```

Bu örnekte aynı `Idempotency-Key` tekrar gelirse yeni ödeme oluşturulmaz.

Not: Gerçek projede bu kod transaction içinde çalışmalı ve DB'de `idempotency_key` için unique constraint olmalıdır. Sadece `findBy...` kontrolü yeterli değildir; iki istek aynı anda gelirse yarış durumu oluşabilir.

---

## 7. Sık Yapılan Hatalar

1. **Sadece frontend'e güvenmek.** Butonu disable etmek iyidir ama yeterli değildir. Aynı istek backend'e yine gelebilir.
2. **Unique constraint koymamak.** Uygulama kontrolü tek başına race condition'a açık kalır.
3. **Aynı key ile farklı payload kabul etmek.** Aynı key farklı tutar veya farklı siparişle gelirse sistem bunu conflict olarak ele almalıdır.
4. **Key'leri sonsuza kadar tutmak.** Idempotency kayıtları belirli süre sonra temizlenmelidir.
5. **Her şeyi idempotent yapmaya çalışmak.** Her işlem için gerekmez; ödeme, sipariş, rezervasyon gibi kritik işlemlerde önceliklidir.

---

## 8. Kısa Özet

Idempotency, tekrar eden isteklerin sistemi bozmasını engeller. Kullanıcı, ağ veya servis kaynaklı retry'lar normaldir; sistem bunları güvenli karşılamalıdır. En pratik çözüm, kritik `POST` işlemlerinde `Idempotency-Key` kullanmak ve bunu veritabanında unique constraint ile desteklemektir.

