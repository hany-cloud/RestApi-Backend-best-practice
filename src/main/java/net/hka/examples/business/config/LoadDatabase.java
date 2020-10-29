package net.hka.examples.business.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hka.examples.business.domain.Employee;
import net.hka.examples.business.domain.Order;
import net.hka.examples.business.domain.OrderStatus;
import net.hka.examples.business.repository.EmployeeRepository;
import net.hka.examples.business.repository.OrderRepository;

@Configuration
@NoArgsConstructor
@Slf4j
class LoadDatabase {

  @Bean
  CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {

	  return args -> {
	      employeeRepository.save(Employee.createEmployee("Bilbo", "Baggins"));
	      employeeRepository.save(Employee.createEmployee("Frodo", "Baggins"));

	      employeeRepository.findAll().forEach(employee -> 
	      	logger.info("Preloaded " + employee)
	      );
	      
	      orderRepository.save(Order.createOrder("MacBook Pro", OrderStatus.COMPLETED));
	      orderRepository.save(Order.createOrder("iPhone", OrderStatus.IN_PROGRESS));

	      orderRepository.findAll().forEach(order -> 
	    	  logger.info("Preloaded " + order)
	      );

	    };
  }
}
