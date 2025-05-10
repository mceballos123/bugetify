import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            System.out.println(" Failed to fetch budgets: " + e.getMessage());
        }

        return budgets;
    }
}
