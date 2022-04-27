package com.techelevator.tenmo.model;

public class Account {

    private Long accountId;
    private Long userId;
    private double balance;

    public Account() {
    }

    public Account(Long accountId, Long userId, double balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deductBalance(double amountToDeduct) {
        this.setBalance(getBalance() - amountToDeduct);
    }

    public void increaseBalance(double amountToAdd) {
        this.setBalance(getBalance() + amountToAdd);
    }
}
