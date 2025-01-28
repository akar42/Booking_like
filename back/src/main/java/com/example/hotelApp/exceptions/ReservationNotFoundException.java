package com.example.hotelApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException() {
        super(ErrorMessages.RESERVATION_NOT_FOUND.getMessage());
    }
}
