package com.zoezhou.fairfax.articleapi.controller;

import com.zoezhou.fairfax.articleapi.ConfigConstants;
import com.zoezhou.fairfax.articleapi.exception.ArticleResouceNotFoundException;
import com.zoezhou.fairfax.articleapi.exception.InvalidRequestException;
import com.zoezhou.fairfax.articleapi.model.Article;
import com.zoezhou.fairfax.articleapi.dto.TagSummary;
import com.zoezhou.fairfax.articleapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;

@RestController
public class ArticleRestController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/articles")
    Article saveArticle(@RequestBody Article article) throws IllegalArgumentException {
        return articleService.saveArticle(article);
    }

    @GetMapping("/articles/{id}")
    Article readArticle(@PathVariable String id)  {
        return articleService.findById(id);
    }

    @GetMapping("/tag/{tagName}/{dateStr}")
    TagSummary readTagSummary(@PathVariable String tagName,
                              @PathVariable String dateStr) {
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(ConfigConstants.DATE_PATTERN));
        } catch (DateTimeParseException ex ) {
            throw new InvalidRequestException("Cannot parse date in url", ex);
        }

        return articleService.buildTagSummaryByTagNameAndDate(tagName, date);
    }


}
