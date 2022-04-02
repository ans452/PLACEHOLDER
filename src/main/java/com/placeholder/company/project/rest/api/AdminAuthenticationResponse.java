package com.placeholder.company.project.rest.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * An API response to pass the "temporary" authentication token of the admin to the external system.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
@JsonInclude( JsonInclude.Include.NON_NULL )
public class AdminAuthenticationResponse extends AbstractResponse {

	/**
	 * The "temporary" authentication token to perform operations that require admin privileges.
	 */
	@JsonProperty
	@NotNull
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken( String token ) {
		this.token = token;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper( this ).omitNullValues()
				.add( "token", token )
				.addValue( super.toString() )
				.toString();
	}
}
