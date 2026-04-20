# Inventory Management and Reporting System | Infosys SpringBoard Internship Project

A full-stack inventory management application built as part of the **Infosys Springboard Virtual Internship**. The backend is a Spring Boot REST API secured with JWT authentication, and the frontend is a React SPA with role-based routing, real-time low-stock alerts, and analytics dashboards.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Features](#features)
- [Getting Started](#getting-started)

---

## Tech Stack

| Layer    | Technology                          |
|----------|-------------------------------------|
| Frontend | React 18, React Router, Recharts    |
| Backend  | Spring Boot 3.5, Spring Security, JPA |
| Database | MySQL 8+                            |
| Auth     | JWT (JSON Web Tokens)               |
| Language | Java 21                             |

---

## Project Structure

```
├── backend/          # Spring Boot REST API
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/inventory/
│   │   │   │   ├── config/         # App & Security configuration
│   │   │   │   ├── controller/     # REST controllers
│   │   │   │   ├── dto/            # Data Transfer Objects
│   │   │   │   ├── entity/         # JPA entities
│   │   │   │   ├── enums/          # Enumerations
│   │   │   │   ├── repository/     # Spring Data repositories
│   │   │   │   ├── security/       # JWT filter & auth utilities
│   │   │   │   ├── service/        # Business logic services
│   │   │   │   └── validation/     # Custom validators
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
│
├── frontend/         # React SPA
│   ├── public/
│   ├── src/
│   │   ├── components/   # Reusable UI components
│   │   ├── context/      # React context providers
│   │   ├── hooks/        # Custom React hooks
│   │   ├── pages/        # Page-level components
│   │   ├── services/     # API service layer
│   │   ├── styles/       # CSS stylesheets
│   │   └── utilities/    # Helper functions & constants
│   └── package.json
│
├── .gitignore
├── LICENSE
└── README.md
```

---

## Features

- **JWT Authentication** — Secure login/signup with role-based access control
- **Product Management** — Full CRUD for products with category and supplier associations
- **Inventory Tracking** — Real-time stock level monitoring with low-stock alerts
- **Transaction Processing** — Record purchases and sales with automatic stock updates
- **Analytics Dashboard** — Visual charts and summary cards for inventory insights
- **Reporting** — Generate inventory and transaction reports
- **User Management** — Admin controls for managing users and roles

---

## Getting Started

### Prerequisites

- Java 21+
- Node.js 16+
- MySQL 8+
- Maven

### Backend

```bash
cd backend
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm start
```

The frontend runs on `http://localhost:3000` and the backend API on `http://localhost:8083`.

> 📖 For detailed project documentation, see [PROJECT_DOCUMENTATION.md](PROJECT_DOCUMENTATION.md)

---

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
