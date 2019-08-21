package com.slimani.bi_sonalgaz.reports;

import java.util.List;

public class ItemReport {



    private Long id;
    private String title;
    private int imageId;
    private String context;
    private String type;
    private List<String> columns;
    private List<String> rows;
    private boolean checked;

    public ItemReport(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
    }

    public ItemReport(String title, int imageId, String context, String type) {
        this.title = title;
        this.imageId = imageId;
        this.context = context;
        this.type = type;
    }

    public ItemReport(String title, int imageId, String context, String type, List<String> columns, List<String> rows) {
        this.title = title;
        this.imageId = imageId;
        this.context = context;
        this.type = type;
        this.columns = columns;
        this.rows = rows;
    }

    public ItemReport() {
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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getRows() {
        return rows;
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
