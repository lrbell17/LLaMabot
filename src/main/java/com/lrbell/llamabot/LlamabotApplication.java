package com.lrbell.llamabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;

@SpringBootApplication(
		exclude = {
				ContextFunctionCatalogAutoConfiguration.class
		}
)
public class LlamabotApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlamabotApplication.class, args);
	}

}
