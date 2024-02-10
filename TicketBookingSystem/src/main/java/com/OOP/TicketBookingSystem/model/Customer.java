package com.OOP.TicketBookingSystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Customer extends User {

    @Column
    private String role = "Customer";

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
