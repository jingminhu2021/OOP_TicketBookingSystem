package com.OOP.TicketBookingSystem.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public interface EmailService {

    public JsonNode sendEmail(String email, String subject, String message);

    public JsonNode sendEmailForTicketConfirm(String email, String subject, String message, List<Integer> ticketId);
}
