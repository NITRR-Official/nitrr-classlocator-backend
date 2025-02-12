package com.classlocator.nitrr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ClasslocatorApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();
		System.setProperty("MONGO_ID", dotenv.get("MONGO_ID"));
		System.setProperty("MONGO_PASSWORD", dotenv.get("MONGO_PASSWORD"));

		ApplicationContext context = SpringApplication.run(ClasslocatorApplication.class, args);

		log.info("Current environment: {}", context.getEnvironment().getActiveProfiles()[0]);
	}

}
