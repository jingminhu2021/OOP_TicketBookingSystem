package com.OOP.TicketBookingSystem.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OOP.TicketBookingSystem.model.Ticket_Type;
import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.service.TicketTypeService;
import com.OOP.TicketBookingSystem.service.UserService;


@RestController
@RequestMapping("/ticketType")
public class TicketTypeController {

    @Autowired
    private UserService userService;

    @Autowired
    private TicketTypeService ticketTypeService;

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/createTicketType")
    public JsonNode createTicketType(@RequestBody Ticket_Type event) {
        try {
            return ticketTypeService.createTicketType(event);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/updateTicketType")
    public JsonNode updateTicketType(@RequestBody Ticket_Type event) {
        User user = userService.getLoggedInUser();
        int managerId = user.getId();
        try {
            return ticketTypeService.updateTicketType(event, managerId);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PostMapping("/viewTicketTypes")
    public List<Ticket_Type> viewTicketTypes(@RequestBody JsonNode body) {
        try {
            return ticketTypeService.viewTicketTypes(body);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PostMapping("/viewSingleTicketType")
    public Ticket_Type viewSingleTicketType(@RequestBody JsonNode body) {
        try {
            return ticketTypeService.viewSingleTicketType(body);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Ticketing_Officer')")
    @PostMapping("/verifyTicket")
    public JsonNode verifyTicket(@RequestBody JsonNode body) {
        try {
            int userId = body.get("userId").asInt();
            int eventId = body.get("eventId").asInt();
            int ticketId = body.get("ticketId").asInt();
            int ticketOfficerId = body.get("ticketOfficerId").asInt();
            int ticketTypeId = body.get("ticketTypeId").asInt();
            return ticketTypeService.verifyTicket(userId, eventId, ticketId, ticketOfficerId, ticketTypeId);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PostMapping("/getTicketDetails")
    public JsonNode getTicketDetails(@RequestBody String body){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            int userId = jsonNode.get("userId").asInt();
            int eventId = jsonNode.get("eventId").asInt();
            int ticketId = jsonNode.get("ticketId").asInt();
            int ticketTypeId = jsonNode.get("ticketTypeId").asInt();

            return ticketTypeService.getTicketDetails(userId, eventId, ticketId, ticketTypeId);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
        
    }
    
}
