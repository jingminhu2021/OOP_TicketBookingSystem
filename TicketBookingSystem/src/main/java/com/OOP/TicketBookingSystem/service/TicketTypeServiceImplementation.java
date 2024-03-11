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

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "Ticket Category already existed");
        node.put("status", false);

        // Check if event category already created for that particular event
        if ((ticketTypeRepo.findByEventCat(eventCat) == null)) {
            try {
                ticketTypeRepo.save(ticket_type);
                node.put("message", "Successfully created Ticket Type");
                node.put("status", true);

                // Testing
                // node.put("eventId", ticket_type.getEventId());
                // node.put("getEventCat", ticket_type.getEventCat());
                // node.put("getEventPrice", ticket_type.getEventPrice());
                // node.put("getNumberOfTix", ticket_type.getNumberOfTix());

            } catch (IllegalArgumentException e) {
                node.put("message", e.toString());

            } catch (OptimisticLockingFailureException e) {
                node.put("message", e.toString());
            }
        }

        return node;
    }
}
