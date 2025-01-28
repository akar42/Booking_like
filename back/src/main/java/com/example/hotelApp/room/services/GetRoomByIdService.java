package com.example.hotelApp.room.services;

import com.example.hotelApp.Query;
import com.example.hotelApp.exceptions.RoomNotFoundException;
import com.example.hotelApp.room.RoomRepository;
import com.example.hotelApp.room.model.Room;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetRoomByIdService implements Query<Integer, Room> {

    private final RoomRepository roomRepository;

    @Override
    public ResponseEntity<Room> execute(Integer id) {
        Optional<Room> roomOptional = roomRepository.findById(id);

        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();

            return ResponseEntity.status(HttpStatus.OK).body(room);
        }

        throw new RoomNotFoundException();
    }
}
