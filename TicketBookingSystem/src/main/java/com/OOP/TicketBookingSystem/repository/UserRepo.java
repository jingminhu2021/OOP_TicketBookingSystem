package com.OOP.TicketBookingSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.OOP.TicketBookingSystem.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM users WHERE email=?",nativeQuery=true)
    public Optional<User> findByOptEmail(String email);
    
    @Query(value = "SELECT * FROM users WHERE email=?",nativeQuery=true)
    public User findByEmail(String email);
}
