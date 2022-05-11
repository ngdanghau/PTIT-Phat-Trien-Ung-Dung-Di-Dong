package com.example.prudentialfinance.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Goal {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("balance")
    @Expose
    private int balance;

    @SerializedName("amount")
    @Expose
    private int amount;

    public int getId() {
        return id;
    }


    @SerializedName("deposit")
    @Expose
    private int deposit;

    @SerializedName("deadline")
    @Expose
    private String deadline;

    @SerializedName("status")
    @Expose
    private int status;

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", amount=" + amount +
                ", deposit=" + deposit +
                ", deadline='" + deadline + '\'' +
                ", status=" + status +
                '}';
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }


}
