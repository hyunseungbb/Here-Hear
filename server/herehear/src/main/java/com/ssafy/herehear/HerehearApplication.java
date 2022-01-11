package com.ssafy.herehear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
//@EnableJpaAuditing
@SpringBootApplication
//@EntityScan(basePackages="com.ssafy.herehear")
public class HerehearApplication {

	public static void main(String[] args) {
		SpringApplication.run(HerehearApplication.class, args);
	}
}
