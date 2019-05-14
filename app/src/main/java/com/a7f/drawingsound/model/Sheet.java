package com.a7f.drawingsound.model;

import java.util.ArrayList;

public class Sheet {
    private String url;

    // sheet info
    //private String x;
    private String title;
    private String composer;
    private String str;
//    private String source;
//    private String meternome;
//    private String l;
//    private String quickness;
//    private String r;
//    private String key;
//    private String data;

    public Sheet(){}

    public Sheet(String title, String composer, String url){
//        this.x = "1";
        this.title = title;
        this.composer = composer;
//        this.source = "Copyright 2005," + composer;
//        this.meternome = "6/8";
//        this.l="1/8";
//        this.quickness = "3/8=116";
//        this.r = "Creepy Jig";
//        this.key = "Em";
//        this.data = "|:\"Em\"EEE E2G|\"C7\"_B2A G2F|\"Em\"EEE E2G|\\\n" +
//                "\"C7\"_B2A \"B7\"=B3|\"Em\"EEE E2G|\n" +
//                "\"C7\"_B2A G2F|\"Em\"GFE \"D (Bm7)\"F2D|\\\n" +
//                "1\"Em\"E3-E3:|2\"Em\"E3-E2B|:\"Em\"e2e gfe|\n" +
//                "\"G\"g2ab3|\"Em\"gfeg2e|\"D\"fedB2A|\"Em\"e2e gfe|\\\n" +
//                "\"G\"g2ab3|\"Em\"gfe\"D\"f2d|\"Em\"e3-e3:|";
        this.url = url;
        this.str = "X:1\nT: Cooley's\nM: 4/4\nL: 1/8\nR: reel\nK: Emin\nD2|:\"Em\"EB{c}BA B2 EB|~B2 AB dBAG|\"D\"FDAD BDAD|FDAD dAFD|\n\"Em\"EBBA B2 EB|B2 AB defg|\"D\"afe^c dBAF|1\"Em\"DEFD E2 D2:|2\"Em\"DEFD E2 gf||\n|:\"Em\"eB B2 efge|eB B2 gedB|\"D\"A2 FA DAFA|A2 FA defg|\n\"Em\"eB B2 eBgB|eB B2 defg|\"D\"afe^c dBAF|1\"Em\"DEFD E2 gf:|2\"Em\"DEFD E4|]";
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
