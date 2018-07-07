package com.zoezhou.fairfax.articleapi.controller;


import com.zoezhou.fairfax.articleapi.ArticleApiApplication;
import com.zoezhou.fairfax.articleapi.model.Article;
import com.zoezhou.fairfax.articleapi.repository.ArticleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArticleApiApplication.class)
@WebAppConfiguration
public class ArticleRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private List<Article> articleList= new ArrayList<>();

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String[] ids = {"1","2","3","4","5","6","7","8","9","10","11"};
        for (String id: ids) {
            articleList.add(articleRepository.save(new Article(id
                    ,id + " latest science shows that potato chips are better for you than sugar"
                    , LocalDate.of(2016,9,22)
                    , id + " some text, potentially containing simple markup about how potato chips are great"
                    , new HashSet<>(Arrays.asList("health", "fitness", "science")))));

        }
    }

    @Test
    public void saveNewArticle() throws Exception {
        String articleJson = "{\"id\":\"200\","
                + "\"title\": \"latest science shows that potato chips are better for you than sugar\","
                + "\"date\":\"2016-09-23\","
                + "\"body\":\"some text, potentially containing simple markup about how potato chips are great\","
                + "\"tags\":[\"health\",\"life\",\"balance\"]}";

        this.mockMvc.perform(post("/articles")
                .contentType(contentType)
                .content(articleJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void updateArticle() throws Exception {
        String articleJson = "{\"id\":\"2\","
                + "\"title\": \"updated latest science shows that potato chips are better for you than sugar\","
                + "\"date\":\"2016-09-22\","
                + "\"body\":\"updated some text, potentially containing simple markup about how potato chips are great\","
                + "\"tags\":[\"health\",\"life\",\"balance\"]}";

        this.mockMvc.perform(post("/articles")
                .contentType(contentType)
                .content(articleJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void saveArticleNoTitle() throws Exception {
        String articleJson = "{\"id\":\"40\","
                + "\"date\":\"20160923\","
                + "\"body\":\"some text, potentially containing simple markup about how potato chips are great\","
                + "\"tags\":[\"health\",\"life\",\"balance\"]}";

        this.mockMvc.perform(post("/articles")
                .contentType(contentType)
                .content(articleJson))
                //          .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveArticleNoId() throws Exception {
        String articleJson = "{"
                + "\"title\": \"updated latest science shows that potato chips are better for you than sugar\","
                + "\"date\":\"20160923\","
                + "\"body\":\"some text, potentially containing simple markup about how potato chips are great\","
                + "\"tags\":[\"health\",\"life\",\"balance\"]}";

        this.mockMvc.perform(post("/articles")
                .contentType(contentType)
                .content(articleJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveArticleEmptyId() throws Exception {
        String articleJson = "{\"id\":\"\","
                + "\"title\": \"latest science shows that potato chips are better for you than sugar\","
                + "\"date\":\"2016-09-23\","
                + "\"body\":\"some text, potentially containing simple markup about how potato chips are great\","
                + "\"tags\":[\"health\",\"life\",\"balance\"]}";

        this.mockMvc.perform(post("/articles")
                .contentType(contentType)
                .content(articleJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveArticleNotValidDateFormat() throws Exception {
        String articleJson = "{\"id\":\"20\","
                + "{\"title\": \"latest science shows that potato chips are better for you than sugar\","
                + "\"date\":\"20160923\","
                + "\"body\":\"some text, potentially containing simple markup about how potato chips are great\","
                + "\"tags\":[\"health\",\"life\",\"balance\"]}";

        this.mockMvc.perform(post("/articles")
                .contentType(contentType)
                .content(articleJson))
      //          .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void readSingleArticle() throws Exception {
        mockMvc.perform(get("/articles/3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is("3")))
                .andExpect(jsonPath("$.title", is("3 latest science shows that potato chips are better for you than sugar")))
                .andExpect(jsonPath("$.date", is("2016-09-22")))
                .andExpect(jsonPath("$.body", is("3 some text, potentially containing simple markup about how potato chips are great")));
    }

    @Test
    public void readSingleArticleNotExist() throws Exception {
        mockMvc.perform(get("/articles/30"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void readTag() throws Exception {
        mockMvc.perform(get("/tag/fitness/20160922"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.tag", is("fitness")))
                .andExpect(jsonPath("$.count", is(11)))
                .andExpect(jsonPath("$.articles", hasSize(10)))
                .andExpect(jsonPath("$.related_tags", hasSize(2)));
    }

    @Test
    public void readTagNotAvailableTag() throws Exception {
        mockMvc.perform(get("/tag/nolife/20160922"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.tag", is("nolife")))
                .andExpect(jsonPath("$.count", is(0)))
                .andExpect(jsonPath("$.articles", hasSize(0)))
                .andExpect(jsonPath("$.related_tags", hasSize(0)));
    }

    @Test
    public void readTagOneArticleNoRelatedTags() throws Exception {
        articleList.add(articleRepository.save(new Article("20"
                ," 20 latest science shows that potato chips are better for you than sugar"
                , LocalDate.of(2016,9,24)
                , "20 some text, potentially containing simple markup about how potato chips are great"
                , new HashSet<>(Arrays.asList("life")))));

        mockMvc.perform(get("/tag/life/20160924"))
 //               .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.tag", is("life")))
                .andExpect(jsonPath("$.count", is(1)))
                .andExpect(jsonPath("$.articles", hasSize(1)))
                .andExpect(jsonPath("$.related_tags", hasSize(0)));
    }

    @Test
    public void readTagNoArticleOnDate() throws Exception {

        mockMvc.perform(get("/tag/life/20180924"))
                //               .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.tag", is("life")))
                .andExpect(jsonPath("$.count", is(0)))
                .andExpect(jsonPath("$.articles", hasSize(0)))
                .andExpect(jsonPath("$.related_tags", hasSize(0)));
    }

    @Test
    public void readTagInvalidDateFormat() throws Exception {

        mockMvc.perform(get("/tag/life/2018-09-24"))
                .andExpect(status().isBadRequest());

    }
}
