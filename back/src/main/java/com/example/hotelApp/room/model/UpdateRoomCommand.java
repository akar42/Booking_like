package com.example.hotelApp.room.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateRoomCommand {
    private Integer id;
    private Room room;
}
