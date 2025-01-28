package com.example.hotelApp.reservation.services;

import com.example.hotelApp.Command;
import com.example.hotelApp.exceptions.ReservationNotFoundException;
import com.example.hotelApp.reservation.ReservationRepository;
import com.example.hotelApp.reservation.model.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DeleteReservationService implements Command<Integer, Void> {

    private final ReservationRepository reservationRepository;

    @Override
    public ResponseEntity<Void> execute(Integer id) {
        Optional<Reservation> reservationToDelete = reservationRepository.findById(id);

        if (reservationToDelete.isPresent()) {
            reservationRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        throw new ReservationNotFoundException();
    }
}
