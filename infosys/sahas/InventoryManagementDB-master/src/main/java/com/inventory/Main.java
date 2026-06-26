package com.inventory;
import com.inventory.config.DBConnection;
import com.inventory.dao.impl.*;
import com.inventory.model.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("----------------------------");
        System.out.println("   INVENTORY MANAGEMENT - DATABASE LAYER  ");
        System.out.println("----------------------------");

        CategoryDAOImpl    categoryDAO    = new CategoryDAOImpl();
        SupplierDAOImpl    supplierDAO    = new SupplierDAOImpl();
        UserDAOImpl        userDAO        = new UserDAOImpl();
        ProductDAOImpl     productDAO     = new ProductDAOImpl();
        TransactionDAOImpl transactionDAO = new TransactionDAOImpl();

        // ══════════════════════════════════════════
        // 1. CATEGORY TESTS
        // ══════════════════════════════════════════
        System.out.println("-----------------------------");
        System.out.println("         CATEGORY TESTS       ");
        System.out.println("-----------------------------");

        System.out.println("Add Electronics : " + categoryDAO.addCategory(new Category("Electronics")));
        System.out.println("Add Furniture   : " + categoryDAO.addCategory(new Category("Furniture")));
        System.out.println("Add Stationery  : " + categoryDAO.addCategory(new Category("Stationery")));

        System.out.println("\n--- All Categories ---");
        categoryDAO.getAllCategories().forEach(System.out::println);

        System.out.println("\n--- Get Category by ID (1) ---");
        System.out.println(categoryDAO.getCategoryById(1));

        System.out.println("\n--- Update Category (id=3) to 'Office Supplies' ---");
        Category updateCat = new Category("Office Supplies");
        updateCat.setId(3);
        System.out.println("Updated : " + categoryDAO.updateCategory(updateCat));

        System.out.println("\n--- All Categories After Update ---");
        categoryDAO.getAllCategories().forEach(System.out::println);

        // ══════════════════════════════════════════
        // 2. SUPPLIER TESTS
        // ══════════════════════════════════════════
        System.out.println("\n----------------------------");
        System.out.println("         SUPPLIER TESTS       ");
        System.out.println("----------------------------");

        System.out.println("Add TechMart  : " + supplierDAO.addSupplier(
                new Supplier("TechMart Pvt Ltd",  "techmart@gmail.com",  "Hyderabad")));
        System.out.println("Add FurniMart : " + supplierDAO.addSupplier(
                new Supplier("FurniMart Co",       "furnimart@gmail.com", "Chennai")));
        System.out.println("Add PaperHub  : " + supplierDAO.addSupplier(
                new Supplier("PaperHub India",     "paperhub@gmail.com",  "Bangalore")));

        System.out.println("\n--- All Suppliers ---");
        supplierDAO.getAllSuppliers().forEach(System.out::println);

        System.out.println("\n--- Get Supplier by ID (2) ---");
        System.out.println(supplierDAO.getSupplierById(2));

        System.out.println("\n--- Update Supplier (id=3) ---");
        Supplier updateSup = new Supplier("PaperHub Updated", "newemail@gmail.com", "Mumbai");
        updateSup.setId(3);
        System.out.println("Updated : " + supplierDAO.updateSupplier(updateSup));

        System.out.println("\n--- All Suppliers After Update ---");
        supplierDAO.getAllSuppliers().forEach(System.out::println);

        // ══════════════════════════════════════════
        // 3. USER TESTS
        // ══════════════════════════════════════════
        System.out.println("\n----------------------------");
        System.out.println("           USER TESTS         ");
        System.out.println("----------------------------");

        System.out.println("Add Admin   : " + userDAO.addUser(
                new User("Priya Sharma", "priya@inv.com", "priya@123", "9876501234", "ADMIN")));
        System.out.println("Add Manager : " + userDAO.addUser(
                new User("Arjun Mehta",  "arjun@inv.com", "arjun@456", "9845012345", "MANAGER")));
        System.out.println("Add Staff   : " + userDAO.addUser(
                new User("Sneha Reddy",  "sneha@inv.com", "sneha@789", "9812034567", "STAFF")));

        System.out.println("--- All Users ---");
        userDAO.getAllUsers().forEach(System.out::println);

        System.out.println("--- Get User by Email ---");
        System.out.println(userDAO.getUserByEmail("arjun@inv.com"));

        System.out.println("\n--- Get User by ID (1) ---");
        System.out.println(userDAO.getUserById(1));

        // Fetch real IDs from DB
        User priya = userDAO.getUserByEmail("priya@inv.com");
        User arjun = userDAO.getUserByEmail("arjun@inv.com");
        User sneha = userDAO.getUserByEmail("sneha@inv.com");

        System.out.println("\n--- Update User (Sneha role → MANAGER) ---");
        sneha.setRole("MANAGER");
        System.out.println("Updated : " + userDAO.updateUser(sneha));
        System.out.println("After   : " + userDAO.getUserById(sneha.getId()));

        // ══════════════════════════════════════════
        // 4. PRODUCT TESTS
        // ══════════════════════════════════════════
        System.out.println("\n----------------------------");
        System.out.println("          PRODUCT TESTS       ");
        System.out.println("----------------------------");

        System.out.println("Add Laptop      : " + productDAO.addProduct(
                new Product("Laptop",        "SKU-L01", 55000.00, 20, "Gaming Laptop",    null, null, 1)));
        System.out.println("Add Phone       : " + productDAO.addProduct(
                new Product("Smartphone",    "SKU-P01", 25000.00, 15, "Android Phone",    null, null, 1)));
        System.out.println("Add Chair       : " + productDAO.addProduct(
                new Product("Office Chair",  "SKU-C01",  8999.00,  4, "Ergonomic Chair",  null, null, 2)));
        System.out.println("Add Notebook    : " + productDAO.addProduct(
                new Product("Notebook Pack", "SKU-N01",   199.00, 100, "Pack of 5",       null, null, 3)));
        System.out.println("Add Pen Pack    : " + productDAO.addProduct(
                new Product("Pen Pack",      "SKU-PP01",   49.00,  3, "Pack of 10 pens",  null, null, 3)));

        System.out.println("\n--- All Products ---");
        productDAO.getAllProducts().forEach(System.out::println);

        System.out.println("\n--- Get Product by ID (1) ---");
        System.out.println(productDAO.getProductById(1));

        System.out.println("\n--- Get Product by SKU (SKU-P01) ---");
        System.out.println(productDAO.getProductBySku("SKU-P01"));

        System.out.println("\n--- Products in Electronics (category_id=1) ---");
        productDAO.getProductsByCategory(1).forEach(System.out::println);

        System.out.println("\n--- ⚠️ Low Stock Alert (qty <= 5) ---");
        productDAO.getLowStockProducts(5).forEach(p ->
                System.out.println("⚠️  LOW STOCK → " + p));

        System.out.println("\n--- Restock Office Chair (id=3) +50 ---");
        System.out.println("Stock Updated : " + productDAO.updateStock(3, 54));
        System.out.println("After Restock : " + productDAO.getProductById(3));

        System.out.println("\n--- Update Product (Laptop price change) ---");
        Product laptop = productDAO.getProductBySku("SKU-L01");
        laptop.setPrice(49999.00);
        laptop.setStockQuantity(25);
        System.out.println("Updated : " + productDAO.updateProduct(laptop));
        System.out.println("After   : " + productDAO.getProductById(laptop.getId()));

        // ══════════════════════════════════════════
        // 5. TRANSACTION TESTS
        // ══════════════════════════════════════════
        System.out.println("----------------------------");
        System.out.println("       TRANSACTION TESTS      ");
        System.out.println("----------------------------");

        // Fetch real product IDs
        Product laptopP   = productDAO.getProductBySku("SKU-L01");
        Product phoneP    = productDAO.getProductBySku("SKU-P01");
        Product notebookP = productDAO.getProductBySku("SKU-N01");

        System.out.println("Add PURCHASE txn : " + transactionDAO.addTransaction(
                new Transaction(10, 550000.00, "PURCHASE", "COMPLETED",
                        "Bulk laptop purchase", "Urgent order",
                        laptopP.getId(), priya.getId(), 1)));

        System.out.println("Add SALE txn     : " + transactionDAO.addTransaction(
                new Transaction(3, 75000.00, "SALE", "COMPLETED",
                        "Laptop sale to customer", "Online order",
                        laptopP.getId(), arjun.getId(), 1)));

        System.out.println("Add PURCHASE txn : " + transactionDAO.addTransaction(
                new Transaction(20, 500000.00, "PURCHASE", "COMPLETED",
                        "Phone stock purchase", "Festival season",
                        phoneP.getId(), priya.getId(), 1)));

        System.out.println("Add RETURN txn   : " + transactionDAO.addTransaction(
                new Transaction(2, 398.00, "RETURN", "PENDING",
                        "Notebook return", "Defective pack",
                        notebookP.getId(), sneha.getId(), 3)));

        System.out.println("Add SALE txn     : " + transactionDAO.addTransaction(
                new Transaction(5, 125000.00, "SALE", "PENDING",
                        "Phone sale", "Bulk order",
                        phoneP.getId(), arjun.getId(), 1)));

        System.out.println("\n--- All Transactions ---");
        transactionDAO.getAllTransactions().forEach(System.out::println);

        System.out.println("\n--- Transactions by Type: SALE ---");
        transactionDAO.getTransactionsByType("SALE").forEach(System.out::println);

        System.out.println("\n--- Transactions by Type: PURCHASE ---");
        transactionDAO.getTransactionsByType("PURCHASE").forEach(System.out::println);

        System.out.println("\n--- Transactions by User (Arjun) ---");
        transactionDAO.getTransactionsByUser(arjun.getId()).forEach(System.out::println);

        System.out.println("\n--- Transactions by Product (Laptop) ---");
        transactionDAO.getTransactionsByProduct(laptopP.getId()).forEach(System.out::println);

        System.out.println("\n--- Update RETURN txn Status → COMPLETED ---");
        System.out.println("Updated : " + transactionDAO.updateTransactionStatus(4, "COMPLETED"));
        System.out.println("After   : " + transactionDAO.getTransactionById(4));

        // ══════════════════════════════════════════
        // 6. DELETE TESTS
        // ══════════════════════════════════════════
        System.out.println("----------------------------");
        System.out.println("          DELETE TESTS        ");
        System.out.println("----------------------------");

        System.out.println("Delete Transaction (id=5) : " +
                transactionDAO.deleteTransaction(5));

        System.out.println("Delete User Sneha         : " +
                userDAO.deleteUser(sneha.getId()));

        System.out.println("Delete Supplier (id=3)    : " +
                supplierDAO.deleteSupplier(3));

        // Try delete category with products (should fail)
        System.out.println("Delete Category (id=1)    : " +
                categoryDAO.deleteCategory(1));

        System.out.println("\n--- Final Product List ---");
        productDAO.getAllProducts().forEach(System.out::println);

        System.out.println("\n--- Final Category List ---");
        categoryDAO.getAllCategories().forEach(System.out::println);

        System.out.println("\n--- Final User List ---");
        userDAO.getAllUsers().forEach(System.out::println);

        // Close connection
        System.out.println("----------------------------");
        System.out.println("Closing DB connection...");
        DBConnection.closeConnection();

        System.out.println("----------------------------");
        System.out.println("     ALL TESTS COMPLETED ✅     ");
        System.out.println("----------------------------");
    }
}