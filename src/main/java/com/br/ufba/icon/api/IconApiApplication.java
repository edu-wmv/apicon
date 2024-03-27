package com.br.ufba.icon.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.br.ufba.icon.api.*")
@EntityScan("com.br.ufba.icon.api.*")
public class IconApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IconApiApplication.class, args);
	}

}
