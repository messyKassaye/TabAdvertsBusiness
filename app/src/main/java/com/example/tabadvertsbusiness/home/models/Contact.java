package com.example.tabadvertsbusiness.home.models;

public class Contact {
    private String link;
    private String name;

    public Contact( String link, String name) {
        this.link = link;
        this.name = name;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
