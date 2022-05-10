package com.example.prudentialfinance.Model;

import android.widget.Adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Account implements Serializable{

    @SerializedName("id")
    @Expose
    private Integer id;


    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("balance")
    @Expose
    private Integer balance;


    @SerializedName("accountnumber")
    @Expose
    private String accountnumber;


    @SerializedName("description")
    @Expose
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public Account(Integer id, String name, Integer balance, String accountnumber, String description) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.accountnumber = accountnumber;
        this.description = description;
    }
}
