package com.OOP.TicketBookingSystem.service;

import java.util.List;

import com.OOP.TicketBookingSystem.model.Customer;
import com.OOP.TicketBookingSystem.model.Event_Manager;
import com.OOP.TicketBookingSystem.model.Ticketing_Officer;
import com.OOP.TicketBookingSystem.model.User;

public interface UserService {

    public Customer createUser(Customer customer);
    
    public Event_Manager createUser(Event_Manager eventManager);

    public Ticketing_Officer createUser(Ticketing_Officer eventManager);

    public User getUserById(int id);

    public List<User> getAllUsers();
}
