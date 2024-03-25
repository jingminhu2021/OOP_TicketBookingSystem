package com.OOP.TicketBookingSystem.service;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.model.Event_Manager;
import com.OOP.TicketBookingSystem.model.Transaction;

public interface EventService {

    public JsonNode createEvent(Event event, String managerName, String image);

    public Event getEventById(int id);

    public List<Event> getEventsByEventManager(Event_Manager eventManager);

    public List<Event> getAllEvents();

    public List<Transaction> getTransaction(int event_id);

    public List<String> getCustomerEmails(List<Transaction> transactions);

    public JsonNode updateEvent(Event event, String eventManagerName);

    public JsonNode viewEventByEventManager(String managerName);

    public boolean systemRefund(List<Transaction> transactions);

    public boolean systemRefund(List<Transaction> transactions, boolean eventManager);

    public JsonNode cancelEventByManager(JsonNode body, String managerName);

    public JsonNode viewEvent(int id);
}
