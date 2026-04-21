package com.library.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "library_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password"; // Change this to your MySQL password
    
    private static Connection connection = null;
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL + DATABASE_NAME, USERNAME, PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                initializeDatabase();
            }
        }
        return connection;
    }
    
    private static void initializeDatabase() {
        try {
            Connection initConn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stmt = initConn.createStatement();
            
            // Create database if not exists
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
            stmt.executeUpdate("USE " + DATABASE_NAME);
            
            // Create books table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS books (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "title VARCHAR(255) NOT NULL," +
                    "author VARCHAR(255) NOT NULL," +
                    "isbn VARCHAR(20) UNIQUE," +
                    "publication_year INT," +
                    "summary TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            stmt.executeUpdate(createTableSQL);
            
            stmt.close();
            initConn.close();
            
            connection = DriverManager.getConnection(URL + DATABASE_NAME, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
