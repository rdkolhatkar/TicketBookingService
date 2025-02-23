package com.ratnakar.practice.TicketBookingAPI.repository;

import com.ratnakar.practice.TicketBookingAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    public User findByEmail(String email);
}
