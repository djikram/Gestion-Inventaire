package com.gestion.inventaire.Serveur.Utils;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtil {
    private static final File LOG_FILE = new File("logs.txt"); 

    public static void log(String operation, String details) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            // Ajouter un horodatage à chaque entrée
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(timestamp + " - " + operation + " : " + details + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 // Méthode pour loguer les opérations
    public static void logOperation(String message) {
        try {
            System.out.println("Le fichier de log est situé à : " + LOG_FILE.getAbsolutePath());

            // Créer le fichier si il n'existe pas
            if (!LOG_FILE.exists()) {
                LOG_FILE.createNewFile();
            }

            // Ouvrir le fichier en mode ajout
            FileWriter writer = new FileWriter(LOG_FILE, true);

            // Écrire le message et un saut de ligne
            writer.write(message + "\n");

            // Fermer le fichier
            writer.close();

            // Afficher le message dans la console pour débogage
            System.out.println("Log écrit : " + message);
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier de logs : " + e.getMessage());
        }
    }
}
