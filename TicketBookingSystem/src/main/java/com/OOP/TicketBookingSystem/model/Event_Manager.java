package com.OOP.TicketBookingSystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Event_Manager extends User{

    @Column
    private String role = "Event_Manager";

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
