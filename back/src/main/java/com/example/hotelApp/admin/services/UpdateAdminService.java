package com.example.hotelApp.admin.services;

import com.example.hotelApp.Command;
import com.example.hotelApp.admin.AdminRepository;
import com.example.hotelApp.admin.model.Admin;
import com.example.hotelApp.admin.model.UpdateAdminCommand;
import com.example.hotelApp.exceptions.AdminNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateAdminService implements Command<UpdateAdminCommand, Admin> {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public ResponseEntity<Admin> execute(UpdateAdminCommand input) {
        Optional<Admin> adminToChange = adminRepository.findById(input.getId());

        if (adminToChange.isPresent()) {
            Admin newAdmin = input.getAdmin();
            newAdmin.setAdminId(input.getId());

//            String password = newAdmin.getPassword();
//            newAdmin.setPassword(passwordEncoder.encode(password));

            adminRepository.save(newAdmin);

            return ResponseEntity.status(HttpStatus.OK).body(newAdmin);
        }

        throw new AdminNotFoundException();
    }
}
