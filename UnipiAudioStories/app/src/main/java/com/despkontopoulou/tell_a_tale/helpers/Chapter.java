package com.despkontopoulou.tell_a_tale.helpers;

public class Chapter {
    private String picture;
    private String text;
    public Chapter(){}
    public Chapter(String picture, String text){
        this.picture=picture;
        this.text=text;
    }

    public String getPicture() {
        return picture;
    }

    public String getText() {
        return text;
    }
}
