package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // private static final String DB_URL = "jdbc:sqlite:backend/db/budgetify.db";
    private static final String DB_URL = "jdbc:sqlite:/Users/mariamjamil/Documents/temp-project/bugetify/backend/db/budgetify.db";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("✅ Connected to SQLite database.");
        } catch (SQLException e) {
            System.out.println("❌ Failed to connect: " + e.getMessage());
        }
        return conn;
    }
}
