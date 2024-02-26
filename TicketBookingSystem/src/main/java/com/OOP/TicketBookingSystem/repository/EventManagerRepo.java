package com.OOP.TicketBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OOP.TicketBookingSystem.model.Event_Manager;

@Repository
public interface EventManagerRepo extends JpaRepository<Event_Manager, Integer> {
    
}
