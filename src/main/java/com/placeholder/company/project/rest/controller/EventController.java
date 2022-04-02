package com.placeholder.company.project.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.placeholder.company.project.db.model.Admin;
import com.placeholder.company.project.db.model.Checkpoint;
import com.placeholder.company.project.db.model.Event;
import com.placeholder.company.project.db.service.AdminService;
import com.placeholder.company.project.db.service.CheckpointService;
import com.placeholder.company.project.db.service.EventService;
import com.placeholder.company.project.rest.GeneralHttpException;
import com.placeholder.company.project.rest.api.EventRegisterRequest;
import com.placeholder.company.project.rest.api.EventReportRequest;
import com.placeholder.company.project.rest.api.EventReportResponse;
import com.placeholder.company.project.rest.api.EventResultRequest;
import com.placeholder.company.project.rest.api.GeneralResponse;
import com.placeholder.company.project.rest.api.constants.ErrorCode;
import com.placeholder.company.project.rest.api.structures.Participant;

@RestController
public class EventController extends AbstractController {

	private final AdminService adminService;
	private final EventService eventService;
	private final CheckpointService checkpointService;

	public EventController( AdminService adminService, EventService eventService, CheckpointService checkpointService ) {
		this.adminService = adminService;
		this.eventService = eventService;
		this.checkpointService = checkpointService;
	}

	@RequestMapping( value = "/event/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity eventRegister( @RequestHeader( value = HttpHeaders.AUTHORIZATION, required = false ) String authorization, @RequestBody EventRegisterRequest request ) {
		try {
			validateAuthorizationHeader( authorization );

			Admin admin = this.adminService.getAdminByToken( authorization );
			if ( admin == null ) { // Couldn't figure out what's wrong here >:(
				throw new GeneralHttpException( HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION, "Request not authorized." );
			}

			this.eventService.createEvent( new Event(request.getEventId(), request.getEventName()) );

			GeneralResponse response = new GeneralResponse();
			response.setErrorCode(ErrorCode.OK);
			response.setErrorMessage( "Event registered successfully." );
			return ResponseEntity.ok().body( response );
		} catch ( GeneralHttpException exception ) {
			return exception.createErrorResponse();
		}
	}

	@RequestMapping( value = "/event/result", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity eventResult( @RequestHeader( value = HttpHeaders.AUTHORIZATION, required = false ) String authorization, @RequestBody EventResultRequest request ) {
		try {
			validateAuthorizationHeader( authorization );

			Event event = this.eventService.getEventById( request.getEventId() );
			if ( event == null ) {
				throw new GeneralHttpException( ErrorCode.INVALID, String.format( "Event with given id '%d' is missing from the system.", request.getEventId() ) );
			}

			Checkpoint checkpoint = this.checkpointService.getAllCheckpointsByEventId( request.getEventId() )
					.stream()
					.filter( checkpoint1 -> checkpoint1.getMessageId() == request.getMessageId() ) // I wonder if it's supposed to do that..
					.findFirst()
					.orElse( null );
			this.checkpointService.createOrUpdateCheckpoint( checkpoint );

			GeneralResponse response = new GeneralResponse();
			response.setErrorCode( ErrorCode.OK );
			response.setErrorMessage( "Event result persisted successfully." );
			return ResponseEntity.ok().body( response );
		} catch ( GeneralHttpException exception ) {
			return exception.createErrorResponse();
		}
	}

	@RequestMapping( value = "/event/report", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity eventReport( @RequestBody EventReportRequest request ) {
		try {
			validateNotNull( "eventId", request.getEventId() );

			List<Checkpoint> checkpoints = this.checkpointService.getAllCheckpointsByEventId( request.getEventId() );

			// This algorithm is rather funky.. I need to rethink this..
			List<Participant> participants = new ArrayList<>();
			for ( Checkpoint checkpoint : checkpoints ) {
				Participant participant = new Participant();
				participant.setRegistrationNumber( checkpoint.getRegistrationNumber() );
				participant.setTotalTimeInMillis( checkpoint.getRegistrationNumber() );
				participant.setCheckpointTimes( checkpoints.stream().map( Checkpoint::getDurationInMillis ).collect( Collectors.toList() ) );
				participants.add( participant );
			}

			EventReportResponse response = new EventReportResponse();
			response.setParticipants( participants );
			return ResponseEntity.ok().body( response );
		} catch ( GeneralHttpException exception ) {
			return exception.createErrorResponse();
		}
	}
}