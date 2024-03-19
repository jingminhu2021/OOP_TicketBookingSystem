package com.OOP.TicketBookingSystem.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

import com.OOP.TicketBookingSystem.model.Transaction;

public interface TransactionService {

    public JsonNode bookTicket(JsonNode body);

    public List<Transaction> bookingHistory(int user_id);
    
    public JsonNode onSiteBookTicket(JsonNode body);

    public JsonNode sendTicketDetailsEmail(String email, int transaction_id);
    
    public void generateQRCode(int ticketId, String text);
}
