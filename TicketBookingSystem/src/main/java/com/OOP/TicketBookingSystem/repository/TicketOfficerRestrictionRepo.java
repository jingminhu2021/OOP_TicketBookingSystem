package com.OOP.TicketBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.OOP.TicketBookingSystem.model.Ticket_Officer_Restriction;

@Repository
public interface TicketOfficerRestrictionRepo extends JpaRepository<Ticket_Officer_Restriction, Integer> {

    @Query(value = "SELECT * FROM ticket_officer_restriction where event_id= :eventId and user_id= :userId", nativeQuery = true)
    public Ticket_Officer_Restriction findByEventIdAndUserId(@Param("eventId")int eventId, @Param("userId")int userId);
}