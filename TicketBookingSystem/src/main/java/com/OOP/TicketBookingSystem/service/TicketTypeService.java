package com.OOP.TicketBookingSystem.service;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

import com.OOP.TicketBookingSystem.model.Ticket_Type;

public interface TicketTypeService {

    public JsonNode createTicketType(Ticket_Type ticket_type);
}
