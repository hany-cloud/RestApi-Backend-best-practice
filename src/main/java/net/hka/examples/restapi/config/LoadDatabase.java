package net.hka.examples.restapi.config;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.hka.common.model.vo.SimpleAddress;
import net.hka.examples.restapi.domain.Employee;
import net.hka.examples.restapi.domain.Order;
import net.hka.examples.restapi.domain.OrderStatus;
import net.hka.examples.restapi.domain.repository.EmployeeRepository;
import net.hka.examples.restapi.domain.repository.OrderRepository;

@Configuration
@NoArgsConstructor
@Slf4j
class LoadDatabase {

	@Bean
	CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
		return args -> {
			employeeRepository.save(Employee.createEmployee("Bilbo", "Baggins",
					SimpleAddress.createAddress("Plainfield", "Lomber", "61644"), new ArrayList<>()));
			employeeRepository.save(Employee.createEmployee("Frodo", "Baggins",
					SimpleAddress.createAddress("Woodridge", "Crystal", "60536"), new ArrayList<>()));

			employeeRepository.findAllWithDocument().forEach(employee -> logger.info("Preloaded " + employee));

			orderRepository.save(Order.createOrder("MacBook Pro", OrderStatus.COMPLETED, LocalDate.of(2020, 10, 31)));
			orderRepository.save(Order.createOrder("iPhone", OrderStatus.IN_PROGRESS, LocalDate.of(2020, 10, 31)));

			orderRepository.findAll().forEach(order -> logger.info("Preloaded " + order));
		};
	}
}
