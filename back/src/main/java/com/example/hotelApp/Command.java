package com.example.hotelApp;

import org.springframework.http.ResponseEntity;
public interface Command<I, O> {
    public ResponseEntity<O> execute(I input);
}
