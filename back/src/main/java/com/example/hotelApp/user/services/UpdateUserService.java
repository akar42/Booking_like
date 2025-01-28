package com.example.hotelApp.user.services;

import com.example.hotelApp.Command;
import com.example.hotelApp.exceptions.UserNotFoundException;
import com.example.hotelApp.user.UserRepository;
import com.example.hotelApp.user.model.UpdateUserCommand;
import com.example.hotelApp.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateUserService implements Command<UpdateUserCommand<Integer>, User> {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<User> execute(UpdateUserCommand<Integer> input) {
        Optional<User> userOptional = userRepository.findById(input.getId());

        if (userOptional.isPresent()) {
            User user = input.getUser();
            user.setUserId(input.getId());

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK).body(user);
        }

        throw new UserNotFoundException();
    }
}
