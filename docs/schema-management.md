# Multi-Schema Management in Spring R2DBC Applications

This document explains how the multi-schema solution works in our Spring Framework Bible application and how to use it effectively.

## Table of Contents

1. [Overview](#overview)
2. [Key Components](#key-components)
3. [How it Works](#how-it-works)
4. [Usage Examples](#usage-examples)
5. [Advanced Scenarios](#advanced-scenarios)
6. [Troubleshooting](#troubleshooting)

## Overview

In PostgreSQL, schemas are containers that hold tables, views, functions, and other database objects. They allow you to organize database objects into logical groups and provide an additional namespacing layer.

Our application uses a flexible schema management solution that allows:
- Working with tables in the default schema (`spring_bible`)
- Accessing data from other schemas (like `master`)
- Performing cross-schema operations without hardcoded schema names
- Dynamically switching between schemas at runtime

## Key Components

The solution consists of several components:

### 1. Configuration Properties

```properties
# Schema Configuration
spring.r2dbc.properties.schema=
app.schema.default=spring_bible
app.schema.master=master
```

- `app.schema.default`: The primary schema where application tables reside
- `app.schema.master`: Additional schema for master data

### 2. Schema Configuration Class

The `SchemaConfig` class (`src/main/java/example/springframeworkbible/config/SchemaConfig.java`) provides:

- A custom naming strategy that automatically prepends the schema name to tables
- Configuration for the R2DBC mapping context
- Custom conversions if needed

### 3. Schema Utilities

The `SchemaUtils` class (`src/main/java/example/springframeworkbible/util/SchemaUtils.java`) offers:

- Methods to get schema names from configuration
- Functions to create database clients for specific schemas
- Utilities to execute SQL in different schemas

### 4. Clean Entity Classes

Entity classes like `Product` and `Category` have simple table annotations without schema names:

```java
@Table("products")  // No hardcoded schema
public class Product {
    // ...
}
```

## How it Works

The solution operates through several mechanisms:

### 1. Custom Naming Strategy

The `SchemaQualifyingNamingStrategy` intercepts table name resolution:

```java
@Override
public String getTableName(Class<?> type) {
    Table table = type.getAnnotation(Table.class);
    if (table == null) {
        return type.getSimpleName().toLowerCase();
    }

    String tableName = table.value();
    // If the table name already contains a schema, return it as is
    if (tableName.contains(".")) {
        return tableName;
    }

    // Otherwise, prepend the default schema
    return defaultSchema + "." + tableName;
}
```

When Spring Data R2DBC creates SQL statements, it calls this method to get table names. The strategy automatically prepends the schema name to unqualified table names.

### 2. Schema-Switching SQL Execution

For direct SQL statements, `SchemaUtils` provides methods to execute in specific schemas:

```java
public DatabaseClient getClientForSchema(String schema) {
    return (DatabaseClient) databaseClient.inConnection(connection -> {
        return Mono.from(connection.createStatement("SET search_path TO " + schema)
                .execute())
                .thenReturn(connection);
    });
}
```

This method sets the PostgreSQL `search_path` to the specified schema for the current connection.

### 3. Cross-Schema Operations

For operations across schemas, you can:

1. Use fully qualified names in SQL: `SELECT * FROM schema1.table1, schema2.table2`
2. Use the `SchemaUtils` to prepare a connection with a specific search path

## Usage Examples

### 1. Basic Repository Operations

Normal repository operations automatically use the default schema:

```java
// Automatically uses spring_bible.products
Flux<Product> products = productRepository.findAll(); 
```

### 2. Working with a Different Schema

To query from another schema:

```java
// Get data from the master schema
Flux<Map<String, Object>> masterData = schemaUtils.executeInSchema(
    schemaUtils.getMasterSchema(),
    "SELECT * FROM some_master_table"
).fetch().all();
```

### 3. Cross-Schema Joins

Joining data across schemas:

```java
String defaultSchema = schemaUtils.getDefaultSchema();
String masterSchema = schemaUtils.getMasterSchema();

String query = String.format(
    "SELECT p.*, m.* FROM %s.products p " +
    "JOIN %s.some_master_table m ON p.external_id = m.id",
    defaultSchema, masterSchema);
    
Flux<Map<String, Object>> joinedData = databaseClient.sql(query)
    .fetch()
    .all();
```

### 4. Dynamic Schema Selection

Selecting the schema at runtime:

```java
public Mono<Map<String, Object>> getEntityFromSpecificSchema(String schema, String entityId) {
    return schemaUtils.executeInSchema(schema, "SELECT * FROM entities WHERE id = :id")
            .bind("id", entityId)
            .fetch()
            .one();
}
```

## Advanced Scenarios

### Multiple Database Support

The solution can be extended to support multiple databases by:

1. Adding connection properties for each database
2. Creating separate `DatabaseClient` beans for each database
3. Extending `SchemaUtils` to work with multiple clients

### Schema Migration

For schema migration with Flyway:

1. Add separate Flyway configuration for each schema
2. Create migration scripts in separate directories
3. Configure Flyway to run migrations for each schema

## Troubleshooting

### Common Issues

1. **Table not found errors**:
   - Verify schema exists in the database
   - Check that entity classes use correct table names
   - Ensure the schema is correctly configured in properties

2. **Permissions issues**:
   - Ensure database user has permissions on all schemas
   - Check schema ownership and grant necessary privileges

3. **Cross-schema join errors**:
   - Verify foreign key relationships across schemas
   - Ensure proper schema qualification in queries

### Debugging Tips

1. Enable detailed R2DBC logging:
   ```properties
   logging.level.org.springframework.data.r2dbc=DEBUG
   logging.level.io.r2dbc=DEBUG
   ```

2. Trace SQL statements:
   ```properties
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   ```

3. Check schema search path in PostgreSQL:
   ```sql
   SHOW search_path;
   ``` 