# 🌱 Spring-Starter Projesi — Çalışma Özeti

> **Spring Boot 4.0.5 + Java 21 + PostgreSQL** üzerinde, **e-ticaret domain'inin minimal hali** (Product, Category, Tag) için yazılmış **eğitim odaklı bir CRUD başlangıç projesi**.
> Library projesinin **küçük kardeşi** ve mantıksal **önceli**: Spring Boot kavramlarını sıfırdan öğrenmek için tasarlanmış, 3 entity üzerinden temel katmanlı mimariyi gösterir.

---

## 1. Projenin Amacı

Bu proje **bir öğrenme aracı**. İçinde tek bir karmaşıklık yok ki "Spring Boot + JPA + REST" üçlüsü net görünsün. Library projesinden farkı:

| Özellik | Spring-Starter | Library |
|---|---|---|
| Entity sayısı | 3 | 12 |
| ID tipi | **`UUID`** (`@UuidGenerator`) | `Long` (`@GeneratedValue IDENTITY`) |
| DTO sayısı | 7 tip × 3 entity = **~21** (Create, Created, List, Get, Update, Updated request/response) | 3 tip × 12 = ~36 |
| İlişki çeşitliliği | M:1 + N:M | M:1, N:M, 1:N, 1:1, self-ref |
| Yorumlar | Türkçe **eğitim açıklamalı** kod | Yorumsuz |
| Hierarchy / kompleks domain | Yok | Var (kategori ağacı, ödünç-iade-ceza) |

---

## 2. Teknoloji Yığını (`pom.xml`)

Library ile **bire bir aynı** dependency seti:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.5</version>
</parent>

<properties>
    <java.version>21</java.version>
</properties>
```

| Dependency | Niçin |
|---|---|
| `spring-boot-starter-webmvc` | REST (Tomcat + Spring MVC + Jackson) |
| `spring-boot-starter-data-jpa` | Hibernate + Spring Data JPA |
| `spring-boot-starter-validation` | DTO doğrulama (henüz kullanılmamış) |
| `spring-boot-starter-actuator` | `/actuator/health` ve metric endpoint'leri |
| `postgresql` (runtime) | PostgreSQL JDBC driver |
| `*-test` | JUnit, Mockito, Spring Test |

> **Spring Boot starter'ı nedir?** Başlangıç paketi. `spring-boot-starter-data-jpa` dediğinde arka planda Hibernate, JPA API, JDBC, transaction, connection pool (HikariCP) gibi 10+ kütüphaneyi senin için derlemiş, sürümleri uyumlu olacak şekilde **transitive** dependency olarak ekliyor.

---

## 3. Klasör Yapısı

```
spring-starter/
├── pom.xml
├── mvnw, mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/turkcell/spring_starter/
    │   │   ├── SpringStarterApplication.java   # Entry point
    │   │   ├── controller/                     # 3 REST controller
    │   │   │   ├── ProductsController.java
    │   │   │   ├── CategoriesController.java
    │   │   │   └── TagsController.java
    │   │   ├── service/                        # 3 service impl
    │   │   │   ├── ProductServiceImpl.java
    │   │   │   ├── CategoryServiceImpl.java
    │   │   │   └── TagServiceImpl.java
    │   │   ├── repository/                     # 3 JpaRepository
    │   │   │   ├── ProductRepository.java
    │   │   │   ├── CategoryRepository.java
    │   │   │   └── TagRepository.java
    │   │   ├── entity/                         # 3 entity
    │   │   │   ├── Product.java
    │   │   │   ├── Category.java
    │   │   │   └── Tag.java
    │   │   └── dto/                            # 21 DTO (düz, alt-paket yok)
    │   │       ├── CreateProductRequest.java
    │   │       ├── CreatedProductResponse.java
    │   │       ├── GetProductResponse.java
    │   │       ├── ListProductResponse.java
    │   │       ├── UpdateProductRequest.java
    │   │       ├── UpdatedProductResponse.java
    │   │       └── ... (Category & Tag için aynı pattern)
    │   └── resources/
    │       └── application.yaml
    └── test/
```

> **DTO organizasyonu farkı:** Library'de `dto/kitap/`, `dto/ogrenci/` gibi alt-paketler var. Burada düz tek paket. **Az dosya varken düz paket OK**, çoğaldıkça alt-paket lazım olur.

---

## 4. Konfigürasyon (`application.yaml`)

```yaml
spring:
  application:
    name: spring-starter
  datasource:
    url: jdbc:postgresql://localhost:5432/eticaret
    username: admin
    password: admin
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

- **DB adı:** `eticaret` (library'de `library`'ydi)
- **`ddl-auto: update`** → entity değiştirsen tablo otomatik `ALTER TABLE` ile güncellenir
- **`show-sql: true`** → konsola SQL basar

---

## 5. Entry Point — `SpringStarterApplication.java`

```java
@SpringBootApplication
// Anotation => Bulunduğu class, fonk, değişkene özellik kazandıran yapıdır.
// SpringBootApplication => Spring Boot uygulaması olduğunu belirtir.
public class SpringStarterApplication {

    // Entrypoint => Uygulamanın başlangıç noktasıdır.
    // Uygulama çalıştığında ilk olarak main metodu çalışır.
    public static void main(String[] args) {
        SpringApplication.run(SpringStarterApplication.class, args);
    }
}
```

### `@SpringBootApplication` üç şeyin birleşimi:

| Annotation | İş |
|---|---|
| **`@Configuration`** | Sınıf, Spring bean tanımlama yeri olarak işaretlenir. |
| **`@EnableAutoConfiguration`** | Classpath'e bakarak (jpa, postgresql, web) otomatik bean konfigürasyonu yapar. |
| **`@ComponentScan`** | Bulunduğu paketten **alt paketlere** doğru `@Component`/`@Service`/`@RestController`/`@Repository` annotated sınıfları bulur. |

### `SpringApplication.run(...)` ne yapar?
1. **ApplicationContext** (IoC container) ayağa kaldırır.
2. Component scan ile bean'leri bulur, instance üretir.
3. **Embedded Tomcat'i** 8080'de başlatır.
4. Uygulamayı çalışır halde bekletir.

> 🔑 **Bütün uygulama tek bir ana sınıfla başlar.** Tomcat'i ayrıca kurmuyorsun, war deploy etmiyorsun. Spring Boot bunu "fat jar" felsefesiyle hallediyor.

---

## 6. Veri Akışı — Katmanlı Mimari

```
HTTP Request (JSON)
       ▼
┌─────────────────────────────────┐
│ CONTROLLER                      │  @RestController, @RequestMapping
│ ProductsController.create()     │  Routing + JSON ↔ DTO (Jackson)
└─────────────────────────────────┘
       ▼  CreateProductRequest (DTO)
┌─────────────────────────────────┐
│ SERVICE                         │  @Service
│ ProductServiceImpl.create()     │  Business logic + ilişki yükleme
└─────────────────────────────────┘
       ▼  Product (Entity)
┌─────────────────────────────────┐
│ REPOSITORY                      │  extends JpaRepository<Product, UUID>
│ productRepository.save(product) │  Hibernate ile DB erişimi
└─────────────────────────────────┘
       ▼  SQL
┌─────────────────────────────────┐
│ POSTGRESQL                      │
│ products tablosu                │
└─────────────────────────────────┘
       ▼
       ▲ (response yukarı çıkar: Entity → DTO → JSON)
```

### Somut akış: POST /api/products

1. **Client gönderir:**
   ```json
   {
     "name": "iPhone 15",
     "description": "Apple smartphone",
     "categoryId": "abc-123-uuid"
   }
   ```

2. **Controller'a düşer** (`ProductsController.create`):
   ```java
   @PostMapping
   public CreatedProductResponse create(@RequestBody CreateProductRequest req) {
       return productService.create(req);
   }
   ```
   `@RequestBody` → Jackson, JSON'u `CreateProductRequest` DTO'sunun setter'larıyla doldurur.

3. **Service çalışır** (`ProductServiceImpl.create`):
   ```java
   Category category = categoryRepository.findById(req.getCategoryId())
       .orElseThrow(() -> new RuntimeException("Category not found"));

   Product product = new Product();
   product.setName(req.getName());
   product.setDescription(req.getDescription());
   product.setCategory(category);

   product = productRepository.save(product);
   ```
   - **`findById` neden?** ID'nin DB'de var olduğunu doğrula + **gerçek entity** referansı al.
   - **`save` ne yapar?** ID null → INSERT; ID var → UPDATE. Hibernate karar verir.

4. **Response oluştur** ve döndür:
   ```java
   CreatedProductResponse resp = new CreatedProductResponse();
   resp.setId(product.getId());
   resp.setName(product.getName());
   resp.setCategoryId(product.getCategory().getId());
   return resp;
   ```

5. **Jackson JSON'a çevirir,** Tomcat 200 OK ile döner.

---

## 7. Entity Katmanı (3 Entity)

### 7.1. `Product.java`

```java
@Entity
@Table(name="products")
public class Product {
    // @GeneratedValue(strategy = GenerationType.IDENTITY) -> 1'er 1'er artan strateji.
    @Id
    @UuidGenerator()
    @Column(name="id")
    private UUID id;

    @Column(name="name", nullable = false, length = 100)
    private String name;

    @Column(name="description", length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name="category_id", nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
        name="product_tags",
        joinColumns = @JoinColumn(name="product_id"),
        inverseJoinColumns = @JoinColumn(name="tag_id")
    )
    private Set<Tag> tags;
}
```

#### Annotation'lar tek tek:

| Annotation | Anlam |
|---|---|
| **`@Entity`** | JPA tarafından yönetilen domain sınıfı. |
| **`@Table(name="products")`** | DB tablo adı `products`. |
| **`@Id`** | Primary key. |
| **`@UuidGenerator()`** | Hibernate'in özel annotation'u. ID null bırakılırsa **otomatik UUID üretir**. |
| **`@Column(name="...", nullable=false, length=100)`** | DB kolon detayları. NOT NULL + VARCHAR(100). |
| **`@ManyToOne`** | "Çok ürün, bir kategoriye bağlı." FK kolonu **bu tabloda** (`category_id`). |
| **`@JoinColumn(name="category_id", nullable=false)`** | FK kolonunun adı ve NOT NULL kısıtı. |
| **`@ManyToMany`** | "Çok ürün, çok tag." Ara tablo gerekir. |
| **`@JoinTable(...)`** | Junction table tanımı. `joinColumns` = bu entity'nin FK'si, `inverseJoinColumns` = karşı entity'nin FK'si. |

### 7.2. UUID vs. Long ID — Neden UUID?

```java
@Id @UuidGenerator
@Column(name = "id")
private UUID id;
```

| Avantaj | Dezavantaj |
|---|---|
| Tahmin edilemez (security) | Daha uzun (16 byte vs 8 byte) |
| Dağıtık sistemlerde çakışma yok | DB index'te yer kaplar |
| Microservice'ler kendi başlarına üretebilir | Insanlara okunabilir değil |
| URL'de güvenli (sıralı değil) | Insertion performans biraz düşer |

> **Library'de `Long` neden?** Tek instance + tek DB; auto-increment kolay ve yeterli. **Spring-starter'da UUID** öğretici amaçla — UUID-based ID kullanmayı göstermek için.

> ⚠️ **Hata uyarısı:** `Category.java` ve `Tag.java`'da `@UuidGenerator` (parantezsiz), `Product.java`'da `@UuidGenerator()` (parantezli). İkisi de **aynı çalışır** — annotation default attribute'larıyla geçer. Kod tutarlı yazılırsa daha iyi.

### 7.3. `Category.java`

```java
@Entity
@Table                                    // name belirtilmemiş → default "category"
public class Category {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
```

#### `@OneToMany(mappedBy = "category")`
- "Bir kategori, çok ürüne sahip."
- **`mappedBy = "category"`** → "FK karşı tarafta (`Product.category`), ben sadece okuma için tutuyorum."
- **Bu taraf sahibi (owner) DEĞİL** → cascade ve insert davranışı `Product` tarafından yönetilir.

### 7.4. `Tag.java`

```java
@Entity
@Table(name="tags")
public class Tag {
    @Id @UuidGenerator
    @Column(name="id")
    private UUID id;

    @Column(name="name", nullable = false, length = 100)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Product> products;
}
// mappedBy ile ilişkiyi Product sınıfındaki tags alanına bağlıyoruz.
// Bu, Tag sınıfının ürünlerle olan ilişkisini yönetmediği anlamına gelir.
// İlişki yönetimi Product sınıfında yapılır.
```

### 7.5. İlişki Şeması (Görsel)

```
┌──────────────┐         ┌──────────────┐
│   CATEGORY   │ 1 ─── N │   PRODUCT    │ N ─── M ┌──────┐
│   id (UUID)  │         │   id (UUID)  │         │  TAG │
│   name (UQ)  │ ◄────── │   name       │ ─────► │  id  │
│              │  FK     │   description│  via    │  name│
│              │         │   category   │ junction│      │
└──────────────┘         │   tags       │ table   └──────┘
                         │              │  product_tags
                         └──────────────┘
```

**Tablo karşılıkları (DB seviyesi):**
- `category` → id, name
- `products` → id, name, description, **category_id (FK)**
- `tags` → id, name
- `product_tags` (junction) → product_id, tag_id

---

## 8. Repository Katmanı

```java
public interface ProductRepository extends JpaRepository<Product, UUID> {
}

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}

public interface TagRepository extends JpaRepository<Tag, UUID> {
}
```

Her biri tek satır. Yine de **bedava** olarak:

| Metot | İş |
|---|---|
| `save(entity)` | Insert / update |
| `findById(UUID)` | `Optional<Entity>` döner |
| `findAll()` | Tüm kayıtlar |
| `findAllById(ids)` | Liste ID'lere göre çoklu okuma |
| `delete(entity)`, `deleteById(id)` | Sil |
| `count()`, `existsById(id)` | Yardımcı |

> 🔮 **Sihir nasıl çalışıyor?** Spring Data JPA, runtime'da bu interface için bir **proxy class** üretir. Uygulamada `productRepository.save(...)` çağırdığında, aslında bu proxy, Hibernate `EntityManager.persist(...)` çağırır.

> 💡 **`<Product, UUID>` generic'i:** Birinci tip = entity sınıfı, ikinci tip = primary key tipi.

---

## 9. Service Katmanı

> ⚠️ **Not:** Bu bölümün kod parçaları **2026-05-01 öncesi** snapshot'ı yansıtır (`RuntimeException` + `CategoryRepository` doğrudan enjekte). Servislerin **güncel halinde** her servis sadece kendi repository'sine bağlı, interface'ler eklendi ve `RuntimeException` → `EntityNotFoundException`'a çevrildi. Güncel mimari için **Bölüm 19**'a bakın.

### 9.1. `ProductServiceImpl.java` — Tam analiz

```java
@Service
public class ProductServiceImpl {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // Constructor injection — Spring otomatik enjekte eder
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public CreatedProductResponse create(CreateProductRequest req) {
        Category category = categoryRepository.findById(req.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + req.getCategoryId()));

        Product product = new Product();
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setCategory(category);

        product = productRepository.save(product);   // ID burada generate olur

        CreatedProductResponse response = new CreatedProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategoryId(product.getCategory().getId());

        return response;
    }
    ...
}
```

### 9.2. `CategoryServiceImpl.java` — Eğitim açıklamalı

```java
public CreatedCategoryResponse create(CreateCategoryRequest req) {
    // Veritabanında insert-update işlemi çalıştırır.
    // Eğer id alanı null ise insert, değilse update işlemi yapar.
    // Entity id'e sahipse update işlemi yapar, yoksa (null) insert işlemi yapar.

    Category category = new Category();
    category.setName(req.getName());

    category = this.categoryRepository.save(category);  // ekledikten sonraki halini al

    CreatedCategoryResponse response = new CreatedCategoryResponse();
    response.setId(category.getId());
    response.setName(category.getName());

    return response;
}
```

#### Niçin `category = repository.save(category)` (geri atama)?
`save(...)` çağrısından sonra entity'nin **ID'si DB tarafından doldurulur** (UUID üretilir). Aynı referans olsa da explicit yeniden atayarak okurun "bundan sonra ID dolu olan haliyle çalışıyorum" mesajını alıyoruz.

### 9.3. `getAll()` — Stream API ile mapping

```java
public List<ListProductResponse> getAll() {
    List<Product> products = productRepository.findAll();

    return products.stream().map(product -> {
        ListProductResponse listProductResponse = new ListProductResponse();
        listProductResponse.setId(product.getId());
        listProductResponse.setName(product.getName());
        listProductResponse.setDescription(product.getDescription());
        listProductResponse.setCategoryName(product.getCategory().getName());
        return listProductResponse;
    }).collect(Collectors.toList());
}
```

#### Stream API kullanımı:

| Adım | İş |
|---|---|
| `.stream()` | List'ten stream başlat |
| `.map(product -> { ... })` | Her elemana lambda uygula (Product → ListProductResponse) |
| `.collect(Collectors.toList())` | Stream'i List'e topla |

> ⚠️ **N+1 problem riski:** `product.getCategory().getName()` her ürün için ayrı `SELECT category WHERE id=?` çalıştırabilir. Çözüm: `@EntityGraph` veya `JOIN FETCH`.

### 9.4. `update()`

```java
public UpdatedProductResponse update(UUID id, UpdateProductRequest req) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

    Category category = categoryRepository.findById(req.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found with id: " + req.getCategoryId()));

    product.setName(req.getName());
    product.setDescription(req.getDescription());
    product.setCategory(category);

    product = productRepository.save(product);

    UpdatedProductResponse response = new UpdatedProductResponse();
    // ... fields
    return response;
}
```

> 💡 **`save()` neden hem create'de hem update'de?** Hibernate, ID kontrolüyle karar verir:
> - ID null → `INSERT INTO products ...`
> - ID dolu (entity managed) → `UPDATE products SET ... WHERE id = ?`

### 9.5. `delete()`

```java
public void delete(UUID id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    productRepository.delete(product);
}
```

`productRepository.deleteById(id)` da olurdu, ama önce `findById` ile **var mı kontrolü** yapılıyor — yoksa anlamlı exception mesajı veriyor.

---

## 10. Controller Katmanı

> ⚠️ **Not:** Bu bölümün `ProductsController` örneği 2026-05-01 öncesi snapshot. Aradaki ara state'te `ProductsController` sadece `POST` içeriyordu; **güncel halde** tam CRUD eklendi ve `Impl` yerine `ProductService` interface'ine bağlandı. Güncel endpoint listesi için **Bölüm 19.7**.

### `ProductsController.java`

```java
@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductServiceImpl productService;

    public ProductsController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @PostMapping
    public CreatedProductResponse create(@RequestBody CreateProductRequest req) {
        return productService.create(req);
    }

    @GetMapping
    public List<ListProductResponse> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public GetProductResponse getById(@PathVariable UUID id) {
        return productService.getById(id);
    }

    @PutMapping("/{id}")
    public UpdatedProductResponse update(@PathVariable UUID id, @RequestBody UpdateProductRequest req) {
        return productService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        productService.delete(id);
    }
}
```

### `CategoriesController.java` (eğitim yorumlarıyla)

```java
// Bu projedeki tüm entityler için tüm CRUD işlemleri kodlanmalı.
// GET-GET BY ID-ADD-UPDATE-DELETE işlemleri kodlanmalı. (CRUD)
// Kütüphane sisteminizi code-first oluşturun.

// (sonraki ders) JPQL (Java Persistence Query Language):
// SQL'e benzer bir sorgu dilidir. Entityler üzerinden sorgulama yapmamızı sağlar.
// SQL'den farklı olarak tablo isimleri yerine entity isimleri kullanılır.
// JPQL, JPA tarafından sağlanan bir sorgu dilidir.
```

### REST Endpoint Listesi

| Resource | Endpoint Pattern |
|---|---|
| **Products** | `GET/POST /api/products`, `GET/PUT/DELETE /api/products/{uuid}` |
| **Categories** | `GET/POST /api/categories`, `GET/PUT/DELETE /api/categories/{uuid}` |
| **Tags** | `GET/POST /api/tags`, `GET/PUT/DELETE /api/tags/{uuid}` |

Toplam **15 endpoint**.

---

## 11. DTO Katmanı — Çok Detaylı Pattern

> ⚠️ **Not:** Bu bölüm DTO'ların **POJO halini** (private alan + getter/setter) anlatır — pattern'ı görmek için. **2026-05-01 itibarıyla tüm DTO'lar `record`'a çevrildi**, validation eklendi. Güncel record örnekleri için **Bölüm 19.3**.

Library'de her entity için **3 DTO** vardı (Create, Update, Response). Spring-starter'da **6 DTO** var:

| DTO | Kullanım |
|---|---|
| `CreateXxxRequest` | POST request body |
| `CreatedXxxResponse` | POST response body |
| `ListXxxResponse` | GET (collection) response |
| `GetXxxResponse` | GET by id response |
| `UpdateXxxRequest` | PUT request body |
| `UpdatedXxxResponse` | PUT response body |

### Niçin bu kadar çok DTO?
Her endpoint farklı veri şekli isteyebilir:
- **List response** = sadece özet alanlar (kategori adı flat string olarak)
- **Get by id response** = tüm detaylar (id, ad, açıklama, kategori id, kategori adı)
- **Created response** = oluşturulan ID + minimum bilgi
- **Updated response** = güncellenmiş ID + minimum bilgi

Bu **separation of concerns** prensibinin DTO seviyesindeki uygulaması. Tek `ProductResponse` ile karıştırırsan, gereksiz alan ya transfer eder ya da nullable yaparsın → API kontratı muğlaklaşır.

### Örnek DTO'lar

`CreateProductRequest.java`:
```java
public class CreateProductRequest {
    private String name;
    private String description;
    private UUID categoryId;     // ID al, entity değil
    // getter/setter
}
```

`CreatedProductResponse.java` — `id` döner (yeni oluşturulan):
```java
public class CreatedProductResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID categoryId;
}
```

`ListProductResponse.java` — `categoryName` flatlanmış:
```java
public class ListProductResponse {
    private UUID id;
    private String name;
    private String description;
    private String categoryName;     // category nesnesi yok, sadece adı
}
```

`GetProductResponse.java` — hem ID hem ad:
```java
public class GetProductResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID categoryId;
    private String categoryName;
}
```

> 💡 **Library'deki "tek `KitapResponse`" yaklaşımı vs spring-starter'daki "6 ayrı response":**
> - Tek response → daha az dosya, esnek ama nullable alanlar olabilir
> - Çoklu response → boilerplate fazla ama API contract net
> - Üretimde ekibinizin tercih ettiği stil seçilir

---

## 12. OOP Konseptleri (Bu projede gözlemlenen)

### 12.1. Encapsulation (Kapsülleme)
Tüm entity ve DTO alanları `private`, dışarıya `public getter/setter` ile açılıyor. Doğrudan field erişimi yok.

### 12.2. Inheritance (Kalıtım)
- `ProductRepository extends JpaRepository<Product, UUID>` → **interface inheritance**
- `Product`, `Category`, `Tag` entity'leri kalıtım kullanmıyor (düz mapping)

### 12.3. Polymorphism (Çok biçimlilik)
- `JpaRepository<Product, UUID>` referansı, runtime'da Spring proxy implementasyonunu çalıştırır.
- `Optional<T>`'in `ifPresent()`, `orElseThrow()` gibi davranışları farklı durumlarda farklı davranır.

### 12.4. Abstraction (Soyutlama)
- **Repository** → "Veritabanına nasıl ulaşılır" detayını gizler.
- **Service** → "Ürün nasıl oluşturulur" iş kuralını gizler.
- **Controller** → "HTTP nasıl ele alınır" detayını gizler.

### 12.5. Composition (HAS-A)
```java
public class Product {
    private Category category;       // Product HAS-A Category
    private Set<Tag> tags;           // Product HAS-MANY Tag
}
```

### 12.6. Dependency Inversion
Service, low-level repository **interface'ine** bağımlı (concrete class'a değil):
```java
private final ProductRepository productRepository;   // interface!
```
Spring'in IoC container'ı runtime'da gerçek implementation'ı (proxy) enjekte eder.

---

## 13. Spring Konseptleri

### 13.1. IoC (Inversion of Control)
Spring sana bağımlılıkları getirir, sen kendin oluşturmazsın. Bu projede `new ProductRepository()` ya da `new CategoryServiceImpl()` yapan **hiçbir satır yok** — Spring her şeyi enjekte ediyor.

### 13.2. Dependency Injection — Constructor Yöntemi
```java
private final ProductRepository productRepository;

public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
}
```
- **`final`** alan = immutable
- Spring 4.3+ ile **tek constructor varsa `@Autowired` zorunlu değil**
- Test edilebilir: `new ProductServiceImpl(mockRepo)`

### 13.3. Bean Lifecycle
1. Spring Boot başlar
2. `@ComponentScan` → `@Service`, `@RestController` etc. annotated sınıfları bulur
3. Her sınıfın **bir instance**'ını oluşturur (default scope: **Singleton**)
4. Constructor parametrelerinden bağımlılıkları çözer (DI)
5. Tüm bean'ler hazır → uygulama request kabul edebilir

### 13.4. Auto-configuration (Magic'in açıklaması)
`spring-boot-starter-data-jpa` classpath'te varsa Spring otomatik:
- `application.yaml`'daki `datasource.url`'den `DataSource` bean'i
- Hibernate `EntityManagerFactory`
- `JpaTransactionManager`
- Repository proxy'leri

Hiçbirini elle yazmıyorsun. **Convention over configuration** felsefesi.

---

## 14. Önemli Syntax / Java Kavramları

### 14.1. `Optional<T>`
```java
Optional<Product> opt = productRepository.findById(id);
Product p = opt.orElseThrow(() -> new RuntimeException("Not found"));
```
- Java 8'le geldi
- `null` yerine **boş olabilen bir kapsayıcı**
- Method'ları: `isPresent()`, `ifPresent(Consumer)`, `orElse(default)`, `orElseThrow(Supplier)`, `map(Function)`

### 14.2. Lambda Expressions
```java
() -> new RuntimeException("Not found")     // Supplier<RuntimeException>
product -> product.getName()                 // Function<Product, String>
(a, b) -> a + b                              // BiFunction<Integer, Integer, Integer>
```
**Lambda'nın syntax'ı:** `(parametreler) -> { gövde }`. Tek satır olunca süslü parantez gerekmez.

### 14.3. Stream API
```java
products.stream()
    .map(p -> {                               // her ürünü dönüştür
        ListProductResponse r = new ListProductResponse();
        r.setId(p.getId());
        ...
        return r;
    })
    .collect(Collectors.toList());            // List'e topla
```
**Diğer faydalı operasyonlar:** `.filter(predicate)`, `.sorted()`, `.distinct()`, `.count()`, `.findFirst()`, `.anyMatch(...)`.

### 14.4. UUID
```java
import java.util.UUID;

UUID id = UUID.randomUUID();              // Yeni rastgele UUID
UUID parsed = UUID.fromString("abc-...");  // String'den parse
String s = id.toString();                  // String'e çevir
```

### 14.5. Method Reference
```java
products.stream().map(this::toResponse)    // (p) -> this.toResponse(p) yerine
```

---

## 15. Eksiklikler / İyileştirme Alanları

> 📌 İşaretler: ✅ Bölüm 18 ve/veya Bölüm 20 ile çözüldü, 🟡 kısmi, ⏳ açık.

| Eksiklik | Durum / Öneri |
|---|---|
| **Validation eklenmemiş** | ✅ Tüm CRUD DTO'ları (Create/Update + Tag/Category/Product/User) record olarak `@NotBlank`, `@Length`, `@NotNull`, `@Email` ile korunuyor. Controller'larda `@Valid` aktif. |
| **Generic `RuntimeException` fırlatılıyor** | ✅ Tüm servislerde `EntityNotFoundException` (404) ile değiştirildi (Bölüm 20). |
| **HTTP status code hep 200/500** | ✅ 201/400/401/404/409 dönüyor; `BusinessException.status` polymorphism'iyle yönetiliyor. |
| **Service interface yok** | ✅ `CategoryService`, `ProductService`, `TagService`, `UserService` interface'leri eklendi; controller'lar interface'e bağlı. (Bkz. Bölüm 20 — interface'ler zorunlu değil, tercihe göre kaldırılabilir.) |
| **Service-to-service repo erişimi** | ✅ `ProductServiceImpl` artık `CategoryRepository`'yi enjekte etmiyor; `Category` entity'sini `CategoryService.getCategoryById()` üzerinden alıyor. Her servis sadece **kendi repository**'sine bağlı. |
| **`getAll()` pagination yok** | ⏳ `Pageable`, `Page<T>` döndür: `Page<ListProductResponse> getAll(Pageable p)` |
| **N+1 problem** | ⏳ `@EntityGraph(attributePaths = "category")` |
| **Lombok yok** | ✅ DTO'lar **record**'a çevrilince boilerplate problemi çözüldü (entity'ler hâlâ POJO; orada Lombok düşünülebilir). |
| **Tag CRUD'ı boş** (üzerinde tag set'leme yok) | 🟡 Tag'ler için tam CRUD (Create/Get/List/Update/Delete) eklendi; ürüne tag bağlama endpoint'i hâlâ yok. |
| **Auditing** | ⏳ `@CreatedDate`, `@LastModifiedDate`, `@CreatedBy` |
| **Şifre yaml'da düz metin** | ⏳ `${DB_PASSWORD}` env var, `application-prod.yml` profili |
| **Tutarsız annotation stili** | ⏳ `@UuidGenerator()` vs `@UuidGenerator` — tek stile getir |

---

## 16. Library ile Karşılaştırma

| Konu | Spring-Starter | Library |
|---|---|---|
| **ID stratejisi** | UUID (`@UuidGenerator`) | Long (`@GeneratedValue IDENTITY`) |
| **Entity sayısı** | 3 (Product, Category, Tag) | 12 (Kitap, Yazar, Ogrenci, ...) |
| **DTO çeşitliliği** | 6 type/entity (Create, Created, Get, List, Update, Updated) | 3 type/entity (Create, Response, Update) |
| **DTO klasörlemesi** | Düz `dto/` | Alt-paket `dto/kitap/`, `dto/ogrenci/` |
| **İlişki çeşitliliği** | M:1 + N:M | M:1, 1:M, N:M, 1:1, self-ref |
| **Kategori hiyerarşisi** | Yok | Var (recursive) |
| **Cascade kullanımı** | Yok | `CascadeType.ALL`, `orphanRemoval=true` |
| **Yorumlar** | Türkçe eğitim açıklamaları | Sade, açıklamasız |
| **Karmaşıklık** | Düşük (eğitim) | Orta (gerçek domain) |
| **Endpoint sayısı** | 15 | 60 |

> 🎓 **Öğrenme sırası:** Önce **spring-starter** ile temelleri kavra, sonra **library** ile çoklu entity ve karmaşık ilişkileri pratiket.

---

## 17. Hızlı Komut Referansı

```bash
# Build
./mvnw clean install

# Çalıştır
./mvnw spring-boot:run

# Test
./mvnw test

# Jar oluştur
./mvnw package
java -jar target/spring-starter-0.0.1-SNAPSHOT.jar
```

**PostgreSQL hazırlığı:**
```bash
docker run --name spring-pg -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin \
  -e POSTGRES_DB=eticaret -p 5432:5432 -d postgres:16
```

**Endpoint test (curl):**
```bash
# Kategori oluştur
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{"name":"Elektronik"}'

# Tüm ürünleri listele
curl http://localhost:8080/api/products
```

---

## 18. Exception Handling & Kimlik Doğrulama Akışı (2026-04-29 Güncelleme)

Bu bölüm, `Eksiklikler` tablosunda (15.) yer alan **"Generic RuntimeException"**, **"HTTP status code hep 200/500"** ve **"Validation eklenmemiş"** maddelerinin **Users akışı** için nasıl çözüldüğünü ve oluşturulan yeni yapıyı özetler.

### 18.1 Neden bu geliştirme?

Önceki durum (sorunlar):

| Sorun | Sonuç |
|---|---|
| `throw new RuntimeException("...")` | Tüm iş kuralı hataları aynı tipten — handler ayrım yapamıyor |
| Tüm hatalar `400 Bad Request` ile dönüyordu | Yanlış semantik: zaten kayıtlı e-posta `409`, yanlış parola `401` olmalı |
| Validasyon hatasında ham `MethodArgumentNotValidException.getMessage()` dönüyordu | Frontend için **structured** değil, parse edilemez tek string |
| `register` endpoint'i `void` → 200 OK + **boş body** | İstemci başarı geri bildirimi alamıyor; cevap belirsiz |
| Aynı pakette `org.springframework.http.HttpStatus`'u **gölgeleyen** boş bir `HttpStatus.java` (BAD_REQUEST = null) duruyordu | Tehlikeli, `@ResponseStatus` annotation'larında null'a yol açabilirdi |

### 18.2 Yeni / değişen dosyalar

```
exception/
├── BusinessException.java            (YENİ — abstract üst sınıf)
├── UserAlreadyExistsException.java   (YENİ)
├── InvalidCredentialsException.java  (YENİ)
├── GlobalExceptionHandler.java       (DEĞİŞTİ — yorum satırındaki ödev tamamlandı)
└── HttpStatus.java                   (SİLİNDİ — ölü/gölgeleyici dosya)

dto/
├── ErrorResponse.java                (YENİ — {title, type, message})
└── ValidationErrorResponse.java      (YENİ — {argument, message: List<String>})

service/UserServiceImpl.java          (DEĞİŞTİ — RuntimeException → custom; register String döner)
controller/UsersController.java       (DEĞİŞTİ — register String döner, @ResponseStatus eklendi)
```

### 18.3 Sınıf hiyerarşisi

```
java.lang.RuntimeException
    └── BusinessException                      (abstract — title, type, status, message)
            ├── UserAlreadyExistsException     → 409 Conflict
            └── InvalidCredentialsException    → 401 Unauthorized
```

`BusinessException` `abstract` çünkü doğrudan fırlatılması anlamsız — her zaman somut bir alt-tipi kullanılmalı. Bu, "kullanıcı `BusinessException` fırlatıp generic bir hata oluşturmasın" güvencesi sağlar.

`BusinessException` 4 alan taşır:
- `title` — kullanıcı dostu başlık ("Kullanıcı zaten mevcut")
- `type` — makine okunabilir kod ("USER_ALREADY_EXISTS")
- `message` (parent class'tan) — ayrıntılı açıklama
- `status` (`org.springframework.http.HttpStatus`) — **her exception kendi HTTP status'unu bilir**

> **Neden status alanı?** Alternatif: handler'da `if (ex instanceof UserAlreadyExistsException) { 409 }` zinciri yazmak. Polymorphism ile bu mantık exception'ın **kendisine** taşındı; handler tek satırda `ResponseEntity.status(ex.getStatus())` yapar. Yeni bir exception eklemek için handler'a dokunmaya **gerek yok**.

### 18.4 Akış diyagramları

#### A) Mutlu yol — Başarılı register

```
Client
  │ POST /api/users  body: {email, password}
  ▼
UsersController.register(@Valid RegisterRequest)
  │ @Valid çalışır → @NotBlank, @Email, @Length kontrolleri geçer
  ▼
UserServiceImpl.registerUser(req)
  │ findByEmail → boş
  │ passwordEncoder.encode(...)
  │ userRepository.save(user)
  │ return "Kayıt başarılı"
  ▼
Controller
  │ @ResponseStatus(CREATED)
  ▼
HTTP 201 Created
Body: "Kayıt başarılı"
```

#### B) Validasyon hatası — boş veya kısa parola

```
Client
  │ POST /api/users  body: {email:"a@b.c", password:""}
  ▼
@Valid → @NotBlank fail
  │ Spring otomatik throw: MethodArgumentNotValidException
  ▼
GlobalExceptionHandler.handleMethodArgumentNotValidException
  │ FieldError listesini Map<field, List<msg>> ile grupla
  │ List<ValidationErrorResponse> oluştur
  ▼
HTTP 400 Bad Request
Body: [
  {"argument":"password", "message":["Parola boş olamaz.","..."]}
]
```

#### C) İş kuralı ihlali — e-posta zaten kayıtlı

```
UserServiceImpl.registerUser
  │ findByEmail → bulundu
  │ throw new UserAlreadyExistsException("Bu e-posta zaten kayıtlı")
  ▼  (Spring AOP exception bubble-up)
GlobalExceptionHandler.handleBusinessException
  │ status = ex.getStatus() = HttpStatus.CONFLICT
  ▼
HTTP 409 Conflict
Body: {
  "title":"Kullanıcı zaten mevcut",
  "type":"USER_ALREADY_EXISTS",
  "message":"Bu e-posta zaten kayıtlı"
}
```

#### D) Login hatası — yanlış parola veya bilinmeyen e-posta

```
UserServiceImpl.login
  │ ya findByEmail boş, ya passwordEncoder.matches false
  │ throw new InvalidCredentialsException("Giriş bilgileri yanlış")
  ▼
GlobalExceptionHandler.handleBusinessException
  ▼
HTTP 401 Unauthorized
Body: {
  "title":"Geçersiz kimlik bilgileri",
  "type":"INVALID_CREDENTIALS",
  "message":"Giriş bilgileri yanlış"
}
```

> **Güvenlik notu:** `login()` "kullanıcı bulunamadı" ve "parola yanlış" durumları için **aynı** mesajı döner. Saldırgan e-posta enumeration yapamaz — kayıtlı e-postayı tahmin edemez.

### 18.5 Status code seçimleri — neden?

| Senaryo | Status | Gerekçe |
|---|---|---|
| `POST /api/users` başarılı | **201 Created** | REST standardı: yeni kaynak oluşturuldu |
| `POST /api/users/login` başarılı | **200 OK** | Yeni kaynak yok; sadece bir doğrulama operasyonu |
| Validasyon hatası (`@Valid` fail) | **400 Bad Request** | İstemci tarafının hatalı veri göndermesi |
| `UserAlreadyExistsException` | **409 Conflict** | Kaynak çakışması — istek formatı doğru ama mevcut state ile çelişiyor |
| `InvalidCredentialsException` | **401 Unauthorized** | Kimlik doğrulanamadı (403 değil — 403 "kimliği biliyorum ama yetkin yok" demek) |
| Yakalanmamış generic `RuntimeException` | **500 Internal Server Error** | Beklenmeyen durum — sunucu hatası |

> **400 vs 409 ayrımı:** "İsteğin kendisi hatalı mı, yoksa istek doğru ama state izin vermiyor mu?" diye sor. E-posta formatı bozuksa **400**; e-posta formatı doğru ama zaten kayıtlıysa **409**.

> **401 vs 403 ayrımı:** Hiç kimliklendirilememişse 401 (kim olduğun belli değil). Kimliğin biliniyor ama bu işlemi yapma yetkin yok ise 403.

### 18.6 ErrorResponse vs ValidationErrorResponse — neden iki ayrı yapı?

| Özellik | `ErrorResponse` | `ValidationErrorResponse` |
|---|---|---|
| Kullanım | İş kuralı / generic hata | Alan bazlı form validasyonu |
| Yapı | **Tek nesne** | **Liste** (her alan için bir öğe) |
| Alanlar | `title`, `type`, `message` (string) | `argument`, `message` (`List<String>`) |
| Status | 4xx (genelde 4xx, 500 fallback) | 400 (yapısal validasyon) |

**Neden ayrı?** Bir login isteğinde **tek hata** vardır ("yanlış bilgi"). Ama bir register isteğinde **birden fazla alan** aynı anda hatalı olabilir (email format yanlış + parola çok kısa). Frontend bunu form üzerine field-by-field göstermek ister: hangi `argument` (alan adı) için hangi `message` (mesaj listesi). İki ayrı yapı, frontend'in `if (Array.isArray(error)) { ...field errors... } else { ...generic error... }` ayrımını net yapmasını sağlar.

### 18.7 GlobalExceptionHandler içindeki üç handler — kapsam matrisi

```
@ExceptionHandler(BusinessException.class)
    → Tüm BusinessException alt-tiplerini yakalar (polymorphism)
    → Spring "en spesifik tipte" eşleşeni seçer

@ExceptionHandler(MethodArgumentNotValidException.class)
    → @Valid fail eden controller method argümanları
    → Sadece bu tipte yakalar (BusinessException'ı kapsamaz)

@ExceptionHandler(RuntimeException.class)  ← FALLBACK
    → Yukarıdaki ikisinden hiçbiri eşleşmezse buraya düşer
    → Spring "en spesifikten en generik'e" doğru eşleştirme yapar
    → BusinessException de bir RuntimeException olmasına rağmen
      üstteki daha spesifik handler önce eşleşir
```

### 18.8 Spring'in arka plan sihri

1. **`@RestControllerAdvice`** → Spring bu sınıfı uygulama başlangıcında bean olarak bulur ve tüm `@RestController`'lar için "global etrafında" çalışan exception handler'ları kayıt eder.
2. **Exception bubble-up** → Service'te `throw` edilen exception, controller'dan geri sıçrayıp Spring DispatcherServlet'in catch bloğuna düşer. Spring `@ExceptionHandler`'lar arasından tipe en uygun olanı çağırır.
3. **`@Valid` zinciri** → Controller method'una `@Valid` annotation'ı yazıldığında Spring otomatik olarak Hibernate Validator'ı tetikler. Hata varsa method gövdesi **hiç çalışmaz**, doğrudan `MethodArgumentNotValidException` fırlatılır.
4. **`ResponseEntity` vs `@ResponseStatus`** → Static (her zaman aynı) status için `@ResponseStatus`. Dinamik (exception'a göre değişen) status için `ResponseEntity.status(...)`. Handler'da exception'dan status okunduğu için `ResponseEntity` tercih edildi.

### 18.9 Eksiklikler tablosundaki etki

| Eksiklik (15. bölüm) | Durum |
|---|---|
| Generic `RuntimeException` fırlatılıyor | ✅ **Tamamen çözüldü** — Product/Category/Tag de `EntityNotFoundException`'a geçti (Bkz. Bölüm 20) |
| HTTP status code hep 200/500 | ✅ **Tamamen çözüldü** (201/400/401/404/409 dönüyor) |
| Validation eklenmemiş | ✅ **Tamamen çözüldü** — Bölüm 20'de tüm CRUD DTO'ları `@Valid` ile korunuyor |

### 18.10 Sonraki adımlar (öneri)

1. ~~Aynı paterni Product/Category/Tag için uygula~~ → ✅ **Yapıldı** (Bölüm 20). Tek `EntityNotFoundException` 4xx mesajıyla; entity başına ayrı sınıf üretmedik (DRY: davranış aynı, sadece mesaj değişiyor).
2. Login başarılı dönüşünü `String` yerine `LoginResponse {token, expiresAt}` yap (JWT için zemin).
3. `RegisterRequest`'e parola karmaşıklık doğrulayıcısı ekle (`@Pattern`).
4. ~~Diğer controller'larda da `@Valid` zorunlu kıl~~ → ✅ **Yapıldı** (Bölüm 20).

---

## 19. Genişletilmiş CRUD + Servis İzolasyonu Refactor'u (2026-05-01)

> Bu bölüm, 2026-05-01'de yapılan **mimari sertleştirme** turunun özetidir. Önceki refactor (Bölüm 18) sadece Users akışını ele almıştı; bu tur Product/Category/Tag akışlarını da aynı standartlara çekiyor ve **service-to-service repo izolasyonu** kuralını netleştiriyor.

### 19.1 Refactor'un kapsamı

| Konu | Önce | Sonra |
|---|---|---|
| **DTO tipi** | POJO (private alanlar + getter/setter) | **Java `record`** (immutable, otomatik accessor, ~70% daha az satır) |
| **Generic `RuntimeException`** | Tüm "not found" durumları | **`EntityNotFoundException` (404)** |
| **Service interface** | Yoktu (controller `Impl`'e bağlı) | `CategoryService`, `ProductService`, `TagService`, `UserService` (controller interface'e bağlı) |
| **`ProductServiceImpl` bağımlılığı** | `ProductRepository` + `CategoryRepository` (cross-aggregate repo) | `ProductRepository` + **`CategoryService`** (service-to-service çağrı) |
| **`CategoryServiceImpl` CRUD** | Eksik (`update`/`delete`/`getById` controller'da çağrılıyordu ama servis tarafında yoktu — derleme hatası riski) | Tam CRUD + `search` + service-to-service için `getCategoryById(UUID)` (entity döner) |
| **`ProductsController`** | Sadece `POST` (4 endpoint eksikti) | Tam CRUD (`GET all`, `GET {id}`, `PUT {id}`, `DELETE {id}`) |
| **CRUD DTO'larında `@Valid`** | Yoktu | `@NotBlank` + `@Length` + `@NotNull` zorunlu; controller'larda `@Valid` |

### 19.2 "Servis kendi repository'sini çağırır" kuralı

#### Kural

> Bir servis, **yalnızca kendi aggregate'ine ait repository**'yi enjekte eder. Başka bir aggregate'in entity'sine ihtiyacı varsa, **o aggregate'in servisini** çağırır; karşı servis kendi repository'siyle veriyi getirir.

#### Önce: ihlal

```java
@Service
public class ProductServiceImpl {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;   // ❌ başka aggregate'in repo'su

    public CreatedProductResponse create(CreateProductRequest req) {
        Category category = categoryRepository.findById(req.getCategoryId())   // ❌
            .orElseThrow(() -> new RuntimeException("Category not found"));
        // ...
    }
}
```

**Niye sorun?**
1. **Encapsulation çatlağı:** Category aggregate'inin "var mı?" kuralı iki yerde tekrar eder (Category servisinde + Product servisinde).
2. **Kategori silme/varlık kuralları değişirse** Product servisi **bilmez** — silent kırılma.
3. Cross-aggregate transaction sınırlarını bulanıklaştırır.
4. Test yazarken Category'nin tüm repo davranışını mock'lamak gerekir.

#### Sonra: kural uygulandı

```java
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;        // ✅ servis (interface)

    public CreatedProductResponse create(CreateProductRequest req) {
        Category category = categoryService.getCategoryById(req.categoryId());  // ✅
        // ... entity'yi al, doğrulama Category aggregate'inin sorumluluğunda
    }
}
```

```java
public interface CategoryService {
    // ... CRUD endpoint'leri için DTO döndüren metotlar ...
    Category getCategoryById(UUID id);   // service-to-service için entity döndüren metot
}

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;   // sadece kendi repo'su

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Kategori bulunamadı: " + id));
    }
}
```

> 💡 **DTO mu entity mi döndürmeli?** İki tip metot var:
> - **Controller'a açılan**: `getById(UUID)` → `GetCategoryResponse` (DTO)
> - **Service-to-service**: `getCategoryById(UUID)` → `Category` (entity, çünkü çağıran servisin entity ile bir şey yapması gerekiyor — ör. JPA ilişki ataması)

### 19.3 DTO'ların record'a dönüşü

Tüm request/response DTO'ları **immutable `record`** oldu. Örnek:

```java
// Önce (CreatedProductResponse.java — 42 satır)
public class CreatedProductResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID categoryId;
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    // ... 4 getter/setter daha
}

// Sonra (1 satır)
public record CreatedProductResponse(UUID id, String name, String description, UUID categoryId) {}
```

Validasyonlu örnek:

```java
public record CreateProductRequest(
    @NotBlank @Length(min = 3, max = 100) String name,
    @Length(max = 500) String description,
    @NotNull UUID categoryId
) {}
```

#### Record kullanırken kazandıklarımız

| Kazanım | Açıklama |
|---|---|
| Immutability | Alan değerleri constructor'da set, sonra değişmez. Thread-safe. |
| Otomatik accessor | `req.getName()` yerine `req.name()` (no-arg accessor; getter prefix yok). |
| Otomatik `equals`/`hashCode`/`toString` | Veri sınıfları için doğal davranış. |
| Daha az satır | ~70% daha az boilerplate. |
| Jackson uyumlu | Spring Boot 3+ ve Jackson 2.12+ record serialization/deserialization'ı doğal destekler. |

#### Servis içi kullanım farkı

Önce: `req.getName()` → Sonra: `req.name()`. Bütün `Impl`'ler bu accessor stiline geçirildi.

### 19.4 `EntityNotFoundException` tasarım kararı

```java
public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String message) {
        super("Kayıt bulunamadı", "ENTITY_NOT_FOUND", message, HttpStatus.NOT_FOUND);
    }
}
```

#### Neden `ProductNotFoundException` + `CategoryNotFoundException` değil?

- Davranış birebir aynı (404 + benzer mesaj).
- Mesajda hangi entity olduğu zaten geçiyor: `"Kategori bulunamadı: <id>"`.
- 3 ayrı sınıf üretmek **DRY ihlali** — sınıf sayısını artırır, yarar getirmez.
- İhtiyaç doğunca (örn. `ProductNotFoundException`'a domain-specific davranış eklenecekse) split edilir.

#### Kullanım

```java
// CategoryServiceImpl
return categoryRepository.findById(id)
    .orElseThrow(() -> new EntityNotFoundException("Kategori bulunamadı: " + id));

// ProductServiceImpl
return productRepository.findById(id)
    .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı: " + id));

// TagServiceImpl
return tagRepository.findById(id)
    .orElseThrow(() -> new EntityNotFoundException("Etiket bulunamadı: " + id));
```

`GlobalExceptionHandler`'a **dokunulmadı** — `@ExceptionHandler(BusinessException.class)` polymorphism ile yeni alt-tipi otomatik yakalıyor. Bu, Bölüm 18.3'teki tasarımın getirisinin pratikteki kanıtı.

### 19.5 Service interface'leri — tartışma

| Lehte | Aleyhte |
|---|---|
| Test mock'lama biraz daha doğal (`@MockBean ProductService`) | Tek impl varsa pure boilerplate |
| Çoklu impl ihtimali (örn. `CachedCategoryServiceImpl`) | YAGNI: gerektiğinde extract edilebilir |
| "Soyutlama hijyeni" — controller `Impl` adına bağlı kalmaz | Spring CGLIB ile interface'siz proxy de mümkün |
| Kurumsal kod tabanlarında geleneksel pattern | Modern Spring rehberleri "interface eklemeden başla" diyor |

> 📝 **Bu projede tutuldu** çünkü hem geleneksel pattern öğretici, hem de service-to-service çağrıda interface'e bağımlılık daha temiz görünüyor. **Zorunlu değil** — bir sonraki refactor'da kaldırılabilir; controller'lar `Impl` sınıfına bağlandığında da çalışır.

### 19.6 Yeni / değişen dosyalar

```
exception/
└── EntityNotFoundException.java        (YENİ — 404 Not Found)

service/
├── CategoryService.java                (YENİ — interface)
├── ProductService.java                 (YENİ — interface)
├── TagService.java                     (YENİ — interface)
├── UserService.java                    (YENİ — interface)
├── CategoryServiceImpl.java            (DEĞİŞTİ — tam CRUD + getCategoryById)
├── ProductServiceImpl.java             (DEĞİŞTİ — tam CRUD + CategoryService DI)
├── TagServiceImpl.java                 (DEĞİŞTİ — RuntimeException → EntityNotFoundException)
└── UserServiceImpl.java                (DEĞİŞTİ — interface implements + record accessor)

controller/
├── CategoriesController.java           (DEĞİŞTİ — interface DI + @Valid)
├── ProductsController.java             (DEĞİŞTİ — eksik 4 endpoint eklendi, interface DI)
├── TagsController.java                 (DEĞİŞTİ — interface DI + @Valid)
└── UsersController.java                (DEĞİŞTİ — interface DI)

dto/  (hepsi POJO → record)
├── CreateCategoryRequest, UpdateCategoryRequest         (validation eklendi)
├── CreatedCategoryResponse, GetCategoryResponse,
│   ListCategoryResponse, UpdatedCategoryResponse
├── UpdateProductRequest                                  (validation eklendi)
├── CreatedProductResponse, GetProductResponse,
│   ListProductResponse, UpdatedProductResponse
├── CreateTagRequest, UpdateTagRequest                   (validation eklendi)
├── CreatedTagResponse, GetTagResponse,
│   ListTagResponse, UpdatedTagResponse
├── RegisterRequest, LoginRequest                        (record + validation)
├── ErrorResponse, ValidationErrorResponse               (record)
```

### 19.7 Endpoint tablosu — güncel

| Method | Path | Açıklama |
|---|---|---|
| **Categories** | | |
| POST | `/api/categories` | Yeni kategori (`@Valid CreateCategoryRequest`) |
| GET | `/api/categories` | Tüm kategoriler |
| GET | `/api/categories/search?query=` | Ada göre arama (JPQL `LIKE`) |
| GET | `/api/categories/{id}` | Kategori detayı |
| PUT | `/api/categories/{id}` | Kategori güncelle (`@Valid UpdateCategoryRequest`) |
| DELETE | `/api/categories/{id}` | Kategori sil |
| **Products** | | |
| POST | `/api/products` | Yeni ürün (kategori varlık kontrolü servis-to-servis) |
| GET | `/api/products` | Tüm ürünler |
| GET | `/api/products/{id}` | Ürün detayı (kategori adı flat olarak) |
| PUT | `/api/products/{id}` | Ürün güncelle |
| DELETE | `/api/products/{id}` | Ürün sil |
| **Tags** | | |
| POST | `/api/tags` | Yeni etiket |
| GET | `/api/tags` | Tüm etiketler |
| GET | `/api/tags/{id}` | Etiket detayı |
| PUT | `/api/tags/{id}` | Etiket güncelle |
| DELETE | `/api/tags/{id}` | Etiket sil |
| **Users** | | |
| POST | `/api/users` | Kayıt — 201 Created |
| POST | `/api/users/login` | Giriş — 200 OK |

**Toplam 18 endpoint** (önce 16'ydı; ProductsController'a 4 endpoint eklendi, search korunuyor).

### 19.8 Sonraki adımlar

1. JWT eklendiğinde `LoginResponse {token, expiresAt}` döndür.
2. `@EntityGraph(attributePaths = "category")` ile N+1 problemini çöz (`Product.getCategory().getName()` her satırda DB sorgusu doğuruyor).
3. `Page<ListProductResponse>` ile pagination.
4. `@UuidGenerator` vs `@UuidGenerator()` tutarsızlığı temizle.
5. Karar: service interface'lerini tutmaya devam mı yoksa kaldır mı? — Ekip kararı.

---

## 20. Tek Cümle Özet

> Spring-starter projesi, Spring Boot'un **`@RestController` + `@Service` + `JpaRepository` + `@Entity`** dörtlüsünü, **UUID ID'li 3 entity** üzerinden **18 REST endpoint** + **kimlik doğrulama** ile gösteren; **record DTO'lar**, **`BusinessException` hiyerarşisi** (`EntityNotFoundException` / `UserAlreadyExistsException` / `InvalidCredentialsException`), **service-to-service repo izolasyonu** ve **interface tabanlı service katmanı** ile orta düzey kurumsal pattern'ları öğreten **eğitim odaklı CRUD başlangıç projesidir** — Library projesinin daha basit ama mimari olarak daha sağlam kardeşi.
