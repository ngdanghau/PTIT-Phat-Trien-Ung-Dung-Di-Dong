package com.example.prudentialfinance.Container;

import com.example.prudentialfinance.Model.Category;
import com.example.prudentialfinance.Model.Summary;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryGetAll {

    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("summary")
    @Expose
    private Summary summary;

    @SerializedName("data")
    @Expose
    private List<Category> data = null;
    @SerializedName("method")
    @Expose
    private String method;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
