package com.OOP.TicketBookingSystem.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

        //check price is negative
        if(ticket_type.getEventPrice().compareTo(java.math.BigDecimal.ZERO) < 0){
            node.put("message", "Price cannot be negative");
            return node;
        }

        //check if number of ticket is negative
        if(ticket_type.getNumberOfTix() < 0){
            node.put("message", "Number of tickets cannot be negative");
            return node;
        }

        //check if cancellation fee is negative or more than 100%
        if(ticket_type.getCancellationFeePercentage().compareTo(java.math.BigDecimal.ZERO) < 0){
            node.put("message", "Cancellation fee cannot be negative");
            return node;
        }else if (ticket_type.getCancellationFeePercentage().compareTo(java.math.BigDecimal.valueOf(100)) > 0){
            node.put("message", "Cancellation fee cannot be more than 100%");
            return node;
        }

        try {
            ticketTypeRepo.save(ticket_type);
            node.put("message", "Successfully created Ticket Type");
            node.put("status", true);

        } catch (IllegalArgumentException e) {
            node.put("message", e.toString());

        } catch (Exception e ){
            node.put("message", e.toString());
        }

        return node;
    }

    public JsonNode updateTicketType(Ticket_Type ticket_type, int managerId) {
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

            //check price is negative
            if(ticket_type.getEventPrice().compareTo(java.math.BigDecimal.ZERO) < 0){
                node.put("message", "Price cannot be negative");
                return node;
            }

            //check if number of ticket is negative
            if(ticket_type.getNumberOfTix() < 0){
                node.put("message", "Number of tickets cannot be negative");
                return node;
            }

            //check if cancellation fee is negative or more than 100%
            if(ticket_type.getCancellationFeePercentage().compareTo(java.math.BigDecimal.ZERO) < 0){
                node.put("message", "Cancellation fee cannot be negative");
                return node;
            }else if (ticket_type.getCancellationFeePercentage().compareTo(java.math.BigDecimal.valueOf(100)) > 0){
                node.put("message", "Cancellation fee cannot be more than 100%");
                return node;
            }

            //check if event belong to the manager
            if (event.getEventManagerId()!=managerId) { 
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
        
                } catch (Exception e ){
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
        int event_id = body.get("event_id").asInt();

        // Get all ticket types for the event
        List<Ticket_Type> ticketTypes = ticketTypeRepo.findByEventId(event_id);   

        return ticketTypes;
    }

    @Override
    public Ticket_Type viewSingleTicketType(JsonNode body) {

        int ticketTypeId = body.get("ticketTypeId").intValue();

        // Get ticket type
        Ticket_Type ticketType = ticketTypeRepo.findById(ticketTypeId).orElse(null);

        return ticketType;
    }

}
