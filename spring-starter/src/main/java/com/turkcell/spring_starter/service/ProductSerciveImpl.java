package com.turkcell.spring_starter.service;

// Implementation sınıfı, interface'i implement eder. Yani ProductService interface'inde tanımlanan methodları burada implement ederiz. Bu sınıf, iş mantığını içerir. Veritabanı işlemleri, hesaplamalar, doğrulamalar gibi işlemler burada yapılır.
// IProductService yerine ProductService olur
// ProductServiceImpl
public class ProductSerciveImpl {
    // Controllerın çağırdığı methodları burada implement ederiz.
    // Örneğin createProduct, getAllProducts, getProductById, updateProduct, deleteProduct gibi methodlar olabilir. 
    // Bu methodlar, controller tarafından çağrılır ve gerekli işlemleri yapar.




}

// Auto-Generated


// Spring IoC nedir? Bean, Service nedir?
// Spring IoC Container, uygulamanın ihtiyaç duyduğu nesneleri yönetir ve oluşturur. 
// Bean, Spring tarafından yönetilen bir nesnedir. Service ise, iş mantığını içeren sınıflardır. 
// Controller, Service, Repository gibi katmanlarda kullanılırlar. Controller, HTTP isteklerini karşılar ve Service'i çağırır. 
// Service ise, iş mantığını içerir ve Repository'yi çağırarak veritabanı işlemlerini yapar. 
// Repository ise, veritabanı ile iletişim kurar ve CRUD işlemlerini gerçekleştirir. 
// Bu katmanlı mimari, uygulamanın daha düzenli ve maintainable olmasını sağlar.