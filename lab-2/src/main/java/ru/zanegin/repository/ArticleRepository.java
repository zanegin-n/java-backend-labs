package ru.zanegin.repository;

import ru.zanegin.model.Article;
import java.util.List;

public interface ArticleRepository {
    int save(Article article);
    Article findById(int id);
    Article findByTitle(String title);
    List<Article> findAll();
    boolean update(Article article);
    void deleteById(int id);
    void createTableIfNotExists();
}