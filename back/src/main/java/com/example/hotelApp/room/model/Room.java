package com.example.hotelApp.room.model;

import com.example.hotelApp.reservation.model.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Room")
@Getter
@Setter
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomId")
    private Integer roomId;

    @Column(name = "roomNumber", unique = true, nullable = false)
    private String roomNumber;

    @Column(name = "roomType")
    private String roomType;

    @Column(name = "price")
    private Double price;

    @Column(name = "guestNumber")
    private Integer guestNumber;

    @Column(name = "floorNumber")
    private Integer floorNumber;

    @Column(name = "facilities")
    private String facilities;

    @ManyToMany(mappedBy = "rooms")
    @JsonIgnore
    private List<Reservation> reservations;
}

