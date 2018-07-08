package com.zoezhou.fairfax.articleapi.service;

import com.zoezhou.fairfax.articleapi.ConfigConstants;
import com.zoezhou.fairfax.articleapi.dto.ArticleDetails;
import com.zoezhou.fairfax.articleapi.exception.ArticleResouceNotFoundException;
import com.zoezhou.fairfax.articleapi.exception.InvalidRequestException;
import com.zoezhou.fairfax.articleapi.model.Article;
import com.zoezhou.fairfax.articleapi.dto.TagSummary;
import com.zoezhou.fairfax.articleapi.repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class ArticleService {
    private static Logger logger = LoggerFactory.getLogger(ArticleService.class.getName());

    @Autowired
    private ArticleRepository articleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TagSummary buildTagSummaryByTagNameAndDate(String tag, LocalDate date) {
        List<Article> articleList = articleRepository.findByDateTagAndCountOrderByTimeStampDesc(date
                , tag, ConfigConstants.ARTICLE_NUMBER_IN_TAGSUMMARY);

        Long totalCount = articleRepository.countByDateAndTag(date, tag);

        TagSummary tagSummary = new TagSummary(tag);
        tagSummary.setCount(totalCount);

        if ( articleList != null && articleList.size() > 0 ) {
            Set<String> articles = new HashSet<>();
            Set<String> relatedTags = new HashSet<>();
            articleList.forEach(a ->
            {
                articles.add(a.getId());
                relatedTags.addAll(a.getTags());
            });
            relatedTags.remove(tag);

            tagSummary.setArticles(articles);
            tagSummary.setRelated_tags(relatedTags);
        } else {
            tagSummary.setArticles(Collections.emptySet());
            tagSummary.setRelated_tags(Collections.emptySet());
        }

        return tagSummary;
    }

    public List<Article> readArticles() {
        return articleRepository.findAll();
    }

    public Article findById(String id) {
        return articleRepository.findById(id)
                .orElseThrow(ArticleResouceNotFoundException::new);
    }

    public Article updateArticle(ArticleDetails articleDetails) {
        Article savedArticle;

        if ( articleDetails != null  && articleDetails.getId() != null && !articleDetails.getId().isEmpty()) {
            Article article = findById(articleDetails.getId());
            article.setTitle(articleDetails.getTitle());
            article.setDate(articleDetails.getDate());
            article.setBody(articleDetails.getBody());
            article.setTags(articleDetails.getTags());
            article.setTimeStamp(LocalTime.now());
            savedArticle = articleRepository.save(article);
        } else {
            throw new InvalidRequestException("Article should not be null");
        }

        return savedArticle;
    }

    public Article addArticle(ArticleDetails articleDetails) {
        Article savedArticle = null;
        if ( articleDetails != null ) {
            Article article = new Article(articleDetails.getTitle()
                    , articleDetails.getDate(), articleDetails.getBody(), articleDetails.getTags());
            savedArticle = articleRepository.save(article);
        }
        return savedArticle;
    }
}
