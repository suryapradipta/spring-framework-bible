package example.springframeworkbible.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SchemaConfig {

    @Value("${app.schema.default}")
    private String defaultSchema;

    /**
     * Custom naming strategy that prepends the schema name to table names
     * that don't already have a schema specified.
     */
    @Bean
    public R2dbcMappingContext r2dbcMappingContext(R2dbcCustomConversions customConversions) {
        R2dbcMappingContext context = new R2dbcMappingContext(new SchemaQualifyingNamingStrategy(defaultSchema));
        context.setSimpleTypeHolder(customConversions.getSimpleTypeHolder());
        return context;
    }

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }

    /**
     * Custom naming strategy that prepends the schema name to tables
     */
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

        @Override
        public String getColumnName(RelationalPersistentProperty property) {
            return NamingStrategy.super.getColumnName(property);
        }

        @Override
        public String getReverseColumnName(RelationalPersistentProperty property) {
            return NamingStrategy.super.getReverseColumnName(property);
        }

        @Override
        public String getKeyColumn(RelationalPersistentProperty property) {
            return NamingStrategy.super.getKeyColumn(property);
        }
    }
} 