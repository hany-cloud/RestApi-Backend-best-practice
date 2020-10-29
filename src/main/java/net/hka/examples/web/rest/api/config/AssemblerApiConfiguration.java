package net.hka.examples.web.rest.api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.NoArgsConstructor;
import net.hka.examples.web.rest.api.OrderAssembler;
import net.hka.examples.web.rest.api.controller.OrderApiController;
import net.hka.examples.web.rest.api.controller.OrderApiControllerV2;

@Configuration
@NoArgsConstructor
class AssemblerApiConfiguration {

	@Bean
	@Qualifier(value="orderAssembler")
	public OrderAssembler orderAssembler() {
		OrderAssembler orderAssembler = new OrderAssembler(OrderApiController.class);
		return orderAssembler;
	}
	
	@Bean
	@Qualifier(value="orderAssemblerV2")
	public OrderAssembler orderAssemblerV2() {
		OrderAssembler orderAssembler = new OrderAssembler(OrderApiControllerV2.class);
		return orderAssembler;
	}
}
