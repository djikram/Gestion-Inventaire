CREATE DATABASE inventaire_db;
USE inventaire_db;
CREATE TABLE produits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    categorie VARCHAR(50) NOT NULL,
    quantite INT DEFAULT 0,
    prix DECIMAL(10, 2) NOT NULL
);
CREATE TABLE utilisateurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom_utilisateur VARCHAR(50) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(100) NOT NULL
);
