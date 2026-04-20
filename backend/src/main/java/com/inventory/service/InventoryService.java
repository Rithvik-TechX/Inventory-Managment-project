package com.inventory.service;

import com.inventory.entity.Product;
import com.inventory.repository.ProductRepository;
import com.inventory.validation.InventoryValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InventoryService {

    private final ProductRepository productRepository;
    private final InventoryValidator validator;
    private final ReportService reportService;

    public InventoryService(ProductRepository productRepository,
                            InventoryValidator validator,
                            ReportService reportService) {
        this.productRepository = productRepository;
        this.validator = validator;
        this.reportService = reportService;
    }

    public void reduceStock(Long id, int quantity) {
        System.out.println("[Inventory] Reducing stock | ID: " + id + ", Qty: " + quantity);

        validator.validateQuantity(quantity);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        validator.validateStock(product.getQuantity(), quantity);

        int newQuantity = product.getQuantity() - quantity;
        product.setQuantity(newQuantity);
        productRepository.save(product);

        System.out.println("[Inventory] Stock reduced successfully | New Quantity: " + newQuantity);

        reportService.checkAndSendLowStockAlert();
    }

    public void increaseStock(Long id, int quantity) {
        System.out.println("[Inventory] Increasing stock | ID: " + id + ", Qty: " + quantity);

        validator.validateQuantity(quantity);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        int newQuantity = product.getQuantity() + quantity;
        product.setQuantity(newQuantity);
        productRepository.save(product);

        System.out.println("[Inventory] Stock increased successfully | New Quantity: " + newQuantity);
    }

    @Transactional(readOnly = true)
    public int getStock(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return product.getQuantity();
    }
}
