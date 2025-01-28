package com.example.hotelApp.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hotelApp.reservation.model.Reservation;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
}
