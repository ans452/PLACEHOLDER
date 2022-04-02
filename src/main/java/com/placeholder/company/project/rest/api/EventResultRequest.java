package com.placeholder.company.project.rest.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * An API request to pass the overview of the participants' intermediate results in an event.
 */
@JsonIgnoreProperties
@JsonInclude( JsonInclude.Include.NON_NULL )
public class EventResultRequest {

	/**
	 * The identifier of the message. Generated during an event to differentiate messages that belong to participants.
	 */
	@JsonProperty
	@NotNull
	private Long messageId;

	/**
	 * The unique identifier for the event.
	 */
	@JsonProperty
	@NotNull
	private Long eventId;

	/**
	 * The registration number of the participant.
	 */
	@JsonProperty
	@NotNull
	private Long registrationNumber;

	/**
	 * The amount of time it took for the participant to arrive at the next checkpoint.
	 */
	@JsonProperty
	@NotNull
	private Long durationInMillis;

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId( Long messageId ) {
		this.messageId = messageId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId( Long eventId ) {
		this.eventId = eventId;
	}

	public Long getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber( Long registrationNumber ) {
		this.registrationNumber = registrationNumber;
	}

	public Long getDurationInMillis() {
		return durationInMillis;
	}

	public void setDurationInMillis( Long durationInMillis ) {
		this.durationInMillis = durationInMillis;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper( this ).omitNullValues()
				.add( "messageId", messageId )
				.add( "eventId", eventId )
				.add( "registrationNumber", registrationNumber )
				.add( "durationInMillis", durationInMillis )
				.toString();
	}
}