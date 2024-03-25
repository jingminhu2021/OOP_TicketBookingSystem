package com.OOP.TicketBookingSystem.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

import com.OOP.TicketBookingSystem.model.Ticket_Type;

public interface TicketTypeService {

    public JsonNode createTicketType(Ticket_Type ticket_type);

    public JsonNode updateTicketType(Ticket_Type ticket_type, String managerName);

    public List<Ticket_Type> viewTicketTypes(JsonNode body);
}
