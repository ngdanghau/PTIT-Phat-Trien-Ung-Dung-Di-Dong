package com.example.prudentialfinance.Container;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportTotalBalance {
    @SerializedName("result")
    @Expose
    private Integer result;
    @SerializedName("week")
    @Expose
    private Double week;
    @SerializedName("method")
    @Expose
    private String method;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Double getWeek() {
        return week;
    }

    public void setWeek(Double week) {
        this.week = week;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
