package com.OOP.TicketBookingSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.OOP.TicketBookingSystem.model.Customer;
import com.OOP.TicketBookingSystem.model.Event_Manager;
import com.OOP.TicketBookingSystem.model.Ticketing_Officer;
import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.repository.CustomerRepo;
import com.OOP.TicketBookingSystem.repository.EventManagerRepo;
import com.OOP.TicketBookingSystem.repository.TicketingOfficerRepo;
import com.OOP.TicketBookingSystem.repository.UserRepo;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired    
    private CustomerRepo customerRepo;
    
    @Autowired
    private EventManagerRepo eventManagerRepo;

    @Autowired
    private TicketingOfficerRepo ticketingOfficerRepo;
    
    @Override
    public Customer createUser(Customer customer){
        return customerRepo.save(customer);
    }

    @Override
    public Event_Manager createUser(Event_Manager eventManager){
        return eventManagerRepo.save(eventManager);
    }

    @Override
    public Ticketing_Officer createUser(Ticketing_Officer ticketingOfficer){
        return ticketingOfficerRepo.save(ticketingOfficer);
    }

    @Override
    public User getUserById(int id) {
        return userRepo.findById(id).get();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
