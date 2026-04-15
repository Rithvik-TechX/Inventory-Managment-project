package com.inventory.service.validation;

import com.inventory.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

    public void validate(Product product) {

        if(product.getName() == null || product.getName().isEmpty()) {
            throw new RuntimeException("Product name required");
        }

        if(product.getPrice() <= 0) {
            throw new RuntimeException("Invalid price");
        }

        if(product.getStock() < 0) {
            throw new RuntimeException("Invalid stock");
        }
    }
}
