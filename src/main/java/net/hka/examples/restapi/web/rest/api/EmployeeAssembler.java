package net.hka.examples.restapi.web.rest.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

import net.hka.common.web.rest.api.assembler.ApiEntityAssembler;
import net.hka.examples.restapi.business.domain.Employee;
import net.hka.examples.restapi.business.domain.EmployeeDocument;
import net.hka.examples.restapi.web.dto.EmployeeDto;
import net.hka.examples.restapi.web.rest.api.controller.EmployeeApiController;

@Component
public class EmployeeAssembler
		implements RepresentationModelAssembler<Employee, EmployeeDto>, ApiEntityAssembler<Employee, EmployeeDto> {

	@Autowired
	EmployeeDocumentAssembler documentAssembler;

	@Override
	public EmployeeDto toModel(final Employee entity) {

		Preconditions.checkNotNull(entity, "employee");

		EmployeeDto employeeDto = EmployeeDto.builder()
				.id(entity.getId())
				.firstName(entity.getFirstName())
				.lastName(entity.getLastName())
				.name(new StringBuilder().append(entity.getFirstName()).append(" ").append(entity.getLastName())
						.toString())
				.address(entity.getAddress()).createdAt(entity.getCreatedAt())
				.documents(entity.getDocuments().stream()
						.map(document -> documentAssembler.toModel(document)).collect(Collectors.toList()))
				.build();

		// Unconditional links to single-item resource and aggregate root
		employeeDto.add(linkTo(methodOn(EmployeeApiController.class).one(employeeDto.getId())).withSelfRel())
				.add(linkTo(methodOn(EmployeeApiController.class).all()).withRel("employees"));

		return employeeDto;
	}

	@Override
	public CollectionModel<EmployeeDto> toCollectionModel(final Iterable<? extends Employee> entities) {

		CollectionModel<EmployeeDto> employeeDtos = RepresentationModelAssembler.super.toCollectionModel(entities);

		employeeDtos.add(linkTo(methodOn(EmployeeApiController.class).all()).withSelfRel());

		return employeeDtos;
	}

	/**
	 * Convert to {@link Employee} from {@link EmployeeDto}
	 */
	@Override
	public Employee toEntity(final EmployeeDto dto) {

		Preconditions.checkNotNull(dto, "employeeDto");

		List<EmployeeDocument> documents = dto.getDocuments() == null ? new ArrayList<>(): 
								dto.getDocuments()
									.stream()
									.map(document -> documentAssembler.toEntity(document)).collect(Collectors.toList());
		return Employee.createEmployee(dto.getFirstName(), dto.getLastName(), dto.getAddress(), documents);
	}

}
