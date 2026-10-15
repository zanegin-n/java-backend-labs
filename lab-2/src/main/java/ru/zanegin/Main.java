package ru.zanegin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zanegin.model.Article;
import ru.zanegin.repository.ArticleRepository;
import ru.zanegin.repository.JdbcArticleRepository;

import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ArticleRepository repository = new JdbcArticleRepository();

        log.info("=== CREATE ===");
        Article article1 = new Article(null, "Java Basics", 0);
        int id1 = repository.save(article1);
        log.info("Created article with id: {}", id1);

        Article article2 = new Article(null, "Advanced Java", 0);
        int id2 = repository.save(article2);
        log.info("Created article with id: {}", id2);

        log.info("=== READ by ID ===");
        Article found = repository.findById(id1);
        log.info("Found: {}", found);

        log.info("=== READ by Title ===");
        Article foundByTitle = repository.findByTitle("Advanced Java");
        log.info("Found by title: {}", foundByTitle);

        log.info("=== UPDATE ===");
        found.setViews(150);
        boolean updated = repository.update(found);
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