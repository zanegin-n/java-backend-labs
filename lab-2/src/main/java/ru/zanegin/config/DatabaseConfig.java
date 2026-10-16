package ru.zanegin.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class DatabaseConfig {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);
    private final DataSource dataSource;

    public DatabaseConfig() {
        HikariConfig config = new HikariConfig();
        
        String url = System.getenv().getOrDefault("DATABASE_URL", "jdbc:postgresql://localhost:5432/lab2_db");
        String user = System.getenv().getOrDefault("DATABASE_USERNAME", "zanegin_user");
        String password = System.getenv().getOrDefault("DATABASE_PASSWORD", "zanegin_password");
        String schema = System.getenv().getOrDefault("DATABASE_SCHEMA", "lab2_schema");

        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(5);
        config.addDataSourceProperty("currentSchema", schema);

        this.dataSource = new HikariDataSource(config);
        log.info("Database connection pool initialized for schema: {}", schema);
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}