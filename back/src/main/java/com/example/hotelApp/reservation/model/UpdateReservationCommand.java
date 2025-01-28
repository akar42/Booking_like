package com.example.hotelApp.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateReservationCommand {
    private Integer id;
    private Reservation reservation;
}
