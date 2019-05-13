package com.a7f.drawingsound.model;

public class Sheet {
    private String composer;
    private String title;
    private String url;
//    private String key;

    public Sheet(){}

    public Sheet(String title, String composer, String url){
        this.title = title;
        this.composer = composer;
        this.url = url;
//        this.key = null;
    }

    public String getTitle(){
        return title;
    }

    public String getComposer(){
        return composer;
    }

    public String getUrl(){
        return url;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setComposer(String composer){
        this.composer = composer;
    }

    public void setUrl(String url){
        this.url = url;
    }

}
