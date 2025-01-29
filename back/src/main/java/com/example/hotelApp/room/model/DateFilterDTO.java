package com.example.hotelApp.room.model;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@Getter
public class DateFilterDTO {
    private Integer requiredGuests;
    private LocalDate startDate;
    private LocalDate endDate;
}
