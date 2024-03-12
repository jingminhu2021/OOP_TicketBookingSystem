package com.OOP.TicketBookingSystem.service;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.model.Event_Manager;
import com.OOP.TicketBookingSystem.repository.EventManagerRepo;
import com.OOP.TicketBookingSystem.repository.EventRepo;

@Service
public class EventServiceImplementation implements EventService {
    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private EventManagerRepo eventManagerRepo;

    @Autowired
    private EmailService emailService;

    @Override
    public JsonNode createEvent(Event event, String managerName) {
        String eventName = event.getEventName();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "Event already existed");
        node.put("status", false);

        if (LocalDateTime.now().plusDays(2).isAfter(event.getDateTime())){ 
            node.put("message", "Event date must be at least 2 days from today");
            return node;
        }

        if (eventRepo.findByExactEvent(eventName) != null) {
            node.put("message", "Event already exists");
            return node;
        }
        
        if (eventManagerRepo.findByName(managerName) == null) {
            node.put("message", "Invalid Event Manager");
            return node;
        }
        
        try {
            event.setEventManagerName(managerName);
            eventRepo.save(event);
            node.put("message", "Successfully created Event");
            node.put("status", true);
        } catch (IllegalArgumentException e) {
            node.put("message", e.toString());
        } catch (OptimisticLockingFailureException e) {
            node.put("message", e.toString());
        }
        
        return node;
    }

    @Override
    public Event getEventById(int id) {
        return eventRepo.findById(id).get();
    }

    @Override
    public List<Event> getEventsByEventManager(Event_Manager eventManager) {
        String eventManagerName = eventManager.getName();
        return eventRepo.findByEventManager(eventManagerName);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    @Override
    public JsonNode updateEvent(Event event) {
        // Todo add check if event manager match
        int id = event.getId();
        String eventName = event.getEventName();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "Event not found");
        node.put("status", false);


        Event existingEvent = eventRepo.findById(id).orElse(null); //check if event exist
        if (existingEvent == null) {
            return node;
        }

        if (!existingEvent.getDateTime().isEqual(event.getDateTime()) //check if event date is at least 2 days from today
            && LocalDateTime.now().plusDays(2).isAfter(event.getDateTime())) {
            node.put("message", "Event date must be at least 2 days from today");
            return node;
        }

        Event eventWithSameName = eventRepo.findByExactEvent(eventName); //check if event name already exist and not the same event
        if (eventWithSameName != null && eventWithSameName.getId() != id) {
            node.put("message", "Event name already exist");
            return node;
        }

        try {
            eventRepo.save(event);
            node.put("message", "Successfully updated Event");
            node.put("status", true);
        } catch (IllegalArgumentException e) {
            node.put("message", e.toString());
        } catch (OptimisticLockingFailureException e) {
            node.put("message", e.toString());
        }

        return node;
    }

    @Override
    public JsonNode viewEventByEventManager(String ManagerName) {
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ObjectNode node = mapper.createObjectNode();

        List<Event> events = eventRepo.findByEventManager(ManagerName);

        node.put("message", "No event found");
        node.put("status", false);

        if (!events.isEmpty()) {
            node.put("message", "Event found");
            node.put("status", true);
            node.set("events", mapper.valueToTree(events));
        }

        return node;
    }

    @Override
    public JsonNode cancelEventByManager(JsonNode body) {
        //To do:
        // 1) check if event belong to the manager - done
        // 2) check if event is not already cancelled - done
        // 3) check if event is not already started - done
        // 4) update the event status to cancelled - done
        // 5) process refund to the customers (put in another function)
        // 6) send email to the customers

        // Data expectation
        // 1) requested event manager name
        // 2) event_id

        int event_id = body.get("event_id").intValue();
        String eventManagerName = body.get("eventManagerName").textValue();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "Event not found");
        node.put("status", false);
        
        Event event = eventRepo.findById(event_id).orElse(null);
        if (event == null) {
            return node;
        }else if (!event.getEventManagerName().equals(eventManagerName)) {
            node.put("message", "Invalid Event Manager");
            return node;
        }else if (LocalDateTime.now().isAfter(event.getDateTime())) {
            node.put("message", "Event already started");
            return node;
        }else if (event.getStatus().equals("Cancelled")) {
            node.put("message", "Event already cancelled");
            return node;
        }
        
        try {
            event.setStatus("Cancelled");
            eventRepo.save(event);
            node.put("message", "Event successfully cancelled");
            node.put("status", true);
        } catch (IllegalArgumentException e) {
            node.put("message", e.toString());
        }
        
        if(node.get("status").asBoolean()){
            // send email to the customers
            // get all the emails of the customers who bought the ticket

            // String [] emails = eventRepo.getCustomerEmails(event_id);
            String subject = String.format("[Notice] %s Cancellation", event.getEventName());
            String message = String.format("The event %s has been cancelled. We are sorry for the inconvenience. Your ticket will be refunded.%n Regards, Event Manager",event.getEventName());
            // for(String email: emails){
            //     emailService.sendEmail(email,subject,message);
            // }
        }

        return node;
    }

    @Override
    public JsonNode viewEvent(JsonNode body) {
        // Obtain event name
        String eventName = body.get("eventName").textValue();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ObjectNode node = mapper.createObjectNode();

        Event event = eventRepo.findByExactEvent(eventName);

        node.put("message", "No event found");
        node.put("status", false);

        // Check if event exists
        if (event != null) {
            node.put("message", "Event found");
            node.put("status", true);
            node.set("event", mapper.valueToTree(event));
        }

        return node;
    }
}
