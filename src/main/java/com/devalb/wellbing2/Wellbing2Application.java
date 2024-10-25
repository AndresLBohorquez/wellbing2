package com.devalb.wellbing2;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Wellbing2Application {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Bogota"));
		SpringApplication.run(Wellbing2Application.class, args);
	}

}
