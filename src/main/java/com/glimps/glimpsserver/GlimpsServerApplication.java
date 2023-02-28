package com.glimps.glimpsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class GlimpsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GlimpsServerApplication.class, args);
	}

}
