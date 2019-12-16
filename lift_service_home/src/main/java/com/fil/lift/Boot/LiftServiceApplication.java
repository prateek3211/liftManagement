package com.fil.lift.Boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.fil.lift")
public class LiftServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiftServiceApplication.class, args);
	}

}
