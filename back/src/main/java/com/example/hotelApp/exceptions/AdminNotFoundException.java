package com.example.hotelApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdminNotFoundException extends RuntimeException{

    public AdminNotFoundException() {
        super(ErrorMessages.ADMIN_NOT_FOUND.getMessage());
    }
}
