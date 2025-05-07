import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:budgetify.db";
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void createTables() {
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

                    CREATE TABLE IF NOT EXISTS Budget (
                        BudgetID INTEGER PRIMARY KEY AUTOINCREMENT,
                        UserID INTEGER NOT NULL,
                        MonthlyBudget REAL NOT NULL,
                        Month TEXT NOT NULL,
                        FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
                    );

                    CREATE TABLE IF NOT EXISTS BankAccounts (
                        AccountID INTEGER PRIMARY KEY AUTOINCREMENT,
                        UserID INTEGER NOT NULL,
                        Account_name TEXT NOT NULL,
                        First_name TEXT NOT NULL,
                        Last_name TEXT NOT NULL,
                        Date_opened TEXT NOT NULL,
                        Number_withdrawals INTEGER DEFAULT 0,
                        Number_deposits INTEGER DEFAULT 0,
                        Provider TEXT NOT NULL,
                        FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
                    );
                """;

        try (Statement stmt = connection.createStatement()) {
            for (String sql : schema.split(";")) {
                if (!sql.trim().isEmpty()) {
                    stmt.execute(sql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // User operations
    public void createUser(String email, String name, String password) throws SQLException {
        String sql = "INSERT INTO Users (email, name, password) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
        }
    }

    public BudgetifyApp.User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new BudgetifyApp.User(
                            rs.getInt("UserID"),
                            rs.getString("email"),
                            rs.getString("name"),
                            rs.getString("password"));
                }
            }
        }
        return null;
    }

    // Transaction operations
    public void createTransaction(int userId, String title, double amount, String category,
            LocalDate date, String paymentMethod) throws SQLException {
        String sql = "INSERT INTO Transactions (UserID, Title, Amount, TransactionCategory, " +
                "TransactionDate, PaymentMethod) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, title);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, category);
            pstmt.setString(5, date.toString());
            pstmt.setString(6, paymentMethod);
            pstmt.executeUpdate();
        }
    }

    public List<BudgetifyApp.Transaction> getTransactions(int userId) throws SQLException {
        List<BudgetifyApp.Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM Transactions WHERE UserID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(new BudgetifyApp.Transaction(
                            rs.getInt("TransactionID"),
                            rs.getInt("UserID"),
                            rs.getString("Title"),
                            rs.getDouble("Amount"),
                            rs.getString("TransactionCategory"),
                            LocalDate.parse(rs.getString("TransactionDate")),
                            rs.getString("PaymentMethod")));
                }
            }
        }
        return transactions;
    }

    // Budget operations
    public void createBudget(int userId, double monthlyBudget, String month) throws SQLException {
        String sql = "INSERT INTO Budget (UserID, MonthlyBudget, Month) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDouble(2, monthlyBudget);
            pstmt.setString(3, month);
            pstmt.executeUpdate();
        }
    }

    public List<BudgetifyApp.Budget> getBudgets(int userId) throws SQLException {
        List<BudgetifyApp.Budget> budgets = new ArrayList<>();
        String sql = "SELECT * FROM Budget WHERE UserID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    budgets.add(new BudgetifyApp.Budget(
                            rs.getInt("BudgetID"),
                            rs.getInt("UserID"),
                            rs.getDouble("MonthlyBudget"),
                            rs.getString("Month")));
                }
            }
        }
        return budgets;
    }

    // Bank Account operations
    public void createBankAccount(int userId, String accountName, String firstName,
            String lastName, LocalDate dateOpened, String provider) throws SQLException {
        String sql = "INSERT INTO BankAccounts (UserID, Account_name, First_name, Last_name, " +
                "Date_opened, Provider) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, accountName);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, dateOpened.toString());
            pstmt.setString(6, provider);
            pstmt.executeUpdate();
        }
    }

    public List<BudgetifyApp.BankAccount> getBankAccounts(int userId) throws SQLException {
        List<BudgetifyApp.BankAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM BankAccounts WHERE UserID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(new BudgetifyApp.BankAccount(
                            rs.getInt("AccountID"),
                            rs.getInt("UserID"),
                            rs.getString("Account_name"),
                            rs.getString("First_name"),
                            rs.getString("Last_name"),
                            LocalDate.parse(rs.getString("Date_opened")),
                            rs.getInt("Number_withdrawals"),
                            rs.getInt("Number_deposits"),
                            rs.getString("Provider")));
                }
            }
        }
        return accounts;
    }

    // Update operations
    public void updateTransaction(int transactionId, String title, double amount,
            String category, LocalDate date, String paymentMethod) throws SQLException {
        String sql = "UPDATE Transactions SET Title = ?, Amount = ?, TransactionCategory = ?, " +
                "TransactionDate = ?, PaymentMethod = ? WHERE TransactionID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, category);
            pstmt.setString(4, date.toString());
            pstmt.setString(5, paymentMethod);
            pstmt.setInt(6, transactionId);
            pstmt.executeUpdate();
        }
    }

    public void updateBudget(int budgetId, double monthlyBudget, String month) throws SQLException {
        String sql = "UPDATE Budget SET MonthlyBudget = ?, Month = ? WHERE BudgetID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, monthlyBudget);
            pstmt.setString(2, month);
            pstmt.setInt(3, budgetId);
            pstmt.executeUpdate();
        }
    }

    public void updateBankAccount(int accountId, String accountName, String firstName,
            String lastName, String provider) throws SQLException {
        String sql = "UPDATE BankAccounts SET Account_name = ?, First_name = ?, " +
                "Last_name = ?, Provider = ? WHERE AccountID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountName);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, provider);
            pstmt.setInt(5, accountId);
            pstmt.executeUpdate();
        }
    }

    // Delete operations
    public void deleteTransaction(int transactionId) throws SQLException {
        String sql = "DELETE FROM Transactions WHERE TransactionID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);
            pstmt.executeUpdate();
        }
    }

    public void deleteBudget(int budgetId) throws SQLException {
        String sql = "DELETE FROM Budget WHERE BudgetID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, budgetId);
            pstmt.executeUpdate();
        }
    }

    public void deleteBankAccount(int accountId) throws SQLException {
        String sql = "DELETE FROM BankAccounts WHERE AccountID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.executeUpdate();
        }
    }
}