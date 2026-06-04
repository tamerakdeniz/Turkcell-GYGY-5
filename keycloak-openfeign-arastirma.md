# Keycloak ile Auth ve OpenFeign ile Sync İletişim — Araştırma Notları

> Mikroservis veya REST API geliştirirken sık karşılaşılan iki konu: **kullanıcı kimliğini/yetkisini merkezi yönetmek** ve **servislerin birbirini HTTP üzerinden senkron çağırması**. Bu not; Keycloak, JWT, Spring Security Resource Server ve OpenFeign kavramlarını günlük hayat örnekleriyle açıklar.

---

## 1. Büyük Resim

Modern backend sistemlerinde genelde şu akış vardır:

```text
Kullanıcı / Frontend
  |
  | 1. Giriş yapar
  v
Keycloak
  |
  | 2. Access Token verir
  v
Frontend
  |
  | 3. Authorization: Bearer <token>
  v
Order API
  |
  | 4. Token doğrulanır
  | 5. Gerekirse OpenFeign ile başka servis çağrılır
  v
Payment API / User API / Stock API
```

Bu resimde:

| Parça | Görev |
|---|---|
| **Keycloak** | Kullanıcı, rol, client, token ve login akışını yönetir. |
| **JWT / Access Token** | Kullanıcının kimliğini ve yetkilerini API'ye taşır. |
| **Spring Security Resource Server** | Gelen token'ı doğrular, kullanıcıyı ve rollerini çıkarır. |
| **OpenFeign** | Bir Spring Boot servisinden başka bir servise tip güvenli HTTP çağrısı yapar. |

**Günlük hayat örneği:**
Bir şirket binasına girmek için resepsiyondan kart alırsın. Bu kartta kim olduğun ve hangi katlara çıkabileceğin yazar. Güvenlik görevlisi kartı kontrol eder. İçeri girdikten sonra farklı departmanlara gidip iş yaptırırsın. Keycloak resepsiyon, JWT giriş kartı, API güvenlik görevlisi, OpenFeign ise departmanlar arası telefon görüşmesi gibidir.

---

## 2. Auth, Authentication ve Authorization

### Authentication Nedir?

Authentication, "Sen kimsin?" sorusunun cevabıdır.

Örnek:

- E-posta + şifre ile giriş yapmak
- SMS kodu doğrulamak
- Google hesabı ile login olmak

### Authorization Nedir?

Authorization, "Bu işlemi yapmaya yetkin var mı?" sorusunun cevabıdır.

Örnek:

- Normal kullanıcı sadece kendi siparişini görür.
- Admin bütün siparişleri görür.
- Muhasebe kullanıcısı fatura ekranına erişir ama ürün silemez.

| Kavram | Soru | Günlük örnek |
|---|---|---|
| Authentication | Kimsin? | Kimlik kartını göstermek |
| Authorization | Ne yapabilirsin? | O kimlikle hangi kapıdan geçebileceğin |

> **NOT:** Login olmak tek başına yetki anlamına gelmez. Kullanıcı sisteme giriş yapmış olabilir ama admin endpoint'ine erişemeyebilir.

---

## 3. Keycloak Nedir?

Keycloak, açık kaynaklı bir **Identity and Access Management (IAM)** sistemidir. Uygulamalara merkezi login, token üretimi, rol yönetimi, SSO ve kullanıcı yönetimi sağlar.

Keycloak ile uygulama içinde şunları elle yazmak zorunda kalmazsın:

- Kullanıcı login ekranı
- Şifre saklama ve hashleme
- Kullanıcıya rol atama
- Token üretme ve yenileme
- SSO (Single Sign-On)
- Sosyal login veya LDAP entegrasyonu

**Günlük hayat örneği:**
Bir AVM düşün. Her mağaza kendi güvenlik kapısını, üyelik sistemini ve kart okuyucusunu kurmak yerine AVM'nin merkezi giriş kartını kabul eder. Keycloak da uygulamalar için merkezi kimlik sistemidir.

---

## 4. Keycloak Temel Kavramları

| Kavram | Açıklama |
|---|---|
| **Realm** | İzole kimlik alanı. Her realm kendi kullanıcılarını, rollerini ve client'larını tutar. |
| **User** | Giriş yapan gerçek kişi veya sistem kullanıcısı. |
| **Client** | Keycloak'a kayıtlı uygulama. Örneğin `frontend-app`, `order-api`. |
| **Role** | Yetki grubu. Örneğin `ADMIN`, `CUSTOMER`, `MANAGER`. |
| **Group** | Kullanıcıları toplu yönetmek için grup. Örneğin `sales-team`. |
| **Access Token** | API'lere gönderilen kısa ömürlü yetki token'ı. |
| **Refresh Token** | Access token süresi dolunca yeni access token almak için kullanılır. |
| **ID Token** | Kullanıcının kimlik bilgisini frontend/client tarafına anlatır. |
| **Service Account** | Bir servisin kullanıcı olmadan kendi adına token almasıdır. |

### Realm Günlük Örneği

Bir üniversitede öğrenciler, akademisyenler ve personel tek kampüste olabilir ama farklı sistemlerde farklı kurallara tabidir. Keycloak'ta ayrı realm kullanmak, ayrı kampüs yönetimi kurmak gibidir.

### Client Günlük Örneği

Şirket binasında her departmanın ayrı kapısı olabilir. Muhasebe kapısı başka, IT kapısı başka. Keycloak'ta her uygulama veya servis ayrı client olarak tanımlanır.

---

## 5. OAuth2 ve OpenID Connect Kısa Özet

Keycloak; OAuth2, OpenID Connect ve SAML gibi standartları destekler. Spring Boot REST API'lerde en sık kullanılan yol **OpenID Connect + JWT Bearer Token** akışıdır.

### OAuth2 Ne Sağlar?

OAuth2, erişim yetkisi verme standardıdır. "Bu uygulama, şu kaynağa şu süreyle erişebilir" der.

### OpenID Connect Ne Ekler?

OpenID Connect, OAuth2 üzerine kimlik katmanı ekler. "Bu kullanıcı kim?" sorusunu standart hale getirir.

| Standart | Odak |
|---|---|
| OAuth2 | Yetki / erişim |
| OpenID Connect | Kimlik + yetki |
| SAML | Daha eski ama kurumsal SSO sistemlerinde hâlâ yaygın |

**Günlük hayat örneği:**
OAuth2, vale anahtarı gibidir. Arabayı kullanmaya izin verir ama evinin anahtarını vermez. OpenID Connect ise vale anahtarına ek olarak "bu anahtar gerçekten Ahmet'e ait" bilgisini de doğrular.

---

## 6. JWT ve Bearer Token

JWT (JSON Web Token), JSON verisini imzalı şekilde taşıyan token formatıdır. API'lerde çoğunlukla şu header ile gönderilir:

```http
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

JWT üç parçadan oluşur:

```text
header.payload.signature
```

| Parça | İçerik |
|---|---|
| Header | Algoritma ve token tipi |
| Payload | Kullanıcı id, username, role, scope, expiration gibi claim'ler |
| Signature | Token'ın değiştirilmediğini kanıtlayan imza |

Örnek payload:

```json
{
  "sub": "7f3a-123",
  "preferred_username": "ahmet",
  "email": "ahmet@example.com",
  "realm_access": {
    "roles": ["CUSTOMER"]
  },
  "exp": 1770000000,
  "iss": "http://localhost:8080/realms/ecommerce"
}
```

> **NOT:** JWT payload Base64 ile kodlanır, şifrelenmez. Bu yüzden token içine şifre, kart numarası, gizli veri koyulmamalıdır.

### Access Token Nasıl Doğrulanır?

Spring Security Resource Server, gelen JWT için genelde şu kontrolleri yapar:

1. Token imzası Keycloak'ın public key'i ile doğrulanır.
2. `exp` süresi dolmuş mu bakılır.
3. `iss` yani issuer doğru realm mi kontrol edilir.
4. Scope/role bilgileri Spring authority'lerine çevrilir.

**Günlük hayat örneği:**
Konser biletinde QR kod vardır. Kapıdaki görevli QR kodu okutur; bilet gerçek mi, tarihi geçmiş mi, bu konser için mi, VIP alanına geçebilir mi kontrol eder.

---

## 7. Keycloak Login Akışı

Web uygulamaları için önerilen akış genelde **Authorization Code Flow**'dur.

```text
1. Kullanıcı frontend'de "Giriş Yap" der.
2. Frontend kullanıcıyı Keycloak login sayfasına yönlendirir.
3. Kullanıcı bilgilerini Keycloak'a girer.
4. Keycloak frontend'e authorization code döner.
5. Frontend/backend bu code'u token endpoint'e gönderir.
6. Keycloak access token, refresh token ve id token döner.
7. Frontend API çağrılarında access token'ı Bearer header ile taşır.
```

### Neden Şifre Direkt API'ye Gitmemeli?

Kullanıcının şifresi uygulamanın her servisine yayılırsa risk büyür. Login işini Keycloak'a bırakmak güvenlik yüzeyini küçültür.

**Günlük hayat örneği:**
Her mağazaya TC kimlik fotokopisi vermek yerine AVM girişinde doğrulanmış bir kart almak daha güvenlidir. Mağazalar sadece kartı kontrol eder.

---

## 8. Spring Boot API'yi Keycloak ile Korumak

Spring Boot tarafında API, OAuth2 Resource Server gibi davranır. Yani login ekranı açmaz; sadece gelen Bearer token'ı doğrular.

### Maven Bağımlılıkları

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

### `application.yml`

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/ecommerce
```

Bu ayarla Spring, Keycloak'ın discovery endpoint'i üzerinden gerekli JWK bilgilerini bulabilir:

```text
http://localhost:8080/realms/ecommerce/.well-known/openid-configuration
```

### Security Config

```java
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
}
```

> **NOT:** `permitAll()` public endpoint'ler içindir. İş kuralı gereği korunması gereken endpoint'leri yanlışlıkla public bırakmak en sık yapılan güvenlik hatalarından biridir.

---

## 9. Keycloak Rolleri Spring Role'lerine Çevirmek

Keycloak rollerini JWT içinde çoğunlukla `realm_access.roles` veya `resource_access.{client}.roles` alanında taşır. Spring Security ise role kontrolünde `ROLE_` prefix'ini bekler.

Basit converter örneği:

```java
@Bean
JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter();

    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
        Collection<GrantedAuthority> authorities = new ArrayList<>(scopes.convert(jwt));

        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null) {
            List<String> roles = (List<String>) realmAccess.getOrDefault("roles", List.of());
            roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .forEach(authorities::add);
        }

        return authorities;
    });

    return converter;
}
```

Security config içinde kullanımı:

```java
.oauth2ResourceServer(oauth2 -> oauth2
        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
)
```

> **NOT:** `hasRole("ADMIN")` aslında `ROLE_ADMIN` authority'sini arar. `hasAuthority("ADMIN")` ile `hasRole("ADMIN")` aynı şey değildir.

---

## 10. Token Relay ve Servisler Arası Auth

Bir API, kullanıcının token'ı ile başka bir API'ye istek atabilir. Buna pratikte **token relay** denir.

```text
Frontend
  |
  | Authorization: Bearer user-token
  v
Order API
  |
  | Authorization: Bearer same-user-token
  v
Payment API
```

Bu yaklaşımda ikinci servis de kullanıcının kim olduğunu bilir.

### Ne Zaman Token Relay?

- Kullanıcı adına işlem yapılıyorsa
- Downstream servis "bu kullanıcı bu kaynağa erişebilir mi?" diye kontrol edecekse
- Audit log'da gerçek kullanıcı görünmeli ise

### Ne Zaman Client Credentials?

- İş kullanıcı adına değil, servis adına yapılıyorsa
- Scheduled job, batch job, sistem entegrasyonu varsa
- Örneğin her gece rapor servisi fatura servisini çağırıyorsa

**Günlük hayat örneği:**
Kullanıcı adına işlem: Senin kimlik kartınla bankada sıra almak.

Servis adına işlem: Şirket muhasebesinin resmi kaşesiyle toplu ödeme dosyası göndermesi.

---

## 11. OpenFeign Nedir?

OpenFeign, Java interface'leri üzerinden HTTP client oluşturmaya yarayan bir araçtır. Spring Cloud OpenFeign ile REST çağrılarını controller yazar gibi tanımlarsın.

Normalde HTTP çağrısı için şöyle kodlar yazılır:

```java
RestClient restClient = RestClient.create();
ProductResponse product = restClient.get()
        .uri("http://product-service/api/products/{id}", id)
        .retrieve()
        .body(ProductResponse.class);
```

OpenFeign ile interface tanımlarsın:

```java
@FeignClient(name = "product-service", url = "${services.product.url}")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponse getById(@PathVariable Long id);
}
```

Kullanım:

```java
@Service
public class OrderService {
    private final ProductClient productClient;

    public OrderService(ProductClient productClient) {
        this.productClient = productClient;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        ProductResponse product = productClient.getById(request.productId());
        // stok, fiyat ve sipariş kuralları
        return new OrderResponse(product.id(), product.name());
    }
}
```

**Günlük hayat örneği:**
Bir müşteri temsilcisi, ürün stok bilgisini öğrenmek için depo departmanını arar. Telefon numarasını, konuşma formatını ve soracağı soruyu biliyorsa süreç standartlaşır. OpenFeign bu telefon rehberi ve konuşma şablonu gibidir.

---

## 12. Sync İletişim Nedir?

Senkron iletişimde çağıran servis, çağrılan servisin cevabını bekler.

```text
Order API ---- HTTP request ----> Stock API
Order API <--- HTTP response ---- Stock API
```

Order API, Stock API cevap verene kadar işlemi tamamlayamaz.

### Avantajları

- Basit anlaşılır.
- Cevap anında gelir.
- Request-response iş akışları için uygundur.
- Kullanıcıya hızlı doğrulama sonucu göstermek kolaydır.

### Dezavantajları

- Çağrılan servis yavaşsa çağıran servis de yavaşlar.
- Çağrılan servis kapalıysa işlem başarısız olabilir.
- Servisler birbirine çalışma zamanı bağımlılığı taşır.
- Zincirleme çağrılarda latency büyür.

| Senaryo | Sync uygun mu? |
|---|---|
| Ürün detayını hemen göstermek | Evet |
| Ödeme onayını anında almak | Evet |
| Sipariş sonrası e-posta göndermek | Genelde hayır |
| Log/event yaymak | Genelde hayır |
| Rapor üretimini arka planda başlatmak | Genelde hayır |

**Günlük hayat örneği:**
Kasada ödeme yaparken POS cihazının bankadan anında onay alması senkron iletişimdir. Banka cevap vermezse kasa işlemi bitiremez.

---

## 13. OpenFeign Kurulumu

### Maven Bağımlılığı

Spring Cloud BOM ile sürüm yönetmek daha sağlıklıdır:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

### Feign'i Aktif Etmek

```java
@SpringBootApplication
@EnableFeignClients
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
```

### Client Tanımı

```java
@FeignClient(name = "payment-service", url = "${services.payment.url}")
public interface PaymentClient {

    @PostMapping("/api/payments")
    PaymentResponse pay(@RequestBody PaymentRequest request);
}
```

### `application.yml`

```yaml
services:
  payment:
    url: http://localhost:8082

spring:
  cloud:
    openfeign:
      client:
        config:
          payment-service:
            connectTimeout: 2000
            readTimeout: 5000
            loggerLevel: basic
```

| Ayar | Anlamı |
|---|---|
| `connectTimeout` | Bağlantı kurmak için beklenecek süre |
| `readTimeout` | Cevap okumak için beklenecek süre |
| `loggerLevel` | Feign log seviyesini belirler |

> **NOT:** Timeout vermeden sync çağrı yapmak tehlikelidir. Cevap dönmeyen servis, çağıran thread'leri tüketebilir.

---

## 14. Feign ile Bearer Token Taşımak

Order API'ye gelen kullanıcı token'ını Payment API'ye taşımak için `RequestInterceptor` kullanılabilir.

```java
@Configuration
public class FeignAuthConfig {

    @Bean
    RequestInterceptor bearerTokenRelayInterceptor() {
        return template -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                String token = jwtAuth.getToken().getTokenValue();
                template.header("Authorization", "Bearer " + token);
            }
        };
    }
}
```

Client'e config bağlamak:

```java
@FeignClient(
        name = "payment-service",
        url = "${services.payment.url}",
        configuration = FeignAuthConfig.class
)
public interface PaymentClient {
    @PostMapping("/api/payments")
    PaymentResponse pay(@RequestBody PaymentRequest request);
}
```

**Günlük hayat örneği:**
Bir departmandan diğerine giderken kendi giriş kartını göstermeye devam edersin. İlk kapıdan geçtin diye içerideki bütün kapılar otomatik açılmaz.

> **Dikkat:** Token relay yapıyorsan downstream servis de token'ı doğrulamalıdır. Sadece gateway doğruladı diye iç servisleri tamamen korumasız bırakmak risklidir.

---

## 15. Feign Hata Yönetimi

Feign çağrısında HTTP `4xx` veya `5xx` dönerse bunu uygulama seviyesinde anlamlı hataya çevirmek gerekir.

```java
public class PaymentErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 400) {
            return new IllegalArgumentException("Ödeme isteği hatalı.");
        }

        if (response.status() == 401 || response.status() == 403) {
            return new AccessDeniedException("Ödeme servisine erişim yetkisi yok.");
        }

        if (response.status() >= 500) {
            return new PaymentServiceUnavailableException("Ödeme servisi geçici olarak kullanılamıyor.");
        }

        return defaultDecoder.decode(methodKey, response);
    }
}
```

Config:

```java
@Bean
ErrorDecoder paymentErrorDecoder() {
    return new PaymentErrorDecoder();
}
```

### Hata Kodlarını Doğru Yorumlamak

| Kod | Anlam | Ne yapılmalı? |
|---|---|---|
| `400` | Request hatalı | Client/request düzeltilmeli |
| `401` | Token yok veya geçersiz | Auth kontrol edilmeli |
| `403` | Yetki yok | Rol/permission kontrol edilmeli |
| `404` | Kaynak yok | İş kuralına göre not found dönülmeli |
| `409` | Çakışma | Idempotency veya concurrency kontrol edilmeli |
| `500` | Karşı servis hatası | Retry/circuit breaker düşünülmeli |
| `503` | Servis geçici kapalı | Retry/backoff veya async süreç düşünülmeli |

---

## 16. Retry, Circuit Breaker ve Timeout

Senkron çağrılarda üç konu özellikle önemlidir:

### Timeout

Her Feign client için connect ve read timeout ayarlanmalıdır.

**Günlük hayat:** Telefonda biri açmazsa sonsuza kadar beklemezsin; belirli sürede kapatırsın.

### Retry

Geçici network hatalarında tekrar deneme yapılabilir. Ama her çağrı retry'a uygun değildir.

| İşlem | Retry uygun mu? |
|---|---|
| Ürün bilgisi okumak | Genelde evet |
| Ödeme almak | Dikkatli, idempotency olmadan hayır |
| Stok düşmek | Dikkatli, idempotency gerekir |
| E-posta göndermek | Tekrarlı gönderim riski var |

### Circuit Breaker

Çağrılan servis sürekli hata veriyorsa bir süre çağrı göndermeyi keser. Böylece sistem kendini korur.

**Günlük hayat:**
Bir departmanı arıyorsun ama telefon sürekli meşgul. Her saniye tekrar aramak yerine 5 dakika bekleyip tekrar denersin.

> **NOT:** Spring Cloud OpenFeign retry davranışı Feign'in default davranışından farklı olabilir. Bu yüzden retry politikasını bilinçli tanımlamak gerekir.

---

## 17. OpenFeign vs RestTemplate vs RestClient vs WebClient

| Araç | Tip | Ne zaman kullanılır? |
|---|---|---|
| `RestTemplate` | Sync/blocking | Eski projelerde yaygın, yeni geliştirmelerde tercih azalıyor |
| `RestClient` | Sync/blocking | Spring Framework 6+ ile modern sync HTTP client |
| `WebClient` | Reactive/non-blocking | Reactive veya yüksek concurrency gereken sistemler |
| `OpenFeign` | Interface tabanlı sync/blocking | Mikroservislerde tip güvenli, okunabilir HTTP client |

> **NOT:** Spring Cloud OpenFeign resmi dokümanlarında proje "feature-complete" olarak geçer ve yeni özellik beklentisi sınırlıdır. Buna rağmen mevcut Spring mikroservis projelerinde hâlâ pratik ve yaygın bir çözümdür. Yeni projede alternatif olarak Spring HTTP Service Clients da değerlendirilebilir.

---

## 18. Keycloak + OpenFeign Birlikte Kullanım Senaryosu

Senaryo:

- Kullanıcı sipariş oluşturmak istiyor.
- `Order API` token'ı doğruluyor.
- `Order API`, `Product API` ile ürün fiyatını alıyor.
- `Order API`, `Payment API` ile ödeme başlatıyor.
- İki downstream servis de aynı Bearer token'ı kontrol ediyor.

```text
Frontend
  |
  | POST /api/orders
  | Authorization: Bearer user-token
  v
Order API
  |
  | GET /api/products/10
  | Authorization: Bearer user-token
  v
Product API

Order API
  |
  | POST /api/payments
  | Authorization: Bearer user-token
  v
Payment API
```

### Order Service Örneği

```java
@Service
public class OrderService {

    private final ProductClient productClient;
    private final PaymentClient paymentClient;
    private final OrderRepository orderRepository;

    public OrderService(
            ProductClient productClient,
            PaymentClient paymentClient,
            OrderRepository orderRepository
    ) {
        this.productClient = productClient;
        this.paymentClient = paymentClient;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        ProductResponse product = productClient.getById(request.productId());

        PaymentResponse payment = paymentClient.pay(new PaymentRequest(
                request.customerId(),
                product.price()
        ));

        Order order = new Order();
        order.setProductId(product.id());
        order.setCustomerId(request.customerId());
        order.setPaymentId(payment.paymentId());
        order.setTotalPrice(product.price());

        Order saved = orderRepository.save(order);

        return new OrderResponse(saved.getId(), saved.getTotalPrice());
    }
}
```

### Transaction İçinde Feign Çağrısı Uyarısı

Yukarıdaki örnek öğretici olması için basittir. Gerçek sistemde transaction içinde dış HTTP çağrısı yapmak DB lock süresini uzatabilir.

Daha sağlıklı yaklaşım:

1. Önce gerekli dış kontrolleri transaction dışında yap.
2. Sadece DB yazma kısmını kısa transaction içinde tut.
3. Sipariş sonrası bildirim/e-posta gibi işler için async event veya outbox pattern kullan.

**Günlük hayat örneği:**
Kasiyer, kasayı açık bırakıp başka mağazayı telefonla 10 dakika beklerse kuyruk kilitlenir. Kasa işlemi kısa tutulmalı, uzun işler ayrı süreçte yapılmalıdır.

---

## 19. Güvenlikte Sık Yapılan Hatalar

1. **JWT içine hassas veri koymak.** JWT okunabilir; şifre, kart numarası, gizli bilgi taşınmaz.
2. **Token süresini çok uzun yapmak.** Çalınan token uzun süre kullanılabilir.
3. **Refresh token'ı frontend'de güvensiz saklamak.** XSS veya storage riskleri düşünülmeli.
4. **İç servisleri auth'suz bırakmak.** Gateway kontrolü faydalı ama tek başına her zaman yeterli değildir.
5. **Role mapping'i yanlış yapmak.** `ROLE_` prefix'i ve Keycloak claim yapısı karıştırılabilir.
6. **Client secret'ı repoya koymak.** Secret'lar environment variable veya secret manager ile yönetilmeli.
7. **HTTPS kullanmamak.** Bearer token taşıyan trafik şifreli olmalıdır.
8. **CORS'u `*` yapmak.** Özellikle credential/token kullanılan sistemlerde risklidir.
9. **Loglara token basmak.** Authorization header maskelenmelidir.
10. **Direct Grant'i gereksiz kullanmak.** Kullanıcı şifresini uygulamaya toplatmak güvenlik yüzeyini büyütür.

---

## 20. OpenFeign'de Sık Yapılan Hatalar

1. **Timeout vermemek.** Servis cevap vermezse thread'ler bekler.
2. **Her hataya retry yapmak.** Ödeme gibi işlemler idempotency olmadan tekrar denenirse çift işlem oluşabilir.
3. **Feign DTO'larını entity ile paylaşmak.** Servisler arası contract için ayrı request/response DTO kullanılmalı.
4. **HTTP hata kodlarını yutmamak.** `500` ile `404` aynı davranmamalıdır.
5. **N+1 servis çağrısı yapmak.** Liste içindeki her item için ayrı Feign çağrısı performansı bozar.
6. **Transaction içinde uzun HTTP çağrıları yapmak.** DB lock ve timeout sorunları büyür.
7. **Token relay unutmak.** Downstream servis `401` döner veya kullanıcı bağlamı kaybolur.
8. **Loglarda body/header hassasiyetini kaçırmak.** Feign `full` log seviyesi prod'da dikkatli kullanılmalıdır.

---

## 21. Kısa Özet

Keycloak, uygulamanın login ve yetki yönetimini merkezi hale getirir. Kullanıcı başarılı giriş yaptıktan sonra API'lere JWT access token gönderir. Spring Security Resource Server bu token'ı doğrular ve endpoint erişimini role/scope bilgisine göre yönetir.

OpenFeign ise Spring Boot servisleri arasında senkron HTTP iletişimini interface tabanlı ve okunabilir hale getirir. Sync iletişim basit ve doğrudandır ama timeout, retry, circuit breaker, hata yönetimi ve auth token taşıma doğru tasarlanmalıdır.

En sağlıklı yaklaşım şudur:

- Auth merkezi olsun: Keycloak.
- API'ler token doğrulasın: Spring Security Resource Server.
- Servis çağrıları tip güvenli olsun: OpenFeign.
- Her sync çağrıda timeout ve hata yönetimi olsun.
- Uzun/garanti gerektiren işler async event veya outbox ile ayrı yönetilsin.

---

## 22. Kaynaklar

- [Keycloak — Planning for securing applications and services](https://www.keycloak.org/securing-apps/overview)
- [Keycloak — Securing applications and services with OpenID Connect](https://www.keycloak.org/securing-apps/oidc-layers)
- [Spring Security — OAuth 2.0 Resource Server JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)
- [Spring Cloud OpenFeign — Reference Documentation](https://docs.spring.io/spring-cloud-openfeign/reference/index.html)
- [Spring Cloud OpenFeign — Features](https://docs.spring.io/spring-cloud-openfeign/reference/spring-cloud-openfeign.html)
