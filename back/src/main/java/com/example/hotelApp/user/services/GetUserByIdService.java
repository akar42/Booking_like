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
public class GetUserByIdService implements Query<Integer, User> {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<User> execute(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            return ResponseEntity.status(HttpStatus.OK).body(user);
        }

        throw new UserNotFoundException();
    }
}
