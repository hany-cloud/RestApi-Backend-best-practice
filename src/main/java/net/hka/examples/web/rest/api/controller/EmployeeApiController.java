package net.hka.examples.web.rest.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.springframework.hateoas.CollectionModel;
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

import net.hka.examples.business.domain.Employee;
import net.hka.examples.business.dto.EmployeeDto;
import net.hka.examples.business.exception.EmployeeNotFoundException;
import net.hka.examples.business.repository.EmployeeRepository;
import net.hka.examples.web.rest.api.EmployeeAssembler;

@RestController
@RequestMapping("/payroll/v1/employees")
public class EmployeeApiController {

	private final EmployeeRepository repository;
	private final EmployeeAssembler assembler;

	EmployeeApiController(EmployeeRepository repository, EmployeeAssembler assembler) {
		
		this.repository = repository;
		this.assembler = assembler;
	}

	// Aggregate root
	@GetMapping
	public ResponseEntity<CollectionModel<EmployeeDto>> all() {

		List<Employee> employees = repository.findAll();

		return new ResponseEntity<>(
				assembler.toCollectionModel(employees), 
                HttpStatus.OK);			
	}

	// Single item
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> one(@PathVariable Long id) {
		
		return repository.findById(id) 
                .map(assembler::toModel) 
                .map(ResponseEntity::ok) 
                .orElseThrow(() -> new EmployeeNotFoundException(id));			
	}

	@PostMapping
	ResponseEntity<EmployeeDto> newEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
		
		Employee newEmployee = repository.save(assembler.toEntity(employeeDto));
		
		return ResponseEntity
				.created(linkTo(methodOn(EmployeeApiController.class).one(newEmployee.getId())).toUri())
				.body(assembler.toModel(newEmployee));
	}

	@PutMapping("/{id}")
	ResponseEntity<?> replaceEmployee(@Valid @RequestBody EmployeeDto employeeDto, @PathVariable Long id) {

		Employee updatedEmployee = repository.findById(id).map(employee -> {
			employee.setFirstName(employeeDto.getFirstName());
			employee.setLastName(employeeDto.getLastName());
			return repository.save(employee);
		}).orElseGet(() -> {
			employeeDto.setId(id);
			return repository.save(assembler.toEntity(employeeDto));
		});
		
		return ResponseEntity
			      .created(linkTo(methodOn(EmployeeApiController.class).one(updatedEmployee.getId())).toUri()) // employeeDto.getRequiredLink(IanaLinkRelations.SELF).toUri()
			      .body(assembler.toModel(updatedEmployee));
	}

	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
