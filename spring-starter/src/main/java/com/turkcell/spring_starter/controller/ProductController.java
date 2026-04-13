package com.turkcell.spring_starter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.ProductCreatedResponse;
import com.turkcell.spring_starter.dto.ProductForCreateDto;
import com.turkcell.spring_starter.service.ProductServiceImpl;

// Altın kural: Veritabanı nesneleri requestte de responseda da kullanılamaz.
@RestController // Uygulamada gerektiğinde controlleri newle.
@RequestMapping("/api/product") 
public class ProductController {


    //private final ProductServiceImpl productServiceImpl = new ProductServiceImpl();
    private final ProductServiceImpl productServiceImpl;

    public ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    @PostMapping
    public ProductCreatedResponse create(@RequestBody ProductForCreateDto productDto) {
        return this.productServiceImpl.create(productDto);
    }
}

/* 

package com.turkcell.spring_starter.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.ProductCreatedResponse;
import com.turkcell.spring_starter.dto.ProductForCreateDto;
import com.turkcell.spring_starter.model.Product;

/*
@RestController
@RequestMapping("/api/product") // localhost:8080/api/products -> bu endpoint'e istek geldiğinde bu controller çalışır. ProductController
// Bu class bir reset controller olduğunu belirtir. Yani bu class HTTP isteklerini karşılamak için kullanılır.
public class ProductController {
    // Kullanıcı ne zaman /api/products endpoint'ine bir GET isteği gönderirse, bu method çalışır ve cevap döner.
    // /api/product -> sayHi(); matchle

    // HTTP Method: GET, POST, PUT, DELETE, PATCH
    @GetMapping("") //Controllerın uzantısı + get'in uzantısı -> /api/product
    public String sayHi(String name, int age) {
        return "Hi, " + name + "! You are " + age + " years old."; // localhost:8080/api/product?name=Tamer&age=23 -> Hi, Tamer! You are 23 years old.
    }

    // Controllerın uzantısı + get'in uzantısı -> /api/product/hello/{name}
    @GetMapping("hello/{name}/{age}") // localhost:8080/api/product/hello/Tamer/23 -> Hello, Tamer! I'm ProductController
    public String sayHello(@PathVariable String name, @PathVariable int age) {
        return "Hello, " + name + "! I'm ProductController" + " and you are " + age + " years old.";
    }

    @PostMapping
    public Product add(@RequestBody Product product){ // Json->Java objesine çevirir. İstek gövdesinde (body) bir Product objesi bekler.
        // isim 1 haneden uzun mu?
        // fiyat..
        // DB'e kaydet..

        return product;
    }
    


}
// Altın Kural: Veritabanı nesneleri requestte de response'ta da kullanılmaz. DTO (Data Transfer Object) kullanılır. ProductRequest, ProductResponse gibi. Ancak şimdilik basit olması açısından direkt Product kullanacağız.
@RestController 
@RequestMapping("/api/product") 
public class ProductController {
    // In-Memory Çalış..
    private List<Product> productList = new ArrayList<>();


    @GetMapping()
    public List<Product> getAllProducts() {
        // Veritabanındaki Productları nesnelerini listele
        return productList;
    }

    @GetMapping("{id}")
    public Product getProductById(@PathVariable int id) 
    {
        // Listeden id == product.getId() ise onu yoksa null dön.
        return productList.stream().filter(i->i.getId() == id).findFirst().orElse(null);
    }

    // Request-Response Pattern => İstek ve cevap arasında bir sözleşme vardır. 
    // İstek ve cevap nesneleri genellikle birbirinden farklıdır. 
    // Bu yüzden DTO kullanılır.
    // Birebir başka bir istek-cevap çiftiyle aynı içeriğe sahip olabilirler ancak isimleri farklıdır. 
    // ProductForCreateDto, ProductForUpdateDto gibi.

    @PostMapping
    public ProductCreatedResponse createProduct(@RequestBody ProductForCreateDto productDto) {
        // Veritabanına product nesnesini ekle..

        // VALIDASYON: İsim 1 haneden uzun mu?
        if (productDto.getName() == null || productDto.getName().length() <= 1) {
            throw new IllegalArgumentException("Ürün adı en az 2 karakterden oluşmalıdır.");
        }

        // VALIDASYON: Fiyat geçerli mi?
        if (productDto.getPrice() == null || productDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Ürün fiyatı 0'dan büyük olmalıdır.");
        }

        // Sen dışardan ProductForCreateDto alıyosun 
        // ama veritabanı Product ile çalışıyor

        // Transfer => MANUAL MAPPING
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setId(productList.size() + 1); // Auto Increment gibi çalışır. Ancak gerçek bir veritabanında bu işi veritabanı yapar.

        // DB'e kaydet (simüle edilmiş In-Memory storage)
        productList.add(product);

        // Domain Nesnesi -> Dto
        ProductCreatedResponse response = new ProductCreatedResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());

        return response;

        // Ben controller olarak iş kodunu çalıştıramam, ama bunu yapmam gerekli...
        // İş kodunu çalıştıracak olan yapıya BAĞLIYIM. O yapıya da SERVICE denir. 
        // Service, controller ile repository arasında bir köprü görevi görür. Controller, HTTP isteklerini karşılar ve Service'i çağırır. 
        // Service ise, iş mantığını içerir ve Repository'yi çağırarak veritabanı işlemlerini yapar. 
        // Repository ise, veritabanı ile iletişim kurar ve CRUD işlemlerini gerçekleştirir. 
        // Bu katmanlı mimari, uygulamanın daha düzenli ve maintainable olmasını sağlar.

        
    }
    @PutMapping
    public void updateProduct(@RequestBody Product product) {
        ///..
        Product productToUpdate = productList.stream().filter(p -> p.getId() == product.getId()).findFirst().orElseThrow();

        productToUpdate.setName(product.getName());
        productToUpdate.setPrice(product.getPrice());
    }
    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable int id) {
        ///.. Todo..
        Product productToDelete = productList.stream().filter(p -> p.getId() == id).findFirst().orElseThrow();
        productList.remove(productToDelete);
    }
}

// DTO => Data Transfer Object. Veritabanı nesneleri requestte de response'ta da kullanılmaz. DTO (Data Transfer Object) kullanılır. 
// ProductRequest, ProductResponse gibi. Ancak şimdilik basit olması açısından direkt Product kullanacağız.
// Entity ile X (Controller, service) arası veri transferi için oluşturulan sınıflardır.

*/