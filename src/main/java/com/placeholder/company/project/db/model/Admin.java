package com.placeholder.company.project.db.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.placeholder.company.project.rest.controller.tools.BasicAccessAuthentication;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.util.encoders.Base64Encoder;

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

	@Override
	public String toString() {
		return "Admin{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", token='" + token + '\'' +
				'}';
	}

	public boolean isAdminAuthenticated(BasicAccessAuthentication authentication ) {
		return Objects.equals(
				BasicAccessAuthentication.getToken(authentication.getAuthentication()),
				BasicAccessAuthentication.getToken("Basic " + Base64.encodeBase64String(
						( String.format( "%s:%s", this.username, this.password ) ).getBytes()))
		);
	}
}
