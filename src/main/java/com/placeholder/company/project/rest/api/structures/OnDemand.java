package com.placeholder.company.project.rest.api.structures;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * The structure for passing relevant data to create an admin on-demand.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
@JsonInclude( JsonInclude.Include.NON_NULL )
public class OnDemand {

	/**
	 * The email address that belongs to the admin.
	 */
	@NotNull
	@JsonProperty
	private String email;

	/**
	 * The authorization token given to the gaming system to create new admins in our system on-demand.
	 */
	@NotNull
	@JsonProperty
	private String authorizationToken;

	public String getEmail() {
		return email;
	}

	public void setEmail( String email ) {
		this.email = email;
	}

	public String getAuthorizationToken() {
		return authorizationToken;
	}

	public void setAuthorizationToken( String authorizationToken ) {
		this.authorizationToken = authorizationToken;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper( this ).omitNullValues()
				.add( "email", email )
				.add( "authorizationToken", authorizationToken )
				.toString();
	}
}
