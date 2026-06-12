# AssetFlow Backend

AssetFlow Backend is a Spring Boot REST API for managing company assets with secure JWT-based authentication. The project follows production-style layered architecture and REST API best practices.

This project is part of my full-stack portfolio built with:

- Java 25
- Spring Boot 3.5.14
- Spring Data JPA
- PostgreSQL
- Docker
- Maven

## Features (Completed)

### Asset Management

- Full CRUD operations
- DTO-based API design
- Request validation
- Global exception handling

### Authentication

- User registration
- User login
- BCrypt password hashing
- JWT authentication
- Protected REST endpoints

### Database

- PostgreSQL
- Spring Data JPA
- Hibernate ORM

### Architecture

- Layered architecture
- RESTful API
- Dockerized PostgreSQL


## Authentication & Security

This project uses Spring Security with JWT-based authentication.

### Flow:
1. User registers via `/auth/register`
2. User logs in via `/auth/login`
3. Server returns JWT token
4. Client includes token in requests:
```http
Authorization: Bearer <JWT_TOKEN>
```  
5. Backend validates token using security filter


### Security Features:
- Stateless authentication (no sessions)
- Passwords stored using BCrypt hashing
- JWT expiration enabled (1 hour)
- Protected API endpoints
- Public endpoints: `/auth/**`, `/health`


## Authentication APIs

Most API endpoints require JWT authentication.

### Register

POST /auth/register

```json
{
  "username": "exampleName",
  "password": "examplePassword"
}
```

---

### Login

POST /auth/login

```json
{
  "username": "exampleName",
  "password": "examplePassword"
}
```

Response:

```json
<JWT_TOKEN>
```


## Tech Stack

- Java 25
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- BCrypt Password Encoder
- Spring Data JPA
- PostgreSQL
- Docker
- Maven
- Lombok
- IntelliJ IDEA

## Getting Started

### Clone

```bash
git clone https://github.com/TalhaBinShakoor/assetflow-backend.git
```

### Start PostgreSQL

```bash
docker compose up -d
```

### Run the application

```bash
./mvnw spring-boot:run
```

or from IntelliJ by running:

```
AssetflowBackendApplication
```

### Test

```
GET http://localhost:8080/health
```

Expected response:

```
AssetFlow backend is running
```

---

## Asset API

All Asset endpoints require a valid JWT token.

Authorization: Bearer <JWT_TOKEN>


### Get all assets

GET /api/assets

---

### Get asset by id

GET /api/assets/{id}

---

### Create asset

POST /api/assets

Request body:
```json
{
  "name": "Laptop",
  "category": "Electronics",
  "status": "IN_USE",
  "purchaseDate": "2026-06-12"
}
```

---

### Update asset

PUT /api/assets/{id}

---

### Delete asset

DELETE /api/assets/{id}

---

## Author

Talha Bin Shakoor