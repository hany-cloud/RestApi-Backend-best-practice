package net.hka.examples.restapi.web.rest.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

import net.hka.common.web.rest.api.assembler.ApiEntityAssembler;
import net.hka.examples.restapi.business.dto.OrderDto;
import net.hka.examples.restapi.domain.Order;
import net.hka.examples.restapi.domain.OrderStatus;
import net.hka.examples.restapi.web.rest.api.controller.OrderApiController;

@Component
public class OrderAssembler implements RepresentationModelAssembler<Order, OrderDto>, ApiEntityAssembler<Order, OrderDto> {

	private final Class<? extends OrderApiController> controllerClass;
	
	public OrderAssembler(final Class<? extends OrderApiController> controllerClass) {
		this.controllerClass = controllerClass;
	}
	
	@Override
	public OrderDto toModel(final Order entity) {
		
		Preconditions.checkNotNull(entity, "order");

		OrderDto orderDto = OrderDto.builder()
			                        .id(entity.getId())
			                        .description(entity.getDescription())
			                        .status(entity.getStatus())
			                        .dueTo(entity.getDueTo())
			                        .createdAt(entity.getCreatedAt())
			                        .build();
		
		// Unconditional links to single-item resource and aggregate root
		orderDto.add(linkTo(methodOn(controllerClass).one(orderDto.getId())).withSelfRel())
				.add(linkTo(methodOn(controllerClass).all()).withRel("orders"));
		
				
		// Conditional links based on state of the order
		if (orderDto.getStatus() == OrderStatus.IN_PROGRESS) {
			orderDto.add(linkTo(methodOn(controllerClass).cancel(orderDto.getId())).withRel("cancel"))
					.add(linkTo(methodOn(controllerClass).complete(orderDto.getId())).withRel("complete"));
		}
		
		return orderDto;
	}

	@Override
	public CollectionModel<OrderDto> toCollectionModel(final Iterable<? extends Order> entities) {
		
		CollectionModel<OrderDto> orderDtos = RepresentationModelAssembler.super.toCollectionModel(entities);

		orderDtos.add(linkTo(methodOn(controllerClass).all()).withSelfRel());

		return orderDtos;
	}
	
	/**
     * Convert to {@link Order} from {@link OrderDto}
     */
	@Override
	public Order toEntity(final OrderDto dto) {
		
		Preconditions.checkNotNull(dto, "orderDto");
		return Order.createOrder(dto.getDescription(), dto.getStatus(), dto.getDueTo());
	}
}
