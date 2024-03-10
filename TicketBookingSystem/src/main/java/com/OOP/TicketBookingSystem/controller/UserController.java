package com.OOP.TicketBookingSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

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
    public void setTicketManager(@RequestBody int id) {
        userService.setTicketManager(id);
    }
    
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getLoggedInUser")
    public User getLoggedInUser() {
        return userService.getLoggedInUser();
    }
}
