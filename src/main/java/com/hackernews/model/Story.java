package com.hackernews.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Model class representing a HackerNews Story item
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Story {

    private Long id;
    private String type;
    private String by;
    private Long time;
    private String text;
    private Boolean dead;
    private Long parent;
    private Long poll;
    private List<Long> kids;
    private String url;
    private Integer score;
    private String title;
    private List<Long> parts;
    private Integer descendants;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getDead() {
        return dead;
    }

    public void setDead(Boolean dead) {
        this.dead = dead;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Long getPoll() {
        return poll;
    }

    public void setPoll(Long poll) {
        this.poll = poll;
    }

    public List<Long> getKids() {
        return kids;
    }

    public void setKids(List<Long> kids) {
        this.kids = kids;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Long> getParts() {
        return parts;
    }

    public void setParts(List<Long> parts) {
        this.parts = parts;
    }

    public Integer getDescendants() {
        return descendants;
    }

    public void setDescendants(Integer descendants) {
        this.descendants = descendants;
    }

    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", by='" + by + '\'' +
                ", title='" + title + '\'' +
                ", score=" + score +
                ", descendants=" + descendants +
                '}';
    }
}
