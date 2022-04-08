package com.placeholder.company.project.rest.controller;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import com.placeholder.company.project.rest.controller.tools.BasicAccessAuthentication;
import org.springframework.http.HttpStatus;

import com.placeholder.company.project.rest.GeneralHttpException;
import com.placeholder.company.project.rest.api.constants.ErrorCode;

public abstract class AbstractController {

	protected void validateAuthorizationHeader( String value ) throws GeneralHttpException {
		if (Strings.isNullOrEmpty(value)) {
			throw new GeneralHttpException( HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION, "Unauthorized request. Value for header 'Authorization' cannot be null." );
		}
		String regex = "^[^:]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);
		if(!m.matches()){
				throw new GeneralHttpException( HttpStatus.UNAUTHORIZED, ErrorCode.INVALID, "Invalid header" );
		}
	}

	protected void validateToken( String value ) throws GeneralHttpException {
		if (Strings.isNullOrEmpty(value)) {
			throw new GeneralHttpException( HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION, "Unauthorized request. Value for header 'Authorization' cannot be null." );
		}
		String regex = "^[^:]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);
		if(!m.matches()){
			throw new GeneralHttpException( HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION, "Invalid header" );
		}
	}

	protected void validateNotNull( String field, Object value) throws GeneralHttpException {
		if ( Objects.isNull(field) || Objects.isNull(value)) {
			throw new GeneralHttpException( HttpStatus.OK, ErrorCode.INVALID, String.format( "Invalid request. Value for field '%s' cannot be null.", field ) );
		}
	}
}
