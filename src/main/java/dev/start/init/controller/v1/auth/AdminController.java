package dev.start.init.controller.v1.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // Define your admin-specific endpoints here

    @GetMapping("/stats")
    public ResponseEntity<String> getAdminStats() {
        // Example admin endpoint
        return ResponseEntity.ok("Admin statistics");
    }
}

