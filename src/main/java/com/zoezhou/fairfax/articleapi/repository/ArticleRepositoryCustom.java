package com.zoezhou.fairfax.articleapi.repository;

import com.zoezhou.fairfax.articleapi.model.Article;

import java.time.LocalDate;
import java.util.List;

public interface ArticleRepositoryCustom {
    List<Article> findByDateTagAndCountOrderByTimeStampDesc(LocalDate date, String tag, int count);
    Long countByDateAndTag(LocalDate date, String tag);
    String findMaxId();
}
