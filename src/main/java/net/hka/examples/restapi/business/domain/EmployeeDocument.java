package net.hka.examples.restapi.business.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.hka.common.domain.AuditTimeBaseEntity;
import net.hka.common.web.multipart.file.storage.payload.FileResource;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@ToString
public class EmployeeDocument extends AuditTimeBaseEntity {

	private @Id @GeneratedValue Long id;
	
	@Embedded
    private FileResource fileResource;
		
	@ManyToOne(fetch = FetchType.EAGER) // fetch type eager is the default fetch for the ManyToOne
	@JoinColumn(name = "employee_id", referencedColumnName = "id")
	private Employee employee;
	
	public static EmployeeDocument create(final FileResource fileResource, final Employee employee) {

		if(fileResource == null) throw new IllegalArgumentException("The fileResource paremter is empty");
		return new EmployeeDocument(fileResource, employee);
	}
    
	private EmployeeDocument(@NotNull FileResource fileResource, @NotBlank Employee employee) {
		this.fileResource = fileResource;
		this.employee = employee;
	}
}
