package com.placeholder.company.project.rest.api.structures;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * The data reported per participant during or after an event.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
@JsonInclude( JsonInclude.Include.NON_NULL )
public class Participant {

	/**
	 * The registration number of the participant.
	 */
	@JsonProperty
	@NotNull
	private Long registrationNumber;

	/**
	 * The total duration of all the checkpoints the participant arrived to.
	 */
	@JsonProperty
	@NotNull
	private Long totalTimeInMillis;

	/**
	 * The duration breakdown of all the checkpoints in the order of creation at the gaming system.
	 */
	@JsonProperty
	@NotNull
	private List<Long> checkpointTimes;

	public  Long getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber( Long registrationNumber ) {
		this.registrationNumber = registrationNumber;
	}

	public Long getTotalTimeInMillis() {
		return totalTimeInMillis;
	}

	public void setTotalTimeInMillis( Long totalTimeInMillis ) {
		this.totalTimeInMillis = totalTimeInMillis;
	}

	public List<Long> getCheckpointTimes() {
		return checkpointTimes;
	}

	public void setCheckpointTimes( List<Long> checkpointTimes ) {
		this.checkpointTimes = checkpointTimes;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper( this ).omitNullValues()
				.add( "registrationNumber", registrationNumber )
				.add( "totalTimeInMillis", totalTimeInMillis )
				.add( "checkpointTimes", checkpointTimes )
				.toString();
	}
}
