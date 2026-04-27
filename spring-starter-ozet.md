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

| Eksiklik | Öneri |
|---|---|
| **Validation eklenmemiş** | DTO'larda `@NotBlank`, `@Size`, `@NotNull` + controller'da `@Valid @RequestBody` |
| **Generic `RuntimeException` fırlatılıyor** | Custom exception'lar (`ProductNotFoundException`) + `@ControllerAdvice` global handler |
| **HTTP status code hep 200/500** | `ResponseEntity<T>` ile 201 Created, 204 No Content, 404 Not Found |
| **Service interface yok** | `ProductService` interface + `ProductServiceImpl` ayrımı (test mock için) |
| **`getAll()` pagination yok** | `Pageable`, `Page<T>` döndür: `Page<ListProductResponse> getAll(Pageable p)` |
| **N+1 problem** | `@EntityGraph(attributePaths = "category")` |
| **Lombok yok** | `@Getter @Setter @NoArgsConstructor` ile boilerplate'i azalt |
| **Tag CRUD'ı boş** (üzerinde tag set'leme yok) | `Product.tags` set etme endpoint'i ekle |
| **Auditing** | `@CreatedDate`, `@LastModifiedDate`, `@CreatedBy` |
| **Şifre yaml'da düz metin** | `${DB_PASSWORD}` env var, `application-prod.yml` profili |
| **Tutarsız annotation stili** | `@UuidGenerator()` vs `@UuidGenerator` — tek stile getir |

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

## 18. Tek Cümle Özet

> Spring-starter projesi, Spring Boot'un **`@RestController` + `@Service` + `JpaRepository` + `@Entity`** dörtlüsünü en yalın şekliyle gösteren, **UUID ID'li 3 entity** üzerinden **15 REST endpoint** sağlayan, kapsamlı DTO ayrıştırması (Create/Created/Get/List/Update/Updated) ile **eğitim odaklı bir CRUD başlangıç projesidir** — Library projesinin daha basit ve yorumlu kardeşi.
