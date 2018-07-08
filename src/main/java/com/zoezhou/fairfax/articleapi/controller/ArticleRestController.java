package com.zoezhou.fairfax.articleapi.controller;

import com.zoezhou.fairfax.articleapi.ConfigConstants;
import com.zoezhou.fairfax.articleapi.dto.ArticleDetails;
import com.zoezhou.fairfax.articleapi.exception.InvalidRequestException;
import com.zoezhou.fairfax.articleapi.model.Article;
import com.zoezhou.fairfax.articleapi.dto.TagSummary;
import com.zoezhou.fairfax.articleapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;

@RestController
public class ArticleRestController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/articles")
    ResponseEntity saveArticle(@RequestBody ArticleDetails articleDetails) {
        Article savedArticle;
        ResponseEntity responseEntity;
        if ( articleDetails != null ) {
            if ( articleDetails.getId() != null && !articleDetails.getId().isEmpty() ) {
                savedArticle = articleService.updateArticle(articleDetails);
                responseEntity = ResponseEntity.ok().build();
            } else {
                savedArticle = articleService.addArticle(articleDetails);
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedArticle.getId())
                        .toUri();
                responseEntity = ResponseEntity.created(location).build();
            }
        } else {
            throw new InvalidRequestException("Article and article ID should not be null");
        }
        return responseEntity;
    }

    @GetMapping("/articles/{id}")
    Article readArticle(@PathVariable String id)  {
        return articleService.findById(id);
    }

    @GetMapping("/tags/{tagName}/{date}")
    TagSummary readTagSummary(@PathVariable String tagName,
                              @PathVariable String date) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(ConfigConstants.DATE_PATTERN));
        } catch (DateTimeParseException ex ) {
            throw new InvalidRequestException("Cannot parse date in url", ex);
        }

        return articleService.buildTagSummaryByTagNameAndDate(tagName, localDate);
    }

    @GetMapping("/articles")
    Collection<Article> readArticles()  {
        return articleService.readArticles();
    }
}
