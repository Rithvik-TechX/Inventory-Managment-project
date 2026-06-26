
-- INVENTORY MANAGEMENT SYSTEM - INIT.SQL


-- 1. Create Database
CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

-- 2. Categories Table
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- 3. Suppliers Table
CREATE TABLE IF NOT EXISTS suppliers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    city VARCHAR(100) NOT NULL
);

-- 4. Users Table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role ENUM('ADMIN', 'MANAGER', 'STAFF') NOT NULL
);

-- 5. Products Table
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    sku VARCHAR(50) NOT NULL UNIQUE,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT NOT NULL,
    description VARCHAR(255),
    supplier_id INT,
    transaction_id INT,
    category_id INT NOT NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL,
    FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE SET NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

-- 6. Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    quantity INT NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    type ENUM('PURCHASE','SALE','RETURN') NOT NULL,
    status ENUM('PENDING','COMPLETED') NOT NULL,
    title VARCHAR(150),
    notes VARCHAR(255),
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    supplier_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL
);

-- 7. Optional: Insert Sample Categories
INSERT INTO categories (name) VALUES
('Electronics'),
('Furniture'),
('Stationery');

-- 8. Optional: Insert Sample Suppliers
INSERT INTO suppliers (name,email,city) VALUES
('TechMart Pvt Ltd','techmart@gmail.com','Hyderabad'),
('FurniMart Co','furnimart@gmail.com','Chennai'),
('PaperHub India','paperhub@gmail.com','Bangalore');

-- 9. Optional: Insert Sample Users
INSERT INTO users (name,email,password,phone,role) VALUES
('Priya Sharma','priya@inv.com','priya@123','9876501234','ADMIN'),
('Arjun Mehta','arjun@inv.com','arjun@456','9845012345','MANAGER'),
('Sneha Reddy','sneha@inv.com','sneha@789','9812034567','STAFF');

-- Note: Products and Transactions are typically inserted via DAO layer
