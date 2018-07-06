package com.zoezhou.fairfax.articleapi.dto;

import java.util.Set;


public class TagSummary {

    private String tag;
    private Long count;

    private Set<String> articles;

    private Set<String> related_tags;

    public TagSummary( String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public Long getCount() {
        return count;
    }

    public Set<String> getArticles() {
        return articles;
    }

    public Set<String> getRelated_tags() {
        return related_tags;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setArticles(Set<String> articles) {
        this.articles = articles;
    }

    public void setRelated_tags(Set<String> related_tags) {
        this.related_tags = related_tags;
    }
}
