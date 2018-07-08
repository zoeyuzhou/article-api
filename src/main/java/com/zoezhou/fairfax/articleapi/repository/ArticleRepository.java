package com.zoezhou.fairfax.articleapi.repository;

import com.zoezhou.fairfax.articleapi.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, String>, ArticleRepositoryCustom {
    Article findOneByTitle(String s);
}
