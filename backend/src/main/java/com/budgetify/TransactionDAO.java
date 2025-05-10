import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // INSERT a new transaction
    public static void addTransaction(int userId, String title, double amount, String category, String date, String method) {
        String sql = "INSERT INTO Transactions (UserID, Title, Amount, TransactionCategory, TransactionDate, PaymentMethod) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, title);
            stmt.setDouble(3, amount);
            stmt.setString(4, category);
            stmt.setString(5, date); // Format: "YYYY-MM-DD"
            stmt.setString(6, method);

            stmt.executeUpdate();
            System.out.println("✅ Transaction added for user " + userId);

        } catch (SQLException e) {
            System.out.println("❌ Failed to add transaction: " + e.getMessage());
        }
    }

    // FETCH transactions for a given user
    public static List<String> getTransactionsByUserId(int userId) {
        String sql = "SELECT * FROM Transactions WHERE UserID = ?";
        List<String> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String t = rs.getInt("TransactionID") + " | " +
                        rs.getString("Title") + " | $" +
                        rs.getDouble("Amount") + " | " +
                        rs.getString("TransactionCategory") + " | " +
                        rs.getString("TransactionDate") + " | " +
                        rs.getString("PaymentMethod");
                transactions.add(t);
            }

        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch transactions: " + e.getMessage());
        }

        return transactions;
    }
    // UPDATE a transaction by ID
    public static void updateTransaction(int transactionId, String newTitle, double newAmount, String newCategory, String newDate, String newMethod) {
        String sql = "UPDATE Transactions SET Title = ?, Amount = ?, TransactionCategory = ?, TransactionDate = ?, PaymentMethod = ? WHERE TransactionID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newTitle);
            stmt.setDouble(2, newAmount);
            stmt.setString(3, newCategory);
            stmt.setString(4, newDate);
            stmt.setString(5, newMethod);
            stmt.setInt(6, transactionId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Transaction updated.");
            } else {
                System.out.println("⚠️ No transaction found with that ID.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to update transaction: " + e.getMessage());
        }
    }

    // DELETE a transaction by ID
    public static void deleteTransaction(int transactionId) {
        String sql = "DELETE FROM Transactions WHERE TransactionID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transactionId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("✅ Transaction deleted.");
            } else {
                System.out.println("⚠️ No transaction found with that ID.");
            }

        } catch (SQLException e) {
            System.out.println("Failed to delete transaction: " + e.getMessage());
        }
    }

}

