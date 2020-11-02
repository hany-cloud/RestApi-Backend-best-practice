package net.hka.common.web.rest.api.response;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

/**
 * Api request field binding errors
 */
public class ApiFieldError {

    private String field;
    private String value;
    private String reason;

    /**
     * Returns {@link ApiFieldError} singleton list given args
     */
    public static List<ApiFieldError> of(final String field, final String value, final String reason) {
    	
        return Collections.singletonList(new ApiFieldError(field, value, reason));
    }

    /**
     * Returns {@link ApiFieldError} list given binding result
     */
    public static List<ApiFieldError> of(final BindingResult result) {
    	
        if (result == null) {
            return Collections.emptyList();
        }

        return result.getFieldErrors()
                     .stream()
                     .map(error -> new ApiFieldError(
                             error.getField(),
                             error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                             error.getDefaultMessage())
                     )
                     .collect(Collectors.toList());
    }

    ApiFieldError() {
    }
    public ApiFieldError(final String field, final String value, final String reason) {
    	
        this.field = field;
        this.value = value;
        this.reason = reason;
    }

	public String getField() {
		return field;
	}

	public void setField(final String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(final String reason) {
		this.reason = reason;
	}
    
    
}
