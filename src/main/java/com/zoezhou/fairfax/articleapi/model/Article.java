package com.zoezhou.fairfax.articleapi.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Article {

    @Id
    private String id;
    private String title;
    private LocalDate date;
    private String body;


    @ElementCollection
    private Set<String> tags = new HashSet<>();

    @JsonIgnore
    private LocalTime timeStamp;

    private Article() {} // JPA only

    public Article(String id, String title, LocalDate date, String body, Set<String> tags) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.body = body;
        this.tags = tags;
        this.timeStamp = LocalTime.now();
    }



    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }

    public Set<String> getTags() {
        return tags;
    }

}
