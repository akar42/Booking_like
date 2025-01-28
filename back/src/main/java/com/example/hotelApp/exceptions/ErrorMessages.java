package com.example.hotelApp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {
    ADMIN_NOT_FOUND("Admin not found"),
    ROOM_NOT_FOUND("Room not found"),
    USER_NOT_FOUND("User not found"),
    RESERVATION_NOT_FOUND("Reservation not found");

    private final String message;
}
