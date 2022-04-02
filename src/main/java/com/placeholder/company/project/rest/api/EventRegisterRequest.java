package com.placeholder.company.project.rest.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * An API request to register a new event from the gaming system.
 */
@JsonIgnoreProperties
@JsonInclude( JsonInclude.Include.NON_NULL )
public class EventRegisterRequest {

	/**
	 * The unique identifier for the event.
	 */
	@JsonProperty
	@NotNull
	private Long eventId;

	/**
	 * The name of the event.
	 */
	@JsonProperty
	@NotNull
	private String eventName;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId( Long eventId ) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName( String eventName ) {
		this.eventName = eventName;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper( this ).omitNullValues()
				.add( "eventId", eventId )
				.add( "eventName", eventName )
				.toString();
	}
}