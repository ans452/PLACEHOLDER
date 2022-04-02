package com.placeholder.company.project.rest.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * An API request to receive a simplified report of an event.
 */
@JsonIgnoreProperties
@JsonInclude( JsonInclude.Include.NON_NULL )
public class EventReportRequest {

	/**
	 * The unique identifier for the event.
	 */
	@JsonProperty
	@NotNull
	private Long eventId;

	/**
	 * The count of TOP participants requested in the report. The default is all participants.
	 */
	@JsonProperty
	private Integer top;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId( Long eventId ) {
		this.eventId = eventId;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop( Integer top ) {
		this.top = top;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper( this ).omitNullValues()
				.add( "eventId", eventId )
				.add( "top", top )
				.toString();
	}
}
