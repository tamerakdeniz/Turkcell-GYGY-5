package com.turkcell.spring_starter.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
*/

@RestController 
@RequestMapping("/api/product") 
public class ProductController {
    // In-Memory Çalış..
    private List<Product> productList = new ArrayList<>();


    @GetMapping()
    public List<Product> getAllProducts() {
        return productList;
    }

    @GetMapping("{id}")
    public Product getProductById(@PathVariable int id) 
    {
        // Listeden id == product.getId() ise onu yoksa null dön.
        return productList.stream().filter(i->i.getId() == id).findFirst().orElse(null);
    }

    @PostMapping
    public void createProduct(@RequestBody Product product) {
        productList.add(product);
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
        ///..
    }
}