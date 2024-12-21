/**
 * 
 */
/**
 * 
 */
module GestionInventaire {
	requires java.sql;
	requires java.desktop;
	requires java.rmi;
	 exports com.gestion.inventaire.Serveur.Services to java.rmi;
	 exports com.gestion.inventaire.Serveur.DAO to java.rmi;
	 exports com.gestion.inventaire.Serveur to java.rmi;
	 exports com.gestion.inventaire.Serveur.Utils to java.rmi; 
	 exports com.gestion.inventaire.Client to java.rmi; 




    //exports com.gestion.inventaire; // Expose le package à d'autres modules, y compris java.rmi

}