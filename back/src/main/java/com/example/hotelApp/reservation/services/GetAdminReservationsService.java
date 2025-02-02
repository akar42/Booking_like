package com.example.hotelApp.reservation.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.hotelApp.Query;
import com.example.hotelApp.reservation.ReservationRepository;
import com.example.hotelApp.reservation.model.Reservation;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GetAdminReservationsService implements Query<Integer, List<Reservation>> {

	private final ReservationRepository reservationRepository;

	@Override
	public ResponseEntity<List<Reservation>> execute(Integer adminId) {
		List<Reservation> reservations = reservationRepository.findAll();

		reservations = reservations.stream()
				.filter(reservation -> reservation.getAdminId() == adminId)
				.toList();

		return ResponseEntity.status(HttpStatus.OK).body(reservations);
	}	
}
