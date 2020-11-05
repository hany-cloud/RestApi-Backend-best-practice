package net.hka.examples.restapi.web.rest.api;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

import net.hka.common.web.rest.api.assembler.ApiEntityAssembler;
import net.hka.examples.restapi.business.dto.EmployeeDocumentDto;
import net.hka.examples.restapi.business.dto.EmployeeDto;
import net.hka.examples.restapi.domain.Employee;
import net.hka.examples.restapi.domain.EmployeeDocument;

@Component
public class EmployeeDocumentAssembler implements RepresentationModelAssembler<EmployeeDocument, EmployeeDocumentDto>,
		ApiEntityAssembler<EmployeeDocument, EmployeeDocumentDto> {

	@Override
	public EmployeeDocumentDto toModel(final EmployeeDocument entity) {

		Preconditions.checkNotNull(entity, "employeeDocument");

		EmployeeDocumentDto employeeDocumentDto = EmployeeDocumentDto.builder()
													.id(entity.getId())
													.fileResource(entity.getFileResource())
													.employee(entity.getEmployee())
													.createdAt(entity.getCreatedAt())
													.build();
		
		return employeeDocumentDto;
	}

	/**
	 * Convert to {@link Employee} from {@link EmployeeDto}
	 */
	@Override
	public EmployeeDocument toEntity(final EmployeeDocumentDto dto) {

		Preconditions.checkNotNull(dto, "employeeDocumentDto");

		return EmployeeDocument.create(dto.getFileResource(), dto.getEmployee());
	}

}
