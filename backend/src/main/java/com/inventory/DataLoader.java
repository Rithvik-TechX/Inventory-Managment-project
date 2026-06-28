package com.inventory;

import com.inventory.entity.*;
import com.inventory.enums.*;
import com.inventory.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    static final String DEFAULT_ADMIN_EMAIL = "admin@inventory.com";
    static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      CategoryRepository categoryRepository,
                      SupplierRepository supplierRepository,
                      ProductRepository productRepository,
                      TransactionRepository transactionRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            ensureDefaultAdminExists();
            System.out.println("✅ Database already seeded. Skipping sample data.");
            return;
        }

        System.out.println("🌱 Seeding database with initial data...");

        // ── Users ──────────────────────────────────────────────────────
        User admin = userRepository.save(User.builder()
                .name("Admin User")
                .email(DEFAULT_ADMIN_EMAIL)
                .password(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD))
                .role(UserRole.ADMIN)
                .phoneNumber("9876543210")
                .build());

        User manager = userRepository.save(User.builder()
                .name("Manager User")
                .email("manager@inventory.com")
                .password(passwordEncoder.encode("manager123"))
                .role(UserRole.MANAGER)
                .phoneNumber("9876543211")
                .build());

        User staff = userRepository.save(User.builder()
                .name("Staff User")
                .email("staff@inventory.com")
                .password(passwordEncoder.encode("staff123"))
                .role(UserRole.STAFF)
                .phoneNumber("9876543212")
                .build());

        User rithvik = userRepository.save(User.builder()
                .name("Rithvik Gandhamalla")
                .email("rithvikgandhamalla14@gmail.com")
                .password(passwordEncoder.encode("rithvik123"))
                .role(UserRole.ADMIN)
                .phoneNumber("9876543213")
                .build());

        // ── Categories ─────────────────────────────────────────────────
        Category electronics = categoryRepository.save(Category.builder()
                .name("Electronics").description("Electronic devices and accessories").build());
        Category stationery = categoryRepository.save(Category.builder()
                .name("Stationery").description("Office stationery and supplies").build());
        Category furniture = categoryRepository.save(Category.builder()
                .name("Furniture").description("Office and home furniture").build());
        Category clothing = categoryRepository.save(Category.builder()
                .name("Clothing").description("Apparel and garments").build());
        Category food = categoryRepository.save(Category.builder()
                .name("Food & Beverages").description("Food items and drinks").build());
        Category tools = categoryRepository.save(Category.builder()
                .name("Tools & Hardware").description("Hardware tools and equipment").build());
        Category medical = categoryRepository.save(Category.builder()
                .name("Medical").description("Medical supplies and equipment").build());

        // ── Suppliers ──────────────────────────────────────────────────
        Supplier techSupplier = supplierRepository.save(Supplier.builder()
                .name("TechWorld Ltd")
                .contactPerson("TechWorld Sales").email("techworld@example.com").phone("040-12345678")
                .address("Plot 12, HITEC City, Hyderabad 500081")
                .build());
        Supplier officeSupplier = supplierRepository.save(Supplier.builder()
                .name("OfficeHub Supplies")
                .contactPerson("OfficeHub Sales").email("officehub@example.com").phone("040-87654321")
                .address("Begumpet, Hyderabad 500016")
                .build());
        Supplier furnSupplier = supplierRepository.save(Supplier.builder()
                .name("HomeStyle Furniture")
                .contactPerson("HomeStyle Sales").email("homestyle@example.com").phone("040-11223344")
                .address("Ameerpet, Hyderabad 500038")
                .build());
        Supplier medSupplier = supplierRepository.save(Supplier.builder()
                .name("MedPharma Inc")
                .contactPerson("MedPharma Sales").email("medpharma@example.com").phone("040-55667788")
                .address("Secunderabad, Hyderabad 500003")
                .build());

        // ── Products ───────────────────────────────────────────────────
        Product laptop = productRepository.save(Product.builder()
                .name("Laptop Dell Inspiron 15")
                .sku("ELEC-001").description("15.6\" FHD, i5, 8GB, 512GB SSD")
                .price(new BigDecimal("54999.00")).quantity(25).maxStock(50).reorderLevel(5)
                .category(electronics).supplier(techSupplier).build());
        Product mouse = productRepository.save(Product.builder()
                .name("Wireless Mouse")
                .sku("ELEC-002").description("Ergonomic wireless mouse with USB receiver")
                .price(new BigDecimal("799.00")).quantity(120).maxStock(200).reorderLevel(20)
                .category(electronics).supplier(techSupplier).build());
        Product keyboard = productRepository.save(Product.builder()
                .name("Mechanical Keyboard")
                .sku("ELEC-003").description("RGB mechanical keyboard, blue switches")
                .price(new BigDecimal("2499.00")).quantity(45).maxStock(80).reorderLevel(10)
                .category(electronics).supplier(techSupplier).build());
        Product headphones = productRepository.save(Product.builder()
                .name("Bluetooth Headphones")
                .sku("ELEC-004").description("Over-ear noise-cancelling headphones")
                .price(new BigDecimal("3499.00")).quantity(8).maxStock(60).reorderLevel(10)
                .category(electronics).supplier(techSupplier).build());
        Product notebook = productRepository.save(Product.builder()
                .name("A4 Spiral Notebook")
                .sku("STAT-001").description("200 pages, ruled, spiral bound")
                .price(new BigDecimal("120.00")).quantity(300).maxStock(500).reorderLevel(50)
                .category(stationery).supplier(officeSupplier).build());
        Product pen = productRepository.save(Product.builder()
                .name("Gel Pen (Pack of 10)")
                .sku("STAT-002").description("Blue gel pens, 0.7mm")
                .price(new BigDecimal("150.00")).quantity(200).maxStock(400).reorderLevel(30)
                .category(stationery).supplier(officeSupplier).build());
        Product desk = productRepository.save(Product.builder()
                .name("Office Desk")
                .sku("FURN-001").description("120cm x 60cm wooden office desk")
                .price(new BigDecimal("8999.00")).quantity(12).maxStock(30).reorderLevel(3)
                .category(furniture).supplier(furnSupplier).build());
        Product chair = productRepository.save(Product.builder()
                .name("Ergonomic Chair")
                .sku("FURN-002").description("High-back ergonomic office chair")
                .price(new BigDecimal("12999.00")).quantity(2).maxStock(20).reorderLevel(5)
                .category(furniture).supplier(furnSupplier).build());
        Product mask = productRepository.save(Product.builder()
                .name("Surgical Mask (Box of 50)")
                .sku("MED-001").description("3-ply disposable surgical masks")
                .price(new BigDecimal("250.00")).quantity(400).maxStock(800).reorderLevel(100)
                .category(medical).supplier(medSupplier).build());
        Product sanitizer = productRepository.save(Product.builder()
                .name("Hand Sanitizer 500ml")
                .sku("MED-002").description("Alcohol-based hand sanitizer")
                .price(new BigDecimal("175.00")).quantity(3).maxStock(100).reorderLevel(20)
                .category(medical).supplier(medSupplier).build());

        // ── Transactions ───────────────────────────────────────────────
        transactionRepository.save(Transaction.builder()
                .product(laptop).user(admin).supplier(techSupplier)
                .quantity(5).totalPrice(new BigDecimal("274995.00"))
                .transactionType(TransactionType.SALE)
                .transactionStatus(TransactionStatus.COMPLETED)
                .description("Bulk sale to TechCorp")
                .referenceId("INV-2026-001")
                .build());

        transactionRepository.save(Transaction.builder()
                .product(mouse).user(manager).supplier(techSupplier)
                .quantity(50).totalPrice(new BigDecimal("39950.00"))
                .transactionType(TransactionType.PURCHASE)
                .transactionStatus(TransactionStatus.COMPLETED)
                .description("Restocked wireless mice")
                .referenceId("PO-2026-001")
                .build());

        transactionRepository.save(Transaction.builder()
                .product(chair).user(admin).supplier(furnSupplier)
                .quantity(10).totalPrice(new BigDecimal("129990.00"))
                .transactionType(TransactionType.PURCHASE)
                .transactionStatus(TransactionStatus.PENDING)
                .description("Pending order for office chairs")
                .referenceId("PO-2026-002")
                .build());

        System.out.println("✅ Database seeded successfully!");
        System.out.println("   👤 Users: " + userRepository.count());
        System.out.println("   📁 Categories: " + categoryRepository.count());
        System.out.println("   🏢 Suppliers: " + supplierRepository.count());
        System.out.println("   📦 Products: " + productRepository.count());
        System.out.println("   📋 Transactions: " + transactionRepository.count());
        System.out.println("\n   🔐 Login credentials:");
        System.out.println("      Admin:   admin@inventory.com / admin123");
        System.out.println("      Manager: manager@inventory.com / manager123");
        System.out.println("      Staff:   staff@inventory.com / staff123");
    }

    private void ensureDefaultAdminExists() {
        if (userRepository.existsByEmail(DEFAULT_ADMIN_EMAIL)) {
            return;
        }

        userRepository.save(User.builder()
                .name("Admin User")
                .email(DEFAULT_ADMIN_EMAIL)
                .password(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD))
                .role(UserRole.ADMIN)
                .phoneNumber("9876543210")
                .build());

        System.out.println("✅ Default admin user created.");
    }
}
