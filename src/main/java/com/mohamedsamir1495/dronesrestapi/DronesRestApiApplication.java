package com.mohamedsamir1495.dronesrestapi;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Drones microservice REST API Documentation",
				description = "Drones microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Mohamed Samir",
						email = "mohamedsamir1495@gmail.com",
						url = "https://www.linkedin.com/in/mohamedsamir1495"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://github.com/mohamedsamir1495"
				)
		),
		externalDocs = @ExternalDocumentation(
				description =  "Drones Accounts microservice REST API Documentation",
				url = "https://github.com/mohamedsamir1495"
		)
)
@ComponentScan()
public class DronesRestApiApplication {


	public static void main(String[] args) {
		SpringApplication.run(DronesRestApiApplication.class, args);
	}

}
