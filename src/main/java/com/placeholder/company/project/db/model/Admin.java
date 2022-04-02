package com.placeholder.company.project.db.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.placeholder.company.project.rest.controller.tools.BasicAccessAuthentication;

@Entity
public class Admin implements Serializable {

	@Id
	private String username;
	private String password;
	private String email;
	private String token;

	public Admin() {
	}

	public Admin( String username, String password, String email ) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getToken() {
		return token;
	}

	public void setToken( String token ) {
		this.token = token;
	}

	public boolean isAdminAuthenticated( BasicAccessAuthentication authentication ) {

		return this.username.equals( authentication.getUsername() ) && this.password.equals( authentication.getPassword() );
	}
}
