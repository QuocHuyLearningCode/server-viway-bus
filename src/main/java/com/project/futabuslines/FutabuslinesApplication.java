package com.project.futabuslines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FutabuslinesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FutabuslinesApplication.class, args);
	}

}
