package net.hka.examples.restapi.web.rest.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

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
import net.hka.examples.restapi.business.dto.EmployeeDto;
import net.hka.examples.restapi.business.exception.EmployeeNotFoundException;
import net.hka.examples.restapi.business.service.EmployeeService;

@RestController
@RequestMapping("/payroll/v1/employees")
public class EmployeeApiController {

	private final EmployeeService employeeService;
	
	private final FileStorageService fileStorageService;

	EmployeeApiController(final EmployeeService employeeService, FileStorageService fileStorageService) {

		this.employeeService = employeeService;
		this.fileStorageService = fileStorageService;
		this.fileStorageService.setFileCodePrefix("EMP");
	}

	// Aggregate root
	@GetMapping
	public ResponseEntity<Iterable<EmployeeDto>> all() {

		return new ResponseEntity<>(employeeService.findAll(), HttpStatus.OK);
	}

	// Single item
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> one(@PathVariable Long id) {

		return employeeService.findById(id).map(ResponseEntity::ok)
				.orElseThrow(() -> new EmployeeNotFoundException(id));
	}

	@PostMapping
	ResponseEntity<EmployeeDto> newEmployee(@Valid @RequestPart("employee") EmployeeDto employeeDto,
			@RequestPart("files") MultipartFile[] files) {
		
		List<FileResource> uplodedFiles = new ArrayList<>();

		if (files != null) {
			// upload employee documents
			uplodedFiles = fileStorageService.upload(files);
		}
		
		try {

			EmployeeDto newEmployeeDto = employeeService.save(employeeDto, uplodedFiles);
			
			return ResponseEntity.created(linkTo(methodOn(EmployeeApiController.class).one(newEmployeeDto.getId())).toUri())
					.body(newEmployeeDto);

		} catch (Throwable e) {

			// delete system files that have been uploaded
			fileStorageService.delete(uplodedFiles);

			throw e;
		}
	}

	@PutMapping("/{id}")
	ResponseEntity<?> replaceEmployee(@Valid @RequestBody EmployeeDto employeeDto, @PathVariable Long id) {
		
		EmployeeDto updatedEmployeeDto = employeeService.replaceEmployee(employeeDto, id);
		return ResponseEntity
				.created(linkTo(methodOn(EmployeeApiController.class).one(updatedEmployeeDto.getId())).toUri())
				.body(updatedEmployeeDto);
	}

	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteEmployee(@PathVariable Long id) {

		employeeService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
