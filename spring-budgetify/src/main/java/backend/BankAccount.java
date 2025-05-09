package com.example.budgetify.model;

public class BankAccount {
    private int accountId;
    private int userId;
    private String accountName;
    private String firstName;
    private String lastName;
    private String dateOpened;
    private int numberWithdrawals;
    private int numberDeposits;
    private String provider;

    // Getters
    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOpened() {
        return dateOpened;
    }

    public int getNumberWithdrawals() {
        return numberWithdrawals;
    }

    public int getNumberDeposits() {
        return numberDeposits;
    }

    public String getProvider() {
        return provider;
    }

    // Setters
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOpened(String dateOpened) {
        this.dateOpened = dateOpened;
    }

    public void setNumberWithdrawals(int numberWithdrawals) {
        this.numberWithdrawals = numberWithdrawals;
    }

    public void setNumberDeposits(int numberDeposits) {
        this.numberDeposits = numberDeposits;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}