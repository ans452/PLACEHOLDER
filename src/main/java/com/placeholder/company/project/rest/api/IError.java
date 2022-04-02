package com.placeholder.company.project.rest.api;

import com.placeholder.company.project.rest.api.constants.ErrorCode;

public interface IError {

	ErrorCode getErrorCode();

	String getErrorMessage();
}
