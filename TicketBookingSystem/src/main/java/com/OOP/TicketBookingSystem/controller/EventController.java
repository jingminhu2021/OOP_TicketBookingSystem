package com.OOP.TicketBookingSystem.controller;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.service.EventService;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @RequestMapping("/createEvent")
    public JsonNode createEvent(@RequestBody Event event){
        
        try{
            return eventService.createEvent(event);
        }catch(Exception e){
            System.err.println(e); 
        }
        return null;
    }

    @RequestMapping("/updateEvent")
    public JsonNode updateEvent(@RequestBody Event event){
        try{
            return eventService.updateEvent(event);
        }catch(Exception e){
            System.err.println(e);
        }
        return null;
    }

}
