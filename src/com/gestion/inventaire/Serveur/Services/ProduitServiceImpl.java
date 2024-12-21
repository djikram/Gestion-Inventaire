package com.gestion.inventaire.Serveur.Services;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

import com.gestion.inventaire.Client.Produit;
import com.gestion.inventaire.Serveur.Utils.LogUtil;

import java.sql.Connection;//
import java.sql.DriverManager;//
import java.sql.PreparedStatement;//
import java.sql.SQLException;//

// La classe implémente ProduitService et hérite de UnicastRemoteObject pour RMI
public class ProduitServiceImpl extends UnicastRemoteObject implements ProduitService {
	
	private static final long serialVersionUID = 1L;
	// Informations pour se connecter à la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/inventaire_db"; // URL de votre base de données
    private static final String USER = "root"; // Utilisateur
    private static final String PASSWORD = ""; // Mot de passe
    
    // Constructeur
    public ProduitServiceImpl() throws RemoteException {
        super(); 
    }
    // Méthode utilitaire pour obtenir une connexion
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    @Override
    public void ajouterProduit(String nom, String categorie, int quantite, double prix) throws RemoteException {
        String query = "INSERT INTO Produits (nom, categorie, quantite, prix) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
               stmt.setString(1, nom);
               stmt.setString(2, categorie);
               stmt.setInt(3, quantite);
               stmt.setDouble(4, prix);
               stmt.executeUpdate();
               // Log de l'opération
               LogUtil.log("Ajout", "Produit ajouté : Nom=" + nom + ", Catégorie=" + categorie + ", Quantité=" + quantite + ", Prix=" + prix);

           }catch (SQLException e) {
            throw new RemoteException("Erreur lors de l'ajout du produit : " + e.getMessage(), e);
        }
    }

    @Override
    public List<Produit> getAllProduits() throws RemoteException {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM Produits";
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

               while (rs.next()) {
                   produits.add(new Produit(
                           rs.getInt("id"),
                           rs.getString("nom"),
                           rs.getString("categorie"),
                           rs.getInt("quantite"),
                           rs.getDouble("prix")
                   ));
               }

           }catch (SQLException e) {
            throw new RemoteException("Erreur lors de la récupération des produits : " + e.getMessage(), e);
        }
        return produits;
    }
    @Override
    public List<Produit> searchProduits(String searchTerm) throws RemoteException {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM Produits WHERE nom LIKE ? OR categorie LIKE ? OR quantite = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

               String wildcardSearch = "%" + searchTerm + "%";
               stmt.setString(1, wildcardSearch);
               stmt.setString(2, wildcardSearch);
               
               try {
                   int quantity = Integer.parseInt(searchTerm); // Vérifie si c'est un nombre
                   stmt.setInt(3, quantity);
               } catch (NumberFormatException e) {
                   stmt.setInt(3, -1); // Impossible d'avoir une quantité négative
               }

               ResultSet rs = stmt.executeQuery();
               while (rs.next()) {
                   produits.add(new Produit(
                           rs.getInt("id"),
                           rs.getString("nom"),
                           rs.getString("categorie"),
                           rs.getInt("quantite"),
                           rs.getDouble("prix")
                   ));
               }

           }catch (SQLException e) {
            throw new RemoteException("Erreur lors de la recherche des produits : " + e.getMessage(), e);
        }
        return produits;
    }

    @Override
    public void updateProduit(int id, String nom, String categorie, int quantite, double prix) throws RemoteException {
        String query = "UPDATE Produits SET nom = ?, categorie = ?, quantite = ?, prix = ? WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

               stmt.setString(1, nom);
               stmt.setString(2, categorie);
               stmt.setInt(3, quantite);
               stmt.setDouble(4, prix);
               stmt.setInt(5, id);
               stmt.executeUpdate();
               // Log de la mise à jour
               LogUtil.log("Modification", "Produit modifié : ID=" + id + ", Nouveau Nom=" + nom + ", Nouvelle Catégorie=" + categorie + ", Nouvelle Quantité=" + quantite + ", Nouveau Prix=" + prix);

           }catch (SQLException e) {
            throw new RemoteException("Erreur lors de la mise à jour du produit : " + e.getMessage(), e);
        }
    }
    @Override
    public void deleteProduit(int id) throws RemoteException {
        String query = "DELETE FROM Produits WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
               stmt.setInt(1, id);
               stmt.executeUpdate();
               // Log de la suppression
               LogUtil.log("Suppression", "Produit supprimé : ID=" + id);

           }catch (SQLException e) {
            throw new RemoteException("Erreur lors de la suppression du produit : " + e.getMessage(), e);
        }
    }
    @Override
    public boolean authenticate(String username, String password) throws RemoteException {
        String query = "SELECT COUNT(*) FROM Utilisateurs WHERE nom_utilisateur = ? AND mot_de_passe = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

               stmt.setString(1, username);
               stmt.setString(2, password);
               ResultSet rs = stmt.executeQuery();

               if (rs.next()) {
                   return rs.getInt(1) > 0;
               }

           }catch (SQLException e) {
            throw new RemoteException("Erreur lors de l'authentification : " + e.getMessage(), e);
        }
        return false;
        
    }

}
