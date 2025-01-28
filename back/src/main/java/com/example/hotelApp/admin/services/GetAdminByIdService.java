package com.example.hotelApp.admin.services;

import com.example.hotelApp.Query;
import com.example.hotelApp.admin.AdminRepository;
import com.example.hotelApp.admin.model.Admin;
import com.example.hotelApp.exceptions.AdminNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetAdminByIdService implements Query<Integer, Admin> {

    private final AdminRepository adminRepository;
    @Override
    public ResponseEntity<Admin> execute(Integer id) {
        Optional<Admin> adminOptional = adminRepository.findById(id);

        if (adminOptional.isPresent())
        {
            return ResponseEntity.status(HttpStatus.OK).body(adminOptional.get());
        }

        throw new AdminNotFoundException();
    }
}
