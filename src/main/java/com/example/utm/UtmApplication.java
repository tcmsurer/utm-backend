package com.example.utm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync; // Yeni import

@SpringBootApplication
@EnableAsync // Bu anotasyonu ekleyin
public class UtmApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtmApplication.class, args);
	}
}