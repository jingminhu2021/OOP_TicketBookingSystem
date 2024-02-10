package com.OOP.TicketBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OOP.TicketBookingSystem.model.Ticketing_Officer;

@Repository
public interface TicketingOfficerRepo extends JpaRepository<Ticketing_Officer, Integer> {
    
}
