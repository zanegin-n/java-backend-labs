package ru.zanegin.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class DatabaseConfig {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);
    private final DataSource dataSource; // final означает что поле для хранения пула изменений больше не изменится

    public DatabaseConfig() {
        HikariConfig config = new HikariConfig();
        
        String url = System.getenv().getOrDefault("DATABASE_URL", "jdbc:postgresql://localhost:5432/lab2_db"); // читаем юрл бд из переменной окружения если её нет используем значение по умолчанию
        String user = System.getenv().getOrDefault("DATABASE_USERNAME", "zanegin_user");
        String password = System.getenv().getOrDefault("DATABASE_PASSWORD", "zanegin_password");
        String schema = System.getenv().getOrDefault("DATABASE_SCHEMA", "lab2_schema");

        config.setJdbcUrl(url); // устанавливаем
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(5);
        config.addDataSourceProperty("currentSchema", schema);

        this.dataSource = new HikariDataSource(config);
        log.info("Database connection pool initialized for schema: {}", schema);
    }

    public DataSource getDataSource() {// геттер для получения пула соединений нужен чтобы передать его в репозиторий
        return dataSource;
    }
}