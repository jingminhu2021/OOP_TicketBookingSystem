package com.OOP.TicketBookingSystem.service;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

import com.OOP.TicketBookingSystem.model.User;

public interface UserService {

    public boolean createUser(User user);

    public User login(JsonNode body);

    public User getUserById(int id);

    public User getUserByEmail(String email);

    public List<User> getAllUsers();

    public void setTicketOfficer(int id);

}
