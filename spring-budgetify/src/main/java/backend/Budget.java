package com.example.budgetify.model;

public class Budget {
    private int budgetId;
    private int userId;
    private double monthlyBudget;
    private String month;

    // Getters
    public int getBudgetId() {
        return budgetId;
    }

    public int getUserId() {
        return userId;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public String getMonth() {
        return month;
    }

    // Setters
    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
