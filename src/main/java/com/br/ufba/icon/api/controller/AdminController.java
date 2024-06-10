package com.br.ufba.icon.api.controller;

import com.br.ufba.icon.api.domain.SystemStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v2/admin")
public class AdminController {
    public final SystemStatus systemStatus;

    public AdminController(SystemStatus systemStatus) {
        this.systemStatus = systemStatus;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/toggle_system_status")
    public ResponseEntity<String> toggleSystemStatus() {
        boolean currentStatus = systemStatus.getStatus();
        systemStatus.setStatus(!currentStatus);
        return ResponseEntity.ok("System status changed to " + (currentStatus ? "inactive" : "active"));
    }
}
