package com.commerce.saleday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SaledayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaledayApplication.class, args);
	}

}
