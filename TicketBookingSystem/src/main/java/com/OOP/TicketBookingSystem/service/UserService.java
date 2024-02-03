package com.OOP.TicketBookingSystem.service;

import java.util.List;

import com.OOP.TicketBookingSystem.model.User;

public interface UserService {

    public User createUser(User user);

    public User getUserById(int id);

    public List<User> getAllUsers();
}
