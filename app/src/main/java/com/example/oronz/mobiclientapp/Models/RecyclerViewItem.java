package com.example.oronz.mobiclientapp.Models;

public class RecyclerViewItem {

    private int drawableId;
    private String name;
    private String title;
    private String ca_id;

    private String available;

    public RecyclerViewItem(String name, String title, String available,String ca_id) {
        this.name = name;
        this.title = title;
        this.ca_id = ca_id;
        this.available = available;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getAvailable() {
        return available;
    }

    public String getCa_id() {
        return ca_id;
    }
}
