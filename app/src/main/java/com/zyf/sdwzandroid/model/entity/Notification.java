package com.zyf.sdwzandroid.model.entity;

import java.util.Date;

public class Notification {
    private String username;
    private long time;
    private String content;

    public Notification(String username, long time, String content) {
        this.username = username;
        this.time = time;
        this.content = content;
    }

    public Notification() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
