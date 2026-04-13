# HTTP İsteğinin Anatomisi

**HTTP (HyperText Transfer Protocol)**, istemci ile sunucu arasındaki iletişimi düzenleyen, uygulama katmanında (OSI Layer 7) çalışan bir protokoldür. Temelinde bir **istek-yanıt (request-response)** modeli yatar ve **durumsuz (stateless)** yapıdadır; yani her istek birbirinden bağımsızdır, sunucu önceki istekleri hatırlamaz.

> **İlgili terim — Session & Cookie:** HTTP stateless olduğu için "oturum" kavramı protokolün kendisinde yoktur. Sunucular, kullanıcıyı tanımak için cookie veya token gibi mekanizmalara başvurur.

---

## 1. İstemci-Sunucu Modeli

Bir web isteğinde şu adımlar gerçekleşir:

1. Tarayıcı, alan adını IP adresine çevirmek için **DNS** sorgusu yapar.
2. Sunucuyla **TCP bağlantısı** kurulur (SYN → SYN-ACK → ACK).
3. HTTPS kullanılıyorsa **TLS el sıkışması (handshake)** yapılır.
4. HTTP isteği gönderilir, sunucu yanıt döner.
5. Bağlantı kapatılır (veya HTTP/1.1+ ile açık tutulur).

---

## 2. HTTP İsteğinin Yapısı

Bir HTTP isteği üç ana bölümden oluşur:

```
┌─────────────────────────────────┐
│         REQUEST LINE            │  ← Metod + URI + HTTP Versiyonu
├─────────────────────────────────┤
│         HEADERS                 │  ← Metadata (üstveri)
├─────────────────────────────────┤
│         (Boş Satır - CRLF)      │  ← Başlık ile gövdeyi ayırır
├─────────────────────────────────┤
│         BODY (isteğe bağlı)     │  ← Gönderilen veri
└─────────────────────────────────┘
```

### Ham İstek Örneği

```http
POST /api/users HTTP/1.1
Host: api.example.com
Content-Type: application/json
Content-Length: 57
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
Accept: application/json
User-Agent: Mozilla/5.0

{
  "name": "Ahmet Yılmaz",
  "email": "ahmet@example.com"
}
```

---

### 2.1 Request Line (İstek Satırı)

```
POST   /api/users   HTTP/1.1
  │         │           │
  │         │           └── HTTP Versiyonu
  │         └────────────── URI (hedef kaynak yolu)
  └──────────────────────── HTTP Metodu
```

---

### 2.2 Headers (Başlıklar)

`Anahtar: Değer` formatında yazılan, istek hakkında metadata taşıyan alanlardır. Kategorilere göre ayrılırlar: genel başlıklar, istek başlıkları, yanıt başlıkları ve varlık (entity) başlıkları.

| Başlık | Açıklama |
|---|---|
| `Host` | Hedef sunucunun alan adı. HTTP/1.1'de **zorunludur**. Aynı IP'de birden fazla site barındıran **virtual hosting** için kritiktir. |
| `Content-Type` | Gövdedeki verinin MIME türü (`application/json`, `text/html`, `multipart/form-data` vb.) |
| `Authorization` | Kimlik doğrulama bilgisi. `Basic` (Base64), `Bearer` (JWT/OAuth token) veya `Digest` şemalarıyla kullanılır. |
| `Accept` | İstemcinin kabul ettiği içerik türleri. `q` değeri tercih sırasını belirtir (`q=1.0` en yüksek öncelik). |
| `Cookie` | Sunucudan önceden alınan çerezleri geri gönderir. |
| `User-Agent` | İsteği yapan uygulamanın kimliği (tarayıcı, mobil uygulama, bot vb.). |
| `Cache-Control` | Önbellekleme davranışını yönetir (`no-cache`, `no-store`, `max-age=3600` vb.). |

> **İlgili terim — JWT (JSON Web Token):** `Authorization: Bearer ...` başlığında taşınan, kullanıcı kimliğini ve yetkilerini içeren imzalı bir token formatıdır. Sunucunun veritabanına sorgu atmadan kimliği doğrulamasını sağlar.

---

### 2.3 Body (Gövde)

Sunucuya gönderilecek veriyi içerir. **GET, HEAD, DELETE** gibi metodlar genellikle gövde taşımaz; **POST, PUT, PATCH** taşır.

Yaygın gövde formatları:

- **JSON** → `application/json` — REST API'lerde standart
- **Form Data** → `application/x-www-form-urlencoded` — HTML formları
- **Multipart** → `multipart/form-data` — Dosya yüklemeleri
- **XML** → `application/xml` — Eski kurumsal sistemler

---

## 3. HTTP Metodları

| Metod | Anlamı | Body | Güvenli | Idempotent |
|---|---|:---:|:---:|:---:|
| **GET** | Kaynak getir | ❌ | ✅ | ✅ |
| **POST** | Kaynak oluştur | ✅ | ❌ | ❌ |
| **PUT** | Kaynağı tamamen güncelle | ✅ | ❌ | ✅ |
| **PATCH** | Kaynağı kısmen güncelle | ✅ | ❌ | ❌ |
| **DELETE** | Kaynağı sil | ❌ | ❌ | ✅ |
| **HEAD** | Sadece başlıkları getir | ❌ | ✅ | ✅ |
| **OPTIONS** | Desteklenen metodları sorgula | ❌ | ✅ | ✅ |

- **Güvenli (Safe):** Sunucu tarafında herhangi bir değişiklik yapmaz.
- **Idempotent:** Aynı isteği birden fazla kez göndermek, tek kez göndermekle aynı sonucu üretir. (Örneğin aynı `DELETE` isteğini iki kez göndermek yine kaynağı silmiş olur.)

> **İlgili terim — REST:** HTTP metodlarını ve URL yapısını belirli kurallara göre kullanan bir API tasarım mimarisidir. Örneğin `GET /users/42` bir kullanıcıyı okur, `DELETE /users/42` onu siler.

---

## 4. URL Anatomisi

```
https://api.example.com:8443/v2/products?category=laptop&page=2#results
  │           │            │      │               │                │
  │           │            │      │               │                └── Fragment (sunucuya gönderilmez)
  │           │            │      │               └─────────────────── Query String
  │           │            │      └─────────────────────────────────── Path
  │           │            └────────────────────────────────────────── Port
  │           └─────────────────────────────────────────────────────── Host
  └─────────────────────────────────────────────────────────────────── Scheme (protokol)
```

- Varsayılan portlar: HTTP → `80`, HTTPS → `443`
- Özel karakterler **URL Encoding** ile `%HH` formatına dönüştürülür (boşluk → `%20`, `@` → `%40` vb.).
- **URI vs URL:** URI bir kaynağı tanımlar; URL ise kaynağın konumunu (adresini) içerir. Her URL bir URI'dır, ancak her URI URL değildir.

---

## 5. HTTP Yanıt Yapısı

```http
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 248
Cache-Control: public, max-age=3600
ETag: "33a64df551425fcc55e"
Set-Cookie: session=xyz789; HttpOnly; Secure

{
  "id": 42,
  "name": "Ahmet Yılmaz",
  "email": "ahmet@example.com"
}
```

Yapı, istek yapısıyla paralel şekilde **Status Line + Headers + Body** olarak üç bölüme ayrılır.

- `ETag`: Kaynağın versiyonunu tanımlayan benzersiz bir tanımlayıcıdır. İstemci, bir sonraki istekte `If-None-Match` başlığıyla bu değeri gönderir; kaynak değişmemişse sunucu sadece `304 Not Modified` döner, böylece bant genişliği tasarrufu sağlanır.
- `Set-Cookie`: Sunucunun istemciye çerez yerleştirdiği başlıktır. `HttpOnly` (JavaScript'ten gizler) ve `Secure` (yalnızca HTTPS) gibi güvenlik bayrakları taşır.

---

## 6. Durum Kodları

| Kod | İsim | Açıklama |
|---|---|---|
| **200** | OK | Başarılı |
| **201** | Created | Kaynak oluşturuldu |
| **204** | No Content | Başarılı, dönecek içerik yok |
| **301** | Moved Permanently | Kalıcı yönlendirme |
| **304** | Not Modified | Önbellek hâlâ geçerli |
| **400** | Bad Request | Hatalı/eksik istek |
| **401** | Unauthorized | Kimlik doğrulama gerekli |
| **403** | Forbidden | Kimlik doğrulandı ama yetkisiz |
| **404** | Not Found | Kaynak bulunamadı |
| **405** | Method Not Allowed | Bu metod bu kaynak için izin verilmiyor |
| **409** | Conflict | Çakışma (örn. e-posta zaten kayıtlı) |
| **422** | Unprocessable Entity | Doğrulama hatası |
| **429** | Too Many Requests | Rate limit aşıldı |
| **500** | Internal Server Error | Sunucuda beklenmedik hata |
| **503** | Service Unavailable | Sunucu geçici olarak kullanılamıyor |

> **401 vs 403 farkı:** 401, kullanıcının kim olduğu bilinmiyor (giriş yapılmamış); 403 ise kim olduğu biliniyor ama erişim izni yok.

> **İlgili terim — Rate Limiting:** Bir istemcinin belirli süre içinde yapabileceği istek sayısını sınırlayan mekanizmadır. Aşımda `429` döner.

---

## 7. HTTP Versiyonları

| Özellik | HTTP/1.1 | HTTP/2 | HTTP/3 |
|---|---|---|---|
| **Protokol** | Metin tabanlı | İkili (Binary) | İkili (Binary) |
| **Taşıma** | TCP | TCP | UDP (QUIC) |
| **Multiplexing** | ❌ | ✅ | ✅ |
| **Header Sıkıştırma** | ❌ | ✅ (HPACK) | ✅ (QPACK) |
| **Head-of-line Blocking** | Var | Kısmen | Yok |

- **HTTP/1.1 (1997):** Hâlâ en yaygın. Persistent connection (kalıcı bağlantı) ve `Host` başlığı bu versiyonla zorunlu hale geldi.
- **HTTP/2 (2015):** Aynı bağlantı üzerinden paralel istek göndermeye (**multiplexing**) izin verir.
- **HTTP/3 (2022):** TCP yerine UDP tabanlı **QUIC** protokolünü kullanır. Paket kaybında diğer istekler bloklanmaz.

---

## 8. HTTPS ve TLS

HTTPS, HTTP'nin **TLS (Transport Layer Security)** ile şifrelenmiş versiyonudur. Üç temel güvenceyi sağlar:

| Güvence | Açıklama |
|---|---|
| **Şifreleme** | Trafik dinlenebilse bile içerik okunamaz |
| **Kimlik Doğrulama** | Sertifika ile sunucunun gerçekten o sunucu olduğu doğrulanır |
| **Bütünlük** | Veri aktarım sırasında değiştirilmemişse garanti altına alınır |

TLS bağlantısı kurulurken "el sıkışma (handshake)" adı verilen bir süreçte taraflar, kullanacakları şifreleme algoritmasını (**cipher suite**) ve oturum anahtarlarını belirler. Bu süreç tamamlandıktan sonra HTTP trafiği şifreli gönderilir.

> **İlgili terim — CA (Certificate Authority):** TLS sertifikalarını imzalayan güvenilir kuruluşlardır. Tarayıcı, sunucunun sertifikasının tanınan bir CA tarafından imzalanıp imzalanmadığını kontrol eder.

---

## 9. Önemli İlgili Kavramlar

| Terim | Kısa Açıklama |
|---|---|
| **CORS** | Farklı origin'lerden (domain) gelen isteklerin güvenliğini yöneten mekanizma. Tarayıcı, bazı çapraz kaynaklı istekler için önce `OPTIONS` ile **preflight** sorgusu gönderir. |
| **Proxy / Reverse Proxy** | İstemci ile sunucu arasında aracı görev gören sunucu. Nginx ve Caddy gibi araçlar reverse proxy olarak yaygın kullanılır. |
| **CDN** | İçerikleri coğrafi olarak dağıtan ağ; statik dosyaların kullanıcıya en yakın sunucudan iletilmesini sağlar. |
| **Webhook** | Sunucunun, bir olay gerçekleştiğinde istemciye HTTP isteği göndererek bildirimde bulunması. Geleneksel istek-yanıt modelinin tersidir. |
| **Endpoint** | API'de belirli bir işlevi temsil eden URL. Örn: `POST /api/auth/login` |

---

*HTTP/1.1 RFC 7230-7235 ve HTTP/2 RFC 7540 standartlarına dayanmaktadır.*
