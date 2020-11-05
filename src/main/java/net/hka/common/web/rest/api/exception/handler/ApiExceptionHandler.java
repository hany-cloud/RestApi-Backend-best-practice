package net.hka.common.web.rest.api.exception.handler;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import net.hka.common.web.rest.api.response.ApiErrorCode;
import net.hka.common.web.rest.api.response.ApiErrorResponse;
import net.hka.common.web.rest.api.response.ApiFieldError;

/**
 * Global API exception handler.
 */
@ControllerAdvice
public class ApiExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

	/**
	 * handle {@link TransactionSystemException}
	 * 
	 * @param e TransactionSystemException
	 * @return {@link ResponseEntity} showing all errors for violation of model
	 *         annotation constraints
	 * 
	 *         * - {@link Valid}, {@link Validated} binding error occur
	 */
	@ExceptionHandler(TransactionSystemException.class)
	protected ResponseEntity<ApiErrorResponse> handlePersistenceException(final TransactionSystemException e) {

		logger.warn("handleMethodArgumentNotValidException : {}", e.getMessage());

		Throwable cause = ((TransactionSystemException) e).getRootCause();
		if (cause instanceof ConstraintViolationException) {

			ConstraintViolationException consEx = (ConstraintViolationException) cause;
			final List<ApiFieldError> errors = new ArrayList<>();
			for (final ConstraintViolation<?> violation : consEx.getConstraintViolations()) {
				ApiFieldError error = new ApiFieldError(violation.getPropertyPath().toString(),
						violation.getInvalidValue() == null ? "null" : violation.getInvalidValue().toString(),
						violation.getMessage());
				errors.add(error);
			}

			return createBadRequestResponse(errors);
		}

		return new ResponseEntity<>(ApiErrorResponse.createException(ApiErrorCode.METHOD_NOT_ALLOWED),
				HttpStatus.METHOD_NOT_ALLOWED);
	}

	/**
	 * handle {@link MethodArgumentNotValidException}
	 *
	 * - {@link Valid}, {@link Validated} binding error occur
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
			final MethodArgumentNotValidException e) {

		logger.warn("handleMethodArgumentNotValidException : {}", e.getMessage());

		return createBadRequestResponse(e.getBindingResult());
	}

	/**
	 * handle {@link BindException}
	 *
	 * - {@link ModelAttribute}'s binding error
	 */
	@ExceptionHandler(BindException.class)
	protected ResponseEntity<ApiErrorResponse> handleBindException(final BindException e) {

		logger.warn("handleBindException : {}", e.getMessage());

		return createBadRequestResponse(e.getBindingResult());
	}

	/**
	 * handle {@link MethodArgumentTypeMismatchException}
	 *
	 * - if enum's type is mismatched
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatchException(
			final MethodArgumentTypeMismatchException e) {

		logger.warn("handleMethodArgumentTypeMismatchException : {}", e.getMessage());

		final String value = e.getValue() == null ? "" : e.getValue().toString();
		final ApiErrorResponse response = ApiErrorResponse.createException(ApiErrorCode.BAD_REQUEST,
				ApiFieldError.of(e.getName(), value, e.getErrorCode()));

		return ResponseEntity.badRequest().body(response);
	}

	/**
	 * handle {@link HttpMessageConversionException}
	 */
	@ExceptionHandler(HttpMessageConversionException.class)
	protected ResponseEntity<ApiErrorResponse> handleHttpMessageConversionException(
			final HttpMessageConversionException e) {

		logger.warn("handleHttpMessageConversionException : {}", e.getMessage());

		return createBadRequestResponse();
	}

	/**
	 * handle {@link HttpRequestMethodNotSupportedException}
	 *
	 * - not supported http method
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ApiErrorResponse> handleHttpRequestMethodNotSupportedException(
			final HttpRequestMethodNotSupportedException e) {

		logger.warn("handleHttpRequestMethodNotSupportedException : {}", e.getMessage());

		return new ResponseEntity<>(ApiErrorResponse.createException(ApiErrorCode.METHOD_NOT_ALLOWED),
				HttpStatus.METHOD_NOT_ALLOWED);
	}

//    /**
//     * handle {@link AccessDeniedException} i.e 403 error
//     */
//    @Override
//    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
//                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
//
//        logger.warn("handleAccessDeniedException : {}", accessDeniedException.getMessage());
//
//        final String jsonResponse = objectMapper.writeValueAsString(
//                ApiErrorResponse.createException(ApiErrorCode.FORBIDDEN));
//
//        httpServletResponse.setStatus(403);
//        httpServletResponse.setContentType("application/json;charset=UTF-8");
//        httpServletResponse.getWriter().write(jsonResponse);
//    }

//    /**
//     * handle 401 error
//     */
//    @Override
//    public void commence(HttpServletRequest httpServletRequest,
//                         HttpServletResponse httpServletResponse,
//                         AuthenticationException e) throws IOException, ServletException {
//
//        final String jsonResponse = objectMapper.writeValueAsString(
//                ApiErrorResponse.createException(ApiErrorCode.UNAUTHORIZED));
//
//        httpServletResponse.setStatus(401);
//        httpServletResponse.setContentType("application/json;charset=UTF-8");
//        httpServletResponse.getWriter().write(jsonResponse);
//    }

	// ============ business exception handle

	/**
	 * handle {@link Exception}
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ApiErrorResponse> handleException(final Exception e) {

		logger.warn("handleException", e);

		final List<ApiFieldError> errors = new ArrayList<>();
		errors.add(new ApiFieldError(e.getClass().toString(), e.getClass().getTypeName(), e.getLocalizedMessage()));

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ApiErrorResponse.createException(ApiErrorCode.INTERNAL_SERVER_ERROR, errors));
	}

	// ============ private

	private ResponseEntity<ApiErrorResponse> createBadRequestResponse(final BindingResult result) {

		return ResponseEntity.badRequest().body(ApiErrorResponse.createException(ApiErrorCode.BAD_REQUEST, result));
	}

	private ResponseEntity<ApiErrorResponse> createBadRequestResponse(final List<ApiFieldError> errors) {

		return ResponseEntity.badRequest().body(ApiErrorResponse.createException(ApiErrorCode.BAD_REQUEST, errors));
	}

	private ResponseEntity<ApiErrorResponse> createBadRequestResponse() {

		return ResponseEntity.badRequest().body(ApiErrorResponse.createException(ApiErrorCode.BAD_REQUEST));
	}
}
