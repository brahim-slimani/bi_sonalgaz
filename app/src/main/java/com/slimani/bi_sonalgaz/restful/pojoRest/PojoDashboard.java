package com.slimani.bi_sonalgaz.restful.pojoRest;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PojoDashboard {

    private Long id;
    private String title;
    private String username;
    private List<PojoReport> reports = new ArrayList<>();
    private JSONArray reportsjsonarray = new JSONArray();
    private String reportsString;



    public PojoDashboard() {
    }

    public PojoDashboard(Long id, String title) {
        this.id = id;
        this.title = title;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<PojoReport> getReports() {
        return reports;
    }

    public void setReports(List<PojoReport> reports) {
        this.reports = reports;
    }

    public JSONArray getReportsjsonarray() {
        return reportsjsonarray;
    }

    public void setReportsjsonarray(JSONArray reportsjsonarray) {
        this.reportsjsonarray = reportsjsonarray;
    }

    public String getReportsString() {
        return reportsString;
    }

    public void setReportsString(String reportsString) {
        this.reportsString = reportsString;
    }
}
