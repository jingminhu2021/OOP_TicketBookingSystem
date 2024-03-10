package com.OOP.TicketBookingSystem.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.service.EventService;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @PreAuthorize("hasRole('Event_Manager')")
    @RequestMapping("/createEvent")
    public JsonNode createEvent(@ModelAttribute Event event) {

        try {
            return eventService.createEvent(event);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
    
    @PreAuthorize("hasRole('Event_Manager')")
    @RequestMapping("/updateEvent")
    public JsonNode updateEvent(@ModelAttribute Event event) {
        try {
            return eventService.updateEvent(event);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @RequestMapping("/viewEventByEventManager")
    public JsonNode viewEventByEventManager(@ModelAttribute String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            return eventService.viewEventByEventManager(jsonNode);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @RequestMapping("/cancelEventByManager")
    public JsonNode cancelEvent(@ModelAttribute String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            return eventService.cancelEventByManager(jsonNode);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
}
