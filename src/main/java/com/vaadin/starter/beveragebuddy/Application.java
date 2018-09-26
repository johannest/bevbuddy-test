package com.vaadin.starter.beveragebuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.vaadin.starter.beveragebuddy.backend.CategoryService;
import com.vaadin.starter.beveragebuddy.ui.MainLayout;

/**
 * Spring boot web application initializer.
 */
@SpringBootApplication(scanBasePackageClasses = { MainLayout.class, Application.class,
		CategoryService.class }, exclude = ErrorMvcAutoConfiguration.class)
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}
