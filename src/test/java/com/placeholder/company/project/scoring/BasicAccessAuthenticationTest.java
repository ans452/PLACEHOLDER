package com.placeholder.company.project.scoring;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.placeholder.company.project.rest.GeneralHttpException;
import com.placeholder.company.project.rest.controller.tools.BasicAccessAuthentication;

@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class BasicAccessAuthenticationTest extends AbstractApplicationScoringTest {

	@Test( expected = GeneralHttpException.class )
	public void testAuthValueAsNull() throws Exception {
		Assert.assertNull( BasicAccessAuthentication.parseAuthorizationHeader( null ) );
	}

	@Test( expected = GeneralHttpException.class )
	public void testAuthValueAsRandomString() throws Exception {
		BasicAccessAuthentication.parseAuthorizationHeader( RandomStringUtils.randomAlphanumeric( 36 ) );
	}

	@Test()
	public void testAuthValueAsWhiteSpace() {
		validateParseFailsWithValue( "Authorization with empty string should have failed.", "" );
		validateParseFailsWithValue( "Authorization with a single 'space' string should have failed.", " " );
		validateParseFailsWithValue( "Authorization with any other 'only' whitespace string should have failed.", " \n \t  " );
	}

	private void validateParseFailsWithValue( String message, String headerValue ) {
		try {
			BasicAccessAuthentication.parseAuthorizationHeader( headerValue );
			Assert.fail( message );
		} catch ( GeneralHttpException ignore ) {
		}
	}

	@Test
	public void testEncodedBasicAuthentication() throws Exception {
		BasicAccessAuthentication.parseAuthorizationHeader( createAuthHeader( "username", "password" ) );
	}

	@Test( expected = GeneralHttpException.class )
	public void testEncodedBasicAuthenticationWithInvalidUsername() throws Exception {
		assumeMainFunctionalityIsWorking();
		BasicAccessAuthentication.parseAuthorizationHeader( createAuthHeader( "username:", "password" ) );
	}

	@Test( expected = GeneralHttpException.class )
	public void testEncodedBasicAuthenticationWithInvalidPassword() throws Exception {
		assumeMainFunctionalityIsWorking();
		BasicAccessAuthentication.parseAuthorizationHeader( createAuthHeader( "username", "password:" ) );
	}

	@Test( expected = GeneralHttpException.class )
	public void testUnencodedAuthentication() throws Exception {
		assumeMainFunctionalityIsWorking();
		BasicAccessAuthentication.parseAuthorizationHeader( "username:password" );
	}

	@Test( expected = GeneralHttpException.class )
	public void testUnencodedBasicAuthentication() throws Exception {
		assumeMainFunctionalityIsWorking();
		BasicAccessAuthentication.parseAuthorizationHeader( "Basic username:password" );
	}

	private String createAuthHeader( String username, String password ) {
		return "Basic " + Base64.encodeBase64String( ( String.format( "%s:%s", username, password ) ).getBytes() );
	}

	private void assumeMainFunctionalityIsWorking() {
		try {
			testEncodedBasicAuthentication();
		} catch ( Throwable e ) {
			Assume.assumeNoException( "The main functionality of the API is not working. Received", e );
		}
	}
}
