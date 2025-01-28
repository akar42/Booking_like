package com.example.hotelApp.admin.services;

import com.example.hotelApp.Command;
import com.example.hotelApp.admin.AdminRepository;
import com.example.hotelApp.admin.model.Admin;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateAdminService implements Command<Admin, Admin> {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Admin> execute(Admin input) {
        //Encoding password
        String rawPassword = input.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        input.setPassword(encodedPassword);

        Admin createdAdmin = adminRepository.save(input);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
    }
}
