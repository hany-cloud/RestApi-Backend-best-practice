package net.hka.examples.restapi.business.domain;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.hka.common.domain.AuditTimeBaseEntity;
import net.hka.common.web.multipart.file.storage.payload.SimpleAddress;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@ToString
public class Employee extends AuditTimeBaseEntity {

	private @Id @GeneratedValue Long id;
	
	@NotBlank
	private @Setter(AccessLevel.PUBLIC) String firstName;
	
	@NotBlank
	private @Setter(AccessLevel.PUBLIC) String lastName;
	
	@Embedded
    private SimpleAddress address;
	
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY) // fetch type lazy is the default fetch for the OneToMany
	private @Setter(AccessLevel.PUBLIC) List<EmployeeDocument> documents;
	
	public static Employee createEmployee(final String firstName, final String lastName, final SimpleAddress address, final List<EmployeeDocument> documents) {
		
		if(firstName.isEmpty()) throw new IllegalArgumentException("The firstName paremter is empty");
		if(lastName.isEmpty()) throw new IllegalArgumentException("The lastName paremter is empty");
		
		return new Employee(firstName, lastName, address, documents);
	}
	
	private Employee(String firstName, String lastName, final SimpleAddress address, final List<EmployeeDocument> documents) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		if(documents != null && documents.size() > 0) this.documents = documents;
	}
}
