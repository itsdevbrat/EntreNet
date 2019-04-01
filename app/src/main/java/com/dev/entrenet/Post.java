package com.dev.entrenet;

public class Post {
    String username;
    String title;
    String desc;
    String userdp;
    double latitude;
    double longitude;

    public Post() {
    }

    public Post(String username, String title, String desc, String userdp ,double latitude,double longitude) {
        this.username = username;
        this.title = title;
        this.desc = desc;
        this.userdp = userdp;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

