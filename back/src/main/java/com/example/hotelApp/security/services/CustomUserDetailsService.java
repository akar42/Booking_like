package com.example.hotelApp.security.services;

import com.example.hotelApp.admin.AdminRepository;
import com.example.hotelApp.admin.model.Admin;
import com.example.hotelApp.security.model.CustomUserDetails;
import com.example.hotelApp.user.UserRepository;
import com.example.hotelApp.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // 1) Try to find user
        Optional<User> userOpt = userRepository.findByLogin(login);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return new CustomUserDetails(
                    user.getLogin(),
                    user.getPassword(),
                    "ROLE_USER"
            );
        }

        // 2) If not found in User, try Admin
        Optional<Admin> adminOpt = adminRepository.findByLogin(login);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return new CustomUserDetails(
                    admin.getLogin(),
                    admin.getPassword(),
                    "ROLE_ADMIN"
            );
        }

        // 3) If neither found, throw exception
        throw new UsernameNotFoundException("Login not found: " + login);
    }
}
