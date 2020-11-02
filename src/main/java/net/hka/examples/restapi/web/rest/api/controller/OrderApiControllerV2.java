package net.hka.examples.restapi.web.rest.api.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.hka.common.web.rest.api.response.ApiErrorCode;
import net.hka.examples.restapi.business.domain.Order;
import net.hka.examples.restapi.business.domain.OrderStatus;
import net.hka.examples.restapi.business.exception.InvalidOrderStatusModificationException;
import net.hka.examples.restapi.business.exception.OrderNotFoundException;
import net.hka.examples.restapi.business.repository.OrderRepository;
import net.hka.examples.restapi.web.rest.api.OrderAssembler;

@RestController
@RequestMapping("/purchase/v2/orders")
public class OrderApiControllerV2 extends OrderApiController {
	
	OrderApiControllerV2(final OrderRepository orderRepository, final @Qualifier(value="orderAssemblerV2") OrderAssembler assembler) {
		
		super(orderRepository, assembler);		
	}

	@PutMapping("/{id}/complete")
	public ResponseEntity<?> complete(@PathVariable Long id) {

		Order order = repository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == OrderStatus.IN_PROGRESS) {
			order.setStatus(OrderStatus.COMPLETED);
			Order updatedOrder = repository.save(order);
			return ResponseEntity.ok(assembler.toModel(updatedOrder));
		} else
			throw new InvalidOrderStatusModificationException(
					"You can't complete an order that is in the " + order.getStatus() + " status",
					ApiErrorCode.METHOD_NOT_ALLOWED);
	}
	
	@DeleteMapping("/{id}/cancel")
	public ResponseEntity<?> cancel(@PathVariable Long id) {

		Order order = repository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == OrderStatus.IN_PROGRESS) {
			order.setStatus(OrderStatus.CANCELLED);
			Order updatedOrder = repository.save(order);
			return ResponseEntity.ok(assembler.toModel(updatedOrder));
		} else
			throw new InvalidOrderStatusModificationException(
					"You can't cancel an order that is in the " + order.getStatus() + " status",
					ApiErrorCode.METHOD_NOT_ALLOWED);

	}
}
