package com.example.tabadvertsbusiness.home.models;

public class Contact {
    private int image_id;
    private String link;
    private String name;

    public Contact(int image_id, String link, String name) {
        this.image_id = image_id;
        this.link = link;
        this.name = name;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
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
