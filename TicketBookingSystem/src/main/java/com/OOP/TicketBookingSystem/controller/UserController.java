package com.OOP.TicketBookingSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.OOP.TicketBookingSystem.model.User;
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

    @GetMapping("/get")
    public User getUserById(@RequestBody int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/getEmail")
    public User getUserByEmail(@RequestBody String email) {
        return userService.getUserByEmail(email);
    }
    
    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @GetMapping("/setTicketManager")
    public void setTicketOfficer(@RequestBody int id) {
        userService.setTicketOfficer(id);
    }
    
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getLoggedInUser")
    public User getLoggedInUser() {
        return userService.getLoggedInUser();
    }

    @GetMapping("/sendEmail")
    public JsonNode sendEmail(@RequestBody String email) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode jsonNode = mapper.readTree(email);
            return emailService.sendEmail(jsonNode);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
}
