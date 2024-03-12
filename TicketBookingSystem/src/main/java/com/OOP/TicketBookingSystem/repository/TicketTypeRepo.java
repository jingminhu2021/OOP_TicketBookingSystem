package com.OOP.TicketBookingSystem.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.OOP.TicketBookingSystem.model.Ticket_Type;

@Repository
public interface TicketTypeRepo extends JpaRepository<Ticket_Type, Integer> {
    
    @Query(value = "SELECT * FROM ticket_type WHERE event_cat=? AND event_id=?", nativeQuery = true)
    public Ticket_Type findByEventCat(String eventCat, int eventId);
}
