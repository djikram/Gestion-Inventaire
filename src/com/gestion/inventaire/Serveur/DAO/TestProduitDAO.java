package com.gestion.inventaire.Serveur.DAO;

public class TestProduitDAO {
    public static void main(String[] args) {
        ProduitDAO dao = new ProduitDAO();

        // Ajouter un produit
        dao.ajouterProduit("Table", "Mobilier", 5, 120.50);

        // Lister les produits
        System.out.println("Liste des produits :");
        for (String produit : dao.listerProduits()) {
            System.out.println(produit);
        }
    }
}

