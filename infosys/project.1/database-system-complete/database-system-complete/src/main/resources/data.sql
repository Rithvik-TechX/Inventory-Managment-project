-- ============================================================
-- Inventory Management System - Sample Seed Data
-- Run AFTER tables are created by Hibernate (ddl-auto=update)
-- ============================================================

-- Categories
INSERT IGNORE INTO categories (id, name, description, is_active) VALUES
(1, 'Electronics',    'Electronic devices and components',   1),
(2, 'Furniture',      'Office and home furniture',           1),
(3, 'Stationery',     'Pens, notebooks, and office supplies',1),
(4, 'Clothing',       'Apparel and accessories',             1),
(5, 'Food & Beverage','Consumable food and drinks',          1);

-- Suppliers
INSERT IGNORE INTO suppliers (id, name, contact_email, contact_phone, address, is_active, created_at) VALUES
(1, 'TechSource Ltd',    'sales@techsource.com',  '+91-9876543210', 'Hyderabad, Telangana', 1, NOW()),
(2, 'FurniCo Pvt Ltd',   'info@furnico.com',      '+91-8765432109', 'Mumbai, Maharashtra',  1, NOW()),
(3, 'OfficeWorks India', 'orders@officeworks.in', '+91-7654321098', 'Chennai, Tamil Nadu',  1, NOW()),
(4, 'FashionHub',        'support@fashionhub.in', '+91-6543210987', 'Delhi, NCR',           1, NOW());

-- Users
INSERT IGNORE INTO users (id, name, email, password, phone_number, role, is_active, created_at, updated_at) VALUES
(1, 'Admin User',   'admin@inventory.com',   '$2a$10$dummyhashedpwd1', '+91-9000000001', 'ADMIN',   1, NOW(), NOW()),
(2, 'Manager One',  'manager@inventory.com', '$2a$10$dummyhashedpwd2', '+91-9000000002', 'MANAGER', 1, NOW(), NOW()),
(3, 'Staff Alice',  'alice@inventory.com',   '$2a$10$dummyhashedpwd3', '+91-9000000003', 'STAFF',   1, NOW(), NOW()),
(4, 'Staff Bob',    'bob@inventory.com',     '$2a$10$dummyhashedpwd4', '+91-9000000004', 'STAFF',   1, NOW(), NOW());

-- Products
INSERT IGNORE INTO products (id, name, description, sku, price, quantity, reorder_level, is_active, category_id, supplier_id, created_at, updated_at) VALUES
(1,  'Laptop Pro 15',       '15-inch professional laptop',         'ELEC-LAPT-001', 75000.00, 50,  5,  1, 1, 1, NOW(), NOW()),
(2,  'Wireless Mouse',      'Ergonomic wireless mouse',            'ELEC-MOUS-001', 1200.00,  150, 20, 1, 1, 1, NOW(), NOW()),
(3,  'Mechanical Keyboard', 'RGB mechanical keyboard',             'ELEC-KEYB-001', 3500.00,  80,  10, 1, 1, 1, NOW(), NOW()),
(4,  'Office Chair',        'Ergonomic office chair with lumbar',  'FURN-CHAR-001', 12000.00, 30,  5,  1, 2, 2, NOW(), NOW()),
(5,  'Standing Desk',       'Height-adjustable standing desk',     'FURN-DESK-001', 25000.00, 15,  3,  1, 2, 2, NOW(), NOW()),
(6,  'A4 Notebook 200pg',   '200-page ruled notebook',             'STAT-NOTE-001', 150.00,   500, 50, 1, 3, 3, NOW(), NOW()),
(7,  'Blue Pen Pack (10)',   'Pack of 10 blue ballpoint pens',      'STAT-PEN-001',  80.00,    300, 30, 1, 3, 3, NOW(), NOW()),
(8,  'Monitor 27inch',      '4K IPS 27-inch monitor',             'ELEC-MONI-001', 28000.00, 25,  3,  1, 1, 1, NOW(), NOW()),
(9,  'USB Hub 7-Port',      '7-port USB 3.0 hub',                 'ELEC-USB-001',  1800.00,  8,   10, 1, 1, 1, NOW(), NOW()),
(10, 'Printer Ink Black',   'Black ink cartridge for HP printers', 'STAT-INK-001',  650.00,   2,   15, 1, 3, 3, NOW(), NOW());

-- Transactions (SALE and PURCHASE samples)
INSERT IGNORE INTO transactions (id, quantity, total_price, transaction_type, transaction_date, reference_id, status, product_id, user_id, supplier_id, created_at) VALUES
(1,  2, 150000.00, 'SALE',     NOW() - INTERVAL 10 DAY, 'TXN-SALE-0001', 'COMPLETED', 1,  3, NULL, NOW()),
(2,  5,   6000.00, 'SALE',     NOW() - INTERVAL 9 DAY,  'TXN-SALE-0002', 'COMPLETED', 2,  3, NULL, NOW()),
(3, 10,  25000.00, 'PURCHASE', NOW() - INTERVAL 8 DAY,  'TXN-PURC-0001', 'COMPLETED', 1,  2, 1,    NOW()),
(4,  1,  12000.00, 'SALE',     NOW() - INTERVAL 7 DAY,  'TXN-SALE-0003', 'COMPLETED', 4,  4, NULL, NOW()),
(5,  3,  10500.00, 'SALE',     NOW() - INTERVAL 6 DAY,  'TXN-SALE-0004', 'COMPLETED', 3,  3, NULL, NOW()),
(6, 20,   3000.00, 'PURCHASE', NOW() - INTERVAL 5 DAY,  'TXN-PURC-0002', 'COMPLETED', 6,  2, 3,    NOW()),
(7,  1,  28000.00, 'SALE',     NOW() - INTERVAL 4 DAY,  'TXN-SALE-0005', 'COMPLETED', 8,  4, NULL, NOW()),
(8,  5,   9000.00, 'SALE',     NOW() - INTERVAL 3 DAY,  'TXN-SALE-0006', 'PENDING',   2,  3, NULL, NOW()),
(9,  2,   3600.00, 'SALE',     NOW() - INTERVAL 2 DAY,  'TXN-SALE-0007', 'COMPLETED', 3,  4, NULL, NOW()),
(10, 1,  25000.00, 'SALE',     NOW() - INTERVAL 1 DAY,  'TXN-SALE-0008', 'COMPLETED', 5,  3, NULL, NOW());
