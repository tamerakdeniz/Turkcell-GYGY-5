# Pipeline Behaviors (CQRS / Mediator) — Araştırma Notları

> Bir developer'ın CQRS + Mediator pattern üzerinde çalışırken **bilmesi şart** olan başlıkları, günlük hayattan örneklerle ve mevcut iki projemizdeki (`library-cqrs`, `spring-cqrs`) kullanıma referansla anlatır.

---

## 1. Pipeline Behavior Nedir?

Bir **Pipeline Behavior**, bir Command/Query'nin gerçek `Handler`'ına ulaşmadan önce ve sonra çalışan ara katmandır. Mediator deseninin "decorator zinciri" olarak düşünebilirsin.

Akış (örnek sıralama ile):

```
Controller -> Mediator.send(command)
              │
              ├─ AuthorizationBehavior        (Order = 10)   ┐
              │   ├─ LoggingBehavior          (Order = 20)   │  öncesi
              │   │   ├─ PerformanceBehavior  (Order = 30)   │
              │   │   │   ├─ TransactionBehavior (Order=40)  │
              │   │   │   │   ├─ Handler.handle(command)
              │   │   │   │   └─ commit / rollback           │
              │   │   │   └─ elapsed > 3000ms ise warn       │  sonrası
              │   │   └─ response log                        │
              │   └─                                         ┘
              └─ response döner
```

### Günlük hayat örneği — Havalimanı kontrolleri

Bir uçağa binmeden önce sırayla:
1. **Pasaport kontrolü** (Authorization) — yetkili misin?
2. **Check-in kaydı** (Logging) — kim, hangi uçuş, kaç bagaj?
3. **Süre kontrolü** (Performance) — kuyrukta 3 saatten fazla mı bekledin? Yöneticiye bildir.
4. **Bagaj etiketleme** (Transaction) — hepsi etiketlendi ya da hiçbiri etiketlenmedi.
5. **Uçağa biniş** (Handler).

Ardından dönüşte **aynı kapılardan ters yönde** geçersin. Pipeline behavior tam olarak böyle çalışır: dışa doğru ne sırayla girersen, çıkarken o sırayla çıkarsın.

---

## 2. Behavior Sıralaması (`@Order`) Neden Önemli?

Bir behavior'ın hangi noktada koşacağı, `@Order` değeri ile belirlenir. Küçük değer önce çalışır (en dışta sarmalanır).

| Order | Behavior              | Görev                                   |
|-------|-----------------------|-----------------------------------------|
| 10    | AuthorizationBehavior | Yetkisiz isteği erkenden kes            |
| 20    | LoggingBehavior       | Request ve response log                 |
| 30    | PerformanceBehavior   | İçerideki tüm zinciri ölç (>3000ms)     |
| 40    | TransactionBehavior   | Handler'a en yakın → commit/rollback    |

### Neden bu sıra?

- **Authorization en başta**: Yetkisiz kullanıcının logu bile tutulmadan kapı kapanmalı.
- **Logging Authorization sonrası**: "Kim, ne istedi?" sadece yetkili request için anlamlı.
- **Performance, Transaction'ı dışarıdan ölçer**: COMMIT/ROLLBACK süresi de toplam süreye dahil olsun ki gerçek "kullanıcı bekleme süresini" görelim.
- **Transaction handler'a en yakın**: Transaction çok uzun açık kalırsa DB lock'ları büyür. Logging ve Auth gibi hızlı işleri transaction dışında bırakırız.

> ⚠️ Yanlış sıralama, susturulmuş bug'ların kaynağıdır: Performance, Transaction'ın **içinde** olsa, commit'i ölçmez. Transaction, Authorization'dan **önce** olsa, yetkisiz isteğe boşuna transaction açarsın.

### Günlük hayat örneği — Restoran sırası

Bir restorana girdiğinde:
1. Önce **rezervasyonu doğrularlar** (Authorization).
2. Sonra **sipariş alınır ve mutfağa not gider** (Logging).
3. **Servis süresi takip edilir** (Performance — masada 1 saatten fazla bekledinse şikayet sistemi tetiklenir).
4. **Hesap aç → tüm yemekler/içecekler eklenir → tek seferde öde** (Transaction).
5. **Yemeği yersin** (Handler).

Sıra bozulursa: Önce sipariş alıp sonra "kapasitemiz yok" demek = saçma → yetki kontrolü en başta olmalı.

---

## 3. Behavior Türleri ve Kullanım Senaryoları

### 3.1 AuthorizationBehavior

JWT veya rol bazlı kontrol. Request'in `RequiresRole` gibi bir marker arayüzü var mı diye bakar; varsa içerideki rol claim'lerine kıyaslar.

**Günlük hayat:** Kapıdaki güvenlikçi. Kart okutmadan içeri sokmaz.

```java
@Order(10)
public class AuthorizationBehavior implements PipelineBehavior {
    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
        // jwt + rol kontrolü
        return next.invoke();
    }
}
```

### 3.2 LoggingBehavior

Request ve response'u tek tek konsola/log sistemine yazar. Bu projede şöyle yaptık:

```java
public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
    String name = request.getClass().getSimpleName();
    System.out.println("[LOG][REQUEST] " + name + " -> " + request);
    R response = next.invoke();
    System.out.println("[LOG][RESPONSE] " + name + " -> " + response);
    return response;
}
```

**Hassas veri uyarısı:** `LoginCommand` gibi şifre içeren request'ler **loglanmamalı**. Bu yüzden `NotLoggableRequest` marker arayüzü vardır:

```java
public record LoginCommand(String email, String password) implements Command<LoginResponse>, NotLoggableRequest {}
```

`supports(...)` içinde tipini kontrol ederiz:

```java
public boolean supports(Object request) {
    return !(request instanceof NotLoggableRequest);
}
```

**Günlük hayat:** Bankadaki hesap hareketleri dökümü. Her işlem (request) ve sonucu (response) kayda alınır; ama PIN dökülmez.

### 3.3 PerformanceBehavior

İstek belirli bir süreyi aşarsa konsola uyarı düşer.

```java
@Order(30)
public class PerformanceBehavior implements PipelineBehavior {
    private static final long THRESHOLD_MS = 3000L;
    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
        long start = System.currentTimeMillis();
        try {
            return next.invoke();
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            if (elapsed > THRESHOLD_MS) {
                System.out.println("[PERF][WARN] " + request.getClass().getSimpleName()
                                   + " yavaş çalıştı: " + elapsed + " ms");
            }
        }
    }
}
```

Neden `try/finally`? **Hata fırlasa bile** ölçüm bitmeli. Aksi halde "yavaş + patladı" durumunda hiç ölçü alamayız.

**Günlük hayat:** Pizza siparişi 30 dakikada gelmezse bedava. Süreyi tutman, sipariş başarılı gelse de gelmese de gerekir.

### 3.4 TransactionBehavior

Bütünlük sağlamak için (ACID — **A**tomicity). Birden fazla DB değişikliği yapan komutlar için ya hepsi olsun ya da hiçbiri.

```java
@Order(40)
public class TransactionBehavior implements PipelineBehavior {
    private final TransactionTemplate tx;

    public boolean supports(Object request) {
        return request instanceof Command<?>; // Query'lere uygulama
    }

    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
        try {
            R result = tx.execute(status -> next.invoke());
            System.out.println("[TX] COMMIT");
            return result;
        } catch (RuntimeException ex) {
            System.out.println("[TX] ROLLBACK -> " + ex.getMessage());
            throw ex;
        }
    }
}
```

#### Neden sadece Command?
- `Query` salt okuma; lock/commit gereksiz, performansı yer.
- `Command` veri değiştirir; tutarlılık için transaction zorunlu.

#### `@Transactional` yerine neden `TransactionTemplate`?
- `@Transactional` Spring AOP proxy'sine bağımlıdır; bizim Mediator zinciri kendi başına bir lambda/delegate çağrısı olduğundan proxy devreye girmez.
- `TransactionTemplate` programatik olarak çalışır → behavior içinden direkt yönetilir, sürpriz yok.

#### Günlük hayat — Para transferi

Hesap A'dan -100 TL düşüldü, sunucu çöktü, hesap B'ye +100 TL yazılmadı → **para buharlaştı**. Transaction olmadan banka çöker. Ya ikisi olsun ya hiçbiri.

---

## 4. ACID Konusu (Transaction'ı anlamak için must)

| Harf | Anlam            | Günlük örnek                                             |
|------|------------------|----------------------------------------------------------|
| A    | **Atomicity**    | Para transferi: ikisi de olsun ya da hiçbiri.            |
| C    | **Consistency**  | Stoğu negatife düşürme; iş kuralları DB sonrasında geçerli kalsın. |
| I    | **Isolation**    | İki kullanıcı son koltuğu aynı anda alamasın.            |
| D    | **Durability**   | Onaylanmış sipariş elektrik kesilse bile kayıp olmasın.  |

Pipeline'daki TransactionBehavior özellikle **A** ve kısmen **I** ile ilgilidir.

### Isolation Levels (kısa)
- `READ_UNCOMMITTED` → kirli okuma. Genelde **kullanma**.
- `READ_COMMITTED` → varsayılan. Çoğu CRUD için yeter.
- `REPEATABLE_READ` → aynı sorgu sürekli aynı sonucu verir.
- `SERIALIZABLE` → en güvenli, en yavaş. Banka/finans için.

### Propagation (kısa)
- `REQUIRED` (varsayılan) → varsa katıl, yoksa aç.
- `REQUIRES_NEW` → her zaman yeni transaction (audit log gibi).
- `NESTED` → savepoint mantığı.

---

## 5. Sık Yapılan Hatalar (Pitfalls)

1. **Loglarda hassas veri.** Şifre, JWT, kart numarası loglanmamalı. → `NotLoggableRequest` veya alan maskeleme.
2. **Transaction içinde uzun iş.** Dış API çağrısı transaction içinden yapılırsa DB lock uzar. → API çağrısını transaction dışına taşı veya outbox pattern kullan.
3. **Sıra hatası.** Performance behavior, transaction'ın içinde kalırsa commit süresini ölçemez. → `@Order` değerlerini doğru ayarla.
4. **Query'ye transaction.** Read-only sorgulara TX açmak gereksiz overhead. → `supports(...)` ile filtrele.
5. **`catch (Exception)` ile yutma.** Exception yutarsan transaction commit olur, oysa rollback gerekir. → `RuntimeException`'ı yeniden fırlat.
6. **Exception'ı dönüştürmek.** `try/catch` ile checked exception'a çevirirsen Spring rollback'i atlayabilir. → `@Transactional(rollbackFor = ...)` veya runtime fırlat.
7. **Behavior `@Component` değil.** Bean değilse Spring listeye almaz, pipeline'a eklenmez. → `@Component` koy ve `@Order` ver.

---

## 6. Test Edilebilirlik

Pipeline behavior'ları unit test etmek kolaydır çünkü `RequestHandlerDelegate<R>` aslında bir `Supplier<R>`. Mock'layabilirsin:

```java
@Test
void perfBehavior_yavasIsiUyarir() {
    var behavior = new PerformanceBehavior();
    var slow = (RequestHandlerDelegate<String>) () -> {
        Thread.sleep(3500);
        return "ok";
    };
    behavior.handle(new SomeCommand(), slow); // log'da [PERF][WARN] görmeli
}
```

---

## 7. Kontrol Listesi (Code Review)

- [ ] Behavior `@Component` ve `@Order(...)` ile işaretli mi?
- [ ] `supports(...)` doğru request tiplerini filtreliyor mu?
- [ ] `next.invoke()` çağrısı `try/finally` veya `try/catch` ile sarılı mı?
- [ ] Hassas request `NotLoggableRequest` marker'ına sahip mi?
- [ ] TransactionBehavior sadece `Command` için aktif mi?
- [ ] Ölçüm (Performance) Transaction'ın **dışında** mı kalıyor?
- [ ] Logger gerçek logger (SLF4J) mi yoksa `System.out` mu? (Prod için SLF4J önerilir.)

---

## 8. Bu Projedeki Dosya Yolları (Hızlı Referans)

`library-cqrs` ve `spring-cqrs` aynı yapıdadır. Sadece kök paket farklıdır.

```
core/
├── mediator/
│   ├── pipeline/
│   │   ├── PipelineBehavior.java
│   │   └── RequestHandlerDelegate.java
│   └── SpringMediator.java          ← behavior zincirini kuran sınıf
├── security/authorization/
│   └── AuthorizationBehavior.java   @Order(10)
├── logging/
│   ├── LoggingBehavior.java         @Order(20)
│   └── NotLoggableRequest.java
├── performance/
│   └── PerformanceBehavior.java     @Order(30)
└── transaction/
    └── TransactionBehavior.java     @Order(40)
```

---

## 9. Kısa Özet

- **Behavior = Decorator katmanı.** Aynı işin tüm command/query'lerde tekrar etmesini önler.
- **`@Order` zincirin sırasını belirler**; outer-to-inner: Auth → Log → Perf → TX → Handler.
- **Logging** request + response'u ayrı ayrı yazsın; hassas veri için marker arayüz kullan.
- **Performance** eşik aşımında konsola uyarı düşürsün; her zaman `try/finally` ile.
- **Transaction** sadece Command'ı sarsın; programatik (`TransactionTemplate`) güvenli yoldur.
- **Hatalar** sessizce yutulmasın; pipeline'da exception fırlamak rollback'i tetikler.
