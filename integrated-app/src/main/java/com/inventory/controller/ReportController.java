package com.inventory.controller;

import com.inventory.dto.DashboardDTO;
import com.inventory.dto.ProductDTO;
import com.inventory.entity.Product;
import com.inventory.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardDTO> getDashboardSummary() {
        return ResponseEntity.ok(reportService.getDashboardSummary());
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Map<String, Object>>> getLowStockReport() {
        List<Product> lowStockProducts = reportService.getLowStockProducts();
        List<Map<String, Object>> report = lowStockProducts.stream().map(p -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", p.getId());
            map.put("name", p.getName());
            map.put("sku", p.getSku());
            map.put("quantity", p.getQuantity());
            map.put("reorderLevel", p.getReorderLevel());
            map.put("shortage", p.getReorderLevel() - p.getQuantity());
            map.put("category", p.getCategory() != null ? p.getCategory().getName() : null);
            map.put("supplier", p.getSupplier() != null ? p.getSupplier().getName() : null);
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(report);
    }

    @GetMapping("/check-alerts")
    public ResponseEntity<Map<String, String>> checkAlerts() {
        reportService.checkAndSendLowStockAlert();
        return ResponseEntity.ok(Map.of("message", "Alert check completed"));
    }
}
