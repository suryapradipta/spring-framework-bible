# Schema Management Flow Diagrams

These diagrams illustrate how the multi-schema solution works in our Spring Framework Bible application.

## Component Interaction Diagram

```mermaid
graph TD
    A[Application Properties] -->|Provides Schema Names| B[SchemaConfig]
    A -->|Provides Schema Names| C[SchemaUtils]
    B -->|Creates| D[Custom Naming Strategy]
    D -->|Configures| E[R2DBC Mapping Context]
    E -->|Affects| F[Repository Operations]
    C -->|Provides| G[Schema-Specific SQL]
    H[Entity Classes] -->|Clean @Table Annotations| F
    F -->|Uses| I[Database]
    G -->|Executes In| I
```

## Schema Resolution Flow

```mermaid
sequenceDiagram
    participant App as Application
    participant Repo as Repository
    participant NamingStrategy as SchemaQualifyingNamingStrategy
    participant DB as Database

    App->>Repo: findAll()
    Repo->>NamingStrategy: getTableName(Product.class)
    NamingStrategy->>NamingStrategy: Check @Table annotation
    NamingStrategy->>NamingStrategy: Add schema prefix if needed
    NamingStrategy-->>Repo: "spring_bible.products"
    Repo->>DB: SELECT * FROM spring_bible.products
    DB-->>Repo: Results
    Repo-->>App: Product entities
```

## Cross-Schema Query Flow

```mermaid
sequenceDiagram
    participant App as Application
    participant Utils as SchemaUtils
    participant DB as Database

    App->>Utils: executeInSchema("master", "SELECT * FROM table")
    Utils->>Utils: getClientForSchema("master")
    Utils->>DB: SET search_path TO master
    Utils->>DB: SELECT * FROM table
    DB-->>Utils: Results from master.table
    Utils-->>App: Query results
```

## Schema Configuration Structure

```mermaid
classDiagram
    class ApplicationProperties {
        +String app.schema.default
        +String app.schema.master
    }
    
    class SchemaConfig {
        -String defaultSchema
        +R2dbcMappingContext r2dbcMappingContext()
        +R2dbcCustomConversions r2dbcCustomConversions()
    }
    
    class SchemaQualifyingNamingStrategy {
        -String defaultSchema
        +String getTableName(Class type)
        +String getColumnName(Property property)
    }
    
    class SchemaUtils {
        -String defaultSchema
        -String masterSchema
        -DatabaseClient databaseClient
        +String getDefaultSchema()
        +String getMasterSchema()
        +DatabaseClient getClientForSchema(String schema)
        +executeInSchema(String schema, String query)
    }
    
    ApplicationProperties --> SchemaConfig : provides config
    ApplicationProperties --> SchemaUtils : provides config
    SchemaConfig --> SchemaQualifyingNamingStrategy : creates
    SchemaQualifyingNamingStrategy --> SchemaQualifyingNamingStrategy : resolves table names
``` 