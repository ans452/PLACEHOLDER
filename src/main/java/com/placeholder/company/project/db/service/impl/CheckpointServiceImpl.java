package com.placeholder.company.project.db.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.placeholder.company.project.db.model.Checkpoint;
import com.placeholder.company.project.db.repository.CheckpointRepository;
import com.placeholder.company.project.db.service.CheckpointService;

@Service( "checkpointService" )
public class CheckpointServiceImpl implements CheckpointService {

	private final CheckpointRepository checkpointRepository;

	public CheckpointServiceImpl( @Qualifier( "checkpointRepository" ) CheckpointRepository checkpointRepository ) {
		this.checkpointRepository = checkpointRepository;
	}

	@Override
	public Checkpoint getCheckpointByIds( Long messageId, Long eventId, Long registrationNumber ) {
		return checkpointRepository.findOne( new Checkpoint.CheckpointId( messageId, eventId, registrationNumber ) );
	}

	@Override
	public void createOrUpdateCheckpoint( Checkpoint checkpoint ) {
		Checkpoint existingCheckPoint = getCheckpointByIds( checkpoint.getMessageId(), checkpoint.getEventId(), checkpoint.getRegistrationNumber() );
		System.out.println( "Updating existing checkpoint: " + existingCheckPoint );

		checkpointRepository.save( checkpoint );
	}

	@Override
	public List<Checkpoint> getAllCheckpointsByEventId( Long eventId ) {
		return checkpointRepository.findAll().stream().filter( e -> e.getEventId().equals( eventId ) ).collect( Collectors.toList() );
	}
}
