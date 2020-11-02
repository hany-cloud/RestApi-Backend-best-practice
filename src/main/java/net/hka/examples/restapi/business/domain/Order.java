package net.hka.examples.restapi.business.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.hka.common.domain.AuditTimeBaseEntity;

@Entity
@Table(name = "CUSTOMER_ORDER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@ToString
public class Order extends AuditTimeBaseEntity {

	private @Id @GeneratedValue Long id;

	@NotBlank
	private String description;
	
	@NotNull
	private @Setter(AccessLevel.PUBLIC) OrderStatus status;
	
	@Column(name = "due_to")
    private LocalDate dueTo;
	
	public static Order createOrder(final String description, final OrderStatus status, final LocalDate dueTo) {

		if(description.isEmpty()) throw new IllegalArgumentException("The description paremter is empty");
		if(status == null) throw new IllegalArgumentException("The status paremter is empty");
		
		return new Order(description, status, dueTo);
	}
	
	private Order(String description, OrderStatus status, LocalDate dueTo) {

		this.description = description;
		this.status = status;
		this.dueTo = dueTo;
	}
}
