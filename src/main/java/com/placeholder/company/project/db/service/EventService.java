package com.placeholder.company.project.db.service;

import com.placeholder.company.project.db.model.Event;
import com.placeholder.company.project.rest.GeneralHttpException;

public interface EventService {

	Event getEventById( Long id );

	void createEvent( Event event ) throws GeneralHttpException;
}
