package com.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IceFactoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(IceFactoryApplication.class, args);
	}

}
