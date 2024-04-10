package com.OOP.TicketBookingSystem.service;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

import com.OOP.TicketBookingSystem.model.Transaction;
import com.OOP.TicketBookingSystem.model.User;

public interface TransactionService {

    public Transaction getTicketDetails(int ticket_id);

    public JsonNode bookTicket(JsonNode body);

    public JsonNode bookTicket(JsonNode body, String email, String name);

    public JsonNode bookingHistory(int user_id);
    
    public JsonNode onSiteBookTicket(JsonNode body);

    public JsonNode sendTicketDetailsEmail(String email, List<Transaction> transactions, String paymentMode);

    public JsonNode sendTicketDetailsEmail(String email, List<Transaction> transactions, String paymentMode, String name);
    
    public JsonNode generateQRCode(int ticketId, String text);

    public void generateQRnSendEmail(List<Transaction> transactions, String paymentMode);

    public void generateQRnSendEmail(List<Transaction> transactions, String paymentMode, String name, String email);

    public JsonNode cancellation(int ticketId, User user);

    public JsonNode success(String sessionId);
}
