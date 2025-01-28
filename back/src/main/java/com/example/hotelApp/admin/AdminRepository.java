package com.example.hotelApp.admin;

import com.example.hotelApp.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hotelApp.admin.model.Admin;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByLogin(String login);
}
