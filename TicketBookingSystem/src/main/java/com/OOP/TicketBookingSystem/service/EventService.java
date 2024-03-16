package com.OOP.TicketBookingSystem.service;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.model.Event_Manager;

public interface EventService {

    public JsonNode createEvent(Event event, String managerName);

    public Event getEventById(int id);

    public List<Event> getEventsByEventManager(Event_Manager eventManager);

    public List<Event> getAllEvents();

    public JsonNode updateEvent(Event event);

    public JsonNode viewEventByEventManager(String managerName);

    public JsonNode cancelEventByManager(JsonNode body);

    public JsonNode viewEvent(JsonNode body);

    public JsonNode viewSalesStatistics(JsonNode body);

    public JsonNode viewAllSalesStatistics(JsonNode body);
}
