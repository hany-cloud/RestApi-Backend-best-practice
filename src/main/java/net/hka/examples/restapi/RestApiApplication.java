package net.hka.examples.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "net.hka.common", "net.hka.examples.restapi" })
@SpringBootApplication
public class RestApiApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(RestApiApplication.class, args);
	}
}
