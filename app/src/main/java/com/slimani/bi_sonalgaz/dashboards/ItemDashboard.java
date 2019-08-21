package com.slimani.bi_sonalgaz.dashboards;

public class ItemDashboard {


    private Long id;
    private String title;
    private int imageId;

    public ItemDashboard(Long id, String title, int imageId) {
        this.id = id;
        this.title = title;
        this.imageId = imageId;
    }

    public ItemDashboard(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
    }


    public ItemDashboard() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
