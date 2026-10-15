package ru.zanegin.model;

public class Article {
    private Integer id;
    private String title;
    private Integer views;

    public Article() {
    }

    public Article(Integer id, String title, Integer views) {
        this.id = id;
        this.title = title;
        this.views = views;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "Article{id=" + id + ", title='" + title + "', views=" + views + "}";
    }
}