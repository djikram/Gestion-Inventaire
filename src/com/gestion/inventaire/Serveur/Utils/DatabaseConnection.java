package com.gestion.inventaire.Serveur.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/inventaire_db"; // URL de la base
    private static final String USER = "root"; // Utilisateur MySQL
    private static final String PASSWORD = ""; // Mot de passe MySQL

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Charge le driver MySQL
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL introuvable !");
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
