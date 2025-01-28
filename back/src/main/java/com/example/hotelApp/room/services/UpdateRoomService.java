package com.example.hotelApp.room.services;

import com.example.hotelApp.Command;
import com.example.hotelApp.exceptions.RoomNotFoundException;
import com.example.hotelApp.room.RoomRepository;
import com.example.hotelApp.room.model.Room;
import com.example.hotelApp.room.model.UpdateRoomCommand;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateRoomService implements Command<UpdateRoomCommand, Room> {

    private final RoomRepository roomRepository;

    @Override
    public ResponseEntity<Room> execute(UpdateRoomCommand input) {
        Optional<Room> roomOptional = roomRepository.findById(input.getId());

        if (roomOptional.isPresent()) {
            Room room = input.getRoom();
            room.setRoomId(input.getId());

            roomRepository.save(room);

            return ResponseEntity.status(HttpStatus.OK).body(room);
        }

        throw new RoomNotFoundException();
    }
}
