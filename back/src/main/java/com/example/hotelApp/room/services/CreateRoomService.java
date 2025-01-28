package com.example.hotelApp.room.services;

import com.example.hotelApp.Command;
import com.example.hotelApp.room.RoomRepository;
import com.example.hotelApp.room.model.Room;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateRoomService implements Command<Room, Room> {

    private final RoomRepository roomRepository;

    @Override
    public ResponseEntity<Room> execute(Room input) {
        Room room = roomRepository.save(input);

        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }
}
