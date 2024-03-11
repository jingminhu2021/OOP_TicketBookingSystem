package com.OOP.TicketBookingSystem.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface EmailService {

    public JsonNode sendEmail(JsonNode body);

}
