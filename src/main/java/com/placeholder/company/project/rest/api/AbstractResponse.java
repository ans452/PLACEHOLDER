package com.placeholder.company.project.rest.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.placeholder.company.project.rest.api.constants.ErrorCode;

/**
 * The abstract HTTP response structure that should be implemented by every response directed to the external systems.
 */
public abstract class AbstractResponse implements IError {

	/**
	 * The resulting error code of the operation. The default response for successful operations is OK.
	 */
	@JsonProperty
	@NotNull
	private	ErrorCode errorCode;

	/**
	 * The resulting error message of the operation. Useful for the external systems to determine the cause of the error.
	 */
	@JsonProperty
	private	String errorMessage;

	@Override
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode( ErrorCode errorCode ) {
		this.errorCode = errorCode;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage( String errorMessage ) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper( this ).omitNullValues()
				.add( "errorCode", errorCode )
				.add( "errorMessage", errorMessage )
				.toString();
	}
}
