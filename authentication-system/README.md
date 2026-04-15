# Inventory Management Authentication Module

This project is a secure **Inventory Management System** backend built with **Spring Boot 3.5**. It features a robust, role-based authentication layer using **JSON Web Tokens (JWT)** to manage access for different types of users (Admin, Supplier, Customer).

## 🚀 Overview

The primary goal of this module is to provide a scalable and secure foundation for an inventory system. It handles user registration, secure login, and protected route access based on specific user roles.

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.11
- **Security**: Spring Security + JWT (JSON Web Token)
- **Data Access**: Spring Data JPA
- **Database**: MySQL (Local / Production)
- **Language**: Java 21
- **Build Tool**: Maven

## ✨ Core Features

### 1. Authentication Layer
- **JWT Implementation**: Custom `JwtAuthenticationFilter` intercepts requests to validate Bearer tokens.
- **Service Layer**: `AuthService` manages password encryption (BCrypt) and token generation via `AuthUtil`.
- **User Management**: Custom `UserDetails` implementation using the `User` entity and `UserRepository`.

### 2. Role-Based Access Control (RBAC)
Dedicated dashboards and endpoints secured by role permissions:
- **ADMIN**: Full access to all modules and system dashboards.
- **SUPPLIER**: Access to stock management and supplier dashboards.
- **CUSTOMER**: Access to product viewing and customer dashboards.

### 3. Inventory Services
- **ProductService**: Logic for adding and managing product metadata.
- **InventoryService**: Real-time stock updates and volume management.
- **ReportService**: Automated generation of inventory status reports.

## 📡 API Endpoints

| Endpoint | Method | Access | Description |
| :--- | :--- | :--- | :--- |
| `/api/v1/auth/signup` | POST | Public | Register a new user with a specific role. |
| `/api/v1/auth/login` | POST | Public | Authenticate and receive a JWT Bearer token. |
| `/api/v1/admin/**` | GET | Admin | Restricted admin dashboards and actions. |
| `/api/v1/supplier/**`| GET | Supplier/Admin | Inventory replenishment and supplier routes. |
| `/api/v1/customer/**`| GET | Customer/Admin | Customer-specific dashboards and orders. |
| `/api/v1/public/health`| GET | Public | Server health status check. |

## 🔧 Recent Changes & Verification

We have recently transitioned the database layer and verified the core functionality:
- **Database Migration**: Successfully transitioned to **MySQL**.
- **Configuration**: Updated `pom.xml` with `mysql-connector-j` and aligned `application.properties` with local MySQL credentials.
- **API Verification**:
  - **POST `/auth/signup`**: Verified new user creation in MySQL.
  - **POST `/auth/login`**: Verified JWT generation and validation.
  - **GET `/admin/dashboard`**: Verified role-based access control (RBAC) with active tokens.
- **Git Cleanup**: Optimized the repository by ignoring build artifacts and sensitive temp files.

## 🚦 Getting Started

### Prerequisites
- JDK 21
- MySQL Server (Running on port 3306)
- Maven (or use the provided `mvnw` wrapper)

### Installation & Run
1. **Clone the repository**:
   ```bash
   git clone https://github.com/Vishnugithub3568/inventory-authentication-module-main.git
   ```
2. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

## 👤 Default Credentials
The application is pre-seeded via `data.sql` with the following test users (password is `password` for all):
- **Admin**: `admin`
- **Supplier**: `supplier`
- **Customer**: `customer`
