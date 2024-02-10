package com.OOP.TicketBookingSystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Ticketing_Officer extends User{
    public Ticketing_Officer(){
        super("Ticketing_Officer");
    }
}
