package com.OOP.TicketBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.OOP.TicketBookingSystem.model.Ticket;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Integer> {
    @Query(value = "SELECT * FROM ticket WHERE email=?", nativeQuery=true)
    public Ticket findByEmail(String email);
}
