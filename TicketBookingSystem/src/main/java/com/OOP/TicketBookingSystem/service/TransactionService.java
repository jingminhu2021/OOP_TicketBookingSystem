package com.OOP.TicketBookingSystem.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

import com.OOP.TicketBookingSystem.model.Transaction;
import com.OOP.TicketBookingSystem.model.User;

public interface TransactionService {

    public Transaction getTicketDetails(int ticket_id);

    public JsonNode bookTicket(JsonNode body);

    public List<Transaction> bookingHistory(int user_id);
    
    public JsonNode onSiteBookTicket(JsonNode body);

    public JsonNode sendTicketDetailsEmail(String email, int transaction_id);
    
    public JsonNode generateQRCode(int ticketId, String text);

    public JsonNode cancellation(int ticketId, User user);

    public JsonNode success(String sessionId);
}
