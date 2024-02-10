package com.OOP.TicketBookingSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.OOP.TicketBookingSystem.model.Customer;
import com.OOP.TicketBookingSystem.model.Event_Manager;
import com.OOP.TicketBookingSystem.model.Ticketing_Officer;
import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/createCustomer")
    public boolean createUser(@RequestBody Customer customer){
        return userService.createUser(customer);
    }

    @RequestMapping("/createEventManager")
    public boolean createUser(@RequestBody Event_Manager eventManager){
        return userService.createUser(eventManager);
    }

    @RequestMapping("/createTicketingOfficer")
    public boolean createUser(@RequestBody Ticketing_Officer ticketing_Officer){
        return userService.createUser(ticketing_Officer);
    }

    @GetMapping("/get")
    public User getUserById(int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/getEmail")
    public User getUserByEmail(String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
