package com.placeholder.company.project.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.placeholder.company.project.db.model.Event;

@Repository( "eventRepository" )
public interface EventRepository extends JpaRepository<Event, Long> {

}
