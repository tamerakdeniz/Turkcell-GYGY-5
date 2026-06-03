# Transactional Outbox Pattern - Araştırma Ödevi

> Bir işlem sırasında hem veritabanını güncellemek hem de dış dünyaya mesaj göndermek gerekiyorsa, bu iki adımı güvenli hale getirmek için kullanılan pattern'dir.

---

## 1. Problem Nedir?

Bir sipariş oluşturduğumuzu düşünelim. Backend genelde iki şey yapmak ister:

1. Siparişi veritabanına kaydetmek.
2. Kargo, fatura veya bildirim servisine "sipariş oluşturuldu" mesajı göndermek.

Kod kabaca şöyle görünebilir:

```java
orderRepository.save(order);
messageBroker.publish("OrderCreated", order.getId());
```

Bu basit görünür ama arada kritik bir risk vardır.

### Riskli Senaryolar

| Durum | Sonuç |
|---|---|
| DB kaydı başarılı, mesaj gönderimi başarısız | Sipariş var ama diğer servislerin haberi yok. |
| Mesaj gönderildi, DB commit başarısız | Diğer servisler aslında olmayan bir siparişi işleyebilir. |
| Mesaj gönderildi ama servis timeout aldı | Mesaj gitti mi gitmedi mi emin olunamaz. |

Bu probleme genelde **dual-write problemi** denir: Aynı anda iki farklı sisteme yazmaya çalışırsın, ama ikisini tek transaction gibi yönetemezsin.

---

## 2. Günlük Hayattan Örnek

Bir restoranda sipariş aldığını düşün.

Normalde iki şey yapılmalı:

1. Sipariş kasa sistemine kaydedilmeli.
2. Mutfak fişi çıkarılmalı.

Eğer kasa kaydı oluşup mutfak fişi çıkmazsa müşteri bekler ama yemek hazırlanmaz. Eğer mutfağa fiş gidip kasa kaydı oluşmazsa yemek hazırlanır ama sistemde sipariş görünmez.

Transactional Outbox, "önce siparişi ve mutfağa gönderilecek fişi aynı deftere yaz, sonra görevli bu fişleri mutfağa tek tek ulaştırsın" yaklaşımıdır.

---

## 3. Transactional Outbox Pattern Nedir?

Transactional Outbox Pattern'de uygulama, dış sisteme mesajı doğrudan göndermek yerine önce kendi veritabanındaki bir **outbox tablosuna** yazar.

Aynı transaction içinde:

1. Ana veri kaydedilir. Örneğin `orders`.
2. Gönderilecek event outbox tablosuna eklenir. Örneğin `outbox_events`.
3. Transaction commit olursa ikisi birlikte kalıcı olur.

Sonra ayrı bir worker veya scheduler outbox tablosunu okur, mesaj broker'a gönderir ve kaydı gönderildi olarak işaretler.

---

## 4. Temel Akış

```text
Client
  |
  v
Order API
  |
  |-- DB Transaction ----------------------
  |   1. orders tablosuna sipariş yaz
  |   2. outbox_events tablosuna event yaz
  |----------------------------------------
  |
  v
Outbox Worker
  |
  v
Message Broker
  |
  v
Kargo / Fatura / Bildirim Servisi
```

Buradaki ana fikir şudur: Uygulama transaction içinde dış sisteme gitmez. Sadece kendi DB'sine yazar. Dış sisteme gönderme işi transaction bittikten sonra ayrı ve tekrar denenebilir bir süreçle yapılır.

---

## 5. Basit Tablo Tasarımı

```sql
CREATE TABLE outbox_events (
    id UUID PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(100) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(30) NOT NULL,
    retry_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    sent_at TIMESTAMP NULL,
    last_error TEXT NULL
);
```

Alanların anlamı:

| Alan | Açıklama |
|---|---|
| `id` | Event'in benzersiz kimliği. Consumer tarafında idempotency için de kullanılır. |
| `aggregate_type` | Event'in hangi varlığa ait olduğu. Örneğin `Order`. |
| `aggregate_id` | İlgili varlığın id değeri. Örneğin sipariş id'si. |
| `event_type` | Event türü. Örneğin `OrderCreated`. |
| `payload` | Mesaj içeriği. Genelde JSON tutulur. |
| `status` | `PENDING`, `SENT`, `FAILED` gibi durum bilgisi. |
| `retry_count` | Gönderim kaç kez denenmiş bilgisidir. |
| `created_at` | Event'in ne zaman oluştuğu. |
| `sent_at` | Broker'a ne zaman gönderildiği. |
| `last_error` | Son gönderim hatasının kısa açıklamasıdır. |

---

## 6. Spring Boot Tarzı Basit Örnek

Sipariş oluştururken event'i de aynı transaction içinde kaydetmek:

```java
@Transactional
public OrderResponse createOrder(CreateOrderRequest request) {
    Order order = new Order();
    order.setCustomerId(request.customerId());
    order.setTotalPrice(request.totalPrice());

    Order savedOrder = orderRepository.save(order);

    OutboxEvent event = new OutboxEvent();
    event.setId(UUID.randomUUID());
    event.setAggregateType("Order");
    event.setAggregateId(savedOrder.getId().toString());
    event.setEventType("OrderCreated");
    event.setPayload("""
            {"orderId": "%s", "totalPrice": %s}
            """.formatted(savedOrder.getId(), savedOrder.getTotalPrice()));
    event.setStatus("PENDING");
    event.setRetryCount(0);
    event.setCreatedAt(LocalDateTime.now());

    outboxEventRepository.save(event);

    return toResponse(savedOrder);
}
```

Worker tarafı:

```java
@Scheduled(fixedDelay = 5000)
public void publishPendingEvents() {
    List<OutboxEvent> events = outboxEventRepository.findTop100ByStatus("PENDING");

    for (OutboxEvent event : events) {
        try {
            messageBroker.publish(event.getEventType(), event.getPayload());
            event.setStatus("SENT");
            event.setSentAt(LocalDateTime.now());
            outboxEventRepository.save(event);
        } catch (RuntimeException ex) {
            event.setRetryCount(event.getRetryCount() + 1);
            event.setLastError(ex.getMessage());

            if (event.getRetryCount() >= 5) {
                event.setStatus("FAILED");
            }

            outboxEventRepository.save(event);
        }
    }
}
```

Bu örnek sade tutulmuştur. Gerçek sistemde batch lock, retry sayısı, hata detayı ve eş zamanlı worker kontrolü ayrıca düşünülmelidir.

---

## 7. Bu Pattern Ne Kazandırır?

- DB kaydı ve event kaydı aynı transaction içinde tutulur.
- Mesaj gönderimi başarısız olursa event kaybolmaz.
- Worker daha sonra tekrar deneyebilir.
- Dış servis geçici olarak kapalı olsa bile ana işlem kaybolmaz.
- Mikroservisler arasında veri tutarlılığı daha yönetilebilir olur.

---

## 8. Dikkat Edilmesi Gerekenler

### 8.1 Consumer Idempotent Olmalı

Outbox mesajı bazı durumlarda broker'a birden fazla kez gidebilir. Bu yüzden mesajı tüketen servis aynı event'i ikinci kez aldığında sistemi bozmamalıdır.

Örneğin fatura servisi `event.id` değerini daha önce işlediyse tekrar fatura oluşturmamalıdır.

### 8.2 Worker Aynı Event'i İki Kez Almamalı

Birden fazla worker çalışıyorsa aynı `PENDING` event'i aynı anda seçebilirler. Bunun için DB lock, `SKIP LOCKED`, status geçişi veya benzeri yöntemler kullanılır.

### 8.3 Outbox Tablosu Temizlenmeli

Başarıyla gönderilmiş event'ler sonsuza kadar tabloda tutulursa tablo büyür. Belirli süre sonra arşivleme veya silme yapılmalıdır.

### 8.4 Transaction İçinde Dış API Çağrısı Yapılmamalı

Outbox'ın amacı zaten dış sisteme gitmeyi transaction dışına almaktır. Transaction içinde broker, e-posta, ödeme sağlayıcı veya başka HTTP servisleri çağırmak DB lock süresini uzatır.

---

## 9. Idempotency ile İlişkisi

Transactional Outbox ve Idempotency genelde birlikte düşünülür.

- Outbox, event'in kaybolmamasını sağlar.
- Idempotency, aynı event tekrar işlense bile yanlış sonuç oluşmamasını sağlar.

Örnek:

1. Sipariş oluşturuldu.
2. `OrderCreated` event'i outbox'a yazıldı.
3. Worker mesajı broker'a gönderdi ama timeout aldı.
4. Worker emin olamadığı için tekrar gönderdi.
5. Fatura servisi aynı `event.id` değerini daha önce işlediyse ikinci faturayı oluşturmaz.

Bu yüzden outbox kullanan sistemlerde consumer tarafında idempotency şarttır.

---

## 10. Kısa Özet

Transactional Outbox Pattern, veritabanı değişikliği ile mesaj gönderimini güvenli hale getirir. Mesajı doğrudan broker'a göndermek yerine önce aynı transaction içinde outbox tablosuna yazarız. Sonra ayrı bir worker bu kayıtları gönderir. Böylece mesaj kaybolmaz, retry yapılabilir ve servisler arası tutarlılık daha sağlıklı yönetilir.
