package com.inventory.controller;

import com.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/reduce/{id}/{qty}")
    public ResponseEntity<Map<String, String>> reduceStock(@PathVariable Long id,
                                                           @PathVariable int qty) {
        inventoryService.reduceStock(id, qty);
        return ResponseEntity.ok(Map.of("message", "Stock reduced", "productId", id.toString(), "reduced", String.valueOf(qty)));
    }

    @PostMapping("/increase/{id}/{qty}")
    public ResponseEntity<Map<String, String>> increaseStock(@PathVariable Long id,
                                                              @PathVariable int qty) {
        inventoryService.increaseStock(id, qty);
        return ResponseEntity.ok(Map.of("message", "Stock increased", "productId", id.toString(), "increased", String.valueOf(qty)));
    }

    @GetMapping("/stock/{id}")
    public ResponseEntity<Map<String, Object>> getStock(@PathVariable Long id) {
        int stock = inventoryService.getStock(id);
        return ResponseEntity.ok(Map.of("productId", id, "stock", stock));
    }
}
