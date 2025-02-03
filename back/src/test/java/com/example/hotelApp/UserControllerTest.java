package com.example.hotelApp;

import com.example.hotelApp.security.configs.SecurityConfig;
import com.example.hotelApp.security.services.JwtService;
import com.example.hotelApp.security.services.CustomUserDetailsService;
import com.example.hotelApp.user.UserController;
import com.example.hotelApp.user.model.RegistrationResponse;
import com.example.hotelApp.user.model.UpdateUserCommand;
import com.example.hotelApp.user.model.User;
import com.example.hotelApp.user.services.CreateUserService;
import com.example.hotelApp.user.services.DeleteUserService;
import com.example.hotelApp.user.services.GetAllUsersService;
import com.example.hotelApp.user.services.GetUserByIdService;
import com.example.hotelApp.user.services.UpdateUserService;
import com.example.hotelApp.user.services.DeleteLoggedUserService;
import com.example.hotelApp.user.services.GetLoggedUserService;
import com.example.hotelApp.user.services.UpdateLoggedUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import(SecurityConfig.class) 
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    
    @MockBean
    private CreateUserService createUserService;
    
    @MockBean
    private GetUserByIdService getUserByIdService;
    
    @MockBean
    private GetAllUsersService getAllUsersService;
    
    @MockBean
    private UpdateUserService updateUserService;
    
    @MockBean
    private DeleteUserService deleteUserService;
    
    @MockBean
    private GetLoggedUserService getLoggedUserService;
    
    @MockBean
    private UpdateLoggedUserService updateLoggedUserService;
    
    @MockBean
    private DeleteLoggedUserService deleteLoggedUserService;
    
    private final String validToken = "validToken";


    @Test
    void testAccessPersonalInfoBeforeLogin() throws Exception {
        mockMvc.perform(get("/api/user/logged"))
                .andExpect(status().isForbidden()); // 401
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testGetLoggedUser() throws Exception {
        String testLogin = "testuser";
        String testJwt = "Bearer " + validToken;

        User mockUser = new User();
        mockUser.setLogin(testLogin);
        mockUser.setName("Test User");

        when(jwtService.extractUsername(anyString())).thenReturn(testLogin);
        when(getLoggedUserService.execute(testLogin)).thenReturn(ResponseEntity.ok(mockUser));

        mockMvc.perform(get("/api/user/logged")
                .header("Authorization", testJwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(testLogin))
                .andExpect(jsonPath("$.name").value("Test User"));
    }


    @Test
    void testCreateUser() throws Exception {
        User newUser = new User();
        newUser.setLogin("newuser");
        newUser.setName("New User");

        RegistrationResponse regResponse = new RegistrationResponse(newUser, "token");

        when(createUserService.execute(any(User.class))).thenReturn(ResponseEntity.ok(regResponse));

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.login").value("newuser"))
                .andExpect(jsonPath("$.user.name").value("New User"))
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserById() throws Exception {
        User adminUser = new User();
        adminUser.setLogin("admin");
        adminUser.setName("Admin User");

        when(getUserByIdService.execute(eq(1))).thenReturn(ResponseEntity.ok(adminUser));

        mockMvc.perform(get("/api/user/{id}", 1)
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("admin"))
                .andExpect(jsonPath("$.name").value("Admin User"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setLogin("user1");
        user1.setName("User One");

        User user2 = new User();
        user2.setLogin("user2");
        user2.setName("User Two");

        List<User> users = Arrays.asList(user1, user2);

        when(getAllUsersService.execute(null)).thenReturn(ResponseEntity.ok(users));

        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].login").value("user1"))
                .andExpect(jsonPath("$[1].login").value("user2"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setLogin("user1");
        updatedUser.setName("Updated User One");

        when(updateUserService.execute(any(UpdateUserCommand.class))).thenReturn(ResponseEntity.ok(updatedUser));

        mockMvc.perform(put("/api/user/{id}", 1)
                .header("Authorization", "Bearer " + validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("user1"))
                .andExpect(jsonPath("$.name").value("Updated User One"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUser() throws Exception {
        when(deleteUserService.execute(eq(1))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/api/user/{id}", 1)
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testUpdateLoggedUser() throws Exception {
        String token = "Bearer " + validToken;
        User updatedUser = new User();
        updatedUser.setLogin("testuser");
        updatedUser.setName("Updated Name");

        when(jwtService.extractUsername(anyString())).thenReturn("testuser");
        when(updateLoggedUserService.execute(any(UpdateUserCommand.class)))
                .thenReturn(ResponseEntity.ok(updatedUser));

        mockMvc.perform(put("/api/user/logged")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testuser"))
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void testDeleteLoggedUser() throws Exception {
        String token = "Bearer " + validToken;

        when(jwtService.extractUsername(anyString())).thenReturn("testuser");
        when(deleteLoggedUserService.execute("testuser")).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/api/user/logged")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }
}
