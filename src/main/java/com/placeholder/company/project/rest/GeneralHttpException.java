package com.placeholder.company.project.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.placeholder.company.project.rest.api.GeneralResponse;
import com.placeholder.company.project.rest.api.constants.ErrorCode;

public class GeneralHttpException extends Exception {

	private HttpStatus httpStatus;
	private ErrorCode errorCode;
	private String message;

	public GeneralHttpException( ErrorCode errorCode, String message ) {
		this( HttpStatus.OK, errorCode, message );
	}

	public GeneralHttpException( HttpStatus httpStatus, ErrorCode errorCode, String message ) {
		super( message );
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.message = message;
	}

	public ResponseEntity createErrorResponse() {
		GeneralResponse response = new GeneralResponse();
		response.setErrorCode( errorCode );
		response.setErrorMessage( message );

		return ResponseEntity.status( httpStatus ).body( response );
	}
}
