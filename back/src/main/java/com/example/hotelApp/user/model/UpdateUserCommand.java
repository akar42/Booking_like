package com.example.hotelApp.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserCommand<IdType> {
    private IdType id;
    private User user;
}
