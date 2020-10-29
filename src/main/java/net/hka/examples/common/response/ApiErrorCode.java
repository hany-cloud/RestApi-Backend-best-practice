package net.hka.examples.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * Api response code
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApiErrorCode {

    // Success
    SUCCESS(200, "", ""),

    // Common
    BAD_REQUEST(400, "C001", "Bad Request"),
    UNAUTHORIZED(401, "C002", "Unauthorized"),
    FORBIDDEN(403, "C003", "Forbidden"),
    METHOD_NOT_ALLOWED(405, "C003", "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "C004", "Internal Server Error");

    private final int status;
    private final String code;
    private final String message;

    ApiErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

	public int getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
    
    
}
