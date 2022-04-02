package com.placeholder.company.project.scoring;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;

import com.placeholder.company.project.rest.api.AdminAuthenticationRequest;
import com.placeholder.company.project.rest.api.AdminAuthenticationResponse;
import com.placeholder.company.project.rest.api.EventRegisterRequest;
import com.placeholder.company.project.rest.api.EventReportRequest;
import com.placeholder.company.project.rest.api.EventReportResponse;
import com.placeholder.company.project.rest.api.EventResultRequest;
import com.placeholder.company.project.rest.api.GeneralResponse;
import com.placeholder.company.project.rest.api.constants.ErrorCode;
import com.placeholder.company.project.rest.api.structures.OnDemand;
import com.placeholder.company.project.rest.controller.AdminAuthenticationController;

@DirtiesContext( classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD )
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class FinalFlowTest extends AbstractApplicationScoringTest {

	private static final String USERNAME = "username2";
	private static final String PASSWORD = "password2";
	private static final String EMAIL = "Employee@Placeholder.com";
	private static final Long EVENT_ID = 1337L;

	@Test
	public void testFinalAdminAuthenticationOnDemand() throws Exception {
		finalAdminAuthenticationOnDemand();
	}

	public String finalAdminAuthenticationOnDemand() throws Exception {
		OnDemand createOnDemand = new OnDemand();
		createOnDemand.setAuthorizationToken( AdminAuthenticationController.authorizationToken );
		createOnDemand.setEmail( EMAIL );

		AdminAuthenticationRequest request = new AdminAuthenticationRequest();
		request.setCreateOnDemand( createOnDemand );

		MockHttpServletResponse httpResponse = postHttpRequest( "/admin/authentication", request, createAuthHeaders( USERNAME, PASSWORD ) );
		AdminAuthenticationResponse response = assertHttpResponseOk( httpResponse, AdminAuthenticationResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.OK, response.getErrorCode() );

		Assert.assertNotNull( "Missing 'token' in response.", response.getToken() );
		Assert.assertEquals( "Incorrect 'token.length',", 36, response.getToken().length() );
		return response.getToken();
	}

	@Test
	public void testFinalAdminAuthenticationWithoutDemand() throws Exception {
		finalAdminAuthenticationWithoutDemand();
	}

	public String finalAdminAuthenticationWithoutDemand() throws Exception {
		String firstToken = finalAdminAuthenticationOnDemand();

		AdminAuthenticationRequest request = new AdminAuthenticationRequest();

		MockHttpServletResponse httpResponse = postHttpRequest( "/admin/authentication", request, createAuthHeaders( USERNAME, PASSWORD ) );
		AdminAuthenticationResponse response = assertHttpResponseOk( httpResponse, AdminAuthenticationResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.OK, response.getErrorCode() );

		Assert.assertNotNull( "Missing 'token' in response.", response.getToken() );
		Assert.assertEquals( "Incorrect 'token.length',", 36, response.getToken().length() );

		Assert.assertEquals( "Incorrect 'token',", firstToken, response.getToken() );
		return response.getToken();
	}

	@Test
	public void testFinalEventRegistration() throws Exception {
		finalEventRegistration();
	}

	public String finalEventRegistration() throws Exception {
		String token = finalAdminAuthenticationWithoutDemand();

		EventRegisterRequest request = createEventRegisterRequest( "test", EVENT_ID );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/register", request, createAuthHeaders( token ) );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.OK, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );

		return token;
	}

	@Test
	public void testFinalEventResult() throws Exception {
		String token = finalEventRegistration();
		testFinalEventResult( token, 10L, EVENT_ID, 91L, 10L );
	}

	public void testFinalEventResult( String token, Long messageId, Long eventId, Long registrationNumber, Long durationInMillis ) throws Exception {
		EventResultRequest request = createEventResultRequest( messageId, eventId, registrationNumber, durationInMillis );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/result", request, createAuthHeaders( token ) );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.OK, response.getErrorCode() );
	}

	@Test
	public void testFinalReportEvent() throws Exception {
		String token = finalEventRegistration();
		testFinalEventResult( token, 10L, EVENT_ID, 91L, 10L );

		EventReportRequest eventReportRequest = createEventReportRequest( EVENT_ID, null );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/report", eventReportRequest, new HttpHeaders() );
		EventReportResponse reportResponse = assertHttpResponseOk( httpResponse, EventReportResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.OK, reportResponse.getErrorCode() );

		Assert.assertEquals( "Incorrect 'participants.size',", 1, reportResponse.getParticipants().size() );

		Assert.assertNotNull( "Missing 'participants[0].checkpointTimes' in response.", reportResponse.getParticipants().get( 0 ).getCheckpointTimes() );
		Assert.assertEquals( "Incorrect 'participants[0].checkpointTimes.size',", 1, reportResponse.getParticipants().get( 0 ).getCheckpointTimes().size() );
		Assert.assertEquals( "Incorrect 'participants[0].registrationNumber',", new Long( 91 ), reportResponse.getParticipants().get( 0 ).getRegistrationNumber() );
		Assert.assertEquals( "Incorrect 'participants[0].totalTimeInMillis',", new Long( 10 ), reportResponse.getParticipants().get( 0 ).getTotalTimeInMillis() );
	}

	@Test
	public void testFinalReportEventBySortedParticipants() throws Exception {
		String token = finalEventRegistration();

		ArrayList<Long> participants = new ArrayList<>( Arrays.asList( 1L, 2L, 3L ) );

		for ( Long participant : participants ) {
			testFinalEventResult( token, 10L, EVENT_ID, participant, participant * 3 );
		}

		EventReportRequest eventReportRequest = createEventReportRequest( EVENT_ID, null );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/report", eventReportRequest, new HttpHeaders() );
		EventReportResponse reportResponse = assertHttpResponseOk( httpResponse, EventReportResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (3rd),", ErrorCode.OK, reportResponse.getErrorCode() );

		Assert.assertEquals( "Incorrect 'participants.size',", 3, reportResponse.getParticipants().size() );

		Assert.assertNotNull( "Missing 'participants[0].checkpointTimes',", reportResponse.getParticipants().get( 0 ).getCheckpointTimes() );
		Assert.assertEquals( "Incorrect 'participants[0].checkpointTimes.size',", 1, reportResponse.getParticipants().get( 0 ).getCheckpointTimes().size() );
		Assert.assertEquals( "Incorrect 'participants[0].registrationNumber',", new Long( 1 ), reportResponse.getParticipants().get( 0 ).getRegistrationNumber() );
		Assert.assertEquals( "Incorrect 'participants[0].totalTimeInMillis',", new Long( 3 ), reportResponse.getParticipants().get( 0 ).getTotalTimeInMillis() );

		Assert.assertNotNull( "Missing 'participants[1].checkpointTimes',", reportResponse.getParticipants().get( 1 ).getCheckpointTimes() );
		Assert.assertEquals( "Incorrect 'participants[1].checkpointTimes.size',", 1, reportResponse.getParticipants().get( 1 ).getCheckpointTimes().size() );
		Assert.assertEquals( "Incorrect 'participants[1].registrationNumber',", new Long( 2 ), reportResponse.getParticipants().get( 1 ).getRegistrationNumber() );
		Assert.assertEquals( "Incorrect 'participants[1].totalTimeInMillis',", new Long( 6 ), reportResponse.getParticipants().get( 1 ).getTotalTimeInMillis() );
	}

	@Test
	public void testFinalReportEventByTopParticipants() throws Exception {
		String token = finalEventRegistration();

		ArrayList<Long> participants = new ArrayList<>( Arrays.asList( 1L, 2L, 3L ) );

		for ( Long participant : participants ) {
			testFinalEventResult( token, 10L, EVENT_ID, participant, 10L );
		}

		EventReportRequest eventReportRequest = createEventReportRequest( EVENT_ID, 2 );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/report", eventReportRequest, new HttpHeaders() );
		EventReportResponse reportResponse = assertHttpResponseOk( httpResponse, EventReportResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (3rd),", ErrorCode.OK, reportResponse.getErrorCode() );

		Assert.assertEquals( "Incorrect 'participants.size',", 2, reportResponse.getParticipants().size() );

		Assert.assertNotNull( "Missing 'participants[0].checkpointTimes',", reportResponse.getParticipants().get( 0 ).getCheckpointTimes() );
		Assert.assertEquals( "Incorrect 'participants[0].checkpointTimes.size',", 1, reportResponse.getParticipants().get( 0 ).getCheckpointTimes().size() );
		Assert.assertEquals( "Incorrect 'participants[0].registrationNumber',", new Long( 1 ), reportResponse.getParticipants().get( 0 ).getRegistrationNumber() );
		Assert.assertEquals( "Incorrect 'participants[0].totalTimeInMillis',", new Long( 10 ), reportResponse.getParticipants().get( 0 ).getTotalTimeInMillis() );
	}

	private EventRegisterRequest createEventRegisterRequest( String eventName, long eventId ) {
		EventRegisterRequest request = new EventRegisterRequest();
		request.setEventName( eventName );
		request.setEventId( eventId );
		return request;
	}

	private EventResultRequest createEventResultRequest( Long messageId, Long eventId, Long registrationNumber, Long durationInMillis ) {
		EventResultRequest request = new EventResultRequest();
		request.setMessageId( messageId );
		request.setEventId( eventId );
		request.setRegistrationNumber( registrationNumber );
		request.setDurationInMillis( durationInMillis );
		return request;
	}

	private EventReportRequest createEventReportRequest( Long eventId, Integer top ) {
		EventReportRequest request = new EventReportRequest();
		request.setEventId( eventId );
		request.setTop( top );
		return request;
	}

	private HttpHeaders createAuthHeaders( String username, String password ) {
		return createAuthHeaders( "Basic " + Base64.encodeBase64String( ( String.format( "%s:%s", username, password ) ).getBytes() ) );
	}

	private HttpHeaders createAuthHeaders( String authValue ) {
		HttpHeaders headers = new HttpHeaders();
		headers.add( HttpHeaders.AUTHORIZATION, authValue );
		return headers;
	}
}
