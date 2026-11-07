package ru.zanegin.repository;

import ru.zanegin.model.Article;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    int save(Article article); // сохраняет статью и возвращает ее сгенер ID
    Optional<Article> findById(int id); // ищет по ид
    Optional<Article> findByTitle(String title); // ищет по назван
    List<Article> findAll(); // возвр все статьи
    boolean update(Article article); // обнов статью
    void deleteById(int id); // удаляет по ид
    void createTableIfNotExists(); // созадет табл если ее нету для корректного перезапуска
}