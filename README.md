# AssetFlow Backend

AssetFlow Backend is a Spring Boot REST API for managing company assets such as laptops, monitors, phones, and accessories.

This project is part of my full-stack portfolio built with:

- Java 25
- Spring Boot 3.5.14
- Spring Data JPA
- PostgreSQL
- Docker
- Maven

## Features

- REST API
- PostgreSQL database
- Dockerized development environment
- Spring Data JPA
- Layered architecture
- Git version control

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

## Roadmap

- [x] Spring Boot setup
- [x] PostgreSQL
- [x] Docker
- [x] Health endpoint
- [ ] Asset CRUD
- [ ] Authentication
- [ ] Users
- [ ] Asset Assignment
- [ ] File Upload
- [ ] Swagger
- [ ] Unit Tests

---

## Author

Talha Bin Shakoor