package net.hka.examples.restapi.business.exception;

import net.hka.common.web.rest.api.response.ApiErrorCode;

public class InvalidOrderStatusModificationException extends RuntimeException {
	
	private static final long serialVersionUID = 1902937808918520655L;
	
	private final ApiErrorCode code;

	public InvalidOrderStatusModificationException(final ApiErrorCode code) {
		
		super();
		this.code = code;
	}

	public InvalidOrderStatusModificationException(final String message, final Throwable cause, final ApiErrorCode code) {
		
		super(message, cause);
		this.code = code;
	}

	public InvalidOrderStatusModificationException(final String message, final ApiErrorCode code) {
		
		super(message);
		this.code = code;
	}

	public InvalidOrderStatusModificationException(final Throwable cause, final ApiErrorCode code) {
		
		super(cause);
		this.code = code;
	}
	
	public ApiErrorCode getCode() {
		return this.code;
	}
	

}
