package com.OOP.TicketBookingSystem.service;

import java.util.List;

import com.OOP.TicketBookingSystem.model.User;
import com.fasterxml.jackson.databind.JsonNode;

public interface UserService {

    public boolean createUser(User user);

    public User login(JsonNode body);

    public User getUserById(int id);

    public User getUserByEmail(String email);

    public List<User> getAllUsers();

    public String getHash(String password, String salt);
}
