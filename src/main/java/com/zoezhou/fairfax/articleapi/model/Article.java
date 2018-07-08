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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private String title;

    private LocalDate date;

    @Column(nullable = false)
    private String body;

    @ElementCollection
    private Set<String> tags = new HashSet<>();

    @JsonIgnore
    private LocalTime timeStamp;

    private Article() {} // JPA only

    public Article( String title, LocalDate date, String body, Set<String> tags) {
        this.title = title;
        this.date = date;
        this.body = body;
        this.tags = tags;
        this.timeStamp = LocalTime.now();
    }

    public void setId(String id) {
        this.id = id;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void setTimeStamp(LocalTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
