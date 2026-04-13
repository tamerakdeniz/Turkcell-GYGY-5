/* 

package com.turkcell.spring_starter.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.turkcell.spring_starter.model.Product;
import com.turkcell.spring_starter.dto.ProductForCreateDto;
import com.turkcell.spring_starter.dto.ProductCreatedResponse;

// Implementation sınıfı, interface'i implement eder. Yani ProductService interface'inde tanımlanan methodları burada implement ederiz. 
// Bu sınıf, iş mantığını içerir. Veritabanı işlemleri, hesaplamalar, doğrulamalar gibi işlemler burada yapılır.
// @Service annotation'ı Spring'e bu sınıfın bir service olduğunu belirtir ve Spring bunu Bean olarak yönetir.
@Service
public class ProductSerciveImpl implements ProductService {
    
    // In-Memory Repository (Simüle edilmiş Veritabanı)
    private List<Product> productList = new ArrayList<>();
    
    // Controllerın çağırdığı methodları burada implement ederiz.
    // Örneğin createProduct, getAllProducts, getProductById, updateProduct, deleteProduct gibi methodlar olabilir. 
    // Bu methodlar, controller tarafından çağrılır ve gerekli işlemleri yapar.
    
    @Override
    public ProductCreatedResponse createProduct(ProductForCreateDto productDto) {
        // VALIDASYON: İsim 1 haneden uzun mu?
        if (productDto.getName() == null || productDto.getName().length() <= 1) {
            throw new IllegalArgumentException("Ürün adı en az 2 karakterden oluşmalıdır.");
        }

        // VALIDASYON: Fiyat geçerli mi?
        if (productDto.getPrice() == null || productDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Ürün fiyatı 0'dan büyük olmalıdır.");
        }
        
        // CREATE: Yeni ürünü oluştur
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setId(productList.size() + 1); // Auto Increment simülasyonu
        
        // DB'e kaydet (In-Memory liste)
        productList.add(product);
        
        // Response DTO olarak döndür
        ProductCreatedResponse response = new ProductCreatedResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        
        return response;
    }
    
    @Override
    public List<Product> getAllProducts() {
        // READ: Veritabanındaki tüm ürünleri listele
        return new ArrayList<>(productList); // Kopya döndür (defensive copy)
    }
    
    @Override
    public Product getProductById(int id) {
        // READ: Belirli bir ürünü ID ile bul
        return productList.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null); // Bulunamazsa null döndür
    }
    
    @Override
    public void updateProduct(Product product) {
        // UPDATE: Var olan ürünü güncelle
        if (product == null || product.getId() == 0) {
            throw new IllegalArgumentException("Geçersiz ürün.");
        }
        
        // Güncellecek ürünü bul
        Product productToUpdate = productList.stream()
                .filter(p -> p.getId() == product.getId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ürün bulunamadı. ID: " + product.getId()));
        
        // Ürünü güncelle
        productToUpdate.setName(product.getName());
        productToUpdate.setPrice(product.getPrice());
    }
    
    @Override
    public void deleteProduct(int id) {
        // DELETE: Ürünü sil
        Product productToDelete = productList.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ürün bulunamadı. ID: " + id));
        
        productList.remove(productToDelete);
    }
}

// Auto-Generated


// Spring IoC nedir? Bean, Service nedir?
// Spring IoC Container, uygulamanın ihtiyaç duyduğu nesneleri yönetir ve oluşturur. 
// Bean, Spring tarafından yönetilen bir nesnedir. Service ise, iş mantığını içeren sınıflardır. 
// Controller, Service, Repository gibi katmanlarda kullanılırlar. Controller, HTTP isteklerini karşılar ve Service'i çağırır. 
// Service ise, iş mantığını içerir ve Repository'yi çağırarak veritabanı işlemlerini yapar. 
// Repository ise, veritabanı ile iletişim kurar ve CRUD işlemlerini gerçekleştirir. 
// Bu katmanlı mimari, uygulamanın daha düzenli ve maintainable olmasını sağlar.

*/

package com.turkcell.spring_starter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.ProductCreatedResponse;
import com.turkcell.spring_starter.dto.ProductForCreateDto;
import com.turkcell.spring_starter.model.Product;

// Implementation
// IProductService ❌
// ProductService ✔
// ProductServiceImpl ✔
@Service // IoC'e bu türü ekledin.
public class ProductServiceImpl {
    // Controller'ın size aktaracağı işleri tanımla.
    // iş kodu..

    // repo
    private final List<Product> productsInMemory = new ArrayList<>();

    public ProductCreatedResponse create(ProductForCreateDto productDto)
    {
        // Aynı isimde 2 ürün olamaz

        // Business Rule

        checkIfProductWithSameNameExist(productDto.getName());
        
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setId(new Random().nextInt(999));

        productsInMemory.add(product); // repo

        ProductCreatedResponse response = new ProductCreatedResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());

        return response;
    }

    public void update() {
        // Aynı iş kuralı..
        checkIfProductWithSameNameExist("");
    }

    // İş kuralları -> Kendine has bir classta bulunmalıdır. -> ProductBusinessRules.java
    private void checkIfProductWithSameNameExist(String name) {
        Product productWithSameName = productsInMemory
                                        .stream()
                                        .filter(product->product.getName().equals(name))
                                        .findFirst()
                                        .orElse(null);

        if(productWithSameName != null)
            throw new RuntimeException("Aynı isimde 2 ürün eklenemez");
    }
}

// Auto-generated

// IProductRepository -> ProductRepository

// ProductRepository <Product> -> Spring auto-generated.

// Spring IoC Nedir? Bean,Service nedir? 

