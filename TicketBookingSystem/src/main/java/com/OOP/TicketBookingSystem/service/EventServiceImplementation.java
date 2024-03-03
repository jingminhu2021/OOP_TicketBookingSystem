package com.OOP.TicketBookingSystem.service;


import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.model.Event_Manager;
import com.OOP.TicketBookingSystem.repository.EventManagerRepo;
import com.OOP.TicketBookingSystem.repository.EventRepo;

@Service
public class EventServiceImplementation implements EventService{
    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private EventManagerRepo eventManagerRepo;

    @Override
    public JsonNode createEvent(Event event){
        String eventName = event.getEventName();
        String eventManagerName = event.getEventManagerName();
        
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        
        node.put("message","Event already existed");
        node.put("status",false);
        
        if((eventRepo.findByExactEvent(eventName) == null)){
            if (eventManagerRepo.findByName(eventManagerName) != null){
                try {
                    eventRepo.save(event);
                    node.put("message", "Successfully created Event");
                    node.put("status",true);
                }catch (IllegalArgumentException e){ 
                    node.put("message", e.toString());
                    
                }catch (OptimisticLockingFailureException e){
                    node.put("message", e.toString());
                }
            }else {
                node.put("message","Invalid Event Manager");
            } 
        }

        return node;
    }

    @Override
    public Event getEventById(int id){
        return eventRepo.findById(id).get();
    }

    @Override
    public List<Event> getEventsByEventManager(Event_Manager eventManager){
        String eventManagerName = eventManager.getName();
        return eventRepo.findByEventManager(eventManagerName);
    }

    @Override
    public List<Event> getAllEvents(){
        return eventRepo.findAll();
    }

    @Override
    public JsonNode updateEvent(Event event){
        //Todo add check if event manager match
        int id = event.getId();
        String eventName = event.getEventName();
        System.out.println(id);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        
        node.put("message","Event not found");
        node.put("status",false);
    
        if(eventRepo.findById(id).orElse(null) != null){
            if(eventRepo.findByExactEvent(eventName)==null){
                try {
                    eventRepo.save(event);
                    node.put("message","Successfully updated Event");
                    node.put("status",true);
    
                }catch (IllegalArgumentException e){    
                    node.put("message", e.toString());
    
                }catch (OptimisticLockingFailureException e){
                    node.put("message", e.toString());
    
                }
            }else{
                node.put("message","Event name already exist");
            }
        }

        return node;
    }

}
