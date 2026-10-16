package ru.zanegin.repository;

import ru.zanegin.model.Article;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    int save(Article article);
    Optional<Article> findById(int id);
    Optional<Article> findByTitle(String title);
    List<Article> findAll();
    boolean update(Article article);
    void deleteById(int id);
    void createTableIfNotExists();
}