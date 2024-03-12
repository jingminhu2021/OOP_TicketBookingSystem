package com.OOP.TicketBookingSystem.service;

import com.fasterxml.jackson.databind.JsonNode;

import com.OOP.TicketBookingSystem.model.Transaction;

public interface TransactionService {

    public JsonNode bookTicket(JsonNode body);
    
}
