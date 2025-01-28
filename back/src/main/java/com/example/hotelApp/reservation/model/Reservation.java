package com.example.hotelApp.reservation.model;

import com.example.hotelApp.admin.model.Admin;
import com.example.hotelApp.room.model.Room;
import com.example.hotelApp.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "Reservation")
@Getter
@Setter
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservationId")
    private Integer reservationId;

//    @ManyToOne
    @Column(name = "userId", nullable = false) // Foreign key to "User" table
    private Integer userId;

//    @ManyToOne
    @Column(name = "adminId") // Foreign key to "Admin" table
    private Integer adminId;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    @Column(name = "totalCost", nullable = false)
    private Double totalCost;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToMany
    @JoinTable(
            name = "Reservation_Room",
            joinColumns = @JoinColumn(name = "reservationId"),
            inverseJoinColumns = @JoinColumn(name = "roomId")
    )
    private Set<Room> rooms; // Many-to-Many relationship with "Room" entity

}
//    @JoinTable(
//            name = "Reservation_Room", // Join table name
//            joinColumns = @JoinColumn(name = "reservationId"), // Foreign key to "Reservation"
//            inverseJoinColumns = @JoinColumn(name = "roomId") // Foreign key to "Room"
//    )
