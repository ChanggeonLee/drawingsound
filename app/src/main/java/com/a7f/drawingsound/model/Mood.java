package com.a7f.drawingsound.model;

public class Mood {

    private String img;
    private String mood;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public Mood(String img, String mood) {

        this.img = img;
        this.mood = mood;

    }
}
