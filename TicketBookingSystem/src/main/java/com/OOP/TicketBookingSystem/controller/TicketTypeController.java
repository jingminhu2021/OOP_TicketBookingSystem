package com.OOP.TicketBookingSystem.controller;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
    private TicketTypeService eventService;

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/createTicketType")
    public JsonNode createTicketType(@RequestBody Ticket_Type event) {
        try {
            return eventService.createTicketType(event);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

}
