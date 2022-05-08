package com.example.prudentialfinance.ContainerModel;

import com.example.prudentialfinance.Model.Account;
import com.example.prudentialfinance.Model.Category;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionDetail {
    @SerializedName("amount")
    @Expose
    private Integer amount;


    @SerializedName("description")
    @Expose
    private String description;


    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("reference")
    @Expose
    private String reference;


    @SerializedName("transactiondate")
    @Expose
    private String transactiondate;


    @SerializedName("id")
    @Expose
    private Integer id;


    @SerializedName("type")
    @Expose
    private Integer type;


    @SerializedName("account")
    @Expose
    private Account account;


    @SerializedName("category")
    @Expose
    private Category category;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTransactiondate() {
        return transactiondate;
    }

    public void setTransactiondate(String transactiondate) {
        this.transactiondate = transactiondate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "TransactionDetail{" +
                "amount=" + amount +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", reference='" + reference + '\'' +
                ", transactiondate='" + transactiondate + '\'' +
                ", id=" + id +
                ", type=" + type +
                ", account=" + account +
                ", category=" + category +
                '}';
    }
}
