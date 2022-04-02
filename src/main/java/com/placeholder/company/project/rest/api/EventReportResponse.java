package com.placeholder.company.project.rest.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.placeholder.company.project.rest.api.structures.Participant;

/**
 * An API response to pass a simplified report of an event.
 */
@JsonIgnoreProperties
@JsonInclude( JsonInclude.Include.NON_NULL )
public class EventReportResponse extends AbstractResponse {

	/**
	 * The name of the event.
	 */
	@JsonProperty
	@NotNull
	private String eventName;

	/**
	 * The participants in the event. Ordered by the best participant to the worst using currently available data.
	 */
	@JsonProperty
	@NotNull
	private List<Participant> participants;

	public String getEventName() {
		return eventName;
	}

	public void setEventName( String eventName ) {
		this.eventName = eventName;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants( List<Participant> participants ) {
		this.participants = participants;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper( this ).omitNullValues()
				.add( "eventName", eventName )
				.add( "participants", participants )
				.addValue( super.toString() )
				.toString();
	}
}
