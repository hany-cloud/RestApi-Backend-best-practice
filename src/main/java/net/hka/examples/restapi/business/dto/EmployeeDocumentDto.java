package net.hka.examples.restapi.business.dto;

import java.time.LocalDateTime;

import javax.persistence.Embedded;

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
import net.hka.common.web.multipart.file.storage.payload.FileResource;
import net.hka.examples.restapi.business.domain.Employee;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "employee_document")
@Relation(collectionRelation = "employee_documents")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDocumentDto extends RepresentationModel<EmployeeDocumentDto> {

	private Long id;
	
	@Embedded
    private FileResource fileResource;
	
	@JsonIgnore
	private Employee employee;
	
	@Setter(AccessLevel.PRIVATE)
	private LocalDateTime createdAt;
	
	@Override
	public String toString() {

		return MoreObjects.toStringHelper(this)
	              .add("id", id)
	              .add("fileResource", fileResource)
	              .add("employee", employee)
	              .add("createdAt", createdAt)
	              .toString();
	}
}
