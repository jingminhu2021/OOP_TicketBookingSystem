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

    @Override
    public JsonNode createEvent(Event event) {
        String eventName = event.getEventName();
        String eventManagerName = event.getEventManagerName();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "Event already existed");
        node.put("status", false);

        if ((eventRepo.findByExactEvent(eventName) == null)) {
            if (eventManagerRepo.findByName(eventManagerName) != null) {
                try {
                    eventRepo.save(event);
                    node.put("message", "Successfully created Event");
                    node.put("status", true);
                } catch (IllegalArgumentException e) {
                    node.put("message", e.toString());

                } catch (OptimisticLockingFailureException e) {
                    node.put("message", e.toString());
                }
            } else {
                node.put("message", "Invalid Event Manager");
            }
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
        System.out.println(id);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "Event not found");
        node.put("status", false);

        if (eventRepo.findById(id).orElse(null) != null) {
            if (eventRepo.findByExactEvent(eventName) == null) {
                try {
                    eventRepo.save(event);
                    node.put("message", "Successfully updated Event");
                    node.put("status", true);

                } catch (IllegalArgumentException e) {
                    node.put("message", e.toString());

                } catch (OptimisticLockingFailureException e) {
                    node.put("message", e.toString());

                }
            } else {
                node.put("message", "Event name already exist");
            }
        }

        return node;
    }

    @Override
    public JsonNode viewEventByEventManager(JsonNode body) {
        String eventManagerName = body.get("eventManagerName").textValue();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ObjectNode node = mapper.createObjectNode();

        List<Event> events = eventRepo.findByEventManager(eventManagerName);

        node.put("message", "No event found");
        node.put("status", false);

        if (!events.isEmpty()) {
            node.put("message", "Event found");
            node.put("status", true);
            // node.put("events", mapper.valueToTree(events));
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

        if (eventRepo.findById(event_id).orElse(null) != null) {
            try {
                Event event = eventRepo.findById(event_id).get();
                // check if event belong to the manager
                if(event.getEventManagerName().equals(eventManagerName)){
                    // check if event is not already started
                    if(LocalDateTime.now().isAfter(event.getDateTime())){
                        node.put("message", "Event already started");
                    }else{
                        // check if event is not already cancelled
                        if(event.getStatus().equals("Cancelled")){
                            node.put("message", "Event already cancelled");
                        }else{
                            event.setStatus("Cancelled");
                            eventRepo.save(event);
                            node.put("message", "Event successfully cancelled");
                            node.put("status", true);
                        }
                    }
                }else{
                    node.put("message", "Invalid Event Manager");
                }
            } catch (IllegalArgumentException e) {
                node.put("message", e.toString());
            }
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
