package com.inventory.service;

public class ProductService {

    public void addProduct(String name, double price, int quantity){

        if(name == null || name.isEmpty()){
            System.out.println("Invalid product name");
            return;
        }

        if(price <= 0){
            System.out.println("Invalid price");
            return;
        }

        if(quantity < 0){
            System.out.println("Invalid quantity");
            return;
        }

        System.out.println("Product added successfully: " + name);
        System.out.println("Price: " + price + ", Quantity: " + quantity);
    }
}
