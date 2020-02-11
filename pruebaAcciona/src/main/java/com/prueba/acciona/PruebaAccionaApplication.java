package com.prueba.acciona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.prueba.acciona.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class PruebaAccionaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PruebaAccionaApplication.class, args);
	}

}
