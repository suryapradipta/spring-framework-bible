# Spring Framework Bible Documentation

This directory contains documentation for the Spring Framework Bible project.

## Schema Management Documentation

The project implements a flexible multi-schema solution for PostgreSQL databases. These documents provide comprehensive information about how the solution works and how to use it.

### Main Documentation

- [Schema Management](schema-management.md) - Comprehensive reference documentation for the multi-schema solution
- [Multi-Schema Tutorial](multi-schema-tutorial.md) - Step-by-step tutorial with practical examples
- [Diagrams](diagrams/schema-flow.md) - Visual diagrams explaining the schema management flow

### Quick Start

To understand how to work with multiple schemas in this project:

1. First read the [Multi-Schema Tutorial](multi-schema-tutorial.md) for a step-by-step introduction
2. Then review the [Schema Management](schema-management.md) document for detailed reference information
3. Finally, check the [Diagrams](diagrams/schema-flow.md) for visual explanations

### Key Components

The multi-schema solution consists of these key components:

1. **Configuration Properties** (`application.properties`)
   ```properties
   app.schema.default=spring_bible
   app.schema.master=master
   ```

2. **Schema Configuration** (`SchemaConfig.java`)
   - Custom naming strategy for automatic schema qualification
   - R2DBC mapping context configuration

3. **Schema Utilities** (`SchemaUtils.java`)
   - Methods for working with multiple schemas
   - Dynamic schema switching

4. **Clean Entity Classes**
   - Entity classes without hardcoded schema names

## Usage Examples

```java
// Basic repository operations (uses default schema)
Flux<Product> products = productRepository.findAll();

// Using a different schema
Flux<Map<String, Object>> masterData = schemaUtils.executeInSchema(
    schemaUtils.getMasterSchema(),
    "SELECT * FROM reference_data"
).fetch().all();

// Cross-schema join
String query = String.format(
    "SELECT p.*, r.* FROM %s.products p JOIN %s.reference_data r ON p.ref_id = r.id",
    schemaUtils.getDefaultSchema(), 
    schemaUtils.getMasterSchema()
);
```

## Troubleshooting

If you encounter issues with schema resolution:

1. Check the schema exists in your database
2. Verify your configuration properties
3. Enable detailed logging:
   ```properties
   logging.level.org.springframework.data.r2dbc=DEBUG
   ```
4. Check PostgreSQL search path:
   ```sql
   SHOW search_path;
   ``` 