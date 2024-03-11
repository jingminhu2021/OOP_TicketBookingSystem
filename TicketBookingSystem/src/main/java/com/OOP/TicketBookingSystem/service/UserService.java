package com.OOP.TicketBookingSystem.service;

import java.util.List;


import com.OOP.TicketBookingSystem.model.User;

public interface UserService {

    public User getUserById(int id);

    public User getUserByEmail(String email);

    public List<User> getAllUsers();

    public void setTicketOfficer(int id);

    public User getLoggedInUser();

    public boolean verifyTicket(int userId, int ticketId);

}
