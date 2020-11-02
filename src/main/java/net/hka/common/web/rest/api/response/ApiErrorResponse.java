package net.hka.common.web.rest.api.response;

import java.util.Collections;
import java.util.List;

import org.springframework.validation.BindingResult;


/**
 * API common response
 */
public class ApiErrorResponse {

    /**
     * http status
     */
    private int status;

    /**
     * error code
     */
    private String code;

    /**
     * error message
     */
    private String message;

    /**
     * error fields
     */
    private List<ApiFieldError> errors;

    /**
     * Returns a error response given {@link ApiErrorCode}
     */
    public static ApiErrorResponse createException(final ApiErrorCode code) {
    	
        return new ApiErrorResponse(code, null);
    }

    /**
     * Returns a error response given {@link ApiErrorCode} and {@link BindingResult}
     */
    public static ApiErrorResponse createException(final ApiErrorCode code,
                                                   final BindingResult bindingResult) {
    	
        return createException(code, ApiFieldError.of(bindingResult));
    }

    /**
     * Returns a error response given {@link ApiErrorCode} and {@link ApiFieldError} errors
     */
    public static ApiErrorResponse createException(final ApiErrorCode code,
                                                   final List<ApiFieldError> errors) {
        
    	return new ApiErrorResponse(code, errors);
    }

    ApiErrorResponse() {
    }
    private ApiErrorResponse(final ApiErrorCode code, final List<ApiFieldError> errors) {
    	
        this.status = code.getStatus();
        this.message = code.getMessage();
        this.code = code.getCode();
        this.errors = errors == null ? Collections.emptyList() : errors;
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(final int status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public List<ApiFieldError> getErrors() {
		return errors;
	}

	public void setErrors(final List<ApiFieldError> errors) {
		this.errors = errors;
	}
}
