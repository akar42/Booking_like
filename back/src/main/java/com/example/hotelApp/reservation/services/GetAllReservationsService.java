package com.example.hotelApp.reservation.services;

import com.example.hotelApp.Query;
import com.example.hotelApp.reservation.ReservationRepository;
import com.example.hotelApp.reservation.model.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllReservationsService implements Query<Void, List<Reservation>> {

    private final ReservationRepository reservationRepository;

    @Override
    public ResponseEntity<List<Reservation>> execute(Void input) {
        List<Reservation> reservations = reservationRepository.findAll();

        reservations = reservations.stream()
                .filter(reservation -> reservation.getAdminId() == null)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(reservations);
    }
}
