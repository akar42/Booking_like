package com.example.hotelApp.user.services;

import com.example.hotelApp.Query;
import com.example.hotelApp.user.UserRepository;
import com.example.hotelApp.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllUsersService implements Query<Void, List<User>> {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<List<User>> execute(Void input) {
        List<User> users = userRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
