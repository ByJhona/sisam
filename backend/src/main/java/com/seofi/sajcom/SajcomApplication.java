package com.seofi.sajcom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class SajcomApplication {

	public static void main(String[] args) {
		SpringApplication.run(SajcomApplication.class, args);
	}

}
