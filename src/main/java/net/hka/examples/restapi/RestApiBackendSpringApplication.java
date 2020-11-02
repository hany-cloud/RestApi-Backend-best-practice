package net.hka.examples.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("net.hka.common.web")
@ComponentScan("net.hka.examples.restapi")
@SpringBootApplication
public class RestApiBackendSpringApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(RestApiBackendSpringApplication.class, args);
	}
}
