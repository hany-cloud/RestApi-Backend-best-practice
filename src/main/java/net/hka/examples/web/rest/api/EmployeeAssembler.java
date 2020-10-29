package net.hka.examples.web.rest.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

import net.hka.examples.business.domain.Employee;
import net.hka.examples.business.dto.EmployeeDto;
import net.hka.examples.common.assembler.ApiEntityAssembler;
import net.hka.examples.web.rest.api.controller.EmployeeApiController;

@Component
public class EmployeeAssembler implements RepresentationModelAssembler<Employee, EmployeeDto>, ApiEntityAssembler<Employee, EmployeeDto> {

	@Override
	public EmployeeDto toModel(Employee entity) { // EmployeeDto

		Preconditions.checkNotNull(entity, "employee");
		
		EmployeeDto employeeDto = EmployeeDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .name(new StringBuilder().append(entity.getFirstName()).append(" ").append(entity.getLastName()).toString())
                .createdAt(entity.getCreatedAt())
                .build();
		
		// Unconditional links to single-item resource and aggregate root
		employeeDto.add(linkTo(methodOn(EmployeeApiController.class).one(employeeDto.getId())).withSelfRel())
				.add(linkTo(methodOn(EmployeeApiController.class).all()).withRel("employees"));
		
		return employeeDto;
	}
	
	@Override
	public CollectionModel<EmployeeDto> toCollectionModel(Iterable<? extends Employee> entities) {
		
		CollectionModel<EmployeeDto> employeeDtos = RepresentationModelAssembler.super.toCollectionModel(entities);

		employeeDtos.add(linkTo(methodOn(EmployeeApiController.class).all()).withSelfRel());

		return employeeDtos;
	}
	
	/**
     * Convert to {@link Employee} from {@link EmployeeDto}
     */
	@Override
	public Employee toEntity(EmployeeDto dto) {
		
		Preconditions.checkNotNull(dto, "employeeDto");
		return Employee.createEmployee(dto.getFirstName(), dto.getLastName());
	}

}
