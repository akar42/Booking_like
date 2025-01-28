package com.example.hotelApp.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateAdminCommand {
    private Integer id;
    private Admin admin;
}
