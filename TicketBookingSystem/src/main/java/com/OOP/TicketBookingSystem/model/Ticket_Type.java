package com.OOP.TicketBookingSystem.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ticket_Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketTypeId;
    @Column
    private int eventId;
    @Column
    private String eventCat;
    @Column
    private BigDecimal eventPrice;
    @Column
    private int numberOfTix;

    public int getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(int ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventCat() {
        return eventCat;
    }

    public void setEventCat(String eventCat) {
        this.eventCat = eventCat;
    }

    public BigDecimal getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(BigDecimal eventPrice) {
        this.eventPrice = eventPrice;
    }

    public int getNumberOfTix() {
        return numberOfTix;
    }

    public void setNumberOfTix(int numberOfTix) {
        this.numberOfTix = numberOfTix;
    }

}