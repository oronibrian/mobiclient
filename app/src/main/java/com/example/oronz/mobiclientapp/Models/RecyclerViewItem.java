package com.example.oronz.mobiclientapp.Models;

public class RecyclerViewItem {

    private int drawableId;
    private String name;
    private String title;
    String ca_id;

    private String available;

    public RecyclerViewItem(int drawableId, String name, String title, String ca_id, String available) {
        this.drawableId = drawableId;
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
