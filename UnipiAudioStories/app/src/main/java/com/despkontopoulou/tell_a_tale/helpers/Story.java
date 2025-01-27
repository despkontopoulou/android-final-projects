package com.despkontopoulou.tell_a_tale.helpers;

import java.util.List;

public class Story {
    private String id;
    private String title;
    private List<Chapter> chapters;

    public Story(){}
    public Story(String title, List<Chapter> chapters){
        this.title=title;
        this.chapters=chapters;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setId(String id){
        this.id=id;
    }
    public String getId(){
        return id;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
