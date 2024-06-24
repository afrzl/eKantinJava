package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Database instance;
    private Connection conn;
    private final String url = "jdbc:sqlite:database.db";

    private Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public Connection getConn() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting database connection", e);
        }
        return conn;
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error closing database connection", e);
        }
    }
}
