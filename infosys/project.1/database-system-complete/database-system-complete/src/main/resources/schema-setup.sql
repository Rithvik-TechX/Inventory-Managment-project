-- ============================================================
-- DATABASE SETUP SCRIPT
-- Run this ONCE before starting the application
-- ============================================================

CREATE DATABASE IF NOT EXISTS inventory_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE inventory_db;

-- Hibernate will auto-create tables via ddl-auto=update
-- But here are the manual DDL statements for reference/deployment:

CREATE TABLE IF NOT EXISTS categories (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    is_active   TINYINT(1) NOT NULL DEFAULT 1,
    INDEX idx_category_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS suppliers (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(150) NOT NULL,
    contact_email VARCHAR(150),
    contact_phone VARCHAR(20),
    contact_info  VARCHAR(255),
    address       VARCHAR(500),
    is_active     TINYINT(1) NOT NULL DEFAULT 1,
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_supplier_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS users (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100)  NOT NULL,
    email        VARCHAR(150)  NOT NULL UNIQUE,
    password     VARCHAR(255)  NOT NULL,
    phone_number VARCHAR(20),
    role         VARCHAR(20)   NOT NULL DEFAULT 'STAFF',
    is_active    TINYINT(1)    NOT NULL DEFAULT 1,
    created_at   DATETIME      NOT NULL,
    updated_at   DATETIME,
    INDEX idx_user_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS products (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(200)   NOT NULL,
    description   VARCHAR(1000),
    sku           VARCHAR(50)    NOT NULL UNIQUE,
    price         DECIMAL(15,2)  NOT NULL,
    quantity      INT            NOT NULL DEFAULT 0,
    reorder_level INT            NOT NULL DEFAULT 10,
    is_active     TINYINT(1)     NOT NULL DEFAULT 1,
    category_name VARCHAR(100),
    category_id   BIGINT,
    supplier_id   BIGINT,
    created_at    DATETIME       NOT NULL,
    updated_at    DATETIME,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    CONSTRAINT fk_product_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL,
    INDEX idx_product_sku      (sku),
    INDEX idx_product_name     (name),
    INDEX idx_product_category (category_id),
    INDEX idx_product_supplier (supplier_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS transactions (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity         INT           NOT NULL,
    total_price      DECIMAL(15,2) NOT NULL,
    transaction_type VARCHAR(20)   NOT NULL,
    transaction_date DATETIME      NOT NULL,
    reference_id     VARCHAR(100),
    status           VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    notes            VARCHAR(500),
    product_id       BIGINT        NOT NULL,
    supplier_id      BIGINT,
    user_id          BIGINT        NOT NULL,
    created_at       DATETIME      NOT NULL,
    CONSTRAINT fk_txn_product  FOREIGN KEY (product_id)  REFERENCES products(id),
    CONSTRAINT fk_txn_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL,
    CONSTRAINT fk_txn_user     FOREIGN KEY (user_id)     REFERENCES users(id),
    INDEX idx_txn_product   (product_id),
    INDEX idx_txn_supplier  (supplier_id),
    INDEX idx_txn_user      (user_id),
    INDEX idx_txn_date      (transaction_date),
    INDEX idx_txn_reference (reference_id),
    INDEX idx_txn_type      (transaction_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
