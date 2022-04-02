package com.placeholder.company.project.db.service;

import java.util.List;

import com.placeholder.company.project.db.model.Checkpoint;

public interface CheckpointService {

	Checkpoint getCheckpointByIds( Long messageId, Long eventId, Long registrationNumber );

	void createOrUpdateCheckpoint( Checkpoint checkpoint );

	List<Checkpoint> getAllCheckpointsByEventId( Long eventId );
}
