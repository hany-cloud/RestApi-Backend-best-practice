package net.hka.examples.restapi.business.exception;

public class EmployeeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmployeeNotFoundException(final Long id) {
		
		super("Could not find employee " + id);
	}
}
