# Spring Framework Bible

A comprehensive reference project demonstrating Spring Framework best practices, focusing on building RESTful APIs with Spring WebFlux, R2DBC, Flyway, and PostgreSQL.

## Project Overview

This project serves as a learning resource and reference implementation for Spring Framework features. It's designed to showcase:

- Clean architecture and project structure
- Reactive programming with Spring WebFlux
- Data access with Spring Data R2DBC
- Database migrations with Flyway
- Best practices for REST API design
- Proper exception handling and validation
- DTOs, mappers, and other patterns

## Technologies

- Java 21
- Spring Boot 3.4.5
- Spring WebFlux (reactive web framework)
- Spring Data R2DBC (reactive database access)
- PostgreSQL (database)
- Flyway (database migrations)
- Lombok (reduces boilerplate code)
- Gradle (build tool)

## Project Structure

The project follows a clean architecture approach with the following layers:

```
src/main/java/example/springframeworkbible/
├── config/          # Configuration classes
├── controller/      # REST API controllers
├── domain/          # Domain entities
├── dto/             # Data Transfer Objects
├── exception/       # Custom exceptions and error handling
├── mapper/          # Entity-DTO mappers
├── repository/      # Data access layer
├── service/         # Business logic layer
└── util/            # Utility classes
```

## Key Features

### 1. REST API with Spring WebFlux

The application demonstrates how to create a reactive RESTful API using Spring WebFlux. It includes:

- Proper use of `@RestController`, `@RequestMapping`, and other annotations
- Handler methods for GET, POST, PUT, DELETE operations
- Path variables, request parameters, and request body handling
- Reactive responses with Mono and Flux

### 2. Clean Architecture

The project demonstrates a clean separation of concerns:

- **Controllers**: Handle HTTP requests and responses
- **Services**: Implement business logic
- **Repositories**: Access data storage
- **DTOs**: Transfer data between layers
- **Mappers**: Convert between entities and DTOs

### 3. Database Access with R2DBC

The project uses Spring Data R2DBC for reactive database access:

- Reactive CRUD operations
- Custom query methods
- Transaction management

### 4. Schema Management with Flyway

Database schema is managed with Flyway migrations:

- Version-controlled database schema
- Automated migrations on application startup

### 5. Exception Handling

The project includes a comprehensive exception handling system:

- Global exception handler with `@RestControllerAdvice`
- Custom exceptions for different error scenarios
- Consistent error responses

### 6. Data Validation

Input validation is implemented using:

- Bean Validation (JSR-380) annotations
- Custom validation logic in services

### 7. Consistent Response Format

All API endpoints return responses in a consistent format:

- Standard response structure with `ApiResponse<T>`
- Proper HTTP status codes
- Informative error messages

## API Endpoints

### Products API

- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/sku/{sku}` - Get product by SKU
- `GET /api/products/search?name={name}` - Search products by name
- `POST /api/products` - Create a new product
- `PUT /api/products/{id}` - Update a product
- `DELETE /api/products/{id}` - Delete a product
- `POST /api/products/{productId}/categories/{categoryId}` - Add a category to a product
- `DELETE /api/products/{productId}/categories/{categoryId}` - Remove a category from a product

### Categories API

- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `GET /api/categories/name/{name}` - Get category by name
- `GET /api/categories/search?name={name}` - Search categories by name
- `POST /api/categories` - Create a new category
- `PUT /api/categories/{id}` - Update a category
- `DELETE /api/categories/{id}` - Delete a category
- `GET /api/categories/product/{productId}` - Get categories for a product

## Getting Started

### Prerequisites

- Java 21 or higher
- PostgreSQL 12 or higher
- Gradle 8.0 or higher

### Database Setup

1. Create a PostgreSQL database:

```sql
CREATE DATABASE spring_bible;
```

2. Update database connection settings in `application.properties` if needed:

```properties
spring.r2dbc.url=r2dbc:postgresql://localhost:5432/spring_bible
spring.r2dbc.username=postgres
spring.r2dbc.password=postgres
```

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:

```bash
./gradlew bootRun
```

The application will start on port 8080, and Flyway will automatically create the database schema.

## Best Practices Demonstrated

### REST API Design

- Use of appropriate HTTP methods (GET, POST, PUT, DELETE)
- Consistent URL patterns
- Proper status codes
- HATEOAS principles

### Error Handling

- Global exception handling
- Specific exception types
- Consistent error responses

### Validation

- Input validation using Bean Validation
- Custom validation logic

### Reactive Programming

- Non-blocking I/O with WebFlux
- Reactive data access with R2DBC
- Proper use of Mono and Flux

### Database Access

- Repository pattern
- Transaction management
- Clean separation from business logic

## Additional Resources

- [Spring Framework Documentation](https://docs.spring.io/spring-framework/reference/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring WebFlux Documentation](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Spring Data R2DBC Documentation](https://docs.spring.io/spring-data/r2dbc/docs/current/reference/html/)
- [Flyway Documentation](https://flywaydb.org/documentation/) 