package com.spring_first_project.myspringapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// component scanning
@SpringBootApplication (scanBasePackages = {"com.spring_first_project.myspringapp",
		"javabean", "config"})
public class MyspringappApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyspringappApplication.class, args);
	}

}
