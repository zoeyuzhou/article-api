package com.zoezhou.fairfax.articleapi.controller;

import com.zoezhou.fairfax.articleapi.ConfigConstants;
import com.zoezhou.fairfax.articleapi.exception.ArticleResouceNotFoundException;
import com.zoezhou.fairfax.articleapi.model.Article;
import com.zoezhou.fairfax.articleapi.dto.TagSummary;
import com.zoezhou.fairfax.articleapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@RestController
public class ArticleRestController {

 //   @Autowired
 //   private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/articles/{id}")
    Article readArticle(@PathVariable String id)  {
        Article result = articleService.findById(id);
        return result;
    }

    @GetMapping("/articles")
    Collection<Article> readArticles() throws ArticleResouceNotFoundException {
        List<Article> articleList = articleService.readArticles();
        return articleList;
    }

    @PostMapping("/articles")
    Article saveArticle(@RequestBody Article article) throws IllegalArgumentException {
//    ResponseEntity<?> saveArticle(@RequestBody Article article) {
        Article result = articleService.saveArticle(article);

   /*     URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{id}")
                            .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).build();
        */
        return result;
    }

    @GetMapping("/tag/{tagName}/{dateStr}")
    TagSummary readTagSummary(@PathVariable String tagName,
                              @PathVariable String dateStr) throws ArticleResouceNotFoundException {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(ConfigConstants.DATE_PATTERN));
        return articleService.buildTagSummaryByTagNameAndDate(tagName, date);
    }


}
