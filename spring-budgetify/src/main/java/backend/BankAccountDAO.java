package com.example.budgetify.dao;
import backend.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankAccountDAO {

    // INSERT a new bank account
    public static void addBankAccount(int userId, String accountName, String firstName, String lastName,
            String dateOpened, int numberWithdrawals, int numberDeposits, String provider) {
        String sql = "INSERT INTO BankAccounts (UserID, Account_name, First_name, Last_name, Date_opened, Number_withdrawals, Number_deposits, Provider) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, accountName);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, dateOpened);
            stmt.setInt(6, numberWithdrawals);
            stmt.setInt(7, numberDeposits);
            stmt.setString(8, provider);

            stmt.executeUpdate();
            System.out.println("✅ Bank account added for user " + userId);

        } catch (SQLException e) {
            System.out.println("❌ Failed to add bank account: " + e.getMessage());
        }
    }

    // FETCH bank accounts for a given user
    public static List<String> getBankAccountsByUserId(int userId) {
        String sql = "SELECT * FROM BankAccounts WHERE UserID = ?";
        List<String> accounts = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String a = rs.getInt("AccountID") + " | " +
                        rs.getString("Account_name") + " | " +
                        rs.getString("First_name") + " | " +
                        rs.getString("Last_name") + " | " +
                        rs.getString("Date_opened") + " | " +
                        rs.getInt("Number_withdrawals") + " | " +
                        rs.getInt("Number_deposits") + " | " +
                        rs.getString("Provider");
                accounts.add(a);
            }

        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch bank accounts: " + e.getMessage());
        }

        return accounts;
    }

    // UPDATE a bank account by ID
    public static void updateBankAccount(int accountId, String newAccountName, String newFirstName, String newLastName,
            String newDateOpened, int newNumberWithdrawals, int newNumberDeposits, String newProvider) {
        String sql = "UPDATE BankAccounts SET Account_name = ?, First_name = ?, Last_name = ?, Date_opened = ?, Number_withdrawals = ?, Number_deposits = ?, Provider = ? WHERE AccountID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newAccountName);
            stmt.setString(2, newFirstName);
            stmt.setString(3, newLastName);
            stmt.setString(4, newDateOpened);
            stmt.setInt(5, newNumberWithdrawals);
            stmt.setInt(6, newNumberDeposits);
            stmt.setString(7, newProvider);
            stmt.setInt(8, accountId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Bank account updated.");
            } else {
                System.out.println("⚠️ No bank account found with that ID.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Failed to update bank account: " + e.getMessage());
        }
    }

    // DELETE a bank account by ID
    public static void deleteBankAccount(int accountId) {
        String sql = "DELETE FROM BankAccounts WHERE AccountID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✅ Bank account deleted.");
            } else {
                System.out.println("⚠️ No bank account found with that ID.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete bank account: " + e.getMessage());
        }
    }
}