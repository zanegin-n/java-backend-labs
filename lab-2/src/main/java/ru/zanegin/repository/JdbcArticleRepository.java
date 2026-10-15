package ru.zanegin.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zanegin.exception.DatabaseException;
import ru.zanegin.model.Article;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcArticleRepository implements ArticleRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcArticleRepository.class);
    private final DataSource dataSource;

    public JdbcArticleRepository() {
        this.dataSource = createDataSource();
        createTableIfNotExists();
    }

    private DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/lab2_db");
        config.setUsername("zanegin_user");
        config.setPassword("zanegin_password");
        config.setMaximumPoolSize(5);
        return new HikariDataSource(config);
    }

    @Override
    public void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS article (
                id SERIAL PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                views INT NOT NULL DEFAULT 0
            )
            """;
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("Table 'article' checked/created successfully");
        } catch (SQLException e) {
            log.error("Failed to create table", e);
            throw new DatabaseException("Ошибка при создании таблицы", e);
        }
    }

    @Override
    public int save(Article article) {
        String sql = "INSERT INTO article (title, views) VALUES (?, ?) RETURNING id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, article.getTitle());
            ps.setInt(2, article.getViews());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    article.setId(id);
                    log.info("Saved article with id: {}", id);
                    return id;
                }
            }
        } catch (SQLException e) {
            log.error("Failed to save article", e);
            throw new DatabaseException("Ошибка при сохранении статьи", e);
        }
        throw new DatabaseException("Не удалось получить ID");
    }

    @Override
    public Article findById(int id) {
        String sql = "SELECT id, title, views FROM article WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Article(rs.getInt("id"), rs.getString("title"), rs.getInt("views"));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to find article by id: {}", id, e);
            throw new DatabaseException("Ошибка при поиске по ID", e);
        }
        return null;
    }

    @Override
    public Article findByTitle(String title) {
        String sql = "SELECT id, title, views FROM article WHERE title = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Article(rs.getInt("id"), rs.getString("title"), rs.getInt("views"));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to find article by title: {}", title, e);
            throw new DatabaseException("Ошибка при поиске по названию", e);
        }
        return null;
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT id, title, views FROM article";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                articles.add(new Article(rs.getInt("id"), rs.getString("title"), rs.getInt("views")));
            }
        } catch (SQLException e) {
            log.error("Failed to find all articles", e);
            throw new DatabaseException("Ошибка при получении списка", e);
        }
        return articles;
    }

    @Override
    public boolean update(Article article) {
        String sql = "UPDATE article SET title = ?, views = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, article.getTitle());
            ps.setInt(2, article.getViews());
            ps.setInt(3, article.getId());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                log.info("Updated article with id: {}", article.getId());
                return true;
            }
        } catch (SQLException e) {
            log.error("Failed to update article: {}", article.getId(), e);
            throw new DatabaseException("Ошибка при обновлении", e);
        }
        return false;
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM article WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                log.info("Deleted article with id: {}", id);
            } else {
                log.warn("Article with id {} not found", id);
            }
        } catch (SQLException e) {
            log.error("Failed to delete article: {}", id, e);
            throw new DatabaseException("Ошибка при удалении", e);
        }
    }
}