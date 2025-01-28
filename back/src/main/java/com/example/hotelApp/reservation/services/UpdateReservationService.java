package com.example.hotelApp.reservation.services;

import com.example.hotelApp.Command;
import com.example.hotelApp.exceptions.ReservationNotFoundException;
import com.example.hotelApp.reservation.ReservationRepository;
import com.example.hotelApp.reservation.model.Reservation;
import com.example.hotelApp.reservation.model.UpdateReservationCommand;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateReservationService implements Command<UpdateReservationCommand, Reservation> {

    private final ReservationRepository reservationRepository;

    @Override
    public ResponseEntity<Reservation> execute(UpdateReservationCommand input) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(input.getId());

        if (reservationOptional.isPresent()) {
            Reservation reservation = input.getReservation();
            reservation.setReservationId(input.getId());

            reservationRepository.save(reservation);

            return ResponseEntity.status(HttpStatus.OK).body(reservation);
        }

        throw new ReservationNotFoundException();
    }
}
