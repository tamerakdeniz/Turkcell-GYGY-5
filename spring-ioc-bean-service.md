# Spring IoC, Bean ve Service

**Spring Framework**, Java ile kurumsal uygulama geliştirmeyi kolaylaştıran açık kaynaklı bir framework'tür. Merkezinde **IoC (Inversion of Control)** prensibi yatar; bu prensip, nesnelerin birbirini kendisinin oluşturması yerine bu sorumluluğun bir dış mekanizmaya devredilmesi anlamına gelir.

> **İlgili terim — Framework vs Library:** Library, senin çağırdığın koddur. Framework ise senin kodunu çağırır — kontrolü o yönetir. Spring bir framework'tür; IoC bu kontrolü senden alması nedeniyle "Inversion of Control" (Kontrolün Tersine Çevrilmesi) olarak adlandırılır.

---

## 1. IoC (Inversion of Control) Nedir?

Geleneksel yaklaşımda bir nesne, ihtiyaç duyduğu başka bir nesneyi **kendisi oluşturur:**

```java
// Geleneksel yaklaşım — sıkı bağlılık (tight coupling)
public class OrderService {
    private PaymentService paymentService = new PaymentService(); // bağımlılık elle oluşturuluyor
}
```

IoC'de ise nesne, bağımlılıklarını **kendisi oluşturmaz; dışarıdan alır:**

```java
// IoC yaklaşımı — gevşek bağlılık (loose coupling)
public class OrderService {
    private PaymentService paymentService; // kim oluşturacak? → Spring

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Bu "dışarıdan verme" işlemine **Dependency Injection (DI)** denir. IoC genel prensip, DI ise bu prensibin uygulanma yöntemidir.

> **İlgili terim — Tight vs Loose Coupling:** `new PaymentService()` yazmak, iki sınıfı birbirine sıkıca bağlar (tight coupling). Birini değiştirmek diğerini etkiler. IoC ile bu bağ gevşer; sınıflar birbirini doğrudan bilmek zorunda kalmaz. Bu, test edilebilirliği ve bakımı kolaylaştırır.

---

## 2. IoC Container (ApplicationContext)

Spring'in IoC mekanizması bir **container** üzerinden çalışır. Bu container, uygulama başladığında devreye girer ve şu işleri yapar:

1. Hangi nesnelerin (Bean) oluşturulacağını tespit eder.
2. Bu nesneleri oluşturur ve yapılandırır.
3. Bağımlılıkları (DI) enjekte eder.
4. Nesnelerin yaşam döngüsünü (lifecycle) yönetir.

```
[Uygulama Başlar]
       │
       ▼
[IoC Container Devreye Girer]
       │
       ├─► Konfigürasyonu Okur (@Component, @Bean, XML vb.)
       │
       ├─► Bean'leri Oluşturur
       │
       ├─► Bağımlılıkları Enjekte Eder (DI)
       │
       └─► Bean'leri Hazır Hale Getirir → Uygulama Çalışır
```

Spring'de container'a **`ApplicationContext`** denir. Daha eski ve sınırlı versiyonu `BeanFactory`'dir; modern projelerde doğrudan `ApplicationContext` (ya da Spring Boot'ta otomatik yapılandırılmış hali) kullanılır.

> **İlgili terim — Spring Boot:** Spring'in "sıfırdan yapılandırma" yükünü ortadan kaldıran üst katmanıdır. `@SpringBootApplication` anotasyonu ile IoC container otomatik başlatılır; çoğu konfigürasyon varsayılan değerlerle gelir.

---

## 3. Bean Nedir?

**Bean**, Spring IoC container tarafından oluşturulan, yapılandırılan ve yönetilen nesnedir. Spring context'inde yaşayan her nesne bir Bean'dir.

```java
@Component          // Bu sınıfı bir Bean olarak kaydet
public class EmailSender {
    public void send(String to, String message) { ... }
}
```

Spring bu sınıfı görür, bir nesne oluşturur ve container'da saklar. Başka bir yerde ihtiyaç duyulduğunda aynı nesneyi verir.

### Bean Tanımlama Yöntemleri

| Yöntem | Açıklama |
|---|---|
| `@Component` | Genel amaçlı Bean. Alt türleri: `@Service`, `@Repository`, `@Controller` |
| `@Bean` (metod üzerinde) | `@Configuration` sınıfı içinde elle Bean tanımlamak için |
| XML Konfigürasyon | Eski yöntem, modern projelerde nadiren kullanılır |

```java
// @Bean ile manuel tanımlama — harici kütüphaneler için tercih edilir
@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

> **Ne zaman `@Bean` kullanılır?** Kendi yazmadığın (örn. üçüncü parti kütüphane) sınıfların üzerine `@Component` yazamazsın. Bu durumda bir `@Configuration` sınıfı içinde `@Bean` metodu ile tanımlama yaparsın.

---

### 3.1 Bean Scope (Kapsam)

Bir Bean'in ne sıklıkla oluşturulacağını **scope** belirler:

| Scope | Davranış | Varsayılan? |
|---|---|:---:|
| **Singleton** | Container'da yalnızca bir örnek; her yerde aynı nesne paylaşılır | ✅ |
| **Prototype** | Her istekte yeni bir nesne oluşturulur | ❌ |
| **Request** | Her HTTP isteği için yeni nesne (web uygulamaları) | ❌ |
| **Session** | Her HTTP oturumu için yeni nesne (web uygulamaları) | ❌ |

```java
@Component
@Scope("prototype")     // varsayılanı geçersiz kıl
public class ShoppingCart { ... }
```

> Singleton scope'ta paylaşılan Bean'ler **thread-safe** olmalıdır; aksi hâlde eş zamanlı erişimde veri bozulması yaşanabilir.

---

### 3.2 Bean Lifecycle (Yaşam Döngüsü)

```
Container Başlar
     │
     ▼
Bean Oluşturulur (constructor)
     │
     ▼
Bağımlılıklar Enjekte Edilir
     │
     ▼
@PostConstruct → Başlangıç işlemleri (DB bağlantısı, cache yükleme vb.)
     │
     ▼
[Bean Kullanımda]
     │
     ▼
@PreDestroy → Kapanış işlemleri (bağlantı kapatma, kaynak serbest bırakma vb.)
     │
     ▼
Container Kapanır
```

---

## 4. Dependency Injection (DI) Yöntemleri

### Constructor Injection ✅ (Önerilen)

```java
@Service
public class OrderService {

    private final PaymentService paymentService;

    @Autowired  // Spring Boot'ta tek constructor varsa bu anotasyon zorunlu değil
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

### Field Injection (Yaygın ama önerilmez)

```java
@Service
public class OrderService {

    @Autowired   // Spring doğrudan alana enjekte eder
    private PaymentService paymentService;
}
```

> **Neden constructor injection önerilir?** Bağımlılıklar `final` yapılabilir (immutability), sınıf Spring olmadan (unit test'te) kolayca örneklenebilir ve eksik bağımlılık derleme zamanında fark edilir.

### Setter Injection (Opsiyonel bağımlılıklar için)

```java
@Autowired
public void setPaymentService(PaymentService paymentService) {
    this.paymentService = paymentService;
}
```

---

## 5. Stereotype Anotasyonlar

`@Component` üç özel alt türe ayrılır. İşlevsel olarak hepsi bir Bean tanımlar; fark **semantik** ve bazı ek özelliklerdir:

```
           @Component
          /     |     \
    @Service  @Repository  @Controller
```

| Anotasyon | Katman | Ek Özellik |
|---|---|---|
| `@Service` | İş mantığı (Business Logic) | Yalnızca semantik ayrım |
| `@Repository` | Veri erişimi (DAO) | Veritabanı istisnalarını Spring'in `DataAccessException`'ına çevirir |
| `@Controller` | Web katmanı (HTTP istekleri) | Spring MVC ile HTTP endpoint'leri işler |
| `@RestController` | REST API | `@Controller` + `@ResponseBody` birleşimi |

---

## 6. @Service Anotasyonu

`@Service`, uygulamanın **iş mantığını** barındıran sınıfları işaretlemek için kullanılır. Teknik olarak `@Component`'ten farkı yoktur; ancak:

- Kodun **okunabilirliğini** artırır (bu sınıf iş mantığı içeriyor demek)
- Takım içinde **mimari anlaşma** sağlar
- Bazı araçlar ve framework'ler bu ayrımı kullanır

```java
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailSender emailSender;

    public UserService(UserRepository userRepository, EmailSender emailSender) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    public User register(String email, String password) {
        // İş mantığı burada
        User user = new User(email, password);
        userRepository.save(user);
        emailSender.send(email, "Hoş geldiniz!");
        return user;
    }
}
```

> **İlgili terim — Layered Architecture (Katmanlı Mimari):** Spring uygulamaları genellikle üç katmandan oluşur: `@Controller` (sunum katmanı, HTTP isteklerini karşılar) → `@Service` (iş mantığı katmanı) → `@Repository` (veri erişim katmanı). Her katman yalnızca bir alt katmanı çağırır.

---

## 7. Component Scan

Spring, Bean'leri otomatik olarak bulmak için **component scan** yapar; belirtilen paketi ve alt paketlerini tarayarak `@Component` (ve türevlerini) işaretli sınıfları tespit eder.

```java
@SpringBootApplication   // @ComponentScan'i de kapsar; bulunduğu paketi ve altını tarar
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }
}
```

Tarama kapsamını özelleştirmek için:

```java
@ComponentScan(basePackages = "com.example.myapp")
```

> Bean bir pakette tanımlı ama component scan kapsamı dışındaysa Spring onu göremez — `NoSuchBeanDefinitionException` hatası alırsın.

---

## 8. Sık Karşılaşılan Hatalar

| Hata | Neden | Çözüm |
|---|---|---|
| `NoSuchBeanDefinitionException` | İstenen Bean container'da yok | Sınıfın `@Component` ile işaretli ve scan kapsamında olduğunu kontrol et |
| `NoUniqueBeanDefinitionException` | Aynı türden birden fazla Bean var | `@Qualifier("beanAdı")` ile hangisinin enjekte edileceğini belirt |
| `UnsatisfiedDependencyException` | Bir Bean'in bağımlılığı çözülemedi | Bağımlılığın da Bean olarak tanımlı olduğundan emin ol |
| Circular Dependency | A→B→A gibi döngüsel bağımlılık | Tasarımı gözden geçir; `@Lazy` geçici çözüm olabilir |

---

## 9. Özet: Temel Kavramlar

| Terim | Kısa Açıklama |
|---|---|
| **IoC** | Nesne oluşturma kontrolünün geliştiriciden alınıp framework'e verilmesi |
| **DI (Dependency Injection)** | Bağımlılıkların dışarıdan (container tarafından) enjekte edilmesi |
| **Bean** | Spring container tarafından yönetilen nesne |
| **ApplicationContext** | Bean'leri saklayan ve yöneten IoC container |
| **Singleton Scope** | Varsayılan kapsam; container genelinde tek örnek |
| **@Component** | Sınıfı Bean olarak işaretleyen genel anotasyon |
| **@Service** | İş mantığı katmanını işaretleyen `@Component` alt türü |
| **@Repository** | Veri erişim katmanını işaretleyen `@Component` alt türü |
| **@Autowired** | Spring'e "bu bağımlılığı enjekte et" diyen anotasyon |
| **@Configuration + @Bean** | Üçüncü parti sınıfları Bean olarak tanımlamak için |
| **Component Scan** | Spring'in `@Component`'leri otomatik bulma süreci |
| **Tight/Loose Coupling** | Sınıfların birbirine bağımlılık derecesi |

---

*Spring Framework 6.x ve Spring Boot 3.x baz alınmıştır.*
