package net.hka.examples.business.exception.handler;

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

import net.hka.examples.business.exception.InvalidOrderStatusModificationException;
import net.hka.examples.business.exception.OrderNotFoundException;
import net.hka.examples.common.response.ApiErrorCode;
import net.hka.examples.common.response.ApiErrorResponse;
import net.hka.examples.common.response.ApiFieldError;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OrderExceptionHandler {
	@ResponseBody
	@ExceptionHandler(OrderNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	ResponseEntity<ApiErrorResponse> orderNotFoundHandler(OrderNotFoundException ex) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(ApiErrorResponse.createException(ApiErrorCode.BAD_REQUEST, ApiFieldError.of("Attempting to get Order", "", ex.getMessage())))
                ;
	}
	
	@ResponseBody
	@ExceptionHandler(InvalidOrderStatusModificationException.class)
	@ResponseStatus(HttpStatus.NOT_MODIFIED)
	ResponseEntity<ApiErrorResponse> invalidOrderStatusModificationHandler(InvalidOrderStatusModificationException ex) {
		return ResponseEntity
				.status(HttpStatus.CONFLICT) // HttpStatus.NOT_MODIFIED
				.header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(ApiErrorResponse.createException(ex.getCode(), ApiFieldError.of("Attempting to modify Order Status is failed", "", ex.getMessage())))
                ;
	}
}
