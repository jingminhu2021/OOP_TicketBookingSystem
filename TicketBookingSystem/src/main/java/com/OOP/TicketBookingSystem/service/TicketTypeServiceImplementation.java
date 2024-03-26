package com.OOP.TicketBookingSystem.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.model.Ticket_Type;
import com.OOP.TicketBookingSystem.repository.TicketTypeRepo;
import com.OOP.TicketBookingSystem.repository.EventRepo;

@Service
public class TicketTypeServiceImplementation implements TicketTypeService {
    @Autowired
    private TicketTypeRepo ticketTypeRepo;

    @Autowired
    private EventRepo eventRepo;

    @Override
    public JsonNode createTicketType(Ticket_Type ticket_type) {
        String eventCat = ticket_type.getEventCat();
        int eventId = ticket_type.getEventId();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "Event does not exist");
        node.put("status", false);

        // Check if event existed in the event table
        if (eventRepo.findById(eventId).orElse(null) == null){
            return node;
        }

        // Check if event is cancelled
        String status = eventRepo.findById(eventId).get().getStatus();
        if (status.equals("Cancelled")){
            node.put("message", "Event is cancelled");
            return node;
        }

        // Check if event category already created for that particular event
        if ((ticketTypeRepo.findByEventCat(eventCat, eventId) != null)) {
            node.put("message", "Ticket Category already existed");
            return node;
        }

        try {
            ticketTypeRepo.save(ticket_type);
            node.put("message", "Successfully created Ticket Type");
            node.put("status", true);

        } catch (Exception e) {
            node.put("message", e.getMessage());
        }

        return node;
    }

    public JsonNode updateTicketType(Ticket_Type ticket_type, String eventManagerName) {
        int ticketTypeId = ticket_type.getTicketTypeId();
        int eventId = ticket_type.getEventId();
        
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "Event does not exist");
        node.put("status", false);

        // Check if event existed in the event table
        Event event = eventRepo.findById(eventId).orElse(null);
        if (event != null){
            String status = event.getStatus();
            // Check if event is cancelled
            if (status.equals("Cancelled")){
                node.put("message", "Event is " + status);
                return node;
            }

            //check if event belong to the manager
            if (!event.getEventManagerName().equals(eventManagerName)) { 
                node.put("message", "Invalid Event Manager");
                return node;
            }

            // Check if ticket type existed in the ticket type table
            if (ticketTypeRepo.findById(ticketTypeId).orElse(null) != null) {

                Ticket_Type originalTicketType = ticketTypeRepo.findById(ticketTypeId).get();
                
                Field[] fields = Ticket_Type.class.getDeclaredFields();
                
                for (Field field : fields) { // Loop through all fields
                    
                    field.setAccessible(true);
                    try {
                        Object newValue = field.get(ticket_type);    
                        Object oldValue = field.get(originalTicketType);

                        if (newValue == null) { // If the new value is null, then set the old value
                            field.set(ticket_type, oldValue); 
                        }
                    } catch (IllegalAccessException e) {
                        node.put("message", e.toString());
                        return node;
                    }
                }

                try {
                    
                    ticketTypeRepo.save(ticket_type);
                    node.put("message", "Successfully updated Ticket Type");
                    node.put("status", true);

                } catch (IllegalArgumentException e) {
                    node.put("message", e.toString());

                } catch (OptimisticLockingFailureException e) {
                    node.put("message", e.toString());
                }
            }
            else {
                node.put("message", "Ticket Type does not exist");
            }
        }

        return node;
    }

    @Override
    public List<Ticket_Type> viewTicketTypes(JsonNode body) {
        // Get event
        String eventName = body.get("eventName").textValue();
        Event event = eventRepo.findByExactEvent(eventName);

        // Get all ticket types for the event
        List<Ticket_Type> ticketTypes = ticketTypeRepo.findByEventId(event.getId());   

        return ticketTypes;
    }
}
