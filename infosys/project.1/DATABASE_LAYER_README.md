# 📦 Database Layer — Inventory Management & Reporting System

## Overview
This is the fully implemented **Database Layer** of the 4-tier Inventory Management system built with **Spring Boot 3 + JPA/Hibernate + MySQL**.

---

## 📁 Package Structure

```
com.inventory.database_system/
├── entity/                  ← JPA Entities (mapped to DB tables)
│   ├── User.java
│   ├── Category.java
│   ├── Supplier.java
│   ├── Product.java
│   └── Transaction.java
│
├── repository/              ← Spring Data JPA Repositories (DB queries)
│   ├── UserRepository.java
│   ├── CategoryRepository.java
│   ├── SupplierRepository.java
│   ├── ProductRepository.java
│   └── TransactionRepository.java
│
├── dto/                     ← Data Transfer Objects (API request/response)
│   ├── ProductDTO.java
│   ├── CategoryDTO.java
│   ├── SupplierDTO.java
│   ├── TransactionDTO.java
│   ├── UserDTO.java
│   ├── ReportDTO.java
│   └── ApiResponse.java
│
├── mapper/                  ← Entity ↔ DTO converters
│   ├── ProductMapper.java
│   ├── CategoryMapper.java
│   ├── SupplierMapper.java
│   ├── TransactionMapper.java
│   └── UserMapper.java
│
├── service/                 ← Business-facing DB operations
│   ├── ProductService.java
│   ├── TransactionService.java
│   └── ReportService.java
│
├── controller/              ← REST API endpoints (for other layers)
│   ├── ProductController.java
│   ├── TransactionController.java
│   └── ReportController.java
│
├── exception/               ← Exception handling
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   ├── InsufficientStockException.java
│   └── GlobalExceptionHandler.java
│
├── config/
│   └── DatabaseConfig.java  ← HikariCP pool config
│
└── util/
    ├── SKUGenerator.java
    └── ReferenceIdGenerator.java
```

---

## 🗄️ Database Schema (5 Tables)

| Table          | Description                                      |
|----------------|--------------------------------------------------|
| `users`        | System users with roles (ADMIN/MANAGER/STAFF)    |
| `categories`   | Product categories                               |
| `suppliers`    | Supplier information                             |
| `products`     | Inventory products with stock levels             |
| `transactions` | All stock movements (SALE/PURCHASE/RETURN/ADJUST)|

---

## 🚀 Setup & Run

### 1. Create the Database
```sql
CREATE DATABASE inventory_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
Or run: `src/main/resources/schema-setup.sql`

### 2. Configure Credentials
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run the Application
```bash
./mvnw spring-boot:run
```

### 4. Load Sample Data (Optional)
Run `src/main/resources/data.sql` after tables are created.

---

## 🌐 REST API Endpoints

### Products
| Method | Endpoint                        | Description             |
|--------|---------------------------------|-------------------------|
| GET    | /api/products                   | All active products     |
| GET    | /api/products/{id}              | Product by ID           |
| GET    | /api/products/sku/{sku}         | Product by SKU          |
| GET    | /api/products/low-stock         | Low stock alerts        |
| GET    | /api/products/out-of-stock      | Out of stock items      |
| GET    | /api/products/search?keyword=   | Search products         |
| GET    | /api/products/inventory-value   | Total inventory value   |
| POST   | /api/products                   | Create product          |
| PUT    | /api/products/{id}              | Update product          |
| DELETE | /api/products/{id}              | Delete product          |

### Transactions
| Method | Endpoint                        | Description              |
|--------|---------------------------------|--------------------------|
| GET    | /api/transactions               | All transactions         |
| GET    | /api/transactions/{id}          | Transaction by ID        |
| GET    | /api/transactions/recent?limit= | Recent transactions      |
| GET    | /api/transactions/date-range    | Filter by date range     |
| GET    | /api/transactions/revenue       | Revenue in date range    |
| POST   | /api/transactions               | Create transaction       |
| PATCH  | /api/transactions/{id}/status   | Update status            |

### Reports
| Method | Endpoint                        | Description              |
|--------|---------------------------------|--------------------------|
| GET    | /api/reports/summary            | Full inventory report    |

---

## ✅ Key Features Implemented

- **5 JPA Entities** with full relationships, validation, and lifecycle hooks
- **5 Repositories** with 30+ custom JPQL/native queries
- **Complete DTOs** — no entity exposure in API responses
- **Mapper classes** — safe entity↔DTO conversion
- **Stock management** — auto adjusts quantity on SALE/PURCHASE
- **Low stock alerts** — via reorderLevel threshold
- **Reporting** — revenue, cost, top products, daily sales, inventory value
- **Global exception handler** — structured error responses
- **HikariCP** — production-grade connection pooling
- **Bean Validation** — field-level input validation
- **Audit fields** — createdAt/updatedAt via @PrePersist/@PreUpdate
- **Database indexes** — on all FK, search, and filter columns
- **Sample seed data** — 5 categories, 4 suppliers, 4 users, 10 products, 10 transactions

---

## 🔗 Integration Points for Other Layers

| Layer               | Integrates via                         |
|---------------------|----------------------------------------|
| Business Logic      | Service classes (ProductService, etc.) |
| Authentication      | UserRepository.findByEmail()           |
| Presentation        | REST Controllers + DTOs                |
