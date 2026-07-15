package com.ats.prompt_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "Prompt Service API",
		version = "1.0.0",
		description = "REST API for creating, updating, searching, and managing AI prompts.",
		contact = @Contact(name = "Adept Tech Solutions"),
		license = @License(name = "Proprietary")
	),
	servers = {
		@Server(url = "http://localhost:8000", description = "Local development server")
	}
)
public class PromptServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromptServiceApplication.class, args);
	}

}
