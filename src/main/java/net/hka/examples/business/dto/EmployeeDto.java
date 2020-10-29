package net.hka.examples.business.dto;

import java.time.LocalDateTime;

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

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "order")
@Relation(collectionRelation = "orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDto extends RepresentationModel<EmployeeDto> {

	private Long id;
	private String firstName;
	private String lastName;
	private String name;
	
	@Setter(AccessLevel.PRIVATE)
	@JsonIgnore
	private LocalDateTime createdAt;
		
	@Override
	public String toString() {

		return MoreObjects.toStringHelper(this)
	              .add("id", id)
	              .add("firstName", firstName)
	              .add("lastName", lastName)
	              .add("name", name)
	              .toString();
	}
}
