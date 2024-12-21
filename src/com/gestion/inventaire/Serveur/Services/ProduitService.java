package com.gestion.inventaire.Serveur.Services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import com.gestion.inventaire.Client.Produit;

public interface ProduitService extends Remote {
    void ajouterProduit(String nom, String categorie, int quantite, double prix) throws RemoteException;
    List<Produit> getAllProduits() throws RemoteException;
    List<Produit> searchProduits(String searchTerm) throws RemoteException;
    void updateProduit(int id, String nom, String categorie, int quantite, double prix) throws RemoteException;
    void deleteProduit(int id) throws RemoteException;
    boolean authenticate(String username, String password) throws RemoteException;

}
