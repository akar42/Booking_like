package com.example.hotelApp.user.services;

import com.example.hotelApp.Command;
import com.example.hotelApp.security.model.CustomUserDetails;
import com.example.hotelApp.security.services.JwtService;
import com.example.hotelApp.user.UserRepository;
import com.example.hotelApp.user.model.RegistrationResponse;
import com.example.hotelApp.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUserService implements Command<User, RegistrationResponse> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<RegistrationResponse> execute(User input) {
        // Encoding the password
        String rawPassword = input.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        input.setPassword(encodedPassword);

        // Save the user
        User savedUser = userRepository.save(input);

        // Generate a token
        CustomUserDetails savedUserDetails = new CustomUserDetails(savedUser.getLogin(), savedUser.getPassword(), "ROLE_USER");
        String token = jwtService.generateToken((UserDetails) savedUserDetails);

        // Wrapper for UserDTO and Jwt
        RegistrationResponse userWithJwt = new RegistrationResponse(savedUser, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(userWithJwt);
    }
}
