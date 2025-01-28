package com.example.hotelApp.room.services;

import com.example.hotelApp.Query;
import com.example.hotelApp.room.RoomRepository;
import com.example.hotelApp.room.model.Room;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllRoomsService implements Query<Void, List<Room>> {

    private final RoomRepository roomRepository;

    @Override
    public ResponseEntity<List<Room>> execute(Void input) {
        List<Room> rooms = roomRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(rooms);
    }
}
