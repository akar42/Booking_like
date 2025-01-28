package com.example.hotelApp.user.services;

import com.example.hotelApp.Query;
import com.example.hotelApp.exceptions.UserNotFoundException;
import com.example.hotelApp.user.UserRepository;
import com.example.hotelApp.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetLoggedUserService implements Query<String, User> {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<User> execute(String login) {
        Optional<User> optionalUser = userRepository.findByLogin(login);

        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalUser.get());
        }

        throw new UserNotFoundException();
    }
}
