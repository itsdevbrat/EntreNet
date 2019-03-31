package com.dev.entrenet;

public class Post {
    String username;
    String title;
    String desc;
    String userdp;

    public Post() {
    }

    public Post(String username, String title, String desc, String userdp) {
        this.username = username;
        this.title = title;
        this.desc = desc;
        this.userdp = userdp;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getUserdp() {
        return userdp;
    }

}

