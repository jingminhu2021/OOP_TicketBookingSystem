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

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.service.EventService;
import com.OOP.TicketBookingSystem.service.UserService;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/createEvent")
    public JsonNode createEvent(@RequestBody Event event) {
        User user = userService.getLoggedInUser();
        String managerName = user.getName();
        
        try {
            return eventService.createEvent(event, managerName);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
    
    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/updateEvent")
    public JsonNode updateEvent(@RequestBody Event event) {
        User user = userService.getLoggedInUser();
        String managerName = user.getName();
        event.setEventManagerName(managerName);
        try {
            return eventService.updateEvent(event);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/viewOwnEventByEventManager")
    public JsonNode viewOwnEventByEventManager() { // view event that was created by the Event Manager
        User user = userService.getLoggedInUser();
        String managerName = user.getName();

        try {
            return eventService.viewEventByEventManager(managerName);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/cancelEventByManager")
    public JsonNode cancelEvent(@RequestBody String body) {
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            return eventService.cancelEventByManager(jsonNode);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
    
    @GetMapping("/viewAllEvents")
    public List<Event> viewAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/viewEvent")
    public JsonNode viewEvent(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            return eventService.viewEvent(jsonNode);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

}
