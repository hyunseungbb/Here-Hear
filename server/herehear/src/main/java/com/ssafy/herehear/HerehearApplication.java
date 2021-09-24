package com.ssafy.herehear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class HerehearApplication {

	public static void main(String[] args) {
		SpringApplication.run(HerehearApplication.class, args);
	}

}
