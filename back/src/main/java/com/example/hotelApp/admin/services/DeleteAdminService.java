package com.example.hotelApp.admin.services;

import com.example.hotelApp.Command;
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
public class DeleteAdminService implements Command<Integer, Void> {

    private final AdminRepository adminRepository;
    @Override
    public ResponseEntity<Void> execute(Integer id) {
        Optional<Admin> adminToDelete = adminRepository.findById(id);

        if (adminToDelete.isPresent())
        {
            adminRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        throw new AdminNotFoundException();
    }
}
