package com.example.hotelApp.admin.services;

import com.example.hotelApp.Query;
import com.example.hotelApp.admin.AdminRepository;
import com.example.hotelApp.admin.model.Admin;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllAdminsService implements Query<Void, List<Admin>> {

    private final AdminRepository adminRepository;
    @Override
    public ResponseEntity<List<Admin>> execute(Void input) {
        List<Admin> adminList = adminRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(adminList);
    }
}
