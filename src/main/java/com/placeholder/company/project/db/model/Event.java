package com.placeholder.company.project.db.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Event implements Serializable {

	@Id
	private Long id;

	private String name;

	public Event() {

	}

	public Event( Long id, String name ) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
