package com.OOP.TicketBookingSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.model.Ticket;
import com.OOP.TicketBookingSystem.service.EmailService;
import com.OOP.TicketBookingSystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/get")
    public User getUserById(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode jsonNode = mapper.readTree(body);
            int id = jsonNode.get("id").asInt();
            return userService.getUserById(id);
        }catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PostMapping("/getEmail")
    public User getUserByEmail(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode jsonNode = mapper.readTree(body);
            String email = jsonNode.get("email").textValue();
            return userService.getUserByEmail(email);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
    
    @PostMapping("/getAll")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/setTicketOfficer")
    public JsonNode setTicketOfficer(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        
        try{
            JsonNode jsonNode = mapper.readTree(body);
            int userId = jsonNode.get("userId").asInt();
            int eventId = jsonNode.get("eventId").asInt();
            return userService.setTicketOfficer(userId, eventId);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
    
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/getLoggedInUser")
    public User getLoggedInUser() {
        return userService.getLoggedInUser();
    }

    // @GetMapping("/sendEmail")
    // public JsonNode sendEmail(@RequestBody String email) {
    //     ObjectMapper mapper = new ObjectMapper();
    //     try{
    //         JsonNode jsonNode = mapper.readTree(email);
    //         return emailService.sendEmail(jsonNode);
    //     } catch (Exception e) {
    //         System.err.println(e);
    //     }
    //     return null;
    // }

    @PreAuthorize("hasRole('Ticket_Officer')")
    @GetMapping("/verifyTicket")
    public boolean verifyTicket(@RequestBody int ticketId, int userId){
        return userService.verifyTicket(ticketId, userId);
    }
}
