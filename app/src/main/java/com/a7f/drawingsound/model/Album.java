package com.a7f.drawingsound.model;

public class Album {
    private String title;
    private String artist;
    private String img;
    private String key;

    public Album(String key, String title, String artist, String img){
        this.title = title;
        this.artist = artist;
        this.img = img;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
