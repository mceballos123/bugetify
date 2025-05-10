import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // INSERT user into the database
    public static void addUser(String email, String name, String password) {
        String sql = "INSERT INTO Users (email, name, password) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, name);
            stmt.setString(3, password);
            stmt.executeUpdate();

            System.out.println(" User added: " + email);

        } catch (SQLException e) {
            System.out.println(" Failed to add user: " + e.getMessage());
        }
    }

    // SELECT all users from the database
    public static List<String> getAllUsers() {
        String sql = "SELECT * FROM Users";
        List<String> users = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String user = rs.getInt("UserID") + " - " +
                        rs.getString("name") + " (" +
                        rs.getString("email") + ")";
                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println(" Failed to fetch users: " + e.getMessage());
        }

        return users;
    }
    public static boolean validateLogin(String email, String password) {
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println(" Login successful for " + email);
                return true;
            } else {
                System.out.println(" Login failed for " + email);
                return false;
            }

        } catch (SQLException e) {
            System.out.println(" Login error: " + e.getMessage());
            return false;
        }
    }

}

