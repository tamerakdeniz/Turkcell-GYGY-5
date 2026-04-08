package com.turkcell.spring_starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Anatation => Bulunduğu class, fonk, değişkene özellik kazandıran yapıdır. SpringBootApplication => Spring Boot uygulaması olduğunu belirtir.
public class SpringStarterApplication {

	// Entrypoint => Uygulamanın başlangıç noktasıdır. Uygulama çalıştığında ilk olarak main metodu çalışır.
	public static void main(String[] args) {
		SpringApplication.run(SpringStarterApplication.class, args);
	}

}
