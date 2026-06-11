# AssetFlow Backend

AssetFlow Backend is a Spring Boot REST API for managing company assets such as laptops, monitors, phones, and accessories.

This project is part of my full-stack portfolio built with:

- Java 25
- Spring Boot 3.5.14
- Spring Data JPA
- PostgreSQL
- Docker
- Maven

## Features (Completed)

- REST API
- Layered architecture (Controller → Service → Repository)
- PostgreSQL database integration
- JPA/Hibernate entity mapping
- Full CRUD operations for Asset module
- Dockerized development environment
- Spring Data JPA

## Tech Stack

- Java
- Spring Boot
- PostgreSQL
- Docker
- Maven
- IntelliJ IDEA

## Getting Started

### Clone

```bash
git clone https://github.com/YOUR_USERNAME/assetflow-backend.git
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

## API Details:

### Base URL

/api/assets


---

### Get all assets

GET /api/assets


---

### Get asset by ID

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
  "purchaseDate": "2026-06-07"
}
```

### Update asset
PUT /api/assets/{id}

### Delete asset
DELETE /api/assets/{id}


## Author

Talha Bin Shakoor