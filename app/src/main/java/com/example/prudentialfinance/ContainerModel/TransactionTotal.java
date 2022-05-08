package com.example.prudentialfinance.ContainerModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionTotal {
    @SerializedName("totalbalance")
    @Expose
    private int totalBalance ;

    @SerializedName("month")
    @Expose
    private int month;

    @SerializedName("week")
    @Expose
    private int week;

    @SerializedName("day")
    @Expose
    private int day;

    @SerializedName("year")
    @Expose
    private int year;

    public int getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(int totalBalance) {
        this.totalBalance = totalBalance;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
