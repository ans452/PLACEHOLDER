package com.placeholder.company.project.db.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@IdClass( Checkpoint.CheckpointId.class )
@Table( uniqueConstraints = @UniqueConstraint( columnNames = { "messageId", "eventId", "registrationNumber" } ) )
public class Checkpoint implements Serializable, Comparable<Checkpoint> {

	@Id
	private Long messageId;

	@Id
	private Long eventId;

	@Id
	private Long registrationNumber;

	private Long durationInMillis;

	public Checkpoint() {
	}

	public Checkpoint( Long messageId, Long eventId, Long registrationNumber, Long durationInMillis ) {
		this.messageId = messageId;
		this.eventId = eventId;
		this.registrationNumber = registrationNumber;
		this.durationInMillis = durationInMillis;
	}

	@Override
	public String toString() {
		return "Checkpoint{" +
				"messageId=" + messageId +
				", eventId=" + eventId +
				", registrationNumber=" + registrationNumber +
				", durationInMillis=" + durationInMillis +
				'}';
	}

	public Long getMessageId() {
		return messageId;
	}

	public Long getEventId() {
		return eventId;
	}

	public Long getRegistrationNumber() {
		return registrationNumber;
	}

	public Long getDurationInMillis() {
		return durationInMillis;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public void setRegistrationNumber(Long registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public void setDurationInMillis(Long durationInMillis) {
		this.durationInMillis = durationInMillis;
	}

	@Override
	public int compareTo( Checkpoint other ) {
		return Long.compare( this.messageId, other.messageId );
	}

	@IdClass( Checkpoint.class )
	public static class CheckpointId implements Serializable {

		@Id
		private Long messageId;

		@Id
		private Long eventId;

		@Id
		private Long registrationNumber;

		public CheckpointId() {
		}

		public CheckpointId( Long messageId, Long eventId, Long registrationNumber ) {
			this.messageId = messageId;
			this.eventId = eventId;
			this.registrationNumber = registrationNumber;
		}

		@Override
		public String toString() {
			return "CheckpointId{" +
					"messageId=" + messageId +
					", eventId=" + eventId +
					", registrationNumber=" + registrationNumber +
					'}';
		}

		@Override
		public boolean equals( Object o ) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}
			CheckpointId that = (CheckpointId)o;
			return Objects.equals( messageId, that.messageId ) &&
					Objects.equals( eventId, that.eventId ) &&
					Objects.equals( registrationNumber, that.registrationNumber );
		}

		@Override
		public int hashCode() {
			return Objects.hash( messageId, eventId, registrationNumber );
		}
	}
}
