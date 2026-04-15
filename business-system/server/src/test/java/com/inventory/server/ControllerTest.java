package com.inventory.server;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.Report.InventoryReportService;
import com.inventory.controller.Controller;
import com.inventory.database_system.entity.Category;
import com.inventory.database_system.entity.Product;
import com.inventory.database_system.entity.Supplier;
import com.inventory.service.InventoryService;
import com.inventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Controller.class)
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private InventoryService inventoryService;

    @MockBean
    private InventoryReportService reportService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        Category cat = new Category();
        cat.setId(1L);
        cat.setName("Electronics");

        Supplier sup = new Supplier();
        sup.setId(1L);
        sup.setName("TechSupplier");

        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Wireless Mouse");
        sampleProduct.setSku("WM-001");
        sampleProduct.setPrice(new BigDecimal("29.99"));
        sampleProduct.setQuantity(100);
        sampleProduct.setReorderLevel(10);
        sampleProduct.setActive(true);
        sampleProduct.setCategory(cat);
        sampleProduct.setSupplier(sup);
    }

    // ── POST /api/product ─────────────────────────────────────────────────────

    @Test
    void addProduct_validBody_returns201() throws Exception {
        doNothing().when(productService).addProduct(any(Product.class));

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProduct)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Product added"));

        verify(productService, times(1)).addProduct(any(Product.class));
    }

    @Test
    void addProduct_serviceThrows_returns500() throws Exception {
        doThrow(new RuntimeException("Invalid price"))
                .when(productService).addProduct(any(Product.class));

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProduct)))
                .andExpect(status().isInternalServerError());
    }

    // ── GET /api/products ─────────────────────────────────────────────────────

    @Test
    void getAllProducts_returns200WithList() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(sampleProduct));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Wireless Mouse"))
                .andExpect(jsonPath("$[0].sku").value("WM-001"))
                .andExpect(jsonPath("$[0].quantity").value(100));
    }

    @Test
    void getAllProducts_emptyList_returns200WithEmptyArray() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ── GET /api/products/active ──────────────────────────────────────────────

    @Test
    void getActiveProducts_returns200WithActiveOnly() throws Exception {
        when(productService.getActiveProducts()).thenReturn(List.of(sampleProduct));

        mockMvc.perform(get("/api/products/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].active").value(true));
    }

    // ── GET /api/product/{id} ─────────────────────────────────────────────────

    @Test
    void getProductById_existingId_returns200WithProduct() throws Exception {
        when(productService.getProduct(1L)).thenReturn(sampleProduct);

        mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Wireless Mouse"))
                .andExpect(jsonPath("$.price").value(29.99));
    }

    @Test
    void getProductById_notFound_returns500() throws Exception {
        when(productService.getProduct(99L))
                .thenThrow(new RuntimeException("Product not found with id: 99"));

        mockMvc.perform(get("/api/product/99"))
                .andExpect(status().isInternalServerError());
    }

    // ── GET /api/products/search ──────────────────────────────────────────────

    @Test
    void searchProducts_withName_returns200WithResults() throws Exception {
        when(productService.searchByName("mouse")).thenReturn(List.of(sampleProduct));

        mockMvc.perform(get("/api/products/search").param("name", "mouse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Wireless Mouse"));
    }

    // ── GET /api/products/category/{id} ──────────────────────────────────────

    @Test
    void getProductsByCategory_returns200() throws Exception {
        when(productService.getProductsByCategory(1L)).thenReturn(List.of(sampleProduct));

        mockMvc.perform(get("/api/products/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category.id").value(1));
    }

    // ── GET /api/products/supplier/{id} ──────────────────────────────────────

    @Test
    void getProductsBySupplier_returns200() throws Exception {
        when(productService.getProductsBySupplier(1L)).thenReturn(List.of(sampleProduct));

        mockMvc.perform(get("/api/products/supplier/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].supplier.id").value(1));
    }

    // ── DELETE /api/product/{id} ──────────────────────────────────────────────

    @Test
    void deleteProduct_existingId_returns200() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted"));

        verify(productService, times(1)).deleteProduct(1L);
    }

    // ── PATCH /api/product/{id}/deactivate ────────────────────────────────────

    @Test
    void deactivateProduct_existingId_returns200() throws Exception {
        doNothing().when(productService).deactivateProduct(1L);

        mockMvc.perform(patch("/api/product/1/deactivate"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deactivated"));

        verify(productService, times(1)).deactivateProduct(1L);
    }

    // ── POST /api/reduce/{id}/{qty} ───────────────────────────────────────────

    @Test
    void reduceStock_validRequest_returns200() throws Exception {
        doNothing().when(inventoryService).reduceStock(1L, 10);

        mockMvc.perform(post("/api/reduce/1/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock reduced"));

        verify(inventoryService, times(1)).reduceStock(1L, 10);
    }

    @Test
    void reduceStock_insufficientStock_returns500() throws Exception {
        doThrow(new RuntimeException("Insufficient stock. Available: 5, Requested: 100"))
                .when(inventoryService).reduceStock(1L, 100);

        mockMvc.perform(post("/api/reduce/1/100"))
                .andExpect(status().isInternalServerError());
    }

    // ── POST /api/increase/{id}/{qty} ─────────────────────────────────────────

    @Test
    void increaseStock_validRequest_returns200() throws Exception {
        doNothing().when(inventoryService).increaseStock(1L, 20);

        mockMvc.perform(post("/api/increase/1/20"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock increased"));

        verify(inventoryService, times(1)).increaseStock(1L, 20);
    }

    // ── GET /api/report ───────────────────────────────────────────────────────

    @Test
    void generateReport_returns200WithMessage() throws Exception {
        doNothing().when(reportService).checkAndSendLowStockAlert();

        mockMvc.perform(get("/api/report"))
                .andExpect(status().isOk())
                .andExpect(content().string("Report generated - check your email"));

        verify(reportService, times(1)).checkAndSendLowStockAlert();
    }
}