package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.example.demo.controller.FetchController;
import com.example.demo.service.FetchService;

@ComponentScan(basePackages = {"com.example.demo"}, basePackageClasses = {FetchController.class, FetchService.class})
@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
