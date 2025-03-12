package com.dino.Mega_City_Cabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MegaCityCabsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MegaCityCabsApplication.class, args);
	}

}
