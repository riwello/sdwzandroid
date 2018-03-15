package com.zyf.sdwzandroid.model.entity;

public class Collect {


    private String username;

    private Integer newsid;

    public Collect() {
    }

    public Collect(String username, Integer newsid) {
        this.username = username;
        this.newsid = newsid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Integer getNewsid() {
        return newsid;
    }

    public void setNewsid(Integer newsid) {
        this.newsid = newsid;
    }
}