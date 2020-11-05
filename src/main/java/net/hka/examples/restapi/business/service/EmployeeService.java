package net.hka.examples.restapi.business.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import net.hka.common.web.multipart.file.storage.payload.FileResource;
import net.hka.examples.restapi.business.dto.EmployeeDto;

@Service
public interface EmployeeService {

	EmployeeDto save(final EmployeeDto employeeDto);	
	
	EmployeeDto save(final EmployeeDto employeeDto, final List<FileResource> uplodedFiles);
	
	EmployeeDto replaceEmployee(final EmployeeDto employeeDto, final Long id);
    
    void delete(final Long id);
    
    Iterable<EmployeeDto> findAll();

    Optional<EmployeeDto> findById(final Long id);
}
