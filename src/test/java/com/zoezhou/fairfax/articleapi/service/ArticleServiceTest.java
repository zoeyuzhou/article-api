package com.zoezhou.fairfax.articleapi.service;

import com.zoezhou.fairfax.articleapi.ArticleApiApplication;
import com.zoezhou.fairfax.articleapi.ConfigConstants;
import com.zoezhou.fairfax.articleapi.dto.TagSummary;
import com.zoezhou.fairfax.articleapi.model.Article;
import com.zoezhou.fairfax.articleapi.repository.ArticleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArticleApiApplication.class)
@WebAppConfiguration
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @MockBean
    private ArticleRepository articleRepository;

    @Before
    public void setUp() {
        List<Article> articles = new ArrayList<>();
        String[] ids = {"1","2","3"};
        for (String id: ids) {
            Article article = new Article("title"
                    ,LocalDate.of(2016,9,22)
                    , "body"
                    , new HashSet<>(Arrays.asList("health", "fitness", "science"))
            );
            article.setId(id);
            articles.add(article);
        }

        LocalDate date = LocalDate.of(2016,9,22);
        Mockito.when(articleRepository.findByDateTagAndCountOrderByTimeStampDesc(date
                , "fitness", ConfigConstants.ARTICLE_NUMBER_IN_TAGSUMMARY))
                .thenReturn(articles);

        Long count = 17L;
        Mockito.when(articleRepository.countByDateAndTag(date, "fitness"))
                .thenReturn(count);
    }

    @Test
    public void buildTagSummaryByTagNameAndDateTest() {
        TagSummary tagSummary = articleService.buildTagSummaryByTagNameAndDate("fitness",LocalDate.of(2016,9,22));
        assertEquals( tagSummary.getCount() , new Long(17) );
        assertThat( tagSummary.getTag(), is("fitness"));
        assertTrue( tagSummary.getRelated_tags().equals(new HashSet<>(Arrays.asList("health", "science"))));
        assertTrue( tagSummary.getArticles().equals(new HashSet<>(Arrays.asList("1", "2", "3"))));
    }
}
