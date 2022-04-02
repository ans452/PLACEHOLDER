package com.placeholder.company.project.scoring;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import com.placeholder.company.project.rest.api.EventRegisterRequest;
import com.placeholder.company.project.rest.api.EventReportRequest;
import com.placeholder.company.project.rest.api.EventReportResponse;
import com.placeholder.company.project.rest.api.EventResultRequest;
import com.placeholder.company.project.rest.api.GeneralResponse;
import com.placeholder.company.project.rest.api.constants.ErrorCode;

@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class EventControllerTest extends AbstractApplicationScoringTest {

	@Test
	public void testEventRegisterOkWithInjectedToken() throws Exception {
		EventRegisterRequest request = createEventRegisterRequest( "test", 1L );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/register", request, createAuthHeaders( "test" ) );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.OK, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testEventRegisterUnauthorizedWithoutToken() throws Exception {
		EventRegisterRequest request = createEventRegisterRequest( "test", -1L );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/register", request, new HttpHeaders() );
		GeneralResponse response = assertHttpResponseErr( httpResponse, GeneralResponse.class, HttpStatus.UNAUTHORIZED );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.AUTHENTICATION, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testEventRegisterUnauthorizedWithIncorrectToken() throws Exception {
		EventRegisterRequest request = createEventRegisterRequest( "test", -1L );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/register", request, createAuthHeaders( "username:password" ) );
		GeneralResponse response = assertHttpResponseErr( httpResponse, GeneralResponse.class, HttpStatus.UNAUTHORIZED );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.AUTHENTICATION, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testEventRegisterFailsWithExistingEventId() throws Exception {
		EventRegisterRequest request = createEventRegisterRequest( "test", 50L );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/register", request, createAuthHeaders( "test" ) );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.OK, response.getErrorCode() );

		request = createEventRegisterRequest( "test2", 50L );

		httpResponse = postHttpRequest( "/event/register", request, createAuthHeaders( "test" ) );
		response = assertHttpResponseErr( httpResponse, GeneralResponse.class, HttpStatus.OK );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.INVALID, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testEventResultUnauthorizedWithoutToken() throws Exception {
		MockHttpServletResponse httpResponse = postHttpRequest( "/event/result", "{}", new HttpHeaders() );
		GeneralResponse response = assertHttpResponseErr( httpResponse, GeneralResponse.class, HttpStatus.UNAUTHORIZED );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.AUTHENTICATION, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testEventResultUnauthorizedWithoutTokenWithContent() throws Exception {
		try {
			testEventResultUnauthorizedWithoutToken();
		} catch ( Throwable e ) {
			Assume.assumeNoException( "The main functionality of the API is not working. Received", e );
		}

		EventResultRequest eventResultRequest = createEventResultRequest( 100L, -1L, 911L, 100L );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/result", eventResultRequest, new HttpHeaders() );
		GeneralResponse response = assertHttpResponseErr( httpResponse, GeneralResponse.class, HttpStatus.UNAUTHORIZED );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.AUTHENTICATION, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testEventResultUnauthorizedWithIncorrectToken() throws Exception {
		EventRegisterRequest request = createEventRegisterRequest( "test", -1L );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/result", request, createAuthHeaders( "username:password" ) );
		GeneralResponse response = assertHttpResponseErr( httpResponse, GeneralResponse.class, HttpStatus.UNAUTHORIZED );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.AUTHENTICATION, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testEventResultWithNonExistingEvent() throws Exception {
		EventResultRequest eventResultRequest = createEventResultRequest( 100L, -1L, 911L, 100L );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/result", eventResultRequest, createAuthHeaders( "test" ) );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.INVALID, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testEventResult() throws Exception {
		EventRegisterRequest eventRegisterRequest = createEventRegisterRequest( "test", 100L );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/register", eventRegisterRequest, createAuthHeaders( "test" ) );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (1st),", ErrorCode.OK, response.getErrorCode() );

		EventResultRequest eventResultRequest = createEventResultRequest( 10L, eventRegisterRequest.getEventId(), 91L, 10L );

		httpResponse = postHttpRequest( "/event/result", eventResultRequest, createAuthHeaders( "test" ) );
		response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (2nd),", ErrorCode.OK, response.getErrorCode() );
	}

	@Test
	public void testReportEventWithoutEventId() throws Exception {
		MockHttpServletResponse httpResponse = postHttpRequest( "/event/report", "{}", new HttpHeaders() );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.INVALID, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testReportEventWithNonExistingEvent() throws Exception {
		EventReportRequest eventReportRequest = createEventReportRequest( -1L, 5 );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/report", eventReportRequest, createAuthHeaders( "test" ) );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode',", ErrorCode.INVALID, response.getErrorCode() );
		Assert.assertNotNull( "Missing 'errorMessage' in response.", response.getErrorMessage() );
	}

	@Test
	public void testReportEvent() throws Exception {
		EventRegisterRequest eventRegisterRequest = createEventRegisterRequest( "test", 150L );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/register", eventRegisterRequest, createAuthHeaders( "test" ) );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (1st),", ErrorCode.OK, response.getErrorCode() );

		EventResultRequest eventResultRequest = createEventResultRequest( 10L, eventRegisterRequest.getEventId(), 91L, 10L );

		httpResponse = postHttpRequest( "/event/result", eventResultRequest, createAuthHeaders( "test" ) );
		response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (2nd),", ErrorCode.OK, response.getErrorCode() );

		EventReportRequest eventReportRequest = createEventReportRequest( eventRegisterRequest.getEventId(), 5 );

		httpResponse = postHttpRequest( "/event/report", eventReportRequest, createAuthHeaders( "test" ) );
		EventReportResponse reportResponse = assertHttpResponseOk( httpResponse, EventReportResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (3rd),", ErrorCode.OK, reportResponse.getErrorCode() );
	}

	@Test
	public void testReportEventWithSingleParticipant() throws Exception {
		EventRegisterRequest eventRegisterRequest = createEventRegisterRequest( "test", 250L );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/register", eventRegisterRequest, createAuthHeaders( "test" ) );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (1st),", ErrorCode.OK, response.getErrorCode() );

		EventResultRequest eventResultRequest = createEventResultRequest( 10L, eventRegisterRequest.getEventId(), 91L, 10L );

		httpResponse = postHttpRequest( "/event/result", eventResultRequest, createAuthHeaders( "test" ) );
		response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (2nd),", ErrorCode.OK, response.getErrorCode() );

		EventReportRequest eventReportRequest = createEventReportRequest( eventRegisterRequest.getEventId(), null );

		httpResponse = postHttpRequest( "/event/report", eventReportRequest, createAuthHeaders( "test" ) );
		EventReportResponse reportResponse = assertHttpResponseOk( httpResponse, EventReportResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (3rd),", ErrorCode.OK, reportResponse.getErrorCode() );

		Assert.assertEquals( "Incorrect 'participants.size',", 1, reportResponse.getParticipants().size() );

		Assert.assertNotNull( "Missing 'participants[0].checkpointTimes',", reportResponse.getParticipants().get( 0 ).getCheckpointTimes() );
		Assert.assertEquals( "Incorrect 'participants[0].checkpointTimes.size',", 1, reportResponse.getParticipants().get( 0 ).getCheckpointTimes().size() );
		Assert.assertEquals( "Incorrect 'participants[0].registrationNumber',", new Long( 91 ), reportResponse.getParticipants().get( 0 ).getRegistrationNumber() );
		Assert.assertEquals( "Incorrect 'participants[0].totalTimeInMillis',", new Long( 10 ), reportResponse.getParticipants().get( 0 ).getTotalTimeInMillis() );
	}

	@Test
	public void testReportEventWithMultipleParticipants() throws Exception {
		EventRegisterRequest eventRegisterRequest = createEventRegisterRequest( "test", 300L );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/register", eventRegisterRequest, createAuthHeaders( "test" ) );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (1st),", ErrorCode.OK, response.getErrorCode() );

		ArrayList<Long> participants = new ArrayList<>( Arrays.asList( 1L, 2L, 3L ) );

		for ( Long participant : participants ) {
			EventResultRequest eventResultRequest = createEventResultRequest( 10L, eventRegisterRequest.getEventId(), participant, 10L );

			httpResponse = postHttpRequest( "/event/result", eventResultRequest, createAuthHeaders( "test" ) );
			response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
			Assert.assertEquals( "Incorrect 'errorCode' (2nd - loop),", ErrorCode.OK, response.getErrorCode() );
		}

		EventReportRequest eventReportRequest = createEventReportRequest( eventRegisterRequest.getEventId(), null );

		httpResponse = postHttpRequest( "/event/report", eventReportRequest, createAuthHeaders( "test" ) );
		EventReportResponse reportResponse = assertHttpResponseOk( httpResponse, EventReportResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (3rd),", ErrorCode.OK, reportResponse.getErrorCode() );

		Assert.assertEquals( "Incorrect 'participants.size',", 3, reportResponse.getParticipants().size() );

		Assert.assertNotNull( "Missing 'participants[0].checkpointTimes',", reportResponse.getParticipants().get( 0 ).getCheckpointTimes() );
		Assert.assertEquals( "Incorrect 'participants[0].checkpointTimes.size',", 1, reportResponse.getParticipants().get( 0 ).getCheckpointTimes().size() );
		Assert.assertEquals( "Incorrect 'participants[0].registrationNumber',", new Long( 1 ), reportResponse.getParticipants().get( 0 ).getRegistrationNumber() );
		Assert.assertEquals( "Incorrect 'participants[0].totalTimeInMillis',", new Long( 10 ), reportResponse.getParticipants().get( 0 ).getTotalTimeInMillis() );
	}

	@Test
	public void testReportEventByTopParticipants() throws Exception {
		EventRegisterRequest eventRegisterRequest = createEventRegisterRequest( "test", 350L );

		MockHttpServletResponse httpResponse = postHttpRequest( "/event/register", eventRegisterRequest, createAuthHeaders( "test" ) );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (1st),", ErrorCode.OK, response.getErrorCode() );

		ArrayList<Long> participants = new ArrayList<>( Arrays.asList( 1L, 2L, 3L ) );

		for ( Long participant : participants ) {
			EventResultRequest eventResultRequest = createEventResultRequest( 10L, eventRegisterRequest.getEventId(), participant, 10L );

			httpResponse = postHttpRequest( "/event/result", eventResultRequest, createAuthHeaders( "test" ) );
			response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
			Assert.assertEquals( "Incorrect 'errorCode' (2nd - loop),", ErrorCode.OK, response.getErrorCode() );
		}

		EventReportRequest eventReportRequest = createEventReportRequest( eventRegisterRequest.getEventId(), 2 );

		httpResponse = postHttpRequest( "/event/report", eventReportRequest, createAuthHeaders( "test" ) );
		EventReportResponse reportResponse = assertHttpResponseOk( httpResponse, EventReportResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (3rd),", ErrorCode.OK, reportResponse.getErrorCode() );

		Assert.assertEquals( "Incorrect 'participants.size',", 2, reportResponse.getParticipants().size() );

		Assert.assertNotNull( "Missing 'participants[0].checkpointTimes',", reportResponse.getParticipants().get( 0 ).getCheckpointTimes() );
		Assert.assertEquals( "Incorrect 'participants[0].checkpointTimes.size',", 1, reportResponse.getParticipants().get( 0 ).getCheckpointTimes().size() );
		Assert.assertEquals( "Incorrect 'participants[0].registrationNumber',", new Long( 1 ), reportResponse.getParticipants().get( 0 ).getRegistrationNumber() );
		Assert.assertEquals( "Incorrect 'participants[0].totalTimeInMillis',", new Long( 10 ), reportResponse.getParticipants().get( 0 ).getTotalTimeInMillis() );
	}

	@Test
	public void testReportEventBySortedParticipants() throws Exception {
		EventRegisterRequest eventRegisterRequest = createEventRegisterRequest( "test", 400L );
		MockHttpServletResponse httpResponse = postHttpRequest( "/event/register", eventRegisterRequest, createAuthHeaders( "test" ) );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (1st),", ErrorCode.OK, response.getErrorCode() );

		ArrayList<Long> participants = new ArrayList<>( Arrays.asList( 3L, 2L, 1L ) );

		for ( Long participant : participants ) {
			EventResultRequest eventResultRequest = createEventResultRequest( 10L, eventRegisterRequest.getEventId(), participant, participant * 3 );

			httpResponse = postHttpRequest( "/event/result", eventResultRequest, createAuthHeaders( "test" ) );
			response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
			Assert.assertEquals( "Incorrect 'errorCode' (2nd - loop),", ErrorCode.OK, response.getErrorCode() );
		}

		EventReportRequest eventReportRequest = createEventReportRequest( eventRegisterRequest.getEventId(), null );

		httpResponse = postHttpRequest( "/event/report", eventReportRequest, createAuthHeaders( "test" ) );
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
	public void testReportEventBySortedParticipantsWithMultipleResults() throws Exception {
		EventRegisterRequest eventRegisterRequest = createEventRegisterRequest( "test", 450L );
		MockHttpServletResponse httpResponse = postHttpRequest( "/event/register", eventRegisterRequest, createAuthHeaders( "test" ) );
		GeneralResponse response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (1st),", ErrorCode.OK, response.getErrorCode() );

		ArrayList<Long> participants = new ArrayList<>( Arrays.asList( 3L, 2L, 1L ) );

		for ( int i = 1; i < 4; i++ ) {
			for ( Long participant : participants ) {
				EventResultRequest eventResultRequest = createEventResultRequest( 10L * participant * i, eventRegisterRequest.getEventId(), participant, participant * 3 * i );

				httpResponse = postHttpRequest( "/event/result", eventResultRequest, createAuthHeaders( "test" ) );
				response = assertHttpResponseOk( httpResponse, GeneralResponse.class );
				Assert.assertEquals( "Incorrect 'errorCode' (2nd - loop),", ErrorCode.OK, response.getErrorCode() );
			}
		}

		EventReportRequest eventReportRequest = createEventReportRequest( eventRegisterRequest.getEventId(), null );

		httpResponse = postHttpRequest( "/event/report", eventReportRequest, createAuthHeaders( "test" ) );
		EventReportResponse reportResponse = assertHttpResponseOk( httpResponse, EventReportResponse.class );
		Assert.assertEquals( "Incorrect 'errorCode' (3rd),", ErrorCode.OK, reportResponse.getErrorCode() );

		Assert.assertEquals( "Incorrect 'participants.size',", 3, reportResponse.getParticipants().size() );

		Assert.assertNotNull( "Missing 'participants[0].checkpointTimes',", reportResponse.getParticipants().get( 0 ).getCheckpointTimes() );
		Assert.assertEquals( "Incorrect 'participants[0].checkpointTimes.size',", 3, reportResponse.getParticipants().get( 0 ).getCheckpointTimes().size() );
		Assert.assertEquals( "Incorrect 'participants[0].registrationNumber',", new Long( 1 ), reportResponse.getParticipants().get( 0 ).getRegistrationNumber() );
		Assert.assertEquals( "Incorrect 'participants[0].totalTimeInMillis',", new Long( 18 ), reportResponse.getParticipants().get( 0 ).getTotalTimeInMillis() );

		Assert.assertNotNull( "Missing 'participants[1].checkpointTimes',", reportResponse.getParticipants().get( 1 ).getCheckpointTimes() );
		Assert.assertEquals( "Incorrect 'participants[1].checkpointTimes.size',", 3, reportResponse.getParticipants().get( 1 ).getCheckpointTimes().size() );
		Assert.assertEquals( "Incorrect 'participants[1].registrationNumber',", new Long( 2 ), reportResponse.getParticipants().get( 1 ).getRegistrationNumber() );
		Assert.assertEquals( "Incorrect 'participants[1].totalTimeInMillis',", new Long( 36 ), reportResponse.getParticipants().get( 1 ).getTotalTimeInMillis() );
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

	private HttpHeaders createAuthHeaders( String authValue ) {
		HttpHeaders headers = new HttpHeaders();
		headers.add( HttpHeaders.AUTHORIZATION, authValue );
		return headers;
	}
}
