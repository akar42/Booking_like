package com.example.hotelApp;

import com.example.hotelApp.room.RoomController;
import com.example.hotelApp.security.configs.SecurityConfig;
import com.example.hotelApp.security.services.CustomUserDetailsService;
import com.example.hotelApp.security.services.JwtService;
import com.example.hotelApp.room.model.DateFilterDTO;
import com.example.hotelApp.room.model.Room;
import com.example.hotelApp.room.services.CreateRoomService;
import com.example.hotelApp.room.services.DeleteRoomService;
import com.example.hotelApp.room.services.GetAllRoomsService;
import com.example.hotelApp.room.services.GetAvailableRoomsInTimePeriodService;
import com.example.hotelApp.room.services.GetRoomByIdService;
import com.example.hotelApp.room.services.UpdateRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
@Import(SecurityConfig.class) 
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private CreateRoomService createRoomService;
    
    @MockBean
    private GetRoomByIdService getRoomByIdService;
    
    @MockBean
    private GetAllRoomsService getAllRoomsService;
    
    @MockBean
    private UpdateRoomService updateRoomService;
    
    @MockBean
    private DeleteRoomService deleteRoomService;
    
    @MockBean
    private GetAvailableRoomsInTimePeriodService getAvailableRoomsInTimePeriodService;
    
    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    
    @Test
    public void testGetAllRooms() throws Exception {
        Room room1 = new Room();
        room1.setRoomId(1);
        room1.setRoomNumber("101");
        
        Room room2 = new Room();
        room2.setRoomId(2);
        room2.setRoomNumber("102");
        
        List<Room> rooms = Arrays.asList(room1, room2);
        
        when(getAllRoomsService.execute(null))
                .thenReturn(ResponseEntity.ok(rooms));
        
        String token = "validToken";
        
        mockMvc.perform(get("/api/rooms")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].roomId").value(1));
    }
    
	@Test
	public void testGetAvailableRoomsWithFilter() throws Exception {
		DateFilterDTO filter = new DateFilterDTO();
		filter.setRequiredGuests(2);
		filter.setStartDate(LocalDate.of(2025, 1, 1));
		filter.setEndDate(LocalDate.of(2025, 1, 5));

		Room room = new Room();
		room.setRoomId(1);
		room.setRoomNumber("101");
		room.setRoomType("Deluxe");
		room.setPrice(150.0);
		room.setGuestNumber(2);
		room.setFloorNumber(1);
		room.setFacilities("Wi-Fi, TV, Mini-bar");
		room.setReservations(Collections.emptyList());

		List<Room> availableRooms = Collections.singletonList(room);

		when(getAvailableRoomsInTimePeriodService.execute(eq(filter)))
				.thenReturn(ResponseEntity.ok(availableRooms));

		mockMvc.perform(post("/api/rooms/filter")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(filter)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].roomId").value(1))
				.andExpect(jsonPath("$[0].roomNumber").value("101"));
	}
    
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateRoom_withValidToken() throws Exception {
        Room room = new Room();
        room.setRoomId(1);
        room.setRoomNumber("101");
        room.setRoomType("Deluxe");
        room.setPrice(150.0);
        room.setGuestNumber(2);
        room.setFloorNumber(1);
        room.setFacilities("Wi-Fi, TV, Mini-bar");
        room.setReservations(Collections.emptyList());
        
        when(createRoomService.execute(any(Room.class)))
                .thenReturn(ResponseEntity.ok(room));
        
        String token = "validToken";
        
        mockMvc.perform(post("/api/room")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId").value(1))
                .andExpect(jsonPath("$.roomNumber").value("101"));
    }
        
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateRoom_withValidToken() throws Exception {
        int roomId = 1;
        Room updatedRoom = new Room();
        updatedRoom.setRoomId(roomId);
        updatedRoom.setRoomNumber("101");
        updatedRoom.setRoomType("Deluxe Updated");
        updatedRoom.setPrice(160.0);
        updatedRoom.setGuestNumber(3);
        updatedRoom.setFloorNumber(1);
        updatedRoom.setFacilities("Wi-Fi, TV, Mini-bar, Air Conditioner");
        updatedRoom.setReservations(Collections.emptyList());
        
        when(updateRoomService.execute(any()))
                .thenReturn(ResponseEntity.ok(updatedRoom));
        
        String token = "validToken";
        
        mockMvc.perform(put("/api/room/{id}", roomId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRoom)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomType").value("Deluxe Updated"))
                .andExpect(jsonPath("$.price").value(160.0));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteRoom_withValidToken() throws Exception {
        int roomId = 1;
        
        when(deleteRoomService.execute(eq(roomId)))
                .thenReturn(ResponseEntity.ok().build());
        
        String token = "validToken";
        
        mockMvc.perform(delete("/api/room/{id}", roomId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
