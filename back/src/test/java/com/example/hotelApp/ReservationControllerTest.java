package com.example.hotelApp;

import com.example.hotelApp.reservation.ReservationController;
import com.example.hotelApp.reservation.model.Reservation;
import com.example.hotelApp.reservation.model.UpdateReservationCommand;
import com.example.hotelApp.reservation.services.CreateReservationService;
import com.example.hotelApp.reservation.services.DeleteReservationService;
import com.example.hotelApp.reservation.services.GetAdminReservationsService;
import com.example.hotelApp.reservation.services.GetAllReservationsService;
import com.example.hotelApp.reservation.services.GetReservationById;
import com.example.hotelApp.reservation.services.UpdateReservationService;
import com.example.hotelApp.security.services.CustomUserDetailsService;
import com.example.hotelApp.security.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private CreateReservationService createReservationService;
    
    @MockBean
    private GetReservationById getReservationById;
    
    @MockBean
    private GetAllReservationsService getAllReservationsService;
    
    @MockBean
    private UpdateReservationService updateReservationService;
    
    @MockBean
    private DeleteReservationService deleteReservationService;
    
    @MockBean
    private GetAdminReservationsService getAdminReservationsService;
    
    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    
    @Test
    @WithMockUser(roles = "USER")
    public void testCreateReservation() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setUserId(100);
        reservation.setAdminId(10);
        reservation.setStartDate(LocalDate.of(2025, 1, 1));
        reservation.setEndDate(LocalDate.of(2025, 1, 5));
        reservation.setTotalCost(500.0);
        reservation.setStatus("NEW");
        reservation.setRooms(Collections.emptySet());
        
        when(createReservationService.execute(any(Reservation.class)))
                .thenReturn(ResponseEntity.ok(reservation));
        
        mockMvc.perform(post("/api/reservation")
				.with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1))
                .andExpect(jsonPath("$.userId").value(100))
                .andExpect(jsonPath("$.adminId").value(10))
                .andExpect(jsonPath("$.status").value("NEW"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetReservationById() throws Exception {
        int reservationId = 1;
        Reservation reservation = new Reservation();
        reservation.setReservationId(reservationId);
        reservation.setUserId(100);
        reservation.setAdminId(10);
        reservation.setStartDate(LocalDate.of(2025, 1, 1));
        reservation.setEndDate(LocalDate.of(2025, 1, 5));
        reservation.setTotalCost(500.0);
        reservation.setStatus("CONFIRMED");
        reservation.setRooms(Collections.emptySet());
        
        when(getReservationById.execute(eq(reservationId)))
                .thenReturn(ResponseEntity.ok(reservation));
        
        mockMvc.perform(get("/api/reservation/{id}", reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(reservationId))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllReservations() throws Exception {
        Reservation reservation1 = new Reservation();
        reservation1.setReservationId(1);
        reservation1.setUserId(100);
        reservation1.setAdminId(10);
        reservation1.setStartDate(LocalDate.of(2025, 1, 1));
        reservation1.setEndDate(LocalDate.of(2025, 1, 5));
        reservation1.setTotalCost(500.0);
        reservation1.setStatus("CONFIRMED");
        reservation1.setRooms(Collections.emptySet());
        
        Reservation reservation2 = new Reservation();
        reservation2.setReservationId(2);
        reservation2.setUserId(101);
        reservation2.setAdminId(11);
        reservation2.setStartDate(LocalDate.of(2025, 2, 1));
        reservation2.setEndDate(LocalDate.of(2025, 2, 5));
        reservation2.setTotalCost(600.0);
        reservation2.setStatus("NEW");
        reservation2.setRooms(Collections.emptySet());
        
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);
        
        when(getAllReservationsService.execute(null))
                .thenReturn(ResponseEntity.ok(reservations));
        
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].reservationId").value(1))
                .andExpect(jsonPath("$[1].reservationId").value(2));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAdminReservations() throws Exception {
        int adminId = 10;
        Reservation reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setUserId(100);
        reservation.setAdminId(adminId);
        reservation.setStartDate(LocalDate.of(2025, 1, 1));
        reservation.setEndDate(LocalDate.of(2025, 1, 5));
        reservation.setTotalCost(500.0);
        reservation.setStatus("CONFIRMED");
        reservation.setRooms(Collections.emptySet());
        
        List<Reservation> reservations = Collections.singletonList(reservation);
        
        when(getAdminReservationsService.execute(eq(adminId)))
                .thenReturn(ResponseEntity.ok(reservations));
        
        mockMvc.perform(get("/api/reservations/admin/{adminId}", adminId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].adminId").value(adminId));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateReservation() throws Exception {
        int reservationId = 1;
        Reservation updatedReservation = new Reservation();
        updatedReservation.setReservationId(reservationId);
        updatedReservation.setUserId(100);
        updatedReservation.setAdminId(10);
        updatedReservation.setStartDate(LocalDate.of(2025, 1, 2));
        updatedReservation.setEndDate(LocalDate.of(2025, 1, 6));
        updatedReservation.setTotalCost(550.0);
        updatedReservation.setStatus("UPDATED");
        updatedReservation.setRooms(Collections.emptySet());
        
        UpdateReservationCommand updateCommand = new UpdateReservationCommand(reservationId, updatedReservation);
        
        when(updateReservationService.execute(updateCommand))
                .thenReturn(ResponseEntity.ok(updatedReservation));
        
        mockMvc.perform(put("/api/reservation/{id}", reservationId)
				.with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedReservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(reservationId))
                .andExpect(jsonPath("$.status").value("UPDATED"))
                .andExpect(jsonPath("$.totalCost").value(550.0));
    }
    
    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteReservation() throws Exception {
        int reservationId = 1;
        
        when(deleteReservationService.execute(eq(reservationId)))
                .thenReturn(ResponseEntity.ok().build());
        
        mockMvc.perform(delete("/api/reservation/{id}", reservationId)
				.with(csrf()))
                .andExpect(status().isOk());
    }
}
