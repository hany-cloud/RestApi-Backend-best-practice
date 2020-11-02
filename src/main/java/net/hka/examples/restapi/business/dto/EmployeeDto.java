package net.hka.examples.restapi.business.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;

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
import net.hka.common.web.multipart.file.storage.payload.SimpleAddress;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "employee")
@Relation(collectionRelation = "employees")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDto extends RepresentationModel<EmployeeDto> {

	private Long id;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	private String name;
	
	private SimpleAddress address;
	
	@Setter(AccessLevel.PRIVATE)
	@JsonIgnore
	private LocalDateTime createdAt;
		
	private List<EmployeeDocumentDto> documents;
	
	@Override
	public String toString() {

		return MoreObjects.toStringHelper(this)
	              .add("id", id)
	              .add("firstName", firstName)
	              .add("lastName", lastName)
	              .add("name", name)
	              .add("address", address)
	              .add("documents", documents)
	              .toString();
	}
}
