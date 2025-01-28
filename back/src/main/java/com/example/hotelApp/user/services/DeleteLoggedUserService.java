package com.example.hotelApp.user.services;

import com.example.hotelApp.Command;
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
public class DeleteLoggedUserService implements Command<String, Void> {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<Void> execute(String login) {
        Optional<User> userOptional = userRepository.findByLogin(login);

        if (userOptional.isPresent()) {
            userRepository.deleteById(userOptional.get().getUserId());

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        throw new UserNotFoundException();
    }
}
