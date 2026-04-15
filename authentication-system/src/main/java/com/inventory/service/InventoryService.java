package com.inventory.service;

public class InventoryService {

    public void updateStock(int productId, int quantity){

        if(productId <= 0){
            System.out.println("Invalid product ID");
            return;
        }

        if(quantity < 0){
            System.out.println("Invalid quantity");
            return;
        }

        System.out.println("Stock updated for product ID: " + productId);
        System.out.println("Updated quantity: " + quantity);
    }
}
