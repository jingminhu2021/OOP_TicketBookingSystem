package com.OOP.TicketBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.OOP.TicketBookingSystem.model.Ticket_Officer_Restriction;

@Repository
public interface TicketOfficerRestrictionRepo extends JpaRepository<Ticket_Officer_Restriction, Integer> {

}