package com.OOP.TicketBookingSystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Ticketing_Officer extends User{
    @Column
    private String role = "Ticketing_Officer";

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
