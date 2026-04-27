# 📚 Library Projesi — Çalışma Özeti

> **Code-first** yaklaşımıyla yazılmış, **Spring Boot 4.0.6 + Java 21 + PostgreSQL** üzerine kurulu klasik bir **kütüphane yönetim sistemi** REST API'si.
> 12 entity, 12 repository, 12 service ve 12 controller'dan oluşan, **katmanlı (layered) mimariyi** uygulayan eğitim/üretim seviyesi bir CRUD projesidir.

---

## 1. Projenin Amacı ve Genel Bakış

Library projesi, gerçek bir kütüphanenin işleyişini modelleyen bir backend uygulamasıdır:

- **Kitap kataloglama** (kitaplar, yazarlar, kategoriler, yayınevleri)
- **Üye yönetimi** (öğrenciler)
- **Personel yönetimi** (görevliler ve yetkileri)
- **Ödünç alma / iade** süreçleri (kopya bazlı)
- **Rezervasyon** sistemi
- **Ceza** takibi

Kullanıcı bu sistemi `/api/...` endpoint'leri üzerinden HTTP ile yönetir; veritabanı olarak `library` adındaki PostgreSQL veritabanı kullanılır. Tablolar **Hibernate tarafından otomatik** oluşturulur (`ddl-auto: update`).

---

## 2. Teknoloji Yığını (pom.xml)

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.6</version>
</parent>

<properties>
    <java.version>21</java.version>
</properties>
```

| Dependency | Niçin Kullanılıyor |
|---|---|
| `spring-boot-starter-webmvc` | REST API yapmak için. `@RestController`, `@GetMapping`, embedded Tomcat. |
| `spring-boot-starter-data-jpa` | ORM (Hibernate) + Spring Data JPA repository soyutlaması. |
| `spring-boot-starter-validation` | DTO doğrulama (`@NotBlank`, `@Email` vs.) — bağımlılık eklenmiş ama henüz kullanılmamış. |
| `spring-boot-starter-actuator` | `/actuator/health` gibi izleme endpoint'leri. |
| `postgresql` (runtime) | PostgreSQL JDBC sürücüsü. Compile-time'da görünmez, çalışırken yüklenir. |
| `*-test` scope'lu olanlar | JUnit, Mockito, Spring Test. Sadece test sırasında classpath'te. |

**Kritik nokta:** `spring-boot-starter-parent`, sürüm yönetimini ve **opinionated default'ları** (Jackson, Tomcat, Hibernate vb.) sizin yerinize hallederek "sadece kod yaz, alt yapıyla uğraşma" deneyimi sağlar.

---

## 3. Klasör Yapısı

```
library/
├── pom.xml
├── mvnw, mvnw.cmd                     # Maven wrapper (Maven yüklü olmasa bile çalıştırır)
└── src/
    ├── main/
    │   ├── java/com/turkcell/library/
    │   │   ├── LibraryApplication.java      # Entry point
    │   │   ├── controller/                  # 12 REST controller (HTTP katmanı)
    │   │   ├── service/                     # 12 service impl (iş kuralları)
    │   │   ├── repository/                  # 12 JpaRepository interface (DB erişimi)
    │   │   ├── entity/                      # 12 JPA entity (DB tabloları)
    │   │   └── dto/                         # Request/Response DTO'lar (paket bazlı)
    │   │       ├── kitap/
    │   │       ├── ogrenci/
    │   │       └── ...
    │   └── resources/
    │       └── application.yaml             # Konfigürasyon
    └── test/                                # Test dosyaları
```

**Paket organizasyonu felsefesi:** **Katman bazlı** (layer-by-layer) — controller'lar bir paket, service'ler bir paket, vs. Küçük/orta projelerde temiz görünür; büyük projelerde "feature bazlı" (her feature kendi paketi içinde controller+service+entity) tercih edilir. Bu proje küçük ölçekli olduğu için katman bazlı tercih edilmiş.

---

## 4. Konfigürasyon (`application.yaml`)

```yaml
spring:
  application:
    name: library
  datasource:
    url: jdbc:postgresql://localhost:5432/library
    username: admin
    password: admin
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update      # Entity'lere göre tabloları otomatik oluşturur/günceller
    show-sql: true          # Çalışan SQL'leri konsola basar (debug amaçlı)
```

**Kritik ayarlar:**

- **`ddl-auto: update`** → Entity'de yeni alan eklediğinde tabloyu **`ALTER TABLE`** ile günceller. **Production'da `validate` veya `none`** kullanılır; aksi halde yanlışlıkla şema değişikliği yapılır.
- **`show-sql: true`** → Hibernate'in ürettiği SQL ekrana basılır. Öğrenirken çok faydalı, production'da gereksiz log doldurur.

> 🔒 **Güvenlik notu:** Şifre düz metin olarak yaml'da. Production'da `application.properties` yerine **environment variable** veya **Vault** ile yönetilmeli.

---

## 5. Entry Point — `LibraryApplication.java`

```java
@SpringBootApplication
public class LibraryApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }
}
```

### `@SpringBootApplication` ne yapar?
Üç annotation'ın bileşkesidir:
1. **`@Configuration`** → Bu sınıf bir Spring bean tanımı kaynağıdır.
2. **`@EnableAutoConfiguration`** → Classpath'teki kütüphanelere bakarak (jpa, web, vs.) otomatik konfigürasyon yapar (DataSource bean'i kur, Tomcat başlat vs.).
3. **`@ComponentScan`** → Bu sınıfın bulunduğu paketten **aşağıya doğru** tüm `@Component`, `@Service`, `@Repository`, `@RestController` annotated sınıfları **bulup bean olarak** kaydeder.

> ⚠️ Bu yüzden tüm kodun `com.turkcell.library` altında olması zorunludur. Üst pakete koyarsan ComponentScan göremez ve bean'ler oluşturulmaz.

`SpringApplication.run(...)` çağrısı:
- IoC Container'ı (`ApplicationContext`) ayağa kaldırır,
- Bean'leri tarar ve instance'larını yaratır (singleton scope default),
- Embedded Tomcat'i 8080 portunda başlatır.

---

## 6. Katmanlı Mimari ve Veri Akışı

```
┌──────────────────┐    HTTP Request (JSON)
│   Client         │ ────────────────────►
│  (Postman, FE)   │
└──────────────────┘
         ▼
┌────────────────────────────────────────────────────┐
│  CONTROLLER (HTTP katmanı)                         │
│  @RestController, @RequestMapping("/api/kitaplar") │
│  - JSON ↔ DTO dönüşümü (Jackson otomatik)          │
│  - URL/HTTP method routing                         │
│  - DTO'yu service'e devreder                       │
└────────────────────────────────────────────────────┘
         ▼
┌────────────────────────────────────────────────────┐
│  SERVICE (İş kuralları katmanı)                    │
│  @Service                                          │
│  - DTO → Entity mapping                            │
│  - İlişkili entity'leri yükler (kategori, yazar)   │
│  - Validation, business rule'lar                   │
│  - Repository'yi çağırır                           │
└────────────────────────────────────────────────────┘
         ▼
┌────────────────────────────────────────────────────┐
│  REPOSITORY (Persistence katmanı)                  │
│  extends JpaRepository<Kitap, Long>                │
│  - findById, save, findAll, delete (otomatik)      │
│  - Hibernate JPA implementasyonu kullanır          │
└────────────────────────────────────────────────────┘
         ▼
┌────────────────────────────────────────────────────┐
│  ENTITY (Domain modeli + ORM mapping)              │
│  @Entity, @Table, @Column, @ManyToOne ...          │
│  - Java sınıfı = DB tablosu                        │
└────────────────────────────────────────────────────┘
         ▼
┌──────────────────┐    SQL
│  PostgreSQL      │
└──────────────────┘
```

### Somut akış örneği — POST /api/kitaplar

1. **HTTP'den JSON gelir:**
   ```json
   {
     "isbn": "978-1234",
     "kategoriId": 1,
     "yayineviId": 2,
     "baslik": "Suç ve Ceza",
     "yazarIdleri": [1, 5]
   }
   ```
2. **Controller'a düşer:**
   ```java
   @PostMapping
   public KitapResponse create(@RequestBody CreateKitapRequest request) {
       return kitapService.create(request);
   }
   ```
   `@RequestBody` → Jackson, JSON'u `CreateKitapRequest` DTO'sunun setter'larıyla doldurur.

3. **Service'e geçer:**
   ```java
   public KitapResponse create(CreateKitapRequest request) {
       Kategori kategori = kategoriRepository.findById(request.getKategoriId())
           .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
       Yayinevi yayinevi = yayineviRepository.findById(request.getYayineviId())
           .orElseThrow(() -> new RuntimeException("Yayınevi bulunamadı"));

       Kitap kitap = new Kitap();
       kitap.setIsbn(request.getIsbn());
       kitap.setAnaKategori(kategori);
       kitap.setYayinevi(yayinevi);
       // ... diğer alanlar
       kitap.setYazarlar(loadYazarlar(request.getYazarIdleri()));

       return toResponse(kitapRepository.save(kitap));
   }
   ```
   - **Neden ID'leri ayrı yüklüyoruz?** Çünkü ilişkili entity'lerin DB'de var olduğunu doğrulamak ve **proxy değil gerçek nesne** atamak için.
4. **Repository → DB:** `kitapRepository.save(kitap)` → Hibernate `INSERT INTO kitap ...` SQL'ini üretir. ID döner.
5. **Entity → Response DTO:** `toResponse(kitap)` ile sadece kullanıcıya göstermek istediğimiz alanları içeren bir `KitapResponse` üretilir.
6. **Response:** Jackson, DTO'yu JSON'a çevirip 200 OK ile döner.

---

## 7. Entity Katmanı — JPA Mapping Detayları

Toplam **12 entity** var. Her biri DB'deki bir tablo. Aşağıdaki entity grupları farklı ilişki tiplerinin örneklerini gösteriyor.

### 7.1. `Kitap` Entity — En zengin örnek

```java
@Entity
@Table(name = "kitap")
public class Kitap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kitap_id")
    private Long kitapId;

    @Column(name = "isbn", nullable = false, unique = true, length = 20)
    private String isbn;

    @ManyToOne
    @JoinColumn(name = "kategori_id", nullable = false)
    private Kategori anaKategori;          // N:1 — bir kategoride çok kitap

    @ManyToOne
    @JoinColumn(name = "yayinevi_id", nullable = false)
    private Yayinevi yayinevi;             // N:1

    @ManyToMany
    @JoinTable(
        name = "kitap_kategori",
        joinColumns = @JoinColumn(name = "kitap_id"),
        inverseJoinColumns = @JoinColumn(name = "kategori_id")
    )
    private Set<Kategori> ekKategoriler;   // N:M — bir kitap birden çok kategoride

    @ManyToMany
    @JoinTable(
        name = "kitap_yazar",
        joinColumns = @JoinColumn(name = "kitap_id"),
        inverseJoinColumns = @JoinColumn(name = "yazar_id")
    )
    private Set<Yazar> yazarlar;           // N:M — kitap-yazar

    @OneToMany(mappedBy = "kitap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KitapKopya> kopyalar;     // 1:N — bir kitabın birden çok fiziksel kopyası
}
```

#### Annotation'ların anlamı:

| Annotation | İş |
|---|---|
| **`@Entity`** | Bu sınıf JPA tarafından yönetilen bir tabloya karşılık gelir. |
| **`@Table(name="kitap")`** | DB'deki tablo adını belirler (default: sınıf adı küçük). |
| **`@Id`** | Primary key alanı. Tüm entity'lerde zorunlu. |
| **`@GeneratedValue(strategy = IDENTITY)`** | DB'nin auto-increment (PostgreSQL'de `BIGSERIAL`) özelliğini kullan. **Diğer stratejiler:** `SEQUENCE`, `TABLE`, `AUTO`, `UUID`. |
| **`@Column(...)`** | Kolon detayları. `nullable=false` → NOT NULL, `unique=true` → UNIQUE constraint, `length=20` → `VARCHAR(20)`, `columnDefinition="TEXT"` → SQL tipini elle belirt. |
| **`@ManyToOne`** | "Bu entity'nin yanında **çok**, karşı tarafta **bir**" var. FK kolonu **bu tablodadır** (`kategori_id`). |
| **`@OneToMany(mappedBy="kitap")`** | Bidirectional ilişkide **ters taraf**. `mappedBy` "FK karşı tarafta, ben sadece okuma için tutuyorum" demektir. |
| **`@ManyToMany`** + **`@JoinTable`** | Ara tablo (junction table) ile N:M ilişki. `joinColumns` = bu tarafın FK'si, `inverseJoinColumns` = karşı tarafın FK'si. |
| **`@JoinColumn(name="...")`** | FK kolon adını belirler. |
| **`cascade = CascadeType.ALL`** | Parent kaydedilince/silinince child'lar da işleme dahil edilir (insert/update/delete birlikte). |
| **`orphanRemoval = true`** | Parent'tan koparılan child'lar otomatik silinir (örn. `kitap.getKopyalar().remove(k)` yapınca SQL'de DELETE atılır). |

#### Default değer atama:
```java
@Column(name = "dil", length = 30)
private String dil = "Türkçe";          // Java tarafında default; DB tarafında değil!
```
Bu, **Java nesne oluşturulduğunda** geçerlidir; DB'ye DEFAULT olarak yansımaz. DB-level default için `columnDefinition = "VARCHAR(30) DEFAULT 'Türkçe'"` kullanılırdı.

### 7.2. `Kategori` Entity — Self-referencing (recursive) ilişki

```java
@Entity
@Table(name = "kategori")
public class Kategori {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kategoriId;

    @ManyToOne
    @JoinColumn(name = "ust_kategori_id")     // FK aynı tabloya işaret eder
    private Kategori ustKategori;

    @OneToMany(mappedBy = "ustKategori")
    private List<Kategori> altKategoriler;
    ...
}
```

**Senaryo:** "Edebiyat" → "Roman" → "Polisiye Roman" gibi hiyerarşik kategori ağacı kurmak için.
**Database tasarımı:** Tek `kategori` tablosunda `ust_kategori_id` adında nullable bir FK var, kendi tablosuna referans veriyor.

### 7.3. `OduncAlma` Entity — Çoklu N:1 + 1:1

```java
@Entity
@Table(name = "odunc_alma")
public class OduncAlma {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oduncId;

    @ManyToOne @JoinColumn(name = "ogrenci_id", nullable = false)
    private Ogrenci ogrenci;       // Ödüncü kim aldı

    @ManyToOne @JoinColumn(name = "kopya_id", nullable = false)
    private KitapKopya kopya;      // Hangi fiziksel kopya

    @ManyToOne @JoinColumn(name = "gorevli_id", nullable = false)
    private Gorevli gorevli;       // Hangi görevli ödünç verdi

    @OneToOne(mappedBy = "oduncAlma")
    private Iade iade;             // Tek bir iade kaydı olabilir (varsa)
}
```

**Tasarım kararı:** Kitap değil, **kitabın kopyası** ödünç verilir. Çünkü aynı kitabın 3 fiziksel kopyası varsa, hangisinin verildiğini bilmek lazım.

### 7.4. İlişki Yönetimi Özeti

| İlişki | Library Projesi'nde Örneği | DB Karşılığı |
|---|---|---|
| **One-to-Many** | `Kitap → KitapKopya` | Child tabloda FK |
| **Many-to-One** | `Kitap → Kategori (anaKategori)` | Bu tabloda FK |
| **Many-to-Many** | `Kitap ↔ Yazar` | Ara tablo (`kitap_yazar`) |
| **One-to-One** | `OduncAlma ↔ Iade` | Bir tarafın FK'si unique |
| **Self-referencing** | `Kategori → Kategori (ustKategori)` | Aynı tabloya FK |

---

## 8. Repository Katmanı

```java
public interface KitapRepository extends JpaRepository<Kitap, Long> {
}
```

Tek satırlık bu interface'in **bedava** verdikleri:

| Metot | İş |
|---|---|
| `save(entity)` | INSERT (id null ise) veya UPDATE (id varsa) |
| `findById(id)` | `Optional<Entity>` döner |
| `findAll()` | Tüm kayıtlar |
| `findAllById(ids)` | Birden çok ID ile çoklu okuma |
| `deleteById(id)` / `delete(entity)` | DELETE |
| `count()` | Kayıt sayısı |
| `existsById(id)` | Var mı kontrolü |

**Nasıl çalışıyor?** Spring Data JPA, runtime'da bu interface'in bir **proxy implementasyonunu** üretir (Hibernate JPA aracılığıyla). Sen interface'i tanımlarsın, Spring sınıfı yazar.

> 💡 **`JpaRepository<Entity, ID>`** generic'idir; `Entity` = yönetilen sınıf, `ID` = primary key tipi (`Long`, `UUID`, vs.).

**Bu projede özel sorgu yok.** İhtiyaç olsa şöyle yazılırdı:
```java
List<Kitap> findByBaslikContaining(String baslik);   // Method name → otomatik SQL
@Query("SELECT k FROM Kitap k WHERE k.yayinYili > :yil")
List<Kitap> kitaplarYilSonrasi(@Param("yil") int yil);  // JPQL
```

---

## 9. Service Katmanı

### 9.1. Yapı — Constructor Injection

```java
@Service
public class KitapServiceImpl {

    private final KitapRepository kitapRepository;
    private final KategoriRepository kategoriRepository;
    private final YayineviRepository yayineviRepository;
    private final YazarRepository yazarRepository;

    public KitapServiceImpl(KitapRepository kitapRepository,
                            KategoriRepository kategoriRepository,
                            YayineviRepository yayineviRepository,
                            YazarRepository yazarRepository) {
        this.kitapRepository = kitapRepository;
        this.kategoriRepository = kategoriRepository;
        ...
    }
}
```

#### Neden constructor injection?
- **`final`** alanlar → değişmezlik garantisi (immutability).
- **Test edilebilir** → test sınıfı `new KitapServiceImpl(mockRepo, ...)` yapabilir.
- **NullPointerException riski yok** → bağımlılık olmadan instance oluşturulamaz.
- **Spring 4.3+ tek constructor varsa `@Autowired` zorunlu değil** → bu yüzden hiçbir yerde `@Autowired` yok.

> ❌ **Field injection antipattern'i** (kullanılmıyor, doğru):
> ```java
> @Autowired private KitapRepository kitapRepository;  // Test zor, immutable değil
> ```

### 9.2. CRUD Pattern (Tüm service'lerde tekrarlanır)

```java
public KitapResponse create(CreateKitapRequest request) {
    Kategori kategori = kategoriRepository.findById(request.getKategoriId())
        .orElseThrow(() -> new RuntimeException("Kategori bulunamadı: " + request.getKategoriId()));
    Yayinevi yayinevi = yayineviRepository.findById(request.getYayineviId())
        .orElseThrow(() -> new RuntimeException("Yayınevi bulunamadı: " + request.getYayineviId()));

    Kitap kitap = new Kitap();
    kitap.setIsbn(request.getIsbn());
    kitap.setAnaKategori(kategori);
    kitap.setYayinevi(yayinevi);
    kitap.setBaslik(request.getBaslik());
    kitap.setDil(request.getDil() != null ? request.getDil() : "Türkçe");  // null guard
    kitap.setEkKategoriler(loadKategoriler(request.getEkKategoriIdleri()));
    kitap.setYazarlar(loadYazarlar(request.getYazarIdleri()));

    return toResponse(kitapRepository.save(kitap));
}
```

#### Önemli syntax'lar:

**`Optional.orElseThrow(...)`**
```java
Optional<Kategori> opt = kategoriRepository.findById(id);  // boş veya dolu olabilir
Kategori k = opt.orElseThrow(() -> new RuntimeException("..."));  // boşsa exception fırlat
```
- **Lambda** (`() -> new RuntimeException(...)`) → exception'ı **lazy** üretir; sadece gerekirse oluşur.
- Kayıt yoksa runtime'da exception, Spring default olarak HTTP 500 döner.

**Null guard ternary:**
```java
request.getDil() != null ? request.getDil() : "Türkçe"
```
DTO'da kullanıcı dil göndermediyse default değer ata.

### 9.3. Helper Metotlar — Private mapping

```java
private Set<Kategori> loadKategoriler(Set<Long> ids) {
    if (ids == null || ids.isEmpty()) return new HashSet<>();
    return new HashSet<>(kategoriRepository.findAllById(ids));
}

private KitapResponse toResponse(Kitap kitap) {
    KitapResponse response = new KitapResponse();
    response.setKitapId(kitap.getKitapId());
    response.setIsbn(kitap.getIsbn());
    ...
    if (kitap.getYazarlar() != null) {
        response.setYazarlar(kitap.getYazarlar().stream()
            .map(y -> y.getAd() + " " + y.getSoyad())     // Yazar → "Ad Soyad" string
            .collect(Collectors.toSet()));
    }
    return response;
}
```

#### Stream API kullanımı:
- **`.stream()`** → koleksiyondan stream başlat.
- **`.map(y -> ...)`** → her elemana fonksiyon uygula (transform).
- **`.collect(Collectors.toSet())`** → stream'i set'e topla.
- Yan etki yok, immutable, fonksiyonel.

> 🔄 **Neden DTO mapping elle?** Bu projede MapStruct/ModelMapper gibi otomatik mapper kütüphaneleri kullanılmıyor. Eğitim amaçlı **elle yazıldı** — neyin nereye gittiğini açıkça görüyorsun.

---

## 10. Controller Katmanı

```java
@RestController
@RequestMapping("/api/kitaplar")
public class KitaplarController {

    private final KitapServiceImpl kitapService;

    public KitaplarController(KitapServiceImpl kitapService) {
        this.kitapService = kitapService;
    }

    @PostMapping
    public KitapResponse create(@RequestBody CreateKitapRequest request) {
        return kitapService.create(request);
    }

    @GetMapping
    public List<KitapResponse> getAll() {
        return kitapService.getAll();
    }

    @GetMapping("/{id}")
    public KitapResponse getById(@PathVariable Long id) {
        return kitapService.getById(id);
    }

    @PutMapping("/{id}")
    public KitapResponse update(@PathVariable Long id, @RequestBody UpdateKitapRequest request) {
        return kitapService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        kitapService.delete(id);
    }
}
```

### Annotation karşılıkları:

| Annotation | HTTP Karşılığı |
|---|---|
| **`@RestController`** | `@Controller` + `@ResponseBody`. Tüm metotların dönüş değeri otomatik JSON'a serialize edilir. |
| **`@RequestMapping("/api/kitaplar")`** | Bu sınıftaki tüm endpoint'lerin önüne ekli prefix. |
| **`@PostMapping`** | `POST /api/kitaplar` |
| **`@GetMapping`** | `GET /api/kitaplar` |
| **`@GetMapping("/{id}")`** | `GET /api/kitaplar/5` (path variable) |
| **`@PutMapping("/{id}")`** | `PUT /api/kitaplar/5` |
| **`@DeleteMapping("/{id}")`** | `DELETE /api/kitaplar/5` |
| **`@PathVariable Long id`** | URL'deki `{id}` segmentini parametreye bağlar. |
| **`@RequestBody CreateKitapRequest request`** | Request body'sindeki JSON'u DTO'ya parse eder (Jackson). |

### REST endpoint listesi (özet)

| Method | URL | İş |
|---|---|---|
| POST | `/api/kitaplar` | Kitap ekle |
| GET | `/api/kitaplar` | Tüm kitaplar |
| GET | `/api/kitaplar/{id}` | Tek kitap |
| PUT | `/api/kitaplar/{id}` | Güncelle |
| DELETE | `/api/kitaplar/{id}` | Sil |

Aynı pattern **12 controller'da tekrarlanıyor:** `/api/ogrenciler`, `/api/kategoriler`, `/api/yazarlar`, `/api/yayinevleri`, `/api/gorevliler`, `/api/yetkiler`, `/api/odunclamalar`, `/api/cezalar`, `/api/rezervasyonlar`, `/api/iadeler`, `/api/kitapkopyalari`.

---

## 11. DTO Katmanı

### Neden DTO?
Entity'leri doğrudan controller'dan döndürmek **kötü pratiktir**:
- Entity, DB şemasına bağlı; değiştirince API kırılır.
- Hassas alanlar (sifreHash, maas) sızabilir.
- Lazy loading proxy'leri serialize ederken **N+1** veya `LazyInitializationException` patlatır.
- API response shape'i ile DB shape'i aynı olmak zorunda değil.

### Bu projede DTO yapısı

Her entity için 3 DTO:
- **`CreateXxxRequest`** → POST body
- **`UpdateXxxRequest`** → PUT body
- **`XxxResponse`** → GET/POST response

Örnek `CreateKitapRequest`:
```java
public class CreateKitapRequest {
    private String isbn;
    private Long kategoriId;        // ID al, entity değil
    private Long yayineviId;
    private String baslik;
    private Integer yayinYili;
    private Integer sayfaSayisi;
    private String dil;
    private Integer baskiNo;
    private String aciklama;
    private Set<Long> ekKategoriIdleri;     // İlişki için ID listesi
    private Set<Long> yazarIdleri;
    // getter/setter'lar
}
```

> 💡 **Niçin ID, entity değil?** Client zaten kategoriyi göndermiyor, sadece "şu kategoriye bağla" diyor. Service ID'yi alıp DB'den yükler.

`KitapResponse` örneği:
```java
public class KitapResponse {
    private Long kitapId;
    private String isbn;
    private String baslik;
    ...
    private Long kategoriId;
    private String kategoriAd;          // Entity yerine flatten edilmiş
    private Long yayineviId;
    private String yayineviAd;
    private Set<String> yazarlar;       // "Ad Soyad" formatında string'ler
}
```

---

## 12. OOP Konseptleri ve Bu Projedeki Uygulamaları

### 12.1. Inheritance (Kalıtım)
- **`extends JpaRepository<Kitap, Long>`** → Tüm CRUD metotları **bedava** miras alır.
- Bu projedeki entity'ler birbirinden kalıtım almıyor (düz mapping). Karmaşık projelerde `@MappedSuperclass` veya `@Inheritance(strategy = ...)` kullanılır.

### 12.2. Encapsulation (Kapsülleme)
- Entity ve DTO alanları **`private`**, dışarıya **getter/setter** üzerinden erişiliyor.
- Repository'ler **interface** → implementasyon detayları gizli.

### 12.3. Abstraction (Soyutlama)
- **Repository interface'leri** → "DB ile nasıl konuşulacağını" soyutlar.
- **Service** → "Kitap nasıl oluşturulur" iş kuralını soyutlar.
- **Controller** → "HTTP request nasıl ele alınır" soyutlar.
- Her katman **bir alttakine bağımlı**, üsttekinin nasıl çalıştığını bilmez.

### 12.4. Polymorphism (Çok biçimlilik)
- `JpaRepository<Kitap, Long>` referansı → runtime'da Spring'in ürettiği proxy implementasyonu çalıştırır. Sen interface ile konuşursun, gerçekte concrete class çalışır.
- `Optional<T>` → boş ya da dolu davranışı tek arayüzden çalıştırır.

### 12.5. Composition (Has-A) — Library'de güçlü kullanılmış
```java
public class Kitap {
    private Kategori anaKategori;       // Kitap HAS-A Kategori
    private Yayinevi yayinevi;          // Kitap HAS-A Yayinevi
    private Set<Yazar> yazarlar;        // Kitap HAS-MANY Yazar
}
```
Inheritance yerine composition tercih edilmiş — esneklik için doğru tercih.

### 12.6. Dependency Inversion (DI) Principle
- High-level service, low-level repository **interface'ine** bağımlı, concrete class'a değil.
- Spring IoC Container bu bağımlılıkları **constructor üzerinden enjekte** eder.
- Projedeki tüm `private final XxxRepository ...` alanları bu prensibin uygulamasıdır.

---

## 13. Spring Konseptleri

### 13.1. IoC (Inversion of Control)
"Bağımlılıklarını kendin oluşturma, ben veririm" felsefesi. `new KitapRepository()` yapmıyoruz — Spring Container bunu yönetiyor.

### 13.2. Dependency Injection (DI)
**Üç yöntemi var:**
1. **Constructor injection** ✅ (bu projede tercih edilen)
2. Setter injection
3. Field injection (`@Autowired` field) ❌ (test zor, immutable değil)

### 13.3. Bean
Spring Container'da yönetilen herhangi bir nesne. Bu projede bean'ler:
- 12 `@Service` (KitapServiceImpl, ...)
- 12 `@RestController` (KitaplarController, ...)
- 12 Repository (Spring Data JPA otomatik bean üretir)
- Auto-configured: `DataSource`, `EntityManager`, `TransactionManager`, `Tomcat`, `ObjectMapper` ...

### 13.4. Component Scanning
`@SpringBootApplication`'ın bulunduğu paketten aşağı doğru tüm `@Component`/`@Service`/`@Repository`/`@Controller` annotated sınıflar bean olarak kaydedilir.

### 13.5. Auto-configuration
`spring-boot-starter-data-jpa` classpath'te varsa Spring otomatik:
- `DataSource` bean'i kurar (yaml'daki `datasource.url`'den)
- Hibernate'i `EntityManagerFactory` olarak ayağa kaldırır
- `TransactionManager` ekler
- Repository proxy'lerini üretir

---

## 14. Eksiklikler / İyileştirme Alanları

> Bu liste hem öğrenme hem code review aklı vermesi için.

| Eksiklik | Öneri |
|---|---|
| **Validation eklenmemiş** | `@NotBlank`, `@Email`, `@Size` ile DTO doğrulaması + `@Valid @RequestBody` |
| **Generic `RuntimeException` kullanılmış** | Custom exception (`KitapNotFoundException`) + `@ControllerAdvice` ile global handler |
| **HTTP status code'lar hep 200/500** | `ResponseEntity<T>` ile 201 Created, 204 No Content, 404 Not Found ayırımı |
| **Service interface yok** | `KitapService` interface + `KitapServiceImpl` ayrımı (test mock kolaylaşır) |
| **`getAll()` pagination yok** | `Pageable` parametre + `Page<KitapResponse>` döndür |
| **N+1 problem riski** | `@EntityGraph` veya `JOIN FETCH` ile ilişkileri tek sorguda çek |
| **Lombok kullanılmıyor** | `@Getter @Setter @NoArgsConstructor @AllArgsConstructor` ile boilerplate azalır |
| **Auditing yok** | `createdAt`, `updatedAt`, `createdBy` için `@CreatedDate`, `@LastModifiedDate` |
| **API versioning yok** | `/api/v1/kitaplar` gibi versiyonlama |
| **Security yok** | Spring Security + JWT/OAuth |
| **Şifreler düz metin yaml'da** | `${DB_PASSWORD}` env var, `application-prod.yml` profili |
| **`oduncTarihi` set edilmiyor** | Service `LocalDateTime.now()` atamalı |

---

## 15. Hızlı Komut Referansı

```bash
# Build
./mvnw clean install

# Çalıştır
./mvnw spring-boot:run

# Test
./mvnw test

# Jar oluştur ve çalıştır
./mvnw package
java -jar target/library-0.0.1-SNAPSHOT.jar
```

**PostgreSQL hazırlığı:**
```bash
docker run --name lib-pg -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin \
  -e POSTGRES_DB=library -p 5432:5432 -d postgres:16
```

---

## 16. Tek Cümle Özet

> Library projesi, Spring Boot'un **otomatik konfigürasyon + Spring Data JPA** süper güçleriyle, klasik bir **Controller → Service → Repository → Entity** katmanlı mimaride yazılmış, 12 kaynaklı (entity başına 5 endpoint) toplam **60 REST endpoint**'in CRUD operasyonlarını sağlayan bir kütüphane backend'idir.
