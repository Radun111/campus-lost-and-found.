
# Lost and Found Application - Backend

## 📌 Overview

This is the backend implementation of a Lost and Found system for an educational institute, built as part of Assignment 1 for CMJD Batch 108/109.

## 🚀 Tech Stack

- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- MySQL

## ⚙️ Features

- User Roles: Admin, Staff, User
- Entities: Item, Request, User
- JWT-based Authentication and Authorization
- CRUD APIs for Items, Requests, and Users
- Status tracking for Items (LOST, FOUND, CLAIMED)
- Status tracking for Requests (PENDING, APPROVED, REJECTED)

## 📁 Project Structure

- `controller/` – REST endpoints
- `service/` – Business logic
- `repository/` – JPA Repositories
- `model/` – Entity classes
- `util/` – JWT utilities
- `filter/` – JWT Authentication Filter

## 🔑 Authentication

- **Signup**: `/auth/signup`
- **Signin**: `/auth/signin` → returns JWT token
- Use the token in the `Authorization: Bearer <token>` header for secured routes

## 🛠️ Configuration

Set up the following in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lostandfound
spring.datasource.username=root
spring.datasource.password=your_password
jwt.secret=your_jwt_secret
