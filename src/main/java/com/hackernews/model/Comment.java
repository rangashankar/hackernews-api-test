package com.hackernews.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Model class representing a HackerNews Comment item
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {

    private Long id;
    private String type;
    private String by;
    private Long time;
    private String text;
    private Boolean dead;
    private Long parent;
    private List<Long> kids;

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

    public List<Long> getKids() {
        return kids;
    }

    public void setKids(List<Long> kids) {
        this.kids = kids;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", by='" + by + '\'' +
                ", parent=" + parent +
                ", text='" + (text != null && text.length() > 50 ? text.substring(0, 50) + "..." : text) + '\'' +
                '}';
    }
}
