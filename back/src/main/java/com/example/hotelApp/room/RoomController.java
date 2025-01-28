package com.example.hotelApp.room;

import com.example.hotelApp.room.model.DateFilterDTO;
import com.example.hotelApp.room.model.Room;
import com.example.hotelApp.room.model.UpdateRoomCommand;
import com.example.hotelApp.room.services.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RoomController {

    private final CreateRoomService createRoomService;
    private final GetRoomByIdService getRoomByIdService;
    private final GetAllRoomsService getAllRoomsService;
    private final UpdateRoomService updateRoomService;
    private final DeleteRoomService deleteRoomService;
    private final GetAvailableRoomsInTimePeriodService getAvailableRoomsInTimePeriodService;

    @PostMapping("/room")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return createRoomService.execute(room);
    }

    @GetMapping("/room/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> getRoomById(@PathVariable Integer id) {
        return getRoomByIdService.execute(id);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getAllRooms() {
        return getAllRoomsService.execute(null);
    }

    @PutMapping("/room/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> updateRoom(@PathVariable Integer id, @RequestBody Room room) {
        return updateRoomService.execute(new UpdateRoomCommand(id, room));
    }

    @DeleteMapping("/room/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Integer id) {
        return deleteRoomService.execute(id);
    }

    @GetMapping("/rooms/filter")
    public ResponseEntity<List<Room>> getAvailableRooms(@RequestBody DateFilterDTO dateFilterDTO) {
        return getAvailableRoomsInTimePeriodService.execute(dateFilterDTO);
    }
}
