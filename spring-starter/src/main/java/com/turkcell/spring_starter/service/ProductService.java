package com.turkcell.spring_starter.service;

import java.util.List;
import com.turkcell.spring_starter.model.Product;
import com.turkcell.spring_starter.dto.ProductForCreateDto;
import com.turkcell.spring_starter.dto.ProductCreatedResponse;

// Service Interface: İş mantığını tanımlayan sözleşme
// Controller bu interface'i kullanarak Product işlemlerini yapar
// Böylece controller, servisin implementasyon detaylarından haberdar olmaz
public interface ProductService {
    
    // CRUD işlemleri
    ProductCreatedResponse createProduct(ProductForCreateDto productDto);
    
    List<Product> getAllProducts();
    
    Product getProductById(int id);
    
    void updateProduct(Product product);
    
    void deleteProduct(int id);
}
