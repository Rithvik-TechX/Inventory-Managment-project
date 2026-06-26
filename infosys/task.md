# Database Layer — Task Checklist

## Phase 1: Enums
- [ ] Create `enums/UserRole.java`
- [ ] Create `enums/TransactionType.java`
- [ ] Create `enums/TransactionStatus.java`
- [ ] Create `enums/MovementType.java`

## Phase 2: Enhanced Entities
- [ ] Modify `Category.java` — add description, constraints, audit
- [ ] Modify `Product.java` — add constraints, minStock, maxStock, active, audit
- [ ] Modify `Supplier.java` — add email, constraints, audit
- [ ] Modify `Transaction.java` — use enums, add indexes, audit
- [ ] Modify `User.java` — use enum, add active, audit
- [ ] Create `entity/Warehouse.java`
- [ ] Create `entity/StockMovement.java`

## Phase 3: Enhanced Repositories
- [ ] Modify `CategoryRepository.java`
- [ ] Modify `ProductRepository.java`
- [ ] Modify `SupplierRepository.java`
- [ ] Modify `TransactionRepository.java`
- [ ] Modify `UserRepository.java`
- [ ] Create `repository/WarehouseRepository.java`
- [ ] Create `repository/StockMovementRepository.java`

## Phase 4: Service Layer
- [ ] Create `service/CategoryService.java`
- [ ] Create `service/ProductService.java`
- [ ] Create `service/SupplierService.java`
- [ ] Create `service/TransactionService.java`
- [ ] Create `service/UserService.java`
- [ ] Create `service/WarehouseService.java`
- [ ] Create `service/ReportService.java`

## Phase 5: DTOs
- [ ] Create `dto/ProductDTO.java`
- [ ] Create `dto/TransactionDTO.java`
- [ ] Create `dto/ReportDTO.java`
- [ ] Create `dto/DashboardDTO.java`

## Phase 6: DataLoader Enhancement
- [ ] Rewrite `DataLoader.java` with rich seed data

## Phase 7: Configuration
- [ ] Update `application.properties`
- [ ] Update `pom.xml`

## Verification
- [ ] Compile successfully
- [ ] Application starts and tables are created
- [ ] Seed data loads correctly
