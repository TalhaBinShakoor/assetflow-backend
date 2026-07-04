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

- User registration and login
- BCrypt password hashing
- JWT-based stateless authentication
- Protected REST endpoints

### Multi-tenant security

- USER role can only access their own assets
- ADMIN role can access all assets
- Ownership enforced at service layer using JWT context
- Role-based authorization (USER vs ADMIN)

### Database

- PostgreSQL
- Spring Data JPA
- Hibernate ORM

### Architecture

- Layered architecture
- RESTful API
- Dockerized PostgreSQL

### Testing

- Controller tests using MockMvc
- Service unit tests using Mockito
- Authentication and validation coverage
- Asset ownership coverage
- Role-based access control coverage
- Unauthorized and forbidden access tests
- 24 automated tests passing

### Logging

- Structured application logging using SLF4J
- Successful registration and login events
- Asset create, update, and delete events
- Invalid JWT authentication warnings
- Sensitive values such as passwords and JWT tokens are never logged


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
- Minimum password length validation during registration
- Invalid JWT tokens return `401 Unauthorized`
- Unauthorized ownership access returns `404 Not Found`

### Role-based Access Control (RBAC)

This project supports two roles:

- USER: can manage only their own assets
- ADMIN: can view and manage all assets in the system

Access control is enforced using Spring Security, JWT, and service-layer authorization checks.

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
- SpringDoc OpenAPI (Swagger)
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

### Backend Profiles And Environment Variables

The backend uses Spring profiles for environment-specific configuration.

Local development uses the default `local` profile:

```bash
mvn spring-boot:run
```

Production should run with the `prod` profile:

```bash
SPRING_PROFILES_ACTIVE=prod
```

Required production environment variables:

```text
DATABASE_URL=jdbc:postgresql://<host>:<port>/<database>
DATABASE_USERNAME=<database-user>
DATABASE_PASSWORD=<database-password>
CORS_ALLOWED_ORIGIN=<deployed-frontend-origin>
JWT_SECRET=<at-least-32-character-secret>
PORT=<optional-platform-port>
```

The production profile intentionally does not provide fallback values for database, CORS, or JWT settings. Missing production configuration should fail early instead of accidentally using local development values.

### Run the application

```bash
./mvnw spring-boot:run
```

or from IntelliJ by running:

```
AssetflowBackendApplication
```

### Health Check

```
GET http://localhost:8080/health
```

Expected response:

```
AssetFlow backend is running
```

### Run Automated Tests

```bash
mvn test
```

Current result:

```text
Tests run: 28, Failures: 0, Errors: 0, Skipped: 0
```


---

## Asset API

All Asset endpoints require a valid JWT token.
Authorization: Bearer <JWT_TOKEN>

All asset operations are scoped to the authenticated user.
Users can only access, update, or delete their own assets.


### Admin APIs

GET /api/admin/assets

- Requires ADMIN role
- Returns all assets in the system

---

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


## Swagger UI is available at:

```json
http://localhost:8080/swagger-ui/index.html
```

### Features:

- Interactive API documentation 
- JWT authentication support using the Authorize button 
- Request/response testing directly from the browser

### API Error Handling

The API uses standardized error responses for validation errors, missing resources, and malformed requests.

### Example:

```json
{
"timestamp": "2026-06-15T21:00:00",
"status": 404,
"error": "Not Found",
"message": "Asset not found with id: 123",
"errors": null
}
```


## Author

Talha Bin Shakoor
