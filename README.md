# 🎒 Campus Lost & Found — Backend API

A secure REST API for a campus **Lost and Found system**. Students report lost items and submit claim requests, admins review them, and the owner gets an email when their request is approved.

Built as Assignment 1 for the **CMJD** program at **IJSE** (Batch 108/109).

🔗 **Frontend:** [lost-and-found-frontend](https://github.com/Radun111/lost-and-found-frontend)

![Java](https://img.shields.io/badge/Java_17-E75480?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)

---

## ⚙️ Features

- **JWT authentication** with access tokens and **refresh tokens**
- **Role-based access**: `USER` (student), `STAFF` and `ADMIN`
- Roles are created automatically on first start (`DataInitializer`)
- Report, view, search and delete lost/found **items**
- Submit claim **requests** that are approved or rejected
- **Email notification** to the user when a request is approved
- Status tracking for items (`LOST`, `FOUND`, `CLAIMED`) and requests (`PENDING`, `APPROVED`, `REJECTED`)
- Global exception handling with clear JSON error responses
- Service-layer **unit tests** with JUnit 5

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.4 |
| Security | Spring Security + JWT |
| Database | MySQL with Spring Data JPA / Hibernate |
| Email | Spring Mail (Gmail SMTP) |
| Build | Maven |
| Testing | JUnit 5 |

## 📁 Project Structure

```plaintext
src/main/java/com/example/lostandfound/
├── config/        # Security, JWT, mail config and role seeding
├── controller/    # REST endpoints
├── dto/           # Request and response objects
├── entity/        # JPA entities (User, Role, Item, Request, RefreshToken)
├── exception/     # Custom exceptions + global handler
├── filter/        # JWT authentication filter
├── repository/    # Spring Data JPA repositories
├── service/       # Business logic
└── util/          # JWT helper
```

## 🔌 API Endpoints

All routes except `/api/auth/**` need the header `Authorization: Bearer <token>`.

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/auth/register` | Create an account | Public |
| POST | `/api/auth/login` | Log in and get access + refresh tokens | Public |
| POST | `/api/auth/refresh` | Get a new access token | Public |
| POST | `/api/items` | Report an item | Logged in |
| GET | `/api/items/{id}` | Get one item | Logged in |
| POST | `/api/items/search` | Search items by criteria | Logged in |
| DELETE | `/api/items/{id}` | Delete an item | Admin |
| POST | `/api/requests` | Submit a claim request | Logged in |
| GET | `/api/requests/{requestId}` | Get one request | Logged in |
| PATCH | `/api/requests/{requestId}/approve` | Approve a request (sends email) | Logged in |
| PATCH | `/api/requests/{requestId}/reject` | Reject a request | Admin |
| GET | `/api/admin/dashboard` | Admin dashboard data | Admin |

## 🛠️ Setup

**Requirements:** JDK 17+, MySQL 8+, Maven (or use the included `mvnw`).

1. Clone the repo:
   ```bash
   git clone https://github.com/Radun111/campus-lost-and-found..git
   cd campus-lost-and-found.
   ```
2. Edit `src/main/resources/application.properties` with your own values:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/lost_and_found_db?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=YOUR_DB_PASSWORD

   jwt.secret=YOUR_BASE64_SECRET
   jwt.expiration=86400000
   jwt.refresh-expiration=604800000

   spring.mail.username=YOUR_EMAIL@gmail.com
   spring.mail.password=YOUR_GMAIL_APP_PASSWORD
   ```
   The database is created automatically on first run.
3. Run the app:
   ```bash
   ./mvnw spring-boot:run
   ```
   The API starts at `http://localhost:8080` and accepts requests from the frontend at `http://localhost:3000`.
4. Run the tests:
   ```bash
   ./mvnw test
   ```

## 👩‍💻 Author

**Raduni Thesanya** · [GitHub](https://github.com/Radun111) · [LinkedIn](https://www.linkedin.com/in/raduni-thesanya/)
