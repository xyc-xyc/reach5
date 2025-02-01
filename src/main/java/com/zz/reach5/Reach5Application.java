package com.zz.reach5;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableVaadin("com.zz.reach5.ui")
public class Reach5Application {

	public static void main(String[] args) {
		SpringApplication.run(Reach5Application.class, args);
	}

}
