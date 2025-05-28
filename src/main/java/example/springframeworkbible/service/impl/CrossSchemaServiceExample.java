package example.springframeworkbible.service.impl;

import example.springframeworkbible.util.SchemaUtils;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Example service showing how to work with multiple schemas.
 * This is just a demonstration and not part of the core application.
 */
@Service
public class CrossSchemaServiceExample {

    private final DatabaseClient databaseClient;
    private final SchemaUtils schemaUtils;

    public CrossSchemaServiceExample(DatabaseClient databaseClient, SchemaUtils schemaUtils) {
        this.databaseClient = databaseClient;
        this.schemaUtils = schemaUtils;
    }

    /**
     * Example method that queries data from the default schema
     */
    public Flux<Map<String, Object>> getProductsFromDefaultSchema() {
        // This will use the default schema configured in SchemaConfig
        return databaseClient.sql("SELECT * FROM products")
                .fetch()
                .all();
    }

    /**
     * Example method that queries data from the master schema
     */
    public Flux<Map<String, Object>> getDataFromMasterSchema() {
        // This will use the master schema
        return schemaUtils.getMasterSchemaClient()
                .sql("SELECT * FROM some_master_table")
                .fetch()
                .all();
    }

    /**
     * Example method that joins data across schemas
     */
    public Flux<Map<String, Object>> joinDataAcrossSchemas() {
        String defaultSchema = schemaUtils.getDefaultSchema();
        String masterSchema = schemaUtils.getMasterSchema();
        
        String query = String.format(
                "SELECT p.*, m.* FROM %s.products p " +
                "JOIN %s.some_master_table m ON p.external_id = m.id",
                defaultSchema, masterSchema);
                
        // Execute the cross-schema query
        return databaseClient.sql(query)
                .fetch()
                .all();
    }

    /**
     * Example method that dynamically switches between schemas
     */
    public Mono<Map<String, Object>> getEntityFromSpecificSchema(String schema, String entityId) {
        return schemaUtils.executeInSchema(schema, "SELECT * FROM entities WHERE id = :id")
                .bind("id", entityId)
                .fetch()
                .one();
    }
} 