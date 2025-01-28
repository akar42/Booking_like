package com.example.hotelApp.user.model;

import com.example.hotelApp.reservation.model.Reservation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "User")
@Getter
@Setter
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Integer userId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "dateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "telephoneNumber")
    private String telephoneNumber;

    @Column(name = "cardNumber")
    private Long cardNumber;

    @Column(name = "cardExpDate")
    private LocalDate cardExpDate;

    @Column(name = "documentNumber")
    private String documentNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private List<Reservation> reservations;

    // Getters and Setters
}
