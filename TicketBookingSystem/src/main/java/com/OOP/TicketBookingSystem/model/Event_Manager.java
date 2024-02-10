package com.OOP.TicketBookingSystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Event_Manager extends User{

    public Event_Manager(){
        super("Event_Manager");
    }
}
