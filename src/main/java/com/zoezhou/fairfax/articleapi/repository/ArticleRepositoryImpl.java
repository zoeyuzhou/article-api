package com.zoezhou.fairfax.articleapi.repository;

import com.zoezhou.fairfax.articleapi.model.Article;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Article> findByDateTagAndCountOrderByTimeStampDesc(LocalDate date, String tag, int count) {
        return entityManager
                .createQuery("SELECT a FROM Article a JOIN a.tags t WHERE t = :tagName and a.date = :date ORDER BY a.timeStamp DESC")
                .setParameter("tagName", tag)
                .setParameter("date", date)
                .setMaxResults(count)
                .getResultList();
    }

    @Override
    public Long countByDateAndTag(LocalDate date, String tag) {
        return Long.parseLong(entityManager
                .createQuery("SELECT count(a) FROM Article a JOIN a.tags t WHERE t = :tagName and a.date = :date")
                .setParameter("tagName", tag)
                .setParameter("date", date)
                .getSingleResult().toString());
    }

    @Override
    public String findMaxId() {
        return entityManager.createQuery("select max(a.id) from Article a")
                .getSingleResult().toString();
    }
}
