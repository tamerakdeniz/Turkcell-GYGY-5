package com.turkcell.spring_starter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
