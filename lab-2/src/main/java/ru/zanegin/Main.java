package ru.zanegin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zanegin.config.DatabaseConfig;
import ru.zanegin.model.Article;
import ru.zanegin.repository.ArticleRepository;
import ru.zanegin.repository.JdbcArticleRepository;

import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        ArticleRepository repository = new JdbcArticleRepository(dbConfig.getDataSource());

        log.info("=== CREATE ===");
        Article article1 = new Article(null, "Java Basics", 0);
        int id1 = repository.save(article1);
        log.info("Created article with id: {}", id1);

        Article article2 = new Article(null, "Advanced Java", 0);
        int id2 = repository.save(article2);
        log.info("Created article with id: {}", id2);

        log.info("=== READ by ID ===");
        repository.findById(id1).ifPresentOrElse(
                found -> log.info("Found: {}", found),
                () -> log.warn("Article with id {} not found", id1)
        );

        log.info("=== READ by Title ===");
        repository.findByTitle("Advanced Java").ifPresentOrElse(
                found -> log.info("Found by title: {}", found),
                () -> log.warn("Article with title 'Advanced Java' not found")
        );

        log.info("=== UPDATE ===");
        article1.setViews(150);
        boolean updated = repository.update(article1);
        log.info("Updated: {}", updated);

        log.info("=== FIND ALL ===");
        List<Article> all = repository.findAll();
        log.info("All articles: {}", all);

        log.info("=== DELETE ===");
        repository.deleteById(id1);
        log.info("Deleted article with id: {}", id1);

        log.info("=== FINAL STATE ===");
        List<Article> remaining = repository.findAll();
        log.info("Remaining articles: {}", remaining);
    }
}