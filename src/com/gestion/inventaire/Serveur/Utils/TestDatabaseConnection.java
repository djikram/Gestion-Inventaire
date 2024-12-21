package com.gestion.inventaire.Serveur.Utils;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabaseConnection {

    public static void main(String[] args) {
        try {
            // Essaie de se connecter à la base de données
            Connection connection = DatabaseConnection.getConnection();
            if (connection != null) {
                System.out.println("Connexion réussie à la base de données !");
                connection.close(); // Fermer la connexion une fois le test effectué
            }
        } catch (SQLException e) {
            // Si la connexion échoue, afficher l'erreur
            System.out.println("Erreur de connexion à la base de données.");
            e.printStackTrace();
        }
    }
}

