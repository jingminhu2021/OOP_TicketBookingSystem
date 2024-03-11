package com.OOP.TicketBookingSystem.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.repository.UserRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.OOP.TicketBookingSystem.model.Ticket;

@Service
public class UserServiceImplementation implements UserService{

    @Autowired
    private UserRepo userRepo;

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

    @Transactional
    @Override
    public JsonNode setTicketOfficer(int id) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "No user found");
        node.put("status", false);
        User user = getUserById(id);
        
        if (user != null){
            
            if (user.getClass().getSimpleName() == "Ticket_Officer"){
                node.put("message", "User is already a Ticket Officer");
            } else {
                try {
                    userRepo.setTicketOfficer(id);
                    node.put("message", "Successfully updated User to Ticket Officer");
                    node.put("status", true);
                } catch (Exception e){
                    node.put("message", e.toString());
                }
            }
        }
        return node;
    }

    @Override
    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return getUserByEmail(email);
    }

    @Override
    public boolean verifyTicket(int userId, int ticketId) {
        if(userRepo.verifyTicket(userId, ticketId) instanceof Ticket){
            return true;
        } 
        return false;
    }

}
