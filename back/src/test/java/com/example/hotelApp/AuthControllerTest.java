package com.example.hotelApp;

import com.example.hotelApp.security.AuthController;
import com.example.hotelApp.security.model.CustomUserDetails;
import com.example.hotelApp.security.model.LoginRequest;
import com.example.hotelApp.security.services.CustomUserDetailsService;
import com.example.hotelApp.security.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf; 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) 
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testUser", "password");
        String requestJson = objectMapper.writeValueAsString(loginRequest);

        CustomUserDetails customUserDetails = new CustomUserDetails("testUser", "encodedPassword", "ROLE_USER");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateToken(customUserDetails)).thenReturn("dummy-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-jwt-token"));
    }

    @Test
    void testAdminLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("adminUser", "password");
        String requestJson = objectMapper.writeValueAsString(loginRequest);

        CustomUserDetails adminUserDetails = new CustomUserDetails("adminUser", "encodedAdminPassword", "ROLE_ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                adminUserDetails,
                null,
                adminUserDetails.getAuthorities()
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateToken(adminUserDetails)).thenReturn("dummy-admin-token");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-admin-token"));
    }

    @Test
    void testLoginFailure_InvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testUser", "wrongPassword");
        String requestJson = objectMapper.writeValueAsString(loginRequest);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

}
