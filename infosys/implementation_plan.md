# Database Layer — Inventory Management & Reporting System

Complete implementation of **Layer 4 (Database Layer)** for the Inventory Management and Reporting System.

## Current State

You currently have:
- **5 basic entities**: Category, Product, Supplier, Transaction, User
- **5 bare repositories**: Empty `JpaRepository` interfaces (no custom queries)
- **1 DataLoader**: Basic seed data
- **Config**: MySQL connection on port 8081

## What's Missing for a Complete Database Layer

| Area | Current | Needed |
|------|---------|--------|
| Entities | Basic fields, no validation | Proper `@Column` constraints, enums, audit fields, indexes |
| Repositories | Empty interfaces | Custom queries for search, filter, reporting, pagination |
| Services | None | Service classes to encapsulate all DB operations |
| DTOs | None | Request/Response DTOs for clean data transfer to upper layers |
| Enums | None | `TransactionType`, `TransactionStatus`, `UserRole` |
| Auditing | None | `@PrePersist` / `@PreUpdate` auto-timestamps |
| New Entities | — | `Warehouse`, `StockMovement` for warehouse-level tracking |
| Seed Data | Minimal (1 record each) | Realistic multi-record dataset |
| Config | Basic | Connection pooling, naming strategy, dialect |

---

## User Review Required

> [!IMPORTANT]
> **New Entities**: I'm proposing adding `Warehouse` and `StockMovement` entities for warehouse-level inventory tracking, which is essential for a real inventory management system. If you don't need these, let me know and I'll skip them.

> [!IMPORTANT]
> **Existing Database**: Since `ddl-auto=update`, all changes will be additive (new columns/tables). Existing data won't be lost, but you may need to manually drop old columns if names change. If you prefer a clean slate, I can switch to `create-drop` temporarily.

---

## Proposed Changes

### Phase 1: Enums

#### [NEW] enums/UserRole.java
```
ADMIN, MANAGER, STAFF
```

#### [NEW] enums/TransactionType.java
```
SALE, PURCHASE, RETURN, ADJUSTMENT
```

#### [NEW] enums/TransactionStatus.java
```
COMPLETED, PENDING, CANCELLED
```

---

### Phase 2: Enhanced Entities

#### [MODIFY] [Category.java](file:///c:/FUNNY/infosys/database-system/src/main/java/com/inventory/database_system/entity/Category.java)
- Add `description` field
- Add `@Column` constraints (name unique, not null)
- Add `createdAt` / `updatedAt` audit timestamps with `@PrePersist` / `@PreUpdate`

#### [MODIFY] [Product.java](file:///c:/FUNNY/infosys/database-system/src/main/java/com/inventory/database_system/entity/Product.java)
- Add `@Column` constraints (name not null, SKU unique)
- Add `active` boolean field
- Add `minStockLevel` (reorder threshold) and `maxStockLevel` fields
- Add `updatedAt` audit timestamp
- Add `@PrePersist` / `@PreUpdate` lifecycle callbacks
- Add `@Table` index on `sku` and `name`

#### [MODIFY] [Supplier.java](file:///c:/FUNNY/infosys/database-system/src/main/java/com/inventory/database_system/entity/Supplier.java)
- Add `email` field
- Add `createdAt` / `updatedAt` audit timestamps
- Add `@Column` constraints

#### [MODIFY] [Transaction.java](file:///c:/FUNNY/infosys/database-system/src/main/java/com/inventory/database_system/entity/Transaction.java)
- Use `@Enumerated(EnumType.STRING)` for `transactionType` and `status`
- Add `@PrePersist` / `@PreUpdate` lifecycle callbacks
- Add `@Table` indexes on `transactionType`, `status`, `createdAt`

#### [MODIFY] [User.java](file:///c:/FUNNY/infosys/database-system/src/main/java/com/inventory/database_system/entity/User.java)
- Use `@Enumerated(EnumType.STRING)` for `role`
- Add `active` boolean field
- Add `updatedAt` audit timestamp
- Add `@PrePersist` / `@PreUpdate` callbacks

#### [NEW] entity/Warehouse.java
- Fields: `id`, `name`, `location`, `capacity`, `active`, `createdAt`, `updatedAt`
- Relationship: `@OneToMany` → `StockMovement`

#### [NEW] entity/StockMovement.java
- Fields: `id`, `quantity`, `movementType` (IN/OUT/TRANSFER), `notes`, `createdAt`
- Relationships: `@ManyToOne` → `Product`, `Warehouse`, `User`
- Purpose: Track stock in/out per warehouse for reporting

---

### Phase 3: Enhanced Repositories (Custom Queries)

#### [MODIFY] [CategoryRepository.java](file:///c:/FUNNY/infosys/database-system/src/main/java/com/inventory/database_system/repository/CategoryRepository.java)
- `findByNameContainingIgnoreCase(String name)` — search
- `findByActiveTrue()` — active categories only

#### [MODIFY] [ProductRepository.java](file:///c:/FUNNY/infosys/database-system/src/main/java/com/inventory/database_system/repository/ProductRepository.java)
- `findByNameContainingIgnoreCase(String name)` — search
- `findByCategoryId(Long categoryId)` — filter by category
- `findBySupplierId(Long supplierId)` — filter by supplier
- `findByQuantityLessThanEqual(int threshold)` — low stock alert
- `findByActiveTrue()` — active products
- `@Query` for top-selling products (join with transactions)
- `@Query` for total inventory value

#### [MODIFY] [SupplierRepository.java](file:///c:/FUNNY/infosys/database-system/src/main/java/com/inventory/database_system/repository/SupplierRepository.java)
- `findByNameContainingIgnoreCase(String name)` — search
- `findByActiveTrue()` — active suppliers

#### [MODIFY] [TransactionRepository.java](file:///c:/FUNNY/infosys/database-system/src/main/java/com/inventory/database_system/repository/TransactionRepository.java)
- `findByTransactionType(TransactionType type)` — filter
- `findByStatus(TransactionStatus status)` — filter
- `findByProductId(Long productId)` — product history
- `findByUserId(Long userId)` — user history
- `findByCreatedAtBetween(LocalDateTime start, LocalDateTime end)` — date range
- `@Query` for daily/monthly sales totals (reporting)
- `@Query` for revenue by category (reporting)
- `@Query` for top products by revenue (reporting)

#### [MODIFY] [UserRepository.java](file:///c:/FUNNY/infosys/database-system/src/main/java/com/inventory/database_system/repository/UserRepository.java)
- `findByEmail(String email)` — login lookup
- `findByRole(UserRole role)` — filter by role
- `findByActiveTrue()` — active users
- `existsByEmail(String email)` — registration check

#### [NEW] repository/WarehouseRepository.java
- `findByActiveTrue()`
- `findByNameContainingIgnoreCase(String name)`

#### [NEW] repository/StockMovementRepository.java
- `findByProductId(Long productId)`
- `findByWarehouseId(Long warehouseId)`
- `findByCreatedAtBetween(LocalDateTime start, LocalDateTime end)`
- `@Query` for current stock per warehouse

---

### Phase 4: Service Layer (Database Operations)

#### [NEW] service/CategoryService.java
- CRUD operations, search, toggle active

#### [NEW] service/ProductService.java
- CRUD, search, filter by category/supplier, low stock alerts, inventory value report

#### [NEW] service/SupplierService.java
- CRUD, search, toggle active

#### [NEW] service/TransactionService.java
- Create transaction (auto-update product quantity), filter, date range queries, reporting methods

#### [NEW] service/UserService.java
- CRUD, find by email, role-based filtering

#### [NEW] service/WarehouseService.java
- CRUD, stock movement operations

#### [NEW] service/ReportService.java
- Aggregation methods: revenue by date range, top products, category-wise sales, monthly trends
- This is the **key service** for the reporting half of the system

---

### Phase 5: DTOs (Data Transfer Objects)

#### [NEW] dto/ProductDTO.java
- Flat representation with `categoryName`, `supplierName` instead of nested objects

#### [NEW] dto/TransactionDTO.java
- Flat representation with `productName`, `userName`

#### [NEW] dto/ReportDTO.java
- `SalesReportDTO`, `InventoryReportDTO`, `CategoryReportDTO` — structured report data

#### [NEW] dto/DashboardDTO.java
- Summary stats: total products, total revenue, low stock count, recent transactions

---

### Phase 6: DataLoader Enhancement

#### [MODIFY] [DataLoader.java](file:///c:/FUNNY/infosys/database-system/src/main/java/com/inventory/database_system/DataLoader.java)
- Add multiple categories (Electronics, Clothing, Food, Furniture)
- Add multiple suppliers (4-5)
- Add multiple users (Admin + 2 Staff)
- Add 10+ products across categories
- Add 15+ transactions (mix of SALE, PURCHASE, different dates)
- Add 2 warehouses with stock movements

---

### Phase 7: Configuration

#### [MODIFY] [application.properties](file:///c:/FUNNY/infosys/database-system/src/main/resources/application.properties)
- Add HikariCP connection pool settings
- Add Hibernate dialect for MySQL 8
- Add physical naming strategy
- Add batch size for performance

#### [MODIFY] [pom.xml](file:///c:/FUNNY/infosys/database-system/pom.xml)
- Add `spring-boot-starter-validation` for `@NotNull`, `@Size`, etc.
- Add project name and description

---

## Open Questions

> [!IMPORTANT]
> 1. **Warehouse tracking** — Do you want the `Warehouse` and `StockMovement` entities, or is single-location inventory sufficient?
> 2. **Clean slate** — Should I drop and recreate all tables (switch to `create-drop` once), or preserve existing data?
> 3. **Any additional entities** — Do you need entities like `PurchaseOrder`, `Invoice`, or `Customer` (separate from User)?

---

## Verification Plan

### Automated Tests
1. `.\mvnw.cmd compile` — verify all code compiles
2. `.\mvnw.cmd spring-boot:run` — verify app starts successfully, tables are created, seed data loads
3. Verify SQL output shows proper table creation with indexes and constraints

### Manual Verification
- Check MySQL database for all tables, columns, constraints, and seed data
- Confirm all repository queries work via the DataLoader or test logs
