package com.placeholder.company.project.rest.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.placeholder.company.project.rest.api.structures.OnDemand;

/**
 * An API request to authenticate the admin before performing any operations which requires admin privileges.
 */
@JsonIgnoreProperties
@JsonInclude( JsonInclude.Include.NON_NULL )
public class AdminAuthenticationRequest {
	/**
	 * Create an admin user on-demand.
	 */
	@JsonProperty
	private OnDemand createOnDemand;

	public OnDemand getCreateOnDemand() {
		return createOnDemand;
	}

	public void setCreateOnDemand( OnDemand createOnDemand ) {
		this.createOnDemand = createOnDemand;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper( this ).omitNullValues()
				.add( "createOnDemand", createOnDemand )
				.toString();
	}
}
