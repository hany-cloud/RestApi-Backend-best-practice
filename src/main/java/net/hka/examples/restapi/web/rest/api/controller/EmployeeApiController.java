package net.hka.examples.restapi.web.rest.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.hka.common.web.multipart.file.storage.payload.FileResource;
import net.hka.common.web.multipart.file.storage.service.FileStorageService;
import net.hka.examples.restapi.business.domain.Employee;
import net.hka.examples.restapi.business.domain.EmployeeDocument;
import net.hka.examples.restapi.business.dto.EmployeeDto;
import net.hka.examples.restapi.business.exception.EmployeeNotFoundException;
import net.hka.examples.restapi.business.repository.EmployeeDocumentRepository;
import net.hka.examples.restapi.business.repository.EmployeeRepository;
import net.hka.examples.restapi.web.rest.api.EmployeeAssembler;

@RestController
@RequestMapping("/payroll/v1/employees")
public class EmployeeApiController {

	private final EmployeeRepository employeeRepository;

	private final EmployeeDocumentRepository employeeDocumentRepository;

	private final EmployeeAssembler assembler;

	private FileStorageService fileStorageService;

	EmployeeApiController(final EmployeeRepository employeeRepository, final EmployeeAssembler assembler,
			final EmployeeDocumentRepository employeeDocumentRepository, final FileStorageService fileStorageService) {

		this.employeeRepository = employeeRepository;
		this.assembler = assembler;
		this.employeeDocumentRepository = employeeDocumentRepository;
		this.fileStorageService = fileStorageService;
		this.fileStorageService.setFileCodePrefix("EMP");
	}

	// Aggregate root
	@GetMapping
	public ResponseEntity<CollectionModel<EmployeeDto>> all() {

		List<Employee> employees = employeeRepository.findAll();

		return new ResponseEntity<>(assembler.toCollectionModel(employees), HttpStatus.OK);
	}

	// Single item
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> one(@PathVariable Long id) {

		return employeeRepository.findById(id).map(assembler::toModel).map(ResponseEntity::ok)
				.orElseThrow(() -> new EmployeeNotFoundException(id));
	}

	@PostMapping
	ResponseEntity<EmployeeDto> newEmployee(@Valid @RequestPart("employee") EmployeeDto employeeDto,
			@RequestPart("files") MultipartFile[] files) {

		Employee newEmployee = employeeRepository.save(assembler.toEntity(employeeDto));
		
		if (files != null) {
			List<FileResource> uplodedFiles = fileStorageService.uploadMultipleFiles(files);
			List<EmployeeDocument> employeeDocuments = uplodedFiles.stream()
					.map(file -> EmployeeDocument.create(file, newEmployee)).collect(Collectors.toList());

			employeeDocumentRepository.saveAll(employeeDocuments);
			newEmployee.setDocuments(employeeDocuments);
		}
		EmployeeDto newEmployeeDto = assembler.toModel(newEmployee);

		return ResponseEntity.created(linkTo(methodOn(EmployeeApiController.class).one(newEmployee.getId())).toUri())
				.body(newEmployeeDto);
	}

	@PutMapping("/{id}")
	ResponseEntity<?> replaceEmployee(@Valid @RequestBody EmployeeDto employeeDto, @PathVariable Long id) {

		Employee updatedEmployee = employeeRepository.findById(id).map(employee -> {
			employee.setFirstName(employeeDto.getFirstName());
			employee.setLastName(employeeDto.getLastName());
			return employeeRepository.save(employee);
		}).orElseGet(() -> {
			employeeDto.setId(id);
			return employeeRepository.save(assembler.toEntity(employeeDto));
		});

		return ResponseEntity
				.created(linkTo(methodOn(EmployeeApiController.class).one(updatedEmployee.getId())).toUri()) // employeeDto.getRequiredLink(IanaLinkRelations.SELF).toUri()
				.body(assembler.toModel(updatedEmployee));
	}

	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteEmployee(@PathVariable Long id) {

		employeeRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
