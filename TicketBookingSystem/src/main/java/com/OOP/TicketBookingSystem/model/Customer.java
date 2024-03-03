package com.OOP.TicketBookingSystem.model;

import jakarta.persistence.Entity;

@Entity
public class Customer extends User {

    public Customer(){
        super("Customer");
    }
}
