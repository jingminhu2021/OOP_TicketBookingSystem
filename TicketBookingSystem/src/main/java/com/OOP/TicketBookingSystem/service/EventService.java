package com.OOP.TicketBookingSystem.service;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.model.Event_Manager;

public interface EventService {

    public JsonNode createEvent(Event event);

    public Event getEventById(int id);

    public List<Event> getEventsByEventManager(Event_Manager eventManager);

    public List<Event> getAllEvents();

    public JsonNode updateEvent(Event event);

    public JsonNode viewEventByEventManager(JsonNode body);

    public JsonNode cancelEventByManager(JsonNode body);

    public JsonNode viewEvent(JsonNode body);
}
