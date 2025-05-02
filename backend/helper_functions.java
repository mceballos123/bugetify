import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class helper_functions {

    private static final String DB_URL = "jdbc:sqlite:budgetify.db";

    // Method to create the SQLite database
    public static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("Database created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error creating database: " + e.getMessage());
        }
    }

    // Method to execute the schema
    public static void executeSchema() {
        String schema = """
            CREATE TABLE IF NOT EXISTS Users (
                UserID INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL UNIQUE,
                name TEXT NOT NULL,
                password TEXT NOT NULL
            );

            CREATE TABLE IF NOT EXISTS Transactions (
                TransactionID INTEGER PRIMARY KEY AUTOINCREMENT,
                UserID INTEGER NOT NULL,
                Title TEXT NOT NULL,
                Amount REAL NOT NULL,
                TransactionCategory TEXT NOT NULL,
                TransactionDate TEXT NOT NULL,
                PaymentMethod TEXT NOT NULL,
                FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(schema);
            System.out.println("Schema executed successfully.");
        } catch (SQLException e) {
            System.out.println("Error executing schema: " + e.getMessage());
        }
    }

    // Helper function to update a transaction
    public static void updateTransaction(int transactionID, String title, double amount, String category, String date, String method) {
        String sql = "UPDATE Transactions SET Title = ?, Amount = ?, TransactionCategory = ?, TransactionDate = ?, PaymentMethod = ? WHERE TransactionID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, category);
            pstmt.setString(4, date);
            pstmt.setString(5, method);
            pstmt.setInt(6, transactionID);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated + " transaction(s) updated.");
        } catch (SQLException e) {
            System.out.println("Error updating transaction: " + e.getMessage());
        }
    }

    // Helper function to delete a transaction
    public static void deleteTransaction(int transactionID) {
        String sql = "DELETE FROM Transactions WHERE TransactionID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transactionID);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted + " transaction(s) deleted.");
        } catch (SQLException e) {
            System.out.println("Error deleting transaction: " + e.getMessage());
        }
    }

    // Helper function to get all transactions
    public static List<String> getAllTransactions() {
        String sql = "SELECT * FROM Transactions";
        List<String> transactions = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String transaction = "ID: " + rs.getInt("TransactionID") +
                                     ", Title: " + rs.getString("Title") +
                                     ", Amount: " + rs.getDouble("Amount") +
                                     ", Category: " + rs.getString("TransactionCategory") +
                                     ", Date: " + rs.getString("TransactionDate") +
                                     ", Method: " + rs.getString("PaymentMethod");
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving transactions: " + e.getMessage());
        }
        return transactions;
    }

    // Helper function to get a transaction by ID
    public static String getTransactionById(int transactionID) {
        String sql = "SELECT * FROM Transactions WHERE TransactionID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transactionID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return "ID: " + rs.getInt("TransactionID") +
                           ", Title: " + rs.getString("Title") +
                           ", Amount: " + rs.getDouble("Amount") +
                           ", Category: " + rs.getString("TransactionCategory") +
                           ", Date: " + rs.getString("TransactionDate") +
                           ", Method: " + rs.getString("PaymentMethod");
                } else {
                    return "Transaction not found.";
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving transaction: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        // Create the database
        createDatabase();

        // Execute the schema
        executeSchema();

        // Example usage of helper functions
        updateTransaction(1, "Groceries", 50.0, "Food", "2025-05-01", "Debit Card");
        deleteTransaction(2);
        List<String> transactions = getAllTransactions();
        transactions.forEach(System.out::println);
        System.out.println(getTransactionById(1));
    }
}