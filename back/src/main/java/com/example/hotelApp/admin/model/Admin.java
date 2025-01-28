package com.example.hotelApp.admin.model;


import com.example.hotelApp.reservation.model.Reservation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Admin")  // Matches your DB table name
@Getter
@Setter
@Data
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adminId")
    private Integer adminId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "jobPosition")
    private String jobPosition;

    // One Admin can have many Reservations
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "adminId")
    private List<Reservation> reservations;

    // Getters, setters, constructors omitted for brevity
    // ...
}


