# Java ve Spring Boot Ders Notları

Bu notlar, workspace'teki kod yorumlarından derlenmiştir. Java OOP, temel programlama konseptleri, Spring Boot mimarisi ve ilgili terimler üzerine odaklanmaktadır. Öğrenilmesi gereken noktalar, örnekler ve ödev değerlendirmeleri dahil edilmiştir.

## 1. Java Temelleri

### Değişkenler (Variables)
Kodun akışında değer tutan isimli verilerdir. Tanımlandıkları yerden itibaren erişilebilir ve değiştirilebilir.

**Terim Açıklaması:**
- **Scope (Kapsama Alanı):** Değişkenin erişilebilir olduğu bölge. Örneğin, `{}` içinde tanımlanan değişkenler sadece o blokta geçerlidir.

**Örnek:**
```java
int age = 25; // Primitive tip
String name = "Tamer"; // Referans tip
```

**Öğrenilmesi Gereken Nokta:** Primitive tipler (int, double, boolean, char) ve non-primitive tipler (String, Arrays, Objects) arasındaki fark. Primitive'ler değer ile geçer (pass by value), non-primitive'ler referans ile geçer.

### Diziler (Arrays)
Birden fazla değeri aynı tipte tutan yapı.

**Örnek:**
```java
int[] numbers = {1, 2, 3};
System.out.println(numbers[0]); // 1
```

### Döngüler (Loops)
İşlemleri tekrarlamak için kullanılır.

**Terim Açıklaması:**
- **Iteration:** Tekrarlama süreci.

**Örnek:**
```java
for (int i = 0; i < 5; i++) {
    System.out.println("Öğrenci: " + i);
}
```

### Karar Blokları
Koşula göre farklı kod bloklarını çalıştırır.

**Örnek:**
```java
if (age > 18) {
    System.out.println("Yetişkin");
} else {
    System.out.println("Çocuk");
}
```

### Methodlar (Functions)
Tekrar kullanılabilir kod blokları.

**Terim Açıklaması:**
- **Access Modifiers:** Erişim belirleyiciler (public, private, protected, default).
- **Return Types:** Dönüş tipleri (void, int, String vb.).
- **Parameters:** Parametreler.
- **Method Overloading:** Aynı isimde farklı parametre listesi ile birden fazla method.
- **Varargs:** Değişken sayıda parametre (String... args gibi).

**Örnek:**
```java
public void calculateGrade(String studentName, int score) {
    // Kod
}
```

## 2. Nesne Yönelimli Programlama (OOP)

### Inheritance (Kalıtım)
Bir sınıfın başka bir sınıftan özelliklerini miras alması.

**Terim Açıklaması:**
- **Subclass (Alt Sınıf):** Miras alan sınıf.
- **Superclass (Üst Sınıf):** Miras veren sınıf.
- **extends:** Miras alma anahtar kelimesi.

**Örnek:**
```java
class Car extends Vehicle {
    // Car, Vehicle'dan miras alır
}
```

### Encapsulation (Kapsülleme)
Veriyi koruma ve gizleme.

**Terim Açıklaması:**
- **Getter/Setter Methods:** Değer alma/verme methodları.

**Örnek:**
```java
private String brand;
public String getBrand() { return brand; }
public void setBrand(String brand) { this.brand = brand; }
```

### Constructor
Nesne oluştururken çağrılan özel method.

**Terim Açıklaması:**
- **Default Constructor:** Parametresiz, otomatik oluşturulan.

**Örnek:**
```java
public Car() {
    setBrand("Car");
}
```

### Interfaces
Sözleşmeler tanımlayan yapılar. Implement edilmesi zorunlu methodları belirtir.

**Terim Açıklaması:**
- **Abstract:** İçeriği olmayan, sadece imza (signature) içeren.
- **Implement:** Arayüzü uygulamak.

**Örnek:**
```java
interface CarRepository {
    void add(Car car);
}

class PgCarRepository implements CarRepository {
    public void add(Car car) {
        // PostgreSQL'e kaydet
    }
}
```

## 3. Spring Boot Kavramları

### Annotations
Sınıf, method veya değişkene özellik kazandıran yapılar.

**Örnek:**
```java
@SpringBootApplication // Spring Boot uygulamasını belirtir
@RestController // HTTP isteklerini karşılayan controller
@RequestMapping("/api/product") // Endpoint tanımı
```

### Controller
HTTP isteklerini karşılayan katman.

**Terim Açıklaması:**
- **HTTP Methods:** GET, POST, PUT, DELETE, PATCH.
- **PathVariable:** URL'den parametre alma.
- **RequestBody:** JSON'dan nesne oluşturma.

**Örnek:**
```java
@GetMapping("/hello/{name}")
public String sayHello(@PathVariable String name) {
    return "Hello, " + name;
}
```

### Service
İş mantığını içeren katman. Controller ile Repository arasında köprü.

**Terim Açıklaması:**
- **IoC (Inversion of Control):** Spring'in nesne yönetim sistemi.
- **Bean:** Spring tarafından yönetilen nesne.

**Örnek:**
```java
@Service
public class ProductServiceImpl implements ProductService {
    // İş mantığı
}
```

### Repository
Veritabanı işlemlerini yapan katman.

**Örnek:**
```java
// CRUD işlemleri
public Product findById(int id) {
    // DB sorgusu
}
```

### DTO (Data Transfer Object)
Veritabanı nesnelerini request/response'ta kullanmamak için oluşturulan sınıflar.

**Terim Açıklaması:**
- **Entity:** Veritabanı nesnesi.
- **Manual Mapping:** Entity ile DTO arasında veri transferi.

**Örnek:**
```java
public class ProductForCreateDto {
    private String name;
    private double price;
}
```

## 4. Ödev ve ? Bulunan Noktalar Değerlendirmesi

### Banking Application Ödevi
- **Açıklama:** Yeni bir proje oluştur (Bankingapplication). Min 3+ özellik: Hesap özeti görme, veritabanı görme, müşteri olma vb. In-Memory Repository kullan. Tek bir main class'ta simülasyon. Console üzerinden çalışacak. Çarşamba ders saatine kadar GitHub teslimi.
- **Değerlendirme:** Bu ödev, OOP ve repository pattern'ini pekiştirmek için tasarlanmış. In-Memory Repository ile RAM'de veri tutmayı öğren. Console tabanlı basit bir banka uygulaması oluştur. Özellikler: Müşteri ekleme, hesap özeti görüntüleme, vb.
- **Örnek Yapı:**
  ```java
  class Customer {
      // Özellikler
  }
  interface CustomerRepository {
      void add(Customer c);
      List<Customer> getAll();
  }
  class InMemoryCustomerRepository implements CustomerRepository {
      // RAM'de list tut
  }
  ```

### Todo ve Eksik Noktalar
- **ProductController'da:** "// isim 1 haneden uzun mu? // fiyat.. // DB'e kaydet.." - Validasyon ve DB işlemleri eksik. Spring Validation ve JPA öğren.
- **Service'de:** "//.. Todo.." - Tam implementasyon eksik. CRUD methodları ekle.
- **HTTP Anatomisi:** "(Pazartesi 13 Nisan - HTTP İsteğinin anatomisi hakkında bir .md dosyası hazırlamak)" - HTTP request yapısını (headers, body, methods) araştır ve .md dosyası hazırla.

## 5. Öğrenilmesi Gereken Ek Noktalar
- **String Pool:** String'lerin bellekte nasıl tutulduğu. `==` vs `equals()`.
- **Pass by Value/Reference:** Primitive'ler kopya, referans tipler orijinal nesneyi etkiler.
- **Spring Boot Mimarisi:** Katmanlı yapı (Controller -> Service -> Repository).
- **Maven:** Proje yönetimi, pom.xml.
