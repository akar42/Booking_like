package com.example.hotelApp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hotelApp.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByLogin(String login);
}
