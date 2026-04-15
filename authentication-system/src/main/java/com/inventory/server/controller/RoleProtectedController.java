package com.inventory.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RoleProtectedController {

    @GetMapping("/admin/dashboard")
    public ResponseEntity<Map<String, String>> adminDashboard() {
        return ResponseEntity.ok(Map.of("message", "Admin access granted"));
    }

    @GetMapping("/supplier/dashboard")
    public ResponseEntity<Map<String, String>> supplierDashboard() {
        return ResponseEntity.ok(Map.of("message", "Supplier access granted"));
    }

    @GetMapping("/customer/dashboard")
    public ResponseEntity<Map<String, String>> customerDashboard() {
        return ResponseEntity.ok(Map.of("message", "Customer access granted"));
    }

    @GetMapping("/public/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
