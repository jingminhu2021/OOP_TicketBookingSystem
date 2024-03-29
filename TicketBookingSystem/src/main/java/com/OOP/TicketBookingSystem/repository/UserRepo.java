package com.OOP.TicketBookingSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.OOP.TicketBookingSystem.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM users WHERE email=?",nativeQuery=true)
    public Optional<User> findByOptEmail(String email);
    
    @Query(value = "SELECT * FROM users WHERE email=?",nativeQuery=true)
    public User findByEmail(String email);

    // find by id
    @Query(value = "SELECT * FROM users WHERE id=?",nativeQuery=true)
    public User findById(int id);

    @Modifying
    @Query (value = "UPDATE users u SET u.dtype='Ticketing_Officer' where u.id = :id", nativeQuery = true)
    public void setTicketOfficer(@Param("id")int id);
}
