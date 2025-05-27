package example.springframeworkbible.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;

/**
 * R2DBC Configuration for PostgreSQL database connection.
 * This class enables reactive repositories and configures the connection factory.
 */
@Configuration
@EnableR2dbcRepositories(basePackages = "example.springframeworkbible.repository")
public class R2dbcConfig extends AbstractR2dbcConfiguration {
    
    @Value("${spring.r2dbc.url}")
    private String url;
    
    @Value("${spring.r2dbc.username}")
    private String username;
    
    @Value("${spring.r2dbc.password}")
    private String password;
    
    @Value("${spring.r2dbc.database:spring_bible}")
    private String database;
    
    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        // Create PostgreSQL connection factory with schema search path
        return new PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host("localhost")
                .port(5432)
                .database(database)
                .username(username)
                .password(password)
                .schema("spring_bible")
                // Set search path to include both schemas
                .preparedStatementCacheQueries(0) // Disable for development
                .build()
        );
    }
    
    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        DatabaseClient client = DatabaseClient.create(connectionFactory);
        // Set search path to include both schemas when connection is established
        client.sql("SET search_path TO spring_bible, master, public").fetch().rowsUpdated().subscribe();
        return client;
    }
    
    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
    
    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }
    
    /**
     * Initializes the database with schema scripts if needed.
     * Note: In production, you should use Flyway for schema migrations.
     */
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        return initializer;
    }
}
