package example.springframeworkbible.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Utility class for working with multiple database schemas.
 * Provides methods to create database clients for specific schemas.
 */
@Component
public class SchemaUtils {

    private final DatabaseClient databaseClient;
    
    @Value("${app.schema.default}")
    private String defaultSchema;
    
    @Value("${app.schema.master}")
    private String masterSchema;

    public SchemaUtils(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    /**
     * Gets the default schema name
     * @return The default schema name
     */
    public String getDefaultSchema() {
        return defaultSchema;
    }

    /**
     * Gets the master schema name
     * @return The master schema name
     */
    public String getMasterSchema() {
        return masterSchema;
    }

    /**
     * Creates a database client that will execute queries in the specified schema.
     * This is useful for cross-schema queries.
     * 
     * @param schema The schema to use
     * @return A DatabaseClient configured for the specified schema
     */
    public DatabaseClient getClientForSchema(String schema) {
        return (DatabaseClient) databaseClient.inConnection(connection -> {
            return Mono.from(connection.createStatement("SET search_path TO " + schema)
                    .execute())
                    .thenReturn(connection);
        });
    }

    /**
     * Get a database client for the master schema
     * @return A DatabaseClient configured for the master schema
     */
    public DatabaseClient getMasterSchemaClient() {
        return getClientForSchema(masterSchema);
    }

    /**
     * Executes a query in the specified schema
     * 
     * @param schema The schema to use
     * @param query The SQL query to execute
     * @return A DatabaseClient.GenericExecuteSpec for the query
     */
    public DatabaseClient.GenericExecuteSpec executeInSchema(String schema, String query) {
        return getClientForSchema(schema).sql(query);
    }
} 
