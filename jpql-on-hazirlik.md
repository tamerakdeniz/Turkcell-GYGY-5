# JPQL (Java Persistence Query Language) — Ön Hazırlık

> JPA tabanlı projelerde sorgu yazmanın **must-know** referansı. Aşağıdaki notlar; JPQL'in temel mantığını, sözdizimini, JOIN ve performans davranışlarını, Spring Data JPA ile entegrasyonunu ve sık yapılan hataları kapsar.

---

## 1. JPQL Nedir?

**JPQL (Java Persistence Query Language)**, JPA spesifikasyonunun standart sorgu dilidir. SQL'e çok benzer ama bir **kritik fark** vardır:

> **JPQL tablolar üzerinde değil, entity sınıfları ve onların alanları üzerinde çalışır.**

```sql
-- SQL (tablo ve kolon ismi)
SELECT * FROM customers WHERE country = 'Turkey';
```

```java
// JPQL (entity ve alan ismi)
SELECT c FROM Customer c WHERE c.country = 'Turkey'
```

`customers` tablosu değil → `Customer` entity'si.
`country` kolonu değil → entity'deki `country` field'ı.

### Neden Var?
- **Veritabanı bağımsızlığı:** Aynı JPQL sorgusu MySQL, PostgreSQL, Oracle, H2 üzerinde sorunsuz çalışır. Hibernate altta o veritabanına özgü SQL'e çevirir.
- **Nesne yönelimli düşünme:** İlişkileri JOIN yazmadan `c.orders` gibi ifade edebilirsin.
- **Tip güvenliği (compile-time'a yakın):** Yanlış entity/alan adı runtime'da hata fırlatır; native SQL'de DB çalıştırınca anlarsın.

---

## 2. JPQL vs Diğer Sorgu Yöntemleri

| Yöntem | Çalıştığı Şey | Veritabanı Bağımsız mı? | Ne Zaman Kullanılır? |
|--------|---------------|-------------------------|----------------------|
| **JPQL** | Entity / Field | ✅ Evet | Standart sorgular, JPA projeleri |
| **HQL** | Entity / Field | ✅ Evet (Hibernate'e özel) | JPQL'in Hibernate süper kümesi |
| **Native SQL** | Tablo / Kolon | ❌ Hayır | DB-spesifik özellikler, kompleks sorgular |
| **Criteria API** | Entity / Field (programatik) | ✅ Evet | Dinamik sorgular, type-safe filtreleme |
| **Spring Data Method Naming** | Entity / Field (otomatik) | ✅ Evet | Basit CRUD/find sorguları |

> **Kritik nokta:** HQL, JPQL'in **süper kümesidir**. JPQL standart, HQL Hibernate'in JPQL'e eklediği fazlalıklardır. JPA spesifikasyonuna sadık kalmak istiyorsan JPQL'de kal.

---

## 3. Temel Sözdizimi

### En Basit Sorgu
```java
SELECT p FROM Product p
```
- `Product` → entity sınıf adı (tablo adı değil!)
- `p` → entity için kullanılan **alias** (zorunlu)
- `SELECT p` → tüm `Product` nesnesini döndür

### WHERE ile Filtreleme
```java
SELECT p FROM Product p WHERE p.unitPrice > 100
```

### Belirli Alanı Seçme
```java
SELECT p.name, p.unitPrice FROM Product p
```
> Bu durumda dönüş tipi `List<Object[]>` olur. Her satırda `[name, unitPrice]` yer alır.

### ORDER BY
```java
SELECT p FROM Product p ORDER BY p.unitPrice DESC, p.name ASC
```

### DISTINCT
```java
SELECT DISTINCT p.category FROM Product p
```

---

## 4. ⚠️ Case Sensitivity — En Sık Yapılan Hata

JPQL'de **entity adları ve alan adları büyük/küçük harfe duyarlıdır.** Anahtar kelimeler değildir.

```java
// ✅ DOĞRU
SELECT p FROM Product p WHERE p.unitPrice > 100

// ❌ HATALI — entity adı yanlış
SELECT p FROM product p WHERE p.unitPrice > 100

// ❌ HATALI — field adı camelCase yerine snake_case yazılmış
SELECT p FROM Product p WHERE p.unit_price > 100
```

> **Kural:** Java sınıf adı ne ise JPQL'de o kullanılır. DB'de `unit_price` olsa bile entity'de `unitPrice` ise JPQL'de `unitPrice` yazarsın.

---

## 5. Parametre Kullanımı

### Named Parameter (Tercih Edilen)
```java
@Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
List<Product> findByCategory(@Param("categoryId") Integer categoryId);
```

### Positional Parameter
```java
@Query("SELECT p FROM Product p WHERE p.category.id = ?1")
List<Product> findByCategory(Integer categoryId);
```

> **Kritik nokta:** **Asla string concatenation ile parametre eklemeyin.** SQL Injection açığı yaratır.
>
> ```java
> // ❌ TEHLİKELİ — SQL Injection
> "SELECT p FROM Product p WHERE p.name = '" + userInput + "'"
>
> // ✅ GÜVENLİ
> "SELECT p FROM Product p WHERE p.name = :name"
> ```

---

## 6. JOIN Türleri — Kritik Konu

JPQL'de JOIN, **entity ilişkileri üzerinden** yapılır. `ON` yazmana gerek yoktur; ilişki zaten entity'de tanımlıdır.

### INNER JOIN (Default)
```java
SELECT p FROM Product p JOIN p.category c WHERE c.name = 'Books'
```
> `p.category` ilişkisi varsa otomatik INNER JOIN olur. `ON` yok.

### LEFT JOIN
```java
SELECT c FROM Category c LEFT JOIN c.products p
```
> Kategorisi olan ya da olmayan tüm kategorileri getirir.

### JOIN FETCH — N+1 Probleminin İlacı

```java
SELECT p FROM Product p JOIN FETCH p.category
```

`FETCH` keyword'ü, ilişkili entity'yi **aynı sorguda** yükler. Lazy loading yüzünden oluşan N+1 problemini çözer.

> **N+1 Problemi:** 100 ürün çekersen, sonra her birinin `category`sine erişmek için 100 ek sorgu daha atılır → toplam **101 sorgu**. `FETCH JOIN` ile **1 sorguya** iner.

### JOIN vs JOIN FETCH Farkı
- `JOIN p.category c` → Filtreleme amaçlı, ama `category`'ler lazy kalır.
- `JOIN FETCH p.category` → Filtreleme + ilişkili entity'yi de yükle.

> **Dikkat:** Aynı sorguda **birden fazla collection için `FETCH JOIN`** kullanmak `MultipleBagFetchException` fırlatır. Bu durumda `Set` kullanmak ya da `EntityGraph` ile çözmek gerekir.

---

## 7. Aggregate Fonksiyonlar

```java
SELECT COUNT(p)        FROM Product p
SELECT SUM(p.unitPrice) FROM Product p
SELECT AVG(p.unitPrice) FROM Product p
SELECT MIN(p.unitPrice) FROM Product p
SELECT MAX(p.unitPrice) FROM Product p
```

### GROUP BY + HAVING
```java
SELECT p.category.name, COUNT(p)
FROM Product p
GROUP BY p.category.name
HAVING COUNT(p) > 5
```

> **Kural:** `WHERE` satır filtreleme öncesi, `HAVING` gruplama sonrası filtrelemedir. `HAVING` yalnızca `GROUP BY` ile kullanılır.

---

## 8. Sık Kullanılan Operatörler

```java
// LIKE
SELECT p FROM Product p WHERE p.name LIKE 'Choc%'
SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(:term)

// BETWEEN
SELECT p FROM Product p WHERE p.unitPrice BETWEEN 10 AND 50

// IN
SELECT p FROM Product p WHERE p.category.id IN (1, 2, 3)
SELECT p FROM Product p WHERE p.category.id IN :ids

// IS NULL / IS NOT NULL
SELECT p FROM Product p WHERE p.discontinuedDate IS NULL

// IS EMPTY (collection için)
SELECT c FROM Category c WHERE c.products IS EMPTY

// MEMBER OF
SELECT o FROM Order o WHERE :product MEMBER OF o.items
```

---

## 9. Subquery (Alt Sorgu)

```java
SELECT p FROM Product p
WHERE p.unitPrice > (
    SELECT AVG(p2.unitPrice) FROM Product p2
)
```

> **Dikkat:** JPQL alt sorgularda `FROM` clause'unda subquery kullanmaya **izin vermez**. Sadece `WHERE` ve `HAVING` içinde kullanılabilir. Bu kısıt için Native SQL'e geçmek gerekebilir.

---

## 10. UPDATE ve DELETE Sorguları

```java
@Modifying
@Transactional
@Query("UPDATE Product p SET p.unitPrice = p.unitPrice * 1.1 WHERE p.category.id = :catId")
int increasePricesByCategory(@Param("catId") Integer catId);

@Modifying
@Transactional
@Query("DELETE FROM Product p WHERE p.discontinuedDate < :date")
int deleteOldProducts(@Param("date") LocalDate date);
```

> **Kritik kurallar:**
> 1. `@Modifying` annotation'u **zorunlu** — yoksa Spring sorguyu SELECT sanır.
> 2. `@Transactional` **zorunlu** — değiştirici işlemler transaction içinde olmalı.
> 3. Bulk UPDATE/DELETE **persistence context'i bypass eder.** Cache'deki entity'ler güncel olmaz; dikkat!

---

## 11. Spring Data JPA ile JPQL Kullanımı

### @Query Annotation
```java
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.unitPrice > :price")
    List<Product> findExpensive(@Param("price") BigDecimal price);
}
```

### Native SQL Kullanımı (Gerektiğinde)
```java
@Query(value = "SELECT * FROM products WHERE unit_price > ?1", nativeQuery = true)
List<Product> findExpensiveNative(BigDecimal price);
```
> Native'de tablo/kolon isimleri (snake_case) kullanılır. JPQL'de entity/field (camelCase) kullanılır.

### Pagination
```java
@Query("SELECT p FROM Product p WHERE p.category.id = :catId")
Page<Product> findByCategoryPaged(@Param("catId") Integer catId, Pageable pageable);
```
> Spring Data, JPQL'e otomatik olarak `LIMIT`/`OFFSET` ekler ve `count(*)` sorgusu üretir.

---

## 12. DTO Projection (Constructor Expression)

Tüm entity'yi getirmek yerine, sadece istenen alanları içeren bir DTO döndürmek hem performanslı hem de temiz bir yaklaşımdır.

```java
public class ProductSummaryDto {
    private String name;
    private BigDecimal unitPrice;

    public ProductSummaryDto(String name, BigDecimal unitPrice) {
        this.name = name;
        this.unitPrice = unitPrice;
    }
}
```

```java
@Query("""
    SELECT new com.turkcell.dto.ProductSummaryDto(p.name, p.unitPrice)
    FROM Product p
    WHERE p.unitPrice > :price
""")
List<ProductSummaryDto> findSummaries(@Param("price") BigDecimal price);
```

> **Kritik nokta:** Constructor expression'da **DTO'nun tam paket yolu** yazılmalıdır. Constructor parametre tipleri ve sırası birebir uymalıdır.

---

## 13. Performans — Bilmen Gereken Kritik Davranışlar

### 1. Lazy vs Eager
- **Default:** `@OneToMany`, `@ManyToMany` → LAZY
- **Default:** `@ManyToOne`, `@OneToOne` → EAGER
- EAGER sevimli görünür ama **gizli performans katili**dir. LAZY tercih edilmeli, gerekince `JOIN FETCH` ile çekilmeli.

### 2. N+1 Sorununu Tespit Et
- `application.properties`'a `spring.jpa.show-sql=true` ekleyerek üretilen SQL'leri izle.
- Tek bir HTTP isteği için DB'ye onlarca sorgu gidiyorsa → N+1 problemi.

### 3. Sayım için `COUNT`, varlık kontrolü için `EXISTS`
```java
// ❌ YAVAŞ — tüm kayıtları çeker
boolean exists = !repository.findByEmail(email).isEmpty();

// ✅ HIZLI — sadece exists check
boolean exists = repository.existsByEmail(email);
```

### 4. `setMaxResults` veya Pageable kullan
Sınırlı veriye ihtiyacın varsa tüm tabloyu çekme. JPQL'de `LIMIT` yoktur; `setMaxResults(n)` ya da `Pageable` ile sınırla.

---

## 14. EntityManager ile Direkt JPQL

Spring Data dışında ham JPQL çalıştırmak istersen:

```java
@PersistenceContext
private EntityManager em;

public List<Product> findExpensive(BigDecimal price) {
    return em.createQuery(
        "SELECT p FROM Product p WHERE p.unitPrice > :price",
        Product.class
    )
    .setParameter("price", price)
    .setMaxResults(10)
    .getResultList();
}
```

### TypedQuery vs Query
- `createQuery(jpql, Product.class)` → `TypedQuery<Product>` döner. **Type-safe.**
- `createQuery(jpql)` → `Query` döner. Cast gerekir, hatalara açık.

> **Kural:** Mümkün olduğunda `TypedQuery` kullan.

---

## 15. Sık Yapılan Hatalar — Kontrol Listesi

| Hata | Sebep | Çözüm |
|------|-------|-------|
| `QuerySyntaxException: Product is not mapped` | Tablo adı yazılmış, entity adı değil | Entity sınıf adını kullan |
| `unexpected token` | Anahtar kelime / parantez hatası | JPQL sözdizimini gözden geçir |
| `MultipleBagFetchException` | İki collection birden FETCH ediliyor | `Set` kullan veya `EntityGraph` |
| `LazyInitializationException` | Session kapandıktan sonra lazy field'a erişildi | `JOIN FETCH` veya `@Transactional` scope'u genişlet |
| `Could not resolve property: xxx` | Field adı yanlış (camelCase değil snake_case yazıldı) | Java field adını birebir kullan |
| `@Modifying` olmadan UPDATE | Spring sorguyu SELECT sayar | `@Modifying` + `@Transactional` ekle |
| Pageable count sorgusu yavaş | Otomatik üretilen count karmaşık | `@Query`'de `countQuery` parametresini elle yaz |

---

## 16. Hızlı Referans — Sözdizimi Cheatsheet

```java
// SELECT
SELECT e            FROM Entity e
SELECT e.field      FROM Entity e
SELECT NEW Dto(...) FROM Entity e
SELECT DISTINCT e   FROM Entity e

// JOIN
JOIN  e.relation r            // INNER
LEFT  JOIN e.relation r       // LEFT
JOIN  FETCH e.relation        // İlişkili entity'yi de yükle

// WHERE
WHERE e.field = :param
WHERE e.field IN :list
WHERE e.field BETWEEN :a AND :b
WHERE e.field LIKE :pattern
WHERE e.field IS NULL
WHERE e.collection IS EMPTY

// GROUP / ORDER
GROUP BY e.field
HAVING COUNT(e) > 5
ORDER BY e.field DESC

// AGGREGATE
COUNT(e), SUM(...), AVG(...), MIN(...), MAX(...)

// MODIFYING
UPDATE Entity e SET e.field = :val WHERE ...
DELETE FROM Entity e WHERE ...
```

---

## 17. Kapanış — Geliştiricinin Aklında Olması Gerekenler

1. **JPQL ≠ SQL.** Entity ve alan adlarıyla çalışır, tablo/kolon değil.
2. **Case sensitive.** Entity ve field adlarında büyük/küçük harf önemli.
3. **Parametreyi her zaman `:name` veya `?1` ile geç.** String concat = SQL Injection.
4. **JOIN FETCH** N+1'in panzehiridir.
5. **UPDATE/DELETE'te** `@Modifying` + `@Transactional` zorunlu.
6. **DTO projection** ile sadece ihtiyacın olan alanları çek.
7. **`show-sql=true`** ile üretilen SQL'leri görmeyi alışkanlık yap.
8. **DB-spesifik özellik** gerekiyorsa Native SQL'e geç, ama `nativeQuery=true` flag'ini unutma.
