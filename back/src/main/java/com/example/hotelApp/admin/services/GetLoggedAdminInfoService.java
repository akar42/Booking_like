package com.example.hotelApp.admin.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.hotelApp.Query;
import com.example.hotelApp.admin.AdminRepository;
import com.example.hotelApp.admin.model.Admin;
import com.example.hotelApp.exceptions.AdminNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GetLoggedAdminInfoService implements Query<String, Admin>{

	private final AdminRepository adminRepository;

	@Override
	public ResponseEntity<Admin> execute(String login) {
		Optional<Admin> optionalAdmin = adminRepository.findByLogin(login);

		if (optionalAdmin.isPresent())
		{
			return ResponseEntity.status(HttpStatus.OK).body(optionalAdmin.get());
		}

		throw new AdminNotFoundException();
	}
	
}
