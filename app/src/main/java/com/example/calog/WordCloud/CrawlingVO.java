package com.example.calog.WordCloud;

public class CrawlingVO {
    private String title;
    private String link;

    @Override
    public String toString() {
        return "CrawlingVO{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
