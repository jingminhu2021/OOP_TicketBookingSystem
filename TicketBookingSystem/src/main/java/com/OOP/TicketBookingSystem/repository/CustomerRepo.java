package com.OOP.TicketBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OOP.TicketBookingSystem.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {

}
