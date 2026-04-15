package com.inventory.controller;

import com.inventory.service.ProductService;
import com.inventory.service.InventoryService;
import com.inventory.service.ReportService;

public class Controller {

    ProductService productService = new ProductService();
    InventoryService inventoryService = new InventoryService();
    ReportService reportService = new ReportService();

    public void addProduct(String name, double price, int quantity){
        System.out.println("Controller: Adding product...");
        productService.addProduct(name, price, quantity);
    }

    public void updateStock(int productId, int quantity){
        System.out.println("Controller: Updating stock...");
        inventoryService.updateStock(productId, quantity);
    }

    public void generateReport(){
        System.out.println("Controller: Generating report...");
        reportService.generateInventoryReport();
    }
}
