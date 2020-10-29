package net.hka.examples.business.exception;

import net.hka.examples.common.response.ApiErrorCode;

public class InvalidOrderStatusModificationException extends RuntimeException {
	
	private static final long serialVersionUID = 1902937808918520655L;
	
	private final ApiErrorCode code;

	public InvalidOrderStatusModificationException(ApiErrorCode code) {
		super();
		this.code = code;
	}

	public InvalidOrderStatusModificationException(String message, Throwable cause, ApiErrorCode code) {
		super(message, cause);
		this.code = code;
	}

	public InvalidOrderStatusModificationException(String message, ApiErrorCode code) {
		super(message);
		this.code = code;
	}

	public InvalidOrderStatusModificationException(Throwable cause, ApiErrorCode code) {
		super(cause);
		this.code = code;
	}
	
	public ApiErrorCode getCode() {
		return this.code;
	}
	

}
