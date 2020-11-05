package net.hka.examples.restapi.business.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.hka.common.web.multipart.file.storage.payload.FileResource;
import net.hka.examples.restapi.business.dto.EmployeeDto;
import net.hka.examples.restapi.domain.Employee;
import net.hka.examples.restapi.domain.EmployeeDocument;
import net.hka.examples.restapi.domain.repository.EmployeeDocumentRepository;
import net.hka.examples.restapi.domain.repository.EmployeeRepository;
import net.hka.examples.restapi.web.rest.api.EmployeeAssembler;

@Service("EmployeeService")
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;

	private final EmployeeDocumentRepository employeeDocumentRepository;

	private final EmployeeAssembler assembler;

	public EmployeeServiceImpl(final EmployeeRepository employeeRepository, final EmployeeAssembler assembler,
			final EmployeeDocumentRepository employeeDocumentRepository) {

		this.employeeRepository = employeeRepository;
		this.assembler = assembler;
		this.employeeDocumentRepository = employeeDocumentRepository;
	}

	@Override
	@Transactional
	public EmployeeDto save(final EmployeeDto employeeDto) {

		if (employeeDto == null)
			throw new IllegalArgumentException("The paremter is null");

		Employee newEmployee = employeeRepository.save(assembler.toEntity(employeeDto));

		return assembler.toModel(newEmployee);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public EmployeeDto save(final EmployeeDto employeeDto, final List<FileResource> uplodedFiles) {

		try {

			if (employeeDto == null)
				throw new IllegalArgumentException("The paremter is null");

			// save the employee
			Employee newEmployee = employeeRepository.save(assembler.toEntity(employeeDto));

			if (uplodedFiles != null && uplodedFiles.size() > 0) {

				List<EmployeeDocument> employeeDocuments = uplodedFiles.stream()
						.map(file -> EmployeeDocument.create(file, newEmployee)).collect(Collectors.toList()); // null

				// save employee documents
				employeeDocumentRepository.saveAll(employeeDocuments);

				// set employee documents for displaying purpose
				newEmployee.setDocuments(employeeDocuments);		
			}
			
			return assembler.toModel(newEmployee);

		} catch (Throwable e) {

			throw e;
		}
	}

	@Override
	@Transactional
	public EmployeeDto replaceEmployee(final EmployeeDto employeeDto, final Long id) {

		if (id == null)
			throw new IllegalArgumentException("The id paremter is null");

		Employee updatedEmployee = employeeRepository.findById(id).map(employee -> {
			employee.setFirstName(employeeDto.getFirstName());
			employee.setLastName(employeeDto.getLastName());
			return employeeRepository.save(employee); // update
		}).orElseGet(() -> {
			employeeDto.setId(id);
			return employeeRepository.save(assembler.toEntity(employeeDto)); // save
		});

		return assembler.toModel(updatedEmployee);
	}

	@Override
	@Transactional
	public void delete(final Long id) {

		if (id == null)
			throw new IllegalArgumentException("The paremter is null");

		employeeRepository.deleteById(id);
	}

	@Override
	public Iterable<EmployeeDto> findAll() {

		List<Employee> employees = employeeRepository.findAll();

		return assembler.toCollectionModel(employees);
	}

	@Override
	public Optional<EmployeeDto> findById(final Long id) {

		return employeeRepository.findById(id).map(assembler::toModel);
	}
}
