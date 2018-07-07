package com.zoezhou.fairfax.articleapi.controller;

import com.zoezhou.fairfax.articleapi.ConfigConstants;
import com.zoezhou.fairfax.articleapi.exception.ArticleResouceNotFoundException;
import com.zoezhou.fairfax.articleapi.exception.InvalidRequestException;
import com.zoezhou.fairfax.articleapi.model.Article;
import com.zoezhou.fairfax.articleapi.dto.TagSummary;
import com.zoezhou.fairfax.articleapi.repository.ArticleRepository;
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
import java.util.List;
import java.util.Optional;

@RestController
public class ArticleRestController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/articles")
    ResponseEntity saveArticle(@RequestBody Article article) {
        boolean articleExists = articleService.articleExists(article);
        Article savedArticle = articleService.saveArticle(article);
        ResponseEntity responseEntity;
        if ( articleExists ) {
            responseEntity = ResponseEntity.ok().build();
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedArticle.getId())
                    .toUri();
            responseEntity = ResponseEntity.created(location).build();
        }
        return responseEntity;
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
