package com.OOP.TicketBookingSystem.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

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
        String eventName = ticket_type.getEventName();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "Event does not exist");
        node.put("status", false);

        // Check if event existed in the event table
        if (eventRepo.findByExactEvent(eventName) != null){
            // Check if event category already created for that particular event
            if ((ticketTypeRepo.findByEventCat(eventCat, eventName) == null)) {
                try {
                    ticketTypeRepo.save(ticket_type);
                    node.put("message", "Successfully created Ticket Type");
                    node.put("status", true);

                } catch (IllegalArgumentException e) {
                    node.put("message", e.toString());

                } catch (OptimisticLockingFailureException e) {
                    node.put("message", e.toString());
                }
            }
            else {
                node.put("message", "Ticket Category already existed");
            }
        }

        return node;
    }
}
