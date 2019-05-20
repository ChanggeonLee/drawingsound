package com.a7f.drawingsound.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Sheet {
    private String url;
    private String title;
    private String composer;
    private String str;
    private String date;
    private String mood;
    private String key;

    //    private String[] note;
    //    private String source;
    //    private String meternome;
    //    private String l;
    //    private String quickness;
    //    private String r;
    //    private String key;
    //    private String data;

    public Sheet(){}

    public Sheet(String title, String composer, String url, String date, String mood, String sheet){
        this.title = title;
        this.composer = composer;
        this.url = url;
        this.date = date;
        this.mood = mood;
        this.str =  "X: 1" +
                    "\nT: " + title +
                    "\nC: " + composer +
                    "\nS: Copyright 2019, "+ composer +
                    "\nM: 4/4" +
                    "\nL: 1/8" +
                    "\nR: reel" +
                    "\nK: Amin" +
                    "\n" + sheet;
    }

    public String getKey() {
        return key;
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

    public String getDate() { return date; }

    public String getStr() {
        return str;
    }

    //    public String getSource() {
    //        return source;
    //    }
    //
    //    public String getMeternome() {
    //        return meternome;
    //    }
    //
    //    public String getL() {
    //        return l;
    //    }
    //
    //    public String getQuickness() {
    //        return quickness;
    //    }
    //
    //    public String getR() {
    //        return r;
    //    }
    //
    //    public String getKey() {
    //        return key;
    //    }
    //
    //    public String getData() {
    //        return data;
    //    }
    //
    //    public String getX() {
    //        return x;
    //    }


    public void setKey(String key) {
        this.key = key;
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

    public void setStr(String str) {
        this.str = str;
    }


    public void setDate(String date) { this.date = date; }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    //    public void setX(String x) {
//        this.x = x;
//    }
//
//    public void setSource(String source) {
//        this.source = source;
//    }
//
//    public void setMeternome(String meternome) {
//        this.meternome = meternome;
//    }
//
//    public void setL(String l) {
//        this.l = l;
//    }
//
//    public void setQuickness(String quickness) {
//        this.quickness = quickness;
//    }
//
//    public void setR(String r) {
//        this.r = r;
//    }
//
//    public void setKey(String key) {
//        this.key = key;
//    }
//
//    public void setData(String data) {
//        this.data = data;
//    }
}
