package com.OOP.TicketBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OOP.TicketBookingSystem.model.Ticket;
import com.OOP.TicketBookingSystem.service.TicketService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/bookTicket")
    public JsonNode bookTicket(@ModelAttribute Ticket ticket) {

        try {
            return ticketService.bookTicket(ticket);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
}
