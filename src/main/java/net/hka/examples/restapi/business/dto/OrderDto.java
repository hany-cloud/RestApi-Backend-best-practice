package net.hka.examples.restapi.business.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import net.hka.examples.restapi.domain.OrderStatus;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "order")
@Relation(collectionRelation = "orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto extends RepresentationModel<OrderDto> {

	private Long id;
	
	@NotBlank 
	@Size(min = 2, max = 100)
	private String description;
	
	@NotNull
	private OrderStatus status;
	
	private LocalDate dueTo;
	
	@Setter(AccessLevel.PRIVATE)
	@JsonIgnore
	private LocalDateTime createdAt;

	@Override
	public String toString() {

		return MoreObjects.toStringHelper(this)
	              .add("id", id)
	              .add("description", description)
	              .add("status", status)  
	              .add("dueTo", dueTo)  
	              .toString();
	}
}
