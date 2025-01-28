package com.example.hotelApp.room.services;

import com.example.hotelApp.Command;
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
public class DeleteRoomService implements Command<Integer, Void> {

    private final RoomRepository roomRepository;

    @Override
    public ResponseEntity<Void> execute(Integer id) {
        Optional<Room> roomOptional = roomRepository.findById(id);

        if (roomOptional.isPresent()) {
            roomRepository.deleteById(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        throw new RoomNotFoundException();
    }
}
