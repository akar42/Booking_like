package com.example.hotelApp.reservation;

import com.example.hotelApp.reservation.model.Reservation;
import com.example.hotelApp.reservation.model.UpdateReservationCommand;
import com.example.hotelApp.reservation.services.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ReservationController {
    private final CreateReservationService createReservationService;
    private final GetReservationById getReservationById;
    private final GetAllReservationsService getAllReservationsService;
    private final UpdateReservationService updateReservationService;
    private final DeleteReservationService deleteReservationService;
    private final GetAdminReservationsService getAdminReservationsService;

    @PostMapping("/reservation")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        return createReservationService.execute(reservation);
    }

    @GetMapping("/reservation/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Integer id) {
        return getReservationById.execute(id);
    }

    @GetMapping("/reservations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return getAllReservationsService.execute(null);
    }

    @GetMapping("/reservations/admin/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getAdminReservations(@PathVariable Integer adminId) {
        return getAdminReservationsService.execute(adminId);
    }

    @PutMapping("/reservation/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Integer id, @RequestBody Reservation reservation) {
        return updateReservationService.execute(new UpdateReservationCommand(id, reservation));
    }

    @DeleteMapping("/reservation/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
        return deleteReservationService.execute(id);
    }
}
