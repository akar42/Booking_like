package com.example.hotelApp.reservation.services;

import com.example.hotelApp.Query;
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
public class GetReservationById implements Query<Integer, Reservation> {

    private final ReservationRepository reservationRepository;

    @Override
    public ResponseEntity<Reservation> execute(Integer id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);

        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            return ResponseEntity.status(HttpStatus.OK).body(reservation);
        }

        throw new ReservationNotFoundException();
    }
}
