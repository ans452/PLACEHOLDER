package com.placeholder.company.project.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.placeholder.company.project.db.model.Checkpoint;

@Repository( "checkpointRepository" )
public interface CheckpointRepository extends JpaRepository<Checkpoint, Checkpoint.CheckpointId> {

}
