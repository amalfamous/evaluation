package org.example;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestJDBC {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/gestion_stock?serverTimezone=UTC&useSSL=false";
        String user = "root";
        String pass = "";
        try (Connection c = DriverManager.getConnection(url, user, pass)) {
            System.out.println("Connexion JDBC OK: " + c.getMetaData().getDatabaseProductName()
                    + " " + c.getMetaData().getDatabaseProductVersion());
        } catch (Exception e) {
            System.err.println("Impossible de se connecter via JDBC:");
            e.printStackTrace();
        }
    }
}

