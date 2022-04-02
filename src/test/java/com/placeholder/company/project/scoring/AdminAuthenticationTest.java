package com.placeholder.company.project.scoring;

import java.util.Random;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import com.placeholder.company.project.rest.api.AdminAuthenticationRequest;
import com.placeholder.company.project.rest.api.AdminAuthenticationResponse;
import com.placeholder.company.project.rest.api.GeneralResponse;
import com.placeholder.company.project.rest.api.constants.ErrorCode;
import com.placeholder.company.project.rest.api.structures.OnDemand;
import com.placeholder.company.project.rest.controller.AdminAuthenticationController;

@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class AdminAuthenticationTest extends AbstractApplicationScoringTest {

	@Test
	public void testAuthorizedWithValidDefaultUser() throws Exception {
		String username = "username";
		String password = "password";

		OnDemand createOnDemand = new OnDemand();
		createOnDemand.setAuthorizationToken( AdminAuthenticationController.authorizationToken );
		createOnDemand.setEmail( "Employee@Placeholder.com" );

		AdminAuthenticationRequest request = new AdminAuthenticationRequest();
		request.setCreateOnDemand( createOnDemand );

		MockHttpServletResponse httpResponse = postHttpRequest( "/admin/authentication", request, createAuthHeaders( username, password ) );
		AdminAuthenticationResponse response = assertHttpResponseOk( httpResponse, AdminAuthenticationResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.OK, response.getErrorCode() );

		Assert.assertNotNull( "Missing 'token' in response.", response.getToken() );
		Assert.assertEquals( "Incorrect 'token.length',", 36, response.getToken().length() );

		System.out.println( "Let's retry the request just in case." );

		httpResponse = postHttpRequest( "/admin/authentication", request, createAuthHeaders( username, password ) );
		response = assertHttpResponseOk( httpResponse, AdminAuthenticationResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.OK, response.getErrorCode() );

		Assert.assertNotNull( "Missing 'token' in response.", response.getToken() );
		Assert.assertEquals( "Incorrect 'token.length',", 36, response.getToken().length() );
	}

	@Test
	public void testAuthorizedWithValidRandomUser() throws Exception {
		assumeMainFunctionalityIsWorking();

		String username = "username" + new Random().nextInt();
		String password = "password" + new Random().nextInt();

		OnDemand createOnDemand = new OnDemand();
		createOnDemand.setAuthorizationToken( AdminAuthenticationController.authorizationToken );
		createOnDemand.setEmail( "Employee@Placeholder.com" );

		AdminAuthenticationRequest request = new AdminAuthenticationRequest();
		request.setCreateOnDemand( createOnDemand );

		MockHttpServletResponse httpResponse = postHttpRequest( "/admin/authentication", request, createAuthHeaders( username, password ) );
		AdminAuthenticationResponse response = assertHttpResponseOk( httpResponse, AdminAuthenticationResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.OK, response.getErrorCode() );

		Assert.assertNotNull( "Missing 'token' in response.", response.getToken() );
		Assert.assertEquals( "Incorrect 'token.length',", 36, response.getToken().length() );
	}

	@Test
	public void testEmptyUnauthorizedWithoutAuthHeader() throws Exception {
		MockHttpServletResponse httpResponse = postHttpRequest( "/admin/authentication", "{}", new HttpHeaders() );
		GeneralResponse response = assertHttpResponseErr( httpResponse, GeneralResponse.class, HttpStatus.UNAUTHORIZED );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.AUTHENTICATION, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testEmptyUnauthorizedWithInvalidAuthHeader() throws Exception {
		MockHttpServletResponse httpResponse = postHttpRequest( "/admin/authentication", "{}", createAuthHeaders( "username:password" ) );
		GeneralResponse response = assertHttpResponseErr( httpResponse, GeneralResponse.class, HttpStatus.UNAUTHORIZED );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.INVALID, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testUnauthorizedWithInvalidAuthHeaderAndToken() throws Exception {
		assumeMainFunctionalityIsWorking();

		OnDemand createOnDemand = new OnDemand();
		createOnDemand.setAuthorizationToken( "123" );
		createOnDemand.setEmail( "Employee@Placeholder.com" );

		AdminAuthenticationRequest request = new AdminAuthenticationRequest();
		request.setCreateOnDemand( createOnDemand );

		MockHttpServletResponse httpResponse = postHttpRequest( "/admin/authentication", "{}", createAuthHeaders( "username:password" ) );
		GeneralResponse response = assertHttpResponseErr( httpResponse, GeneralResponse.class, HttpStatus.UNAUTHORIZED );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.INVALID, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testUnauthorizedWithInvalidAuthHeaderAndValidToken() throws Exception {
		assumeMainFunctionalityIsWorking();

		OnDemand createOnDemand = new OnDemand();
		createOnDemand.setAuthorizationToken( AdminAuthenticationController.authorizationToken );
		createOnDemand.setEmail( "Employee@Placeholder.com" );

		AdminAuthenticationRequest request = new AdminAuthenticationRequest();
		request.setCreateOnDemand( createOnDemand );

		MockHttpServletResponse httpResponse = postHttpRequest( "/admin/authentication", request, createAuthHeaders( "username:password" ) );
		GeneralResponse response = assertHttpResponseErr( httpResponse, GeneralResponse.class, HttpStatus.UNAUTHORIZED );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.INVALID, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testUnauthorizedWithInvalidAuthHeaderEncoding() throws Exception {
		assumeMainFunctionalityIsWorking();

		OnDemand createOnDemand = new OnDemand();
		createOnDemand.setAuthorizationToken( AdminAuthenticationController.authorizationToken );
		createOnDemand.setEmail( "Employee@Placeholder.com" );

		AdminAuthenticationRequest request = new AdminAuthenticationRequest();
		request.setCreateOnDemand( createOnDemand );

		MockHttpServletResponse httpResponse = postHttpRequest( "/admin/authentication", request, createAuthHeaders( "Basic username:	password" ) );
		GeneralResponse response = assertHttpResponseErr( httpResponse, GeneralResponse.class, HttpStatus.UNAUTHORIZED );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.INVALID, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testTokenPersistenceWithValidDefaultUser() throws Exception {
		assumeMainFunctionalityIsWorking();

		String username = "username2";
		String password = "password2";

		OnDemand createOnDemand = new OnDemand();
		createOnDemand.setAuthorizationToken( AdminAuthenticationController.authorizationToken );
		createOnDemand.setEmail( "Employee@Placeholder.com" );

		AdminAuthenticationRequest request = new AdminAuthenticationRequest();
		request.setCreateOnDemand( createOnDemand );

		MockHttpServletResponse httpResponse = postHttpRequest( "/admin/authentication", request, createAuthHeaders( username, password ) );
		AdminAuthenticationResponse response = assertHttpResponseOk( httpResponse, AdminAuthenticationResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.OK, response.getErrorCode() );

		Assert.assertNotNull( "Missing 'token' in response.", response.getToken() );
		Assert.assertEquals( "Incorrect 'token.length',", 36, response.getToken().length() );

		String firstToken = response.getToken();

		httpResponse = postHttpRequest( "/admin/authentication", request, createAuthHeaders( username, password ) );
		response = assertHttpResponseOk( httpResponse, AdminAuthenticationResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.OK, response.getErrorCode() );

		Assert.assertEquals( "Incorrect 'token',", firstToken, response.getToken() );
	}

	@Test
	public void testTokenPersistenceWithValidRandomUser() throws Exception {
		assumeMainFunctionalityIsWorking();

		String username = "username" + new Random().nextInt();
		String password = "password" + new Random().nextInt();

		OnDemand createOnDemand = new OnDemand();
		createOnDemand.setAuthorizationToken( AdminAuthenticationController.authorizationToken );
		createOnDemand.setEmail( "Employee@Placeholder.com" );

		AdminAuthenticationRequest request = new AdminAuthenticationRequest();
		request.setCreateOnDemand( createOnDemand );

		MockHttpServletResponse httpResponse = postHttpRequest( "/admin/authentication", request, createAuthHeaders( username, password ) );
		AdminAuthenticationResponse response = assertHttpResponseOk( httpResponse, AdminAuthenticationResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (1st),", ErrorCode.OK, response.getErrorCode() );

		Assert.assertNotNull( "Missing 'token' in response.", response.getToken() );
		Assert.assertEquals( "Incorrect 'token.length',", 36, response.getToken().length() );

		String firstToken = response.getToken();

		httpResponse = postHttpRequest( "/admin/authentication", request, createAuthHeaders( username, password ) );
		response = assertHttpResponseOk( httpResponse, AdminAuthenticationResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (2nd),", ErrorCode.OK, response.getErrorCode() );

		Assert.assertEquals( "Incorrect 'token',", firstToken, response.getToken() );
	}

	private HttpHeaders createAuthHeaders( String username, String password ) {
		return createAuthHeaders( "Basic " + Base64.encodeBase64String( ( String.format( "%s:%s", username, password ) ).getBytes() ) );
	}

	private HttpHeaders createAuthHeaders( String authValue ) {
		HttpHeaders headers = new HttpHeaders();
		headers.add( HttpHeaders.AUTHORIZATION, authValue );
		return headers;
	}

	private void assumeMainFunctionalityIsWorking() {
		try {
			testAuthorizedWithValidDefaultUser();
		} catch ( Throwable e ) {
			Assume.assumeNoException( "The main functionality of the API is not working. Received", e );
		}
	}
}
