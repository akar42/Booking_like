package com.example.hotelApp.room.services;

import com.example.hotelApp.Query;
import com.example.hotelApp.room.RoomRepository;
import com.example.hotelApp.room.model.DateFilterDTO;
import com.example.hotelApp.room.model.Room;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GetAvailableRoomsInTimePeriodService implements Query<DateFilterDTO, List<Room>> {

    private final RoomRepository roomRepository;

    @Override
    public ResponseEntity<List<Room>> execute(DateFilterDTO input) {
        List<Room> rooms = roomRepository.findAll();
        List<Room> filteredRooms;

        if (input.getRequiredGuests() != null && (input.getStartDate() == null || input.getEndDate() == null)) {
            // Only filter on guests
            filteredRooms = rooms.stream()
                    .filter(room -> room.getGuestNumber() >= input.getRequiredGuests())
                    .toList();

            return ResponseEntity.status(HttpStatus.OK).body(filteredRooms);
        }

        filteredRooms = rooms.stream()
                .filter(room -> room.getReservations().stream()
                        .allMatch(reservation -> reservation.getEndDate().isBefore(input.getStartDate())
                                || reservation.getStartDate().isAfter(input.getEndDate())))
                .toList();

        if (input.getRequiredGuests() != null) {
            filteredRooms = filteredRooms.stream()
                    .filter(room -> room.getGuestNumber() >= input.getRequiredGuests())
                    .toList();
        }

        return ResponseEntity.status(HttpStatus.OK).body(filteredRooms);
    }
}
