-- ============================================
--   INVENTORY MANAGEMENT SYSTEM — init.sql
--   Run this file to set up the full database
-- ============================================

-- Step 1: Create and select database
DROP DATABASE IF EXISTS inventory_db;
CREATE DATABASE inventory_db;
USE inventory_db;

-- Step 2: Create Tables

CREATE TABLE user (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(20),
    role        ENUM('ADMIN','MANAGER','STAFF') DEFAULT 'STAFF',
    createdAt   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE category (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE supplier (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    contactInfo VARCHAR(100),
    address     VARCHAR(255)
);

CREATE TABLE product (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(150) NOT NULL,
    sku           VARCHAR(100) NOT NULL UNIQUE,
    price         DECIMAL(10,2) NOT NULL,
    stockQuantity INT DEFAULT 0,
    description   TEXT,
    expiryDate    DATE,
    imageUrl      VARCHAR(500),
    createdAt     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    category_id   INT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE transaction (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    totalProducts     INT NOT NULL,
    totalPrice        DECIMAL(10,2) NOT NULL,
    transactionType   ENUM('PURCHASE','SALE','RETURN','ADJUSTMENT') NOT NULL,
    transactionStatus ENUM('PENDING','COMPLETED','CANCELLED') DEFAULT 'PENDING',
    description       TEXT,
    note              VARCHAR(255),
    updatedAt         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    createdAt         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    product_id        INT,
    user_id           INT,
    supplier_id       INT,
    FOREIGN KEY (product_id)  REFERENCES product(id),
    FOREIGN KEY (user_id)     REFERENCES user(id),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

-- Step 3: Insert Sample Data

INSERT INTO category (name) VALUES
('Electronics'),
('Furniture'),
('Stationery');

INSERT INTO supplier (name, contactInfo, address) VALUES
('TechMart Pvt Ltd', 'techmart@gmail.com',  'Hyderabad'),
('FurniMart Co',     'furnimart@gmail.com', 'Chennai'),
('PaperHub India',   'paperhub@gmail.com',  'Bangalore');

INSERT INTO user (name, email, password, phoneNumber, role) VALUES
('Priya Sharma', 'priya@inv.com', 'priya@123', '9876501234', 'ADMIN'),
('Arjun Mehta',  'arjun@inv.com', 'arjun@456', '9845012345', 'MANAGER'),
('Sneha Reddy',  'sneha@inv.com', 'sneha@789', '9812034567', 'STAFF');

INSERT INTO product (name, sku, price, stockQuantity, description, category_id) VALUES
('Laptop',        'SKU-L01', 55000.00, 20, 'Gaming Laptop',   1),
('Smartphone',    'SKU-P01', 25000.00, 15, 'Android Phone',   1),
('Office Chair',  'SKU-C01',  8999.00,  4, 'Ergonomic Chair', 2),
('Notebook Pack', 'SKU-N01',   199.00,100, 'Pack of 5',       3),
('Pen Pack',      'SKU-PP01',   49.00,  3, 'Pack of 10 pens', 3);

-- ============================================
--   DATABASE READY ✅
-- ============================================
```

---
