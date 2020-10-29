package net.hka.examples.business.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.hka.examples.common.domain.BaseEntity;

@Entity
@Table(name = "CUSTOMER_ORDER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@ToString
public class Order extends BaseEntity {

	private @Id @GeneratedValue Long id;

	private String description;
	
	private @Setter(AccessLevel.PUBLIC) OrderStatus status;
	
	public static Order createOrder(String description, OrderStatus status) {

		return new Order(description, status);
	}
	
	private Order(String description, OrderStatus status) {

		this.description = description;
		this.status = status;
	}
}
