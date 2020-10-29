package net.hka.examples.business.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.hka.examples.common.domain.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@ToString
public class Employee extends BaseEntity {

	private @Id @GeneratedValue Long id;
	
	@NotBlank
	private @Setter(AccessLevel.PUBLIC) String firstName;
	
	@NotBlank
	private @Setter(AccessLevel.PUBLIC) String lastName;
	
	public static Employee createEmployee(String firstName, String lastName) {

		return new Employee(firstName, lastName);
	}
	
	private Employee(String firstName, String lastName) {

		this.firstName = firstName;
		this.lastName = lastName;		
	}
}
