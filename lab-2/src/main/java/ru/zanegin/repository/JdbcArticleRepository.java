package ru.zanegin.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zanegin.exception.DatabaseException;
import ru.zanegin.model.Article;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcArticleRepository implements ArticleRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcArticleRepository.class);
    private final DataSource dataSource;

    public JdbcArticleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        createTableIfNotExists();
    }

    @Override
    public void createTableIfNotExists() { // создаем таблицу
        String sql = """
            CREATE TABLE IF NOT EXISTS lab2_schema.article (
                id SERIAL PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                views INT NOT NULL DEFAULT 0
            )
            """;
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("Table 'lab2_schema.article' checked/created successfully");
        } catch (SQLException e) {
            log.error("Failed to create table", e);
            throw new DatabaseException("Ошибка при создании таблицы", e);
        }
    }

    @Override
    public int save(Article article) { // функция сохранения
        String sql = "INSERT INTO lab2_schema.article (title, views) VALUES (?, ?) RETURNING id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, article.getTitle());
            ps.setInt(2, article.getViews());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {  // переходим на первую и единственную строку результата
                    int id = rs.getInt("id"); // читаем колонку "id"
                    article.setId(id); // устанавливаем ID в объект статьи
                    log.info("Saved article with id: {}", id);
                    return id;
                }
            }
        } catch (SQLException e) {
            log.error("Failed to save article: {}", article.getTitle(), e);
            throw new DatabaseException("Ошибка при сохранении статьи", e);
        }
        throw new DatabaseException("Не удалось получить сгенерированный ID");
    }

    @Override
    public Optional<Article> findById(int id) { // функция поиска
        String sql = "SELECT id, title, views FROM lab2_schema.article WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id); // Подставляем ID в запрос
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Если есть результат
                    // Кладем найденную статью в Optional
                    return Optional.of(new Article(rs.getInt("id"), rs.getString("title"), rs.getInt("views")));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to find article by id: {}", id, e);
            throw new DatabaseException("Ошибка при поиске статьи по ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Article> findByTitle(String title) { // также функция поиска
        String sql = "SELECT id, title, views FROM lab2_schema.article WHERE title = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title); // Подставляем название
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Article(rs.getInt("id"), rs.getString("title"), rs.getInt("views")));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to find article by title: {}", title, e);
            throw new DatabaseException("Ошибка при поиске статьи по названию", e);
        }
        return Optional.empty(); 
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>(); // Создаем пустой список
        String sql = "SELECT id, title, views FROM lab2_schema.article";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) { // Пока есть строки в результате
                articles.add(new Article(rs.getInt("id"), rs.getString("title"), rs.getInt("views")));
            } // Добавляем каждую статью в список
        } catch (SQLException e) {
            log.error("Failed to find all articles", e);
            throw new DatabaseException("Ошибка при получении списка статей", e);
        }
        return articles;
    }

    @Override
    public boolean update(Article article) { // функция обновления
        String sql = "UPDATE lab2_schema.article SET title = ?, views = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, article.getTitle());
            ps.setInt(2, article.getViews());
            ps.setInt(3, article.getId());
            int rows = ps.executeUpdate();
            // executeUpdate() для INSERT/UPDATE/DELETE. Возвращает количество затронутых строк
            if (rows > 0) { // Если хотя бы одна строка обновлена
                log.info("Updated article with id: {}", article.getId());
                return true;
            }
        } catch (SQLException e) {
            log.error("Failed to update article: {}", article.getId(), e);
            throw new DatabaseException("Ошибка при обновлении статьи", e);
        }
        return false; // Ни одна строка не обновлена — статья с таким ID не существует
    }

    @Override
    public void deleteById(int id) { // фукнция удаления
        String sql = "DELETE FROM lab2_schema.article WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                log.info("Deleted article with id: {}", id);
            } else {
                log.warn("Article with id {} not found for deletion", id); // warn — предупреждение, не ошибка. Удаление несуществующей записи — нормально
            }
        } catch (SQLException e) {
            log.error("Failed to delete article: {}", id, e);
            throw new DatabaseException("Ошибка при удалении статьи", e);
        }
    }
}