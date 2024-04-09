package com.OOP.TicketBookingSystem.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

import com.OOP.TicketBookingSystem.model.Ticket_Type;

public interface TicketTypeService {

    public JsonNode createTicketType(Ticket_Type ticket_type);

    public JsonNode updateTicketType(Ticket_Type ticket_type, int managerId);

    public List<Ticket_Type> viewTicketTypes(JsonNode body);

    public Ticket_Type viewSingleTicketType(JsonNode body);

    public JsonNode verifyTicket(int userId, int eventId, int ticketId, int ticketOfficerId, int ticketTypeId);
}
