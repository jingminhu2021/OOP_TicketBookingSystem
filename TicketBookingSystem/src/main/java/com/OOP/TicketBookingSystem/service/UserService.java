package com.OOP.TicketBookingSystem.service;

import java.util.List;


import com.OOP.TicketBookingSystem.model.User;
import com.fasterxml.jackson.databind.JsonNode;

public interface UserService {

    public User getUserById(int id);

    public User getUserByEmail(String email);

    public List<User> getAllUsers();

    public JsonNode setTicketOfficer(int userId, int eventId);

    public User getLoggedInUser();
}
