package com.gestion.inventaire.Serveur;
import com.gestion.inventaire.Serveur.Services.ProduitService;
import com.gestion.inventaire.Serveur.Services.ProduitServiceImpl;


import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        try {
            // Démarrer le registre RMI sur le port 3000
            LocateRegistry.createRegistry(3000);
            System.out.println("Registre RMI démarré sur le port 3000.");

            // Créer une instance de ProduitServiceImpl
            ProduitService produitService = new ProduitServiceImpl();

            // Enregistrer le service dans le registre RMI
            Naming.rebind("rmi://localhost:3000/ProduitService", produitService);
            
            System.out.println("Service ProduitService enregistré et prêt à recevoir des connexions.");
        } catch (Exception e) {
            System.err.println("Erreur lors du démarrage du serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}

