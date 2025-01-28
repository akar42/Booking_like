package com.example.hotelApp.user;

import com.example.hotelApp.security.services.JwtService;
import com.example.hotelApp.user.model.RegistrationResponse;
import com.example.hotelApp.user.model.UpdateUserCommand;
import com.example.hotelApp.user.model.User;
import com.example.hotelApp.user.services.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final CreateUserService createUserService;
    private final GetUserByIdService getUserByIdService;
    private final GetAllUsersService getAllUsersService;
    private final UpdateUserService updateUserService;
    private final DeleteUserService deleteUserService;

    //Logged in User
    private final JwtService jwtService;
    private final GetLoggedUserService getLoggedUserService;
    private final UpdateLoggedUserService updateLoggedUserService;
    private final DeleteLoggedUserService deleteLoggedUserService;

    @PostMapping("/user")
    public ResponseEntity<RegistrationResponse> createUser(@RequestBody User user) {
        return createUserService.execute(user);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return getUserByIdService.execute(id);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return getAllUsersService.execute(null);
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        return updateUserService.execute(new UpdateUserCommand<>(id, user));
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        return deleteUserService.execute(id);
    }


    @GetMapping("/user/logged")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getInfoLogged(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        String login = jwtService.extractUsername(jwt);

        return getLoggedUserService.execute(login);
    }

    @PutMapping("/user/logged")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> updateInfoLogged(@RequestHeader("Authorization") String token, @RequestBody User user) {
        String jwt = token.substring(7);
        String login = jwtService.extractUsername(jwt);

        return updateLoggedUserService.execute(new UpdateUserCommand<>(login, user));
    }

    @DeleteMapping("/user/logged")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteLoggedUser(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        String login = jwtService.extractUsername(jwt);

        return deleteLoggedUserService.execute(login);
    }
}
