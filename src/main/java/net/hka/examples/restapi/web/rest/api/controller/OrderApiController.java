package net.hka.examples.restapi.web.rest.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.hka.examples.restapi.business.domain.Order;
import net.hka.examples.restapi.business.domain.OrderStatus;
import net.hka.examples.restapi.business.exception.OrderNotFoundException;
import net.hka.examples.restapi.business.repository.OrderRepository;
import net.hka.examples.restapi.web.dto.OrderDto;
import net.hka.examples.restapi.web.rest.api.OrderAssembler;

@RestController
@RequestMapping("/purchase/v1/orders")
public class OrderApiController {

	protected final OrderRepository repository;
	
	protected final OrderAssembler assembler;

	OrderApiController(final OrderRepository repository, final @Qualifier(value="orderAssembler") OrderAssembler assembler) {
		
		this.repository = repository;
		this.assembler = assembler;
	}

	// Aggregate root
	@GetMapping
	public ResponseEntity<CollectionModel<OrderDto>> all() {

		List<Order> orders = repository.findAll();

		return new ResponseEntity<>(
				assembler.toCollectionModel(orders), 
                HttpStatus.OK);			
	}

	// Single item
	@GetMapping("/{id}")
	public ResponseEntity<OrderDto> one(@PathVariable Long id) {
		
		return repository.findById(id) 
                .map(assembler::toModel) 
                .map(ResponseEntity::ok) 
                //.orElse(ResponseEntity.notFound().build())
                .orElseThrow(() -> new OrderNotFoundException(id));
	}

	@PostMapping
	ResponseEntity<OrderDto> newOrder(@Valid @RequestBody OrderDto orderDto) {

		orderDto.setStatus(OrderStatus.IN_PROGRESS);
		Order newOrder = repository.save(assembler.toEntity(orderDto));

		return ResponseEntity
				.created(linkTo(methodOn(OrderApiController.class).one(newOrder.getId())).toUri()) //orderDto.getRequiredLink(IanaLinkRelations.SELF).toUri()
				.body(assembler.toModel(newOrder));
	}

	@PutMapping("/{id}/complete")
	public ResponseEntity<?> complete(@PathVariable Long id) {

		Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == OrderStatus.IN_PROGRESS) {
			order.setStatus(OrderStatus.COMPLETED);
			Order updatedOrder = repository.save(order);
			return ResponseEntity.ok(assembler.toModel(updatedOrder));
		}

		return buildRejectedResponseEntityWithReason(
				"You can't complete an order that is in the " + order.getStatus() + " status");
	}
	
	@DeleteMapping("/{id}/cancel")
	public ResponseEntity<?> cancel(@PathVariable Long id) {

		Order order = repository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == OrderStatus.IN_PROGRESS) {
			order.setStatus(OrderStatus.CANCELLED);
			Order updatedOrder = repository.save(order);
			return ResponseEntity.ok(assembler.toModel(updatedOrder));
		}

		return buildRejectedResponseEntityWithReason(
				"You can't cancel an order that is in the " + order.getStatus() + " status");
	}

	private ResponseEntity<?> buildRejectedResponseEntityWithReason(String reason) {
		
		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
				.body(Problem.create()
						.withTitle("Method not allowed")
						.withDetail(reason));
	}
}
