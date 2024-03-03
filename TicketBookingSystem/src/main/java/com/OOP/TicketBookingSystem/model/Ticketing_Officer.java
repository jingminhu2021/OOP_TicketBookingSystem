package com.OOP.TicketBookingSystem.model;

import jakarta.persistence.Entity;

@Entity
public class Ticketing_Officer extends User{
    public Ticketing_Officer(){
        super("Ticketing_Officer");
    }
}
