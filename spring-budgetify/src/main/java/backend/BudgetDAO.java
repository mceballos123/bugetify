package com.example.budgetify.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import backend.DatabaseConnection;

public class BudgetDAO {

    // INSERT a new budget
    public static void addBudget(int userId, double amount, String month) {
        String sql = "INSERT INTO Budget (UserID, MonthlyBudget, Month) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setDouble(2, amount);
            stmt.setString(3, month);

            stmt.executeUpdate();
            System.out.println("✅ Budget added for user " + userId + " (" + month + ")");

        } catch (SQLException e) {
            System.out.println("❌ Failed to add budget: " + e.getMessage());
        }
    }

    // FETCH budget(s) for a user
    public static List<String> getBudgetsByUserId(int userId) {
        String sql = "SELECT * FROM Budget WHERE UserID = ?";
        List<String> budgets = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String b = rs.getInt("BudgetID") + " | $" +
                        rs.getDouble("MonthlyBudget") + " | " +
                        rs.getString("Month");
                budgets.add(b);
            }

        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch budgets: " + e.getMessage());
        }

        return budgets;
    }

    // UPDATE a budget by ID
    public static void updateBudget(int budgetId, double newMonthlyBudget, String newMonth) {
        String sql = "UPDATE Budget SET MonthlyBudget = ?, Month = ? WHERE BudgetID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newMonthlyBudget);
            stmt.setString(2, newMonth);
            stmt.setInt(3, budgetId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Budget updated.");
            } else {
                System.out.println("⚠️ No budget found with that ID.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Failed to update budget: " + e.getMessage());
        }
    }

    // DELETE a budget by ID
    public static void deleteBudget(int budgetId) {
        String sql = "DELETE FROM Budget WHERE BudgetID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, budgetId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✅ Budget deleted.");
            } else {
                System.out.println("⚠️ No budget found with that ID.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete budget: " + e.getMessage());
        }
    }
}
