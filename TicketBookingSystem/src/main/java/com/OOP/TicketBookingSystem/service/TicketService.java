package com.OOP.TicketBookingSystem.service;

import com.fasterxml.jackson.databind.JsonNode;

import com.OOP.TicketBookingSystem.model.Ticket;

public interface TicketService {

    public JsonNode bookTicket(Ticket ticket);
    
}
