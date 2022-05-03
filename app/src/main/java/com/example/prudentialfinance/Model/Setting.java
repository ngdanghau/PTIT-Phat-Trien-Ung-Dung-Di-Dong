package com.example.prudentialfinance.Model;

public class Setting {
    String title;
    Integer icon;

    public Setting() {
    }

    public Setting(String title, Integer icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "title='" + title + '\'' +
                ", icon=" + icon +
                '}';
    }
}
