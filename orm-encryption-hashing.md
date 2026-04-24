package com.turkcell.spring_starter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.ListCategoryResponse;
import com.turkcell.spring_starter.entity.Category;
import com.turkcell.spring_starter.repository.CategoryRepository;# ORM, DB Bağlantısı, Encryption & Hashing

> Bir geliştiricinin günlük hayatta kullanacağı **must-know** konular. Aşağıdaki notlar; kavramları, aralarındaki farkları ve Java üzerindeki pratik kullanımlarını kapsar.

---

## 1. ORM (Object-Relational Mapping)

### Nedir?
ORM, **veritabanı tablolarını** uygulamadaki **nesnelere (class/object)** eşleyen bir tekniktir. SQL yazmak yerine nesneler üzerinden veritabanı işlemleri yapılmasını sağlar.

**Günlük hayattan örnek:**
Bir kütüphanede kitap aramak için raflara tek tek bakmak yerine, bir **kütüphane kataloğu** (bilgisayar sistemi) kullanırsın. Sen "Suç ve Ceza" yazarsın, sistem senin yerine rafları tarar. İşte ORM, geliştirici ile veritabanı arasındaki "katalog görevlisi"dir.

### Neden Kullanılır?
- SQL sorgularını manuel yazmak yerine **nesne yönelimli** çalışma imkânı verir.
- Tekrarlayan CRUD (Create, Read, Update, Delete) kodlarını azaltır.
- Veritabanı bağımsızlığı sağlar (MySQL → PostgreSQL geçişi kolaylaşır).
- SQL Injection riskini büyük oranda azaltır (hazır parametrik sorgular).

### ORM Olmadan vs. ORM ile

**Klasik JDBC (ORM yok):**
```java
String sql = "SELECT * FROM users WHERE id = ?";
PreparedStatement ps = conn.prepareStatement(sql);
ps.setInt(1, 5);
ResultSet rs = ps.executeQuery();
while (rs.next()) {
    User u = new User();
    u.setId(rs.getInt("id"));
    u.setName(rs.getString("name"));
}
```

**ORM ile (JPA/Hibernate):**
```java
User user = userRepository.findById(5L).orElseThrow();
```

> 📌 **NOT:** ORM, "boilerplate" (tekrar eden kalıp) kodu azaltır ama her durumda en hızlı çözüm değildir. Karmaşık raporlama sorgularında raw SQL daha verimlidir.

### Java'da Popüler ORM Araçları
| Araç | Açıklama |
|------|----------|
| **Hibernate** | Java'daki en yaygın ORM. JPA standardını implemente eder. |
| **JPA (Jakarta Persistence API)** | Bir standart/spesifikasyon. Hibernate, EclipseLink gibi araçlar bunu uygular. |
| **Spring Data JPA** | JPA'yı Spring ekosisteminde daha da soyutlar. `Repository` interface'leri sağlar. |
| **MyBatis** | Tam ORM değil; SQL yazmaya izin veren "semi-ORM". |

### Temel JPA Annotation'ları (Java)

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
}
```

| Annotation | Görevi |
|-----------|--------|
| `@Entity` | Sınıfın bir veritabanı tablosuna karşılık geldiğini söyler. |
| `@Table` | Tablo adını ve detaylarını belirtir. |
| `@Id` | Primary Key alanı. |
| `@GeneratedValue` | Otomatik artan ID. |
| `@Column` | Sütun ayarları (ad, nullable, length). |
| `@OneToMany`, `@ManyToOne`, `@ManyToMany` | İlişkiler. |

### Spring Data JPA - Repository Kullanımı

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByFullNameContaining(String keyword);
}
```

> 📌 **NOT:** Spring Data JPA method isminden otomatik sorgu üretir. `findByEmail` → `SELECT * FROM users WHERE email = ?`.

---

## 2. DB Bağlantısı (Database Connection)

### Bağlantının Temel Yapısı
Uygulamanın bir veritabanına erişebilmesi için şu 4 bilgi gerekir:
1. **URL** (veritabanı adresi)
2. **Kullanıcı adı**
3. **Şifre**
4. **Driver** (dil ↔ veritabanı arasındaki tercüman)

**Günlük hayattan örnek:**
Bir eve gitmek için: **adres** (URL), **kapı anahtarı** (credentials), **taksi şoförü** (driver) lazım. Şoför olmadan adresi bilsen bile ulaşamazsın.

### Java'da Bağlantı Türleri

#### a) JDBC (En Temel Yol)
```java
String url = "jdbc:postgresql://localhost:5432/mydb";
Connection conn = DriverManager.getConnection(url, "user", "pass");
```

#### b) Spring Boot `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=postgres
spring.datasource.password=123456
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

| Özellik | Açıklama |
|---------|----------|
| `ddl-auto=update` | Entity değişikliklerine göre tabloyu günceller. |
| `ddl-auto=create` | Her başlangıçta tabloyu siler ve yeniden oluşturur. |
| `ddl-auto=validate` | Sadece kontrol eder, değişiklik yapmaz. (prod için güvenli) |
| `ddl-auto=none` | Hiçbir şey yapmaz. |

> 📌 **NOT:** `ddl-auto=create` veya `update` production'da **ASLA** kullanılmamalı! Veri kaybına yol açabilir. Prod'da `validate` veya `none` + migration tool (Flyway/Liquibase).

### Connection Pool (Bağlantı Havuzu)
Her sorgu için yeni bağlantı açmak maliyetlidir. Connection Pool, önceden açılmış bağlantıları tutar ve tekrar kullanır.

**Günlük hayattan örnek:**
Restoranda her müşteri geldiğinde yeni garson işe alınmaz. Önceden işe alınmış garsonlar masaları paylaşır. Connection Pool da böyle çalışır.

**Spring Boot'un default'u: HikariCP**
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.idle-timeout=30000
```

> 📌 **NOT:** Havuz boyutu CPU çekirdek sayısı + disk I/O'ya göre ayarlanır. Çok büyük havuz her zaman daha hızlı demek değildir.

### Environment Bazlı Yapılandırma
Veritabanı bilgileri **KESİNLİKLE** koda gömülmemeli. `.env` dosyaları veya environment variable'lar kullanılır.

```properties
spring.datasource.password=${DB_PASSWORD}
```

---

## 3. Encryption (Şifreleme)

### Nedir?
Encryption, veriyi **geri döndürülebilir** şekilde okunmaz hale getirmektir. Bir anahtar ile şifrelenir, aynı ya da başka bir anahtar ile çözülür (decrypt).

**Günlük hayattan örnek:**
Bir mektup yazıp sandığa kilitle. Sandığın anahtarı sende ve alıcıda var. İki tarafta da aynı anahtar ile sandık açılabilir.

### İki Ana Tip

#### a) Symmetric Encryption (Simetrik)
Aynı anahtar ile hem şifreleme hem çözme yapılır.
- **Örnek algoritmalar:** AES, DES, 3DES
- **Kullanım:** Veritabanı alanı şifreleme, dosya şifreleme.

#### b) Asymmetric Encryption (Asimetrik)
**Public key** ile şifrelenir, **private key** ile çözülür.
- **Örnek algoritmalar:** RSA, ECC
- **Kullanım:** HTTPS/TLS, dijital imza, JWT imzalama.

**Günlük hayattan örnek (Asimetrik):**
Posta kutusu gibi. Herkes mektup atabilir (public key), ama sadece kutunun sahibi açabilir (private key).

### Java'da AES (Simetrik) Örneği

```java
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AESExample {
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal("Gizli Mesaj".getBytes());

        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted = cipher.doFinal(encrypted);
        System.out.println(new String(decrypted));
    }
}
```

> 📌 **NOT:** AES günümüzde en güvenilir simetrik algoritmadır. DES ve 3DES artık **deprecated**.

---

## 4. Hashing

### Nedir?
Hashing, veriyi **tek yönlü** olarak sabit uzunlukta bir diziye dönüştürmektir. **Geri döndürülemez**.

**Günlük hayattan örnek:**
Kıyma makinesine et attın → kıyma çıktı. Kıymadan tekrar aynı eti yapamazsın. Hashing de böyle tek yönlüdür.

### Encryption vs Hashing — En Önemli Fark

| Özellik | Encryption | Hashing |
|---------|-----------|---------|
| Geri döndürme | Mümkün (decrypt) | İmkansız |
| Amaç | Veriyi gizli iletmek | Bütünlük doğrulama, şifre saklama |
| Anahtar | Gerekli | Gerekmez |
| Çıktı | Değişken uzunluk | Sabit uzunluk |

> 📌 **NOT:** Kullanıcı şifreleri **ASLA** encrypt edilmez, **her zaman** hash'lenir. Çünkü sistemin bile şifreyi görmesine gerek yoktur.

### Popüler Hash Algoritmaları
| Algoritma | Durumu |
|-----------|--------|
| MD5 | **Kırık (güvensiz)** — sadece checksum için kullanılabilir |
| SHA-1 | **Kırık** |
| SHA-256 / SHA-512 | Güvenli (dosya checksum, JWT vb.) |
| **BCrypt** | Şifre saklamak için **tavsiye edilen** |
| **Argon2** | Modern, en güvenli (password hashing yarışması galibi) |
| PBKDF2 | Güvenli, NIST onaylı |

### Salt Nedir?
Aynı şifreye sahip iki kullanıcının hash'i aynı olmasın diye şifreye eklenen rastgele değerdir.

**Örnek:**
- Şifre: `123456`
- Salt: `x7R!q`
- Hash-lenecek: `123456x7R!q` → farklı hash üretir.

> 📌 **NOT:** Salt, **rainbow table** saldırılarına karşı zorunludur. BCrypt ve Argon2 otomatik salt üretir.

### Java'da BCrypt Örneği (Spring Security)

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordExample {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hashed = encoder.encode("kullanici123");
        System.out.println(hashed);

        boolean matches = encoder.matches("kullanici123", hashed);
        System.out.println(matches); // true
    }
}
```

### Java'da SHA-256 Örneği

```java
import java.security.MessageDigest;

public class HashExample {
    public static String sha256(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
```

> 📌 **NOTw:** SHA-256 şifre saklamak için **YETERSİZDİR** (çok hızlı). BCrypt veya Argon2 kasıtlı olarak yavaştır, brute force'u zorlaştırır.

---

## 5. Pratik Özet & Karar Tablosu

| Senaryo | Kullanılacak Yapı |
|---------|-------------------|
| Tabloyu Java nesnesine çevirmek | ORM (JPA/Hibernate) |
| Uygulamayı DB'ye bağlamak | JDBC + Connection Pool (HikariCP) |
| Kullanıcı şifresi saklama | Hashing (BCrypt/Argon2) |
| TC kimlik, kredi kartı saklama | Encryption (AES) |
| Dosyanın bozulup bozulmadığını kontrol | Hashing (SHA-256) |
| İstemci-sunucu haberleşmesi | Asymmetric Encryption (TLS/HTTPS) |
| JWT token imzalama | Asymmetric Encryption (RSA) veya HMAC |

---

## 6. Akılda Kalsın — Must Olmayan Ama Değerli Notlar 📌

- **N+1 Problemi:** JPA'da bir liste çekerken her eleman için ayrı sorgu atılırsa performans çöker. `@EntityGraph`, `fetch = FetchType.LAZY` + `JOIN FETCH` ile çözülür.
- **Lazy vs Eager Loading:** Default'u bilmek önemli. `@ManyToOne` → EAGER, `@OneToMany` → LAZY.
- **Transaction Yönetimi:** `@Transactional` annotation'ı atomik işlemler için. Ya hepsi olur, ya hiçbiri.
- **HTTPS = TLS + HTTP:** HTTPS bir protokol değil, HTTP'nin TLS ile şifrelenmiş halidir.
- **Hash uzunluğu sabit:** "a" yazsan da 1 GB dosya hash'lesen de SHA-256 → 64 karakter.
- **Pepper:** Salt'a ek olarak uygulamada gizli tutulan "pepper" değeri, DB çalınsa bile kırılmayı zorlaştırır.
- **Flyway/Liquibase:** Production'da şema değişiklikleri için migration tool şart.
- **Repository Pattern:** Spring Data JPA'da `JpaRepository` otomatik CRUD sağlar; `CrudRepository` daha basit versiyonudur.
- **Primary Key stratejisi:** `IDENTITY` (DB üretir), `SEQUENCE` (PostgreSQL için ideal), `UUID` (dağıtık sistem için).
- **Environment Variables:** Şifreleri koda **asla** yazma. `.env` veya secret manager (AWS Secrets, Vault) kullan.

---

> **Özet cümle:** ORM veriyi nesneye çevirir, DB bağlantısı uygulamayı veritabanına bağlar, Encryption veriyi gizler (geri dönülür), Hashing ise veriyi doğrular (geri dönülmez).