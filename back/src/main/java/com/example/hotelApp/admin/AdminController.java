package com.example.hotelApp.admin;

import com.example.hotelApp.admin.model.Admin;
import com.example.hotelApp.admin.model.UpdateAdminCommand;
import com.example.hotelApp.admin.services.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AdminController {
    private final CreateAdminService createAdminService;
    private final GetAdminByIdService getAdminByIdService;
    private final GetAllAdminsService getAllAdminsService;
    private final UpdateAdminService updateAdminService;
    private final DeleteAdminService deleteAdminService;

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Admin> createAdmin (@RequestBody Admin admin) {
        return createAdminService.execute(admin);
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Admin> getAdminById(@PathVariable Integer id) {
        return getAdminByIdService.execute(id);
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return getAllAdminsService.execute(null);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Integer id, @RequestBody Admin admin) {
        return updateAdminService.execute(new UpdateAdminCommand(id, admin));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Integer id) {
        return deleteAdminService.execute(id);
    }
}
