package com.placeholder.company.project.rest.controller.tools;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.base.Strings;
import com.placeholder.company.project.rest.GeneralHttpException;
import com.placeholder.company.project.rest.api.constants.ErrorCode;
import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.http.HttpStatus;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import static javax.crypto.Cipher.SECRET_KEY;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class BasicAccessAuthentication {

	private final String authentication;
	private final String username;
	private final String password;

	private BasicAccessAuthentication( String authorization, String username, String password ) {
		this.authentication = authorization;
		this.username = username;
		this.password = password;
	}

	public String getAuthentication() {
		return this.authentication;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	/**
	 * Validate and parse the given authorization header value.
	 */
	public static BasicAccessAuthentication parseAuthorizationHeader( String authHeader ) throws GeneralHttpException {
		validateAuthHeader(authHeader);
    String base64Credentials = authHeader.substring("Basic".length()).trim();
    byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
    String credentials = new String(credDecoded, StandardCharsets.UTF_8);

    final String[] authentication = credentials.split(":", 2);
	for(String s : authentication){
		validateString(s);
	}
		return new BasicAccessAuthentication( authHeader, authentication[0], authentication[1] );
  }
  
  public String getToken() {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(
					this.authentication.getBytes(StandardCharsets.UTF_8));
			return new String(Hex.encode(hash)).substring(0,36);
		}
		catch(Exception e) {
			return "";
		}
  }

  public static void validateAuthHeader(String authHeader) throws GeneralHttpException {

		if(!Strings.isNullOrEmpty(authHeader)) {
			String regex = "^Basic [^:]+$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(authHeader);
			if (!matcher.matches()) {
				throw new GeneralHttpException(HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION, "Invalid authHeader");
			}
		}
		else {
			throw new GeneralHttpException(HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION, "Invalid authHeader");
		}
  }

  private static void validateString(String s) throws GeneralHttpException {
	  String regex = "^[^:]+$";
	  Pattern pattern = Pattern.compile(regex);
	  Matcher matcher = pattern.matcher(s);
	  if (!matcher.matches()) {
		  throw new GeneralHttpException(HttpStatus.UNAUTHORIZED, ErrorCode.INVALID, "Unauthorized request. Value for header 'Authorization' cannot be null.");
	  }
  }


	@Override
	public String toString() {
		return "BasicAccessAuthentication{" +
				"authentication='" + authentication + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				'}';
	}

	public static void main(String[] args) throws GeneralHttpException, NoSuchAlgorithmException {
		BasicAccessAuthentication basicAccessAuthentication = parseAuthorizationHeader("Basic dXNlcm5hbWU6cGFzc3dvcmQ=");
		System.out.println(basicAccessAuthentication.getToken().length());
	}
}
