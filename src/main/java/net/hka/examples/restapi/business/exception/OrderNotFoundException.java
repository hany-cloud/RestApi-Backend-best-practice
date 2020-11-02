package net.hka.examples.restapi.business.exception;

public class OrderNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderNotFoundException(final Long id) {
		
		super("Could not find order " + id);
	}
}
