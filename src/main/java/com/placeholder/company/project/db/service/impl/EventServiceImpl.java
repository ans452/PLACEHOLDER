package com.placeholder.company.project.db.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.placeholder.company.project.db.model.Event;
import com.placeholder.company.project.db.repository.EventRepository;
import com.placeholder.company.project.db.service.EventService;
import com.placeholder.company.project.rest.GeneralHttpException;
import com.placeholder.company.project.rest.api.constants.ErrorCode;

@Service( "eventService" )
public class EventServiceImpl implements EventService {

	private final EventRepository eventRepository;

	public EventServiceImpl( @Qualifier( "eventRepository" ) EventRepository eventRepository ) {
		this.eventRepository = eventRepository;
	}

	@Override
	public Event getEventById( Long id ) {
		return eventRepository.findOne( id );
	}

	@Override
	public void createEvent( Event event ) throws GeneralHttpException {
		Event existingEntry = eventRepository.findOne( event.getId() );

		if ( existingEntry != null ) {
			// Generally, shouldn't happen, unless implemented incorrectly.
			throw new GeneralHttpException( ErrorCode.INVALID, "An event with given id already exists." );
		}

		eventRepository.save( event );
	}
}
