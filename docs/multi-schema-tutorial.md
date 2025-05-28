# Multi-Schema Tutorial: Understanding and Implementation

This tutorial walks you through how our multi-schema solution works and provides practical examples to help you understand how to use and extend it.

## Understanding Database Schemas

Before diving into the implementation, let's understand what database schemas are:

- **Schema**: A namespace that contains database objects like tables, views, indexes, etc.
- **PostgreSQL Schema**: In PostgreSQL, schemas allow you to organize database objects into logical groups.
- **Schema Qualification**: When referring to a table, you can qualify it with its schema name: `schema_name.table_name`.
- **Search Path**: PostgreSQL uses a search path to determine which schema to look in when an unqualified name is used.

## The Challenge

In a typical Spring application, we often face these challenges:

1. **Hardcoded Schema Names**: Entity classes with hardcoded schema names (`@Table("schema.table")`) are not flexible.
2. **Cross-Schema Operations**: Accessing tables from multiple schemas is difficult.
3. **Dynamic Schema Selection**: Changing schemas at runtime is complex.
4. **Code Maintenance**: Changing the schema requires updating all entity classes.

## Our Solution

We've implemented a flexible schema management solution that addresses these challenges:

1. **Configuration-Based**: Schemas are defined in configuration, not code.
2. **Automatic Schema Qualification**: A custom naming strategy automatically adds schema names.
3. **Schema Utilities**: Tools for working with multiple schemas dynamically.
4. **Clean Entity Classes**: No hardcoded schema names in entity classes.

## Tutorial: How to Use the Multi-Schema Solution

### Step 1: Configure Schemas

First, configure your schemas in `application.properties`:

```properties
# Schema Configuration
app.schema.default=spring_bible
app.schema.master=master
```

These properties define:
- `app.schema.default`: The primary schema for your application tables
- `app.schema.master`: A secondary schema for master data

### Step 2: Create Clean Entity Classes

Create entity classes without hardcoded schema names:

```java
@Data
@Table("products")  // No schema name here
public class Product {
    @Id
    private Long id;
    
    @Column("name")
    private String name;
    
    // Other fields...
}
```

### Step 3: Basic Repository Operations

Standard repository operations automatically use the default schema:

```java
@Service
public class ProductService {
    private final ProductRepository productRepository;
    
    // Constructor...
    
    public Flux<Product> getAllProducts() {
        // This automatically uses the default schema (spring_bible.products)
        return productRepository.findAll();
    }
    
    public Mono<Product> createProduct(Product product) {
        // This automatically uses the default schema
        return productRepository.save(product);
    }
}
```

### Step 4: Working with Multiple Schemas

To work with tables in other schemas, use the `SchemaUtils`:

```java
@Service
public class MultiSchemaService {
    private final SchemaUtils schemaUtils;
    private final DatabaseClient databaseClient;
    
    // Constructor...
    
    public Flux<Map<String, Object>> getMasterData() {
        // Query data from the master schema
        return schemaUtils.executeInSchema(
                schemaUtils.getMasterSchema(),
                "SELECT * FROM reference_data"
            )
            .fetch()
            .all();
    }
    
    public Flux<Map<String, Object>> getProductsWithMasterData() {
        // Cross-schema join
        String defaultSchema = schemaUtils.getDefaultSchema();
        String masterSchema = schemaUtils.getMasterSchema();
        
        String query = String.format(
                "SELECT p.*, r.* FROM %s.products p " +
                "JOIN %s.reference_data r ON p.ref_id = r.id",
                defaultSchema, masterSchema);
                
        return databaseClient.sql(query)
                .fetch()
                .all();
    }
}
```

### Step 5: Dynamic Schema Selection

For dynamic schema selection at runtime:

```java
@RestController
@RequestMapping("/api/data")
public class MultiSchemaController {
    private final SchemaUtils schemaUtils;
    
    // Constructor...
    
    @GetMapping("/{schema}/{table}")
    public Flux<Map<String, Object>> getDataFromSchema(
            @PathVariable String schema,
            @PathVariable String table) {
        
        // Security check to prevent SQL injection
        validateSchemaAndTable(schema, table);
        
        // Execute query in the specified schema
        return schemaUtils.executeInSchema(
                schema,
                "SELECT * FROM " + table
            )
            .fetch()
            .all();
    }
    
    private void validateSchemaAndTable(String schema, String table) {
        // Implement validation logic
        // Only allow specific schemas and tables
    }
}
```

## Technical Implementation Details

### 1. Custom Naming Strategy

The `SchemaQualifyingNamingStrategy` is the core of our solution:

```java
private static class SchemaQualifyingNamingStrategy implements NamingStrategy {
    private final String defaultSchema;

    public SchemaQualifyingNamingStrategy(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

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
    
    // Other methods...
}
```

This strategy intercepts the table name resolution process and automatically adds the schema name to unqualified table names.

### 2. Schema Utilities

The `SchemaUtils` class provides methods for working with schemas:

```java
@Component
public class SchemaUtils {
    private final DatabaseClient databaseClient;
    
    @Value("${app.schema.default}")
    private String defaultSchema;
    
    @Value("${app.schema.master}")
    private String masterSchema;
    
    // Constructor...
    
    public DatabaseClient getClientForSchema(String schema) {
        return (DatabaseClient) databaseClient.inConnection(connection -> {
            return Mono.from(connection.createStatement("SET search_path TO " + schema)
                    .execute())
                    .thenReturn(connection);
        });
    }
    
    public DatabaseClient.GenericExecuteSpec executeInSchema(String schema, String query) {
        return getClientForSchema(schema).sql(query);
    }
    
    // Other methods...
}
```

This utility class uses PostgreSQL's `SET search_path` command to dynamically change the schema for a connection.

## Practical Exercises

### Exercise 1: Query Data from Multiple Schemas

Try implementing a service that retrieves data from both schemas and combines the results:

```java
public Flux<CombinedData> getCombinedData() {
    // Get products from the default schema
    Flux<Product> products = productRepository.findAll();
    
    // Get reference data from the master schema
    Flux<Map<String, Object>> referenceData = schemaUtils.executeInSchema(
            schemaUtils.getMasterSchema(),
            "SELECT * FROM reference_data"
        )
        .fetch()
        .all();
    
    // Combine the data
    return products.flatMap(product -> 
        referenceData
            .filter(ref -> matchesProduct(product, ref))
            .next()
            .map(ref -> combineData(product, ref))
    );
}
```

### Exercise 2: Create a Multi-Tenant Application

Extend the solution to support multi-tenancy:

1. Add a tenant identifier to your configuration
2. Create a schema per tenant
3. Select the tenant schema dynamically based on a request parameter
4. Implement security checks to ensure a tenant can only access their schema

## Conclusion

This multi-schema solution provides a flexible way to work with multiple schemas in a Spring R2DBC application. By using configuration-based schema management and custom naming strategies, you can keep your entity classes clean and your code maintainable. 