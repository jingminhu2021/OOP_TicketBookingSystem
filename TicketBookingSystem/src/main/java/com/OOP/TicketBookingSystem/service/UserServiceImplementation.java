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
    public boolean createUser(User user){
        if(user instanceof Customer){
            customerRepo.save((Customer) user);
            return true;
        }else if (user instanceof Event_Manager){
            eventManagerRepo.save((Event_Manager) user);
            return true;
        }else if (user instanceof Ticketing_Officer){
            ticketingOfficerRepo.save((Ticketing_Officer) user);
            return true;
        }
        return false;
    }   


    @Override
    public User getUserById(int id) {
        return userRepo.findById(id).get();
    }

    @Override
    public User getUserByEmail(String email){
        return userRepo.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
