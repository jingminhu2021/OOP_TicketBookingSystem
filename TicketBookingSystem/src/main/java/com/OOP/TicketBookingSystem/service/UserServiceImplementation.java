package com.OOP.TicketBookingSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.repository.UserRepo;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User createUser(User user) {
        return userRepo.save(user);
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
