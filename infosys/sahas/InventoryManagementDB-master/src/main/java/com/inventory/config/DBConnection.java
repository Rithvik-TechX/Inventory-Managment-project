package com.inventory.config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "root"; // your MySQL password

    private static Connection connection = null;

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connected successfully!");
            }
        } catch (SQLException e) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database reconnected successfully!");
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                System.out.println("🔌 Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
