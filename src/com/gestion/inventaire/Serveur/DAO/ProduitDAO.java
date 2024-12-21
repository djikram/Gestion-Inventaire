package com.gestion.inventaire.Serveur.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.gestion.inventaire.Serveur.Utils.DatabaseConnection;

public class ProduitDAO {

    public void ajouterProduit(String nom, String categorie, int quantite, double prix) {
        String query = "INSERT INTO Produits (nom, categorie, quantite, prix) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, nom);
            stmt.setString(2, categorie);
            stmt.setInt(3, quantite);
            stmt.setDouble(4, prix);
            stmt.executeUpdate();

            System.out.println("Produit ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> listerProduits() {
        String query = "SELECT * FROM Produits";
        List<String> produits = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                produits.add(rs.getString("nom") + " - " +
                             rs.getString("categorie") + " - " +
                             rs.getInt("quantite") + " - " +
                             rs.getDouble("prix") + "€");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }
}

