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
        String schema = "CREATE TABLE IF NOT EXISTS Users (" +
                "    UserID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    email TEXT NOT NULL UNIQUE," +
                "    name TEXT NOT NULL," +
                "    password TEXT NOT NULL" +
                ");" +
                "CREATE TABLE IF NOT EXISTS Transactions (" +
                "    TransactionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    UserID INTEGER NOT NULL," +
                "    Title TEXT NOT NULL," +
                "    Amount REAL NOT NULL," +
                "    TransactionCategory TEXT NOT NULL," +
                "    TransactionDate TEXT NOT NULL," +
                "    PaymentMethod TEXT NOT NULL," +
                "    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE" +
                ");" +
                "CREATE TABLE IF NOT EXISTS Budget (" +
                "    BudgetID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    UserID INTEGER NOT NULL," +
                "    MonthlyBudget REAL NOT NULL," +
                "    Month TEXT NOT NULL," +
                "    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE" +
                ");" +
                "CREATE TABLE IF NOT EXISTS BankAccounts (" +
                "    AccountID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    UserID INTEGER NOT NULL," +
                "    Account_name TEXT NOT NULL," +
                "    First_name TEXT NOT NULL," +
                "    Last_name TEXT NOT NULL," +
                "    Date_opened TEXT NOT NULL," +
                "    Number_withdrawals INTEGER," +
                "    Number_deposits INTEGER," +
                "    Provider TEXT NOT NULL," +
                "    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE" +
                ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(schema);
            System.out.println("Schema executed successfully.");
        } catch (SQLException e) {
            System.out.println("Error executing schema: " + e.getMessage());
        }
    }

    // Helper function to create a new user
    public static void createUser(String email, String name, String password) {
        String sql = "INSERT INTO Users (email, name, password) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            int rowsInserted = pstmt.executeUpdate();
            System.out.println(rowsInserted + " user(s) inserted.");
        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    // Helper function to get a user by email
    public static String getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return "ID: " + rs.getInt("UserID") +
                            ", Email: " + rs.getString("email") +
                            ", Name: " + rs.getString("name") +
                            ", Password: " + rs.getString("password");
                } else {
                    return "User not found.";
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
            return null;
        }
    }

    // Helper function to update a user
    public static void updateUser(int userID, String email, String name, String password) {
        String sql = "UPDATE Users SET email = ?, name = ?, password = ? WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            pstmt.setInt(4, userID);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated + " user(s) updated.");
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    // Helper function to delete a user
    public static void deleteUser(int userID) {
        String sql = "DELETE FROM Users WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted + " user(s) deleted.");
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    public static void createTransaction(int userID, String title, double amount, String category, String date,
            String method) {
        String sql = "INSERT INTO Transactions (UserID, Title, Amount, TransactionCategory, TransactionDate, PaymentMethod) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            pstmt.setString(2, title);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, category);
            pstmt.setString(5, date);
            pstmt.setString(6, method);
            int rowsInserted = pstmt.executeUpdate();
            System.out.println(rowsInserted + " transaction(s) inserted.");
        } catch (SQLException e) {
            System.out.println("Error creating transaction: " + e.getMessage());
        }
    }

    // ========== Budget Helper Functions ==========

    public static void createBudget(int userID, double monthlyBudget, String month) {
        String sql = "INSERT INTO Budget (UserID, MonthlyBudget, Month) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            pstmt.setDouble(2, monthlyBudget);
            pstmt.setString(3, month);
            int rowsInserted = pstmt.executeUpdate();
            System.out.println(rowsInserted + " budget(s) inserted.");
        } catch (SQLException e) {
            System.out.println("Error creating budget: " + e.getMessage());
        }
    }

    public static void updateBudget(int budgetID, double monthlyBudget, String month) {
        String sql = "UPDATE Budget SET MonthlyBudget = ?, Month = ? WHERE BudgetID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, monthlyBudget);
            pstmt.setString(2, month);
            pstmt.setInt(3, budgetID);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated + " budget(s) updated.");
        } catch (SQLException e) {
            System.out.println("Error updating budget: " + e.getMessage());
        }
    }

    public static void deleteBudget(int budgetID) {
        String sql = "DELETE FROM Budget WHERE BudgetID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, budgetID);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted + " budget(s) deleted.");
        } catch (SQLException e) {
            System.out.println("Error deleting budget: " + e.getMessage());
        }
    }

    public static List<String> getAllBudgets() {
        String sql = "SELECT * FROM Budget";
        List<String> budgets = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String budget = "ID: " + rs.getInt("BudgetID") +
                        ", UserID: " + rs.getInt("UserID") +
                        ", Budget: $" + rs.getDouble("MonthlyBudget") +
                        ", Month: " + rs.getString("Month");
                budgets.add(budget);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving budgets: " + e.getMessage());
        }
        return budgets;
    }

    // ========== Bank Accounts Helper Functions ==========

    public static void createBankAccount(int userID, String accountName, String firstName, String lastName,
            String dateOpened,
            int numWithdrawals, int numDeposits, String provider) {
        String sql = "INSERT INTO BankAccounts (UserID, Account_name, First_name, Last_name, Date_opened, Number_withdrawals, Number_deposits, Provider) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            pstmt.setString(2, accountName);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, dateOpened);
            pstmt.setInt(6, numWithdrawals);
            pstmt.setInt(7, numDeposits);
            pstmt.setString(8, provider);
            int rowsInserted = pstmt.executeUpdate();
            System.out.println(rowsInserted + " bank account(s) inserted.");
        } catch (SQLException e) {
            System.out.println("Error creating bank account: " + e.getMessage());
        }
    }

    public static void updateBankAccount(int accountID, String accountName, int numWithdrawals, int numDeposits,
            String provider) {
        String sql = "UPDATE BankAccounts SET Account_name = ?, Number_withdrawals = ?, Number_deposits = ?, Provider = ? WHERE AccountID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountName);
            pstmt.setInt(2, numWithdrawals);
            pstmt.setInt(3, numDeposits);
            pstmt.setString(4, provider);
            pstmt.setInt(5, accountID);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated + " bank account(s) updated.");
        } catch (SQLException e) {
            System.out.println("Error updating bank account: " + e.getMessage());
        }
    }

    public static void deleteBankAccount(int accountID) {
        String sql = "DELETE FROM BankAccounts WHERE AccountID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountID);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted + " bank account(s) deleted.");
        } catch (SQLException e) {
            System.out.println("Error deleting bank account: " + e.getMessage());
        }
    }

    public static List<String> getAllBankAccounts() {
        String sql = "SELECT * FROM BankAccounts";
        List<String> accounts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String acc = "ID: " + rs.getInt("AccountID") +
                        ", UserID: " + rs.getInt("UserID") +
                        ", Name: " + rs.getString("Account_name") +
                        ", Opened: " + rs.getString("Date_opened") +
                        ", Withdrawals: " + rs.getInt("Number_withdrawals") +
                        ", Deposits: " + rs.getInt("Number_deposits") +
                        ", Provider: " + rs.getString("Provider");
                accounts.add(acc);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving bank accounts: " + e.getMessage());
        }
        return accounts;
    }

    // Helper function to create a transaction
    public static void createTransaction(String title, double amount, String category, String date, String method) {
        String sql = "INSERT INTO Transactions (Title, Amount, TransactionCategory, TransactionDate, PaymentMethod) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, category);
            pstmt.setString(4, date);
            pstmt.setString(5, method);
            int rowsInserted = pstmt.executeUpdate();
            System.out.println(rowsInserted + " transaction(s) inserted.");
        } catch (SQLException e) {
            System.out.println("Error creating transaction: " + e.getMessage());
        }
    }

    // Helper function to update a transaction
    public static void updateTransaction(int transactionID, String title, double amount, String category, String date,
            String method) {
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