package net.hka.examples.restapi.business.exception.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import net.hka.common.web.rest.api.response.ApiErrorCode;
import net.hka.common.web.rest.api.response.ApiErrorResponse;
import net.hka.common.web.rest.api.response.ApiFieldError;
import net.hka.examples.restapi.business.exception.EmployeeNotFoundException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EmployeeNotFoundExceptionHandler {
	
	@ResponseBody
	@ExceptionHandler(EmployeeNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	ResponseEntity<ApiErrorResponse> employeeNotFoundHandler(final EmployeeNotFoundException ex) {
		
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(ApiErrorResponse.createException(ApiErrorCode.BAD_REQUEST, ApiFieldError.of("Attempting to get Employee", "", ex.getMessage())))
                ;		
	}
}
