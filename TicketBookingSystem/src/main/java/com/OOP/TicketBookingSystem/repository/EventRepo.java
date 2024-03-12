package com.OOP.TicketBookingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.OOP.TicketBookingSystem.model.Event;

@Repository
public interface EventRepo extends JpaRepository<Event, Integer> {
    @Query(value = "SELECT * FROM event WHERE event_manager_name=?", nativeQuery = true)
    public List<Event> findByEventManager(String name);

    @Query(value = "SELECT * FROM event WHERE event_name=?", nativeQuery = true)
    public Event findByExactEvent(String name);

    @Query(value = "SELECT * FROM event where event_name LIKE CONCAT('%', :name, '%')", nativeQuery = true)
    public List<Event> findBySimilarEvent(@Param("name") String name);

    @Query(value = "SELECT * FROM event WHERE id=?", nativeQuery = true)
    public Event findEventById(int id);
}
