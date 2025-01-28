package com.example.hotelApp.reservation.services;

import com.example.hotelApp.Command;
import com.example.hotelApp.reservation.ReservationRepository;
import com.example.hotelApp.reservation.model.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateReservationService implements Command<Reservation, Reservation> {

    private final ReservationRepository reservationRepository;
    @Override
    public ResponseEntity<Reservation> execute(Reservation input) {
        Reservation createdReservation = reservationRepository.save(input);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }
}
