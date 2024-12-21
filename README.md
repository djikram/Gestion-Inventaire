# Gestion d'Inventaire avec Accès Distant:RMI

## Description du projet
Ce projet implémente un système de gestion d'inventaire permettant à une petite entreprise de gérer ses produits à distance. Il est divisé en deux parties principales : un serveur et un client, communiquant via RMI. Les données des produits sont stockées dans une base de données MySQL, et le projet respecte une architecture claire et modulaire.

## Fonctionnalités principales
- **Côté serveur :**
  - Gestion des produits (CRUD).
  - Connexion à une base de données MySQL via JDBC.
  - Fourniture de services aux clients distants.
  - Système de logs pour tracer les opérations effectuées.
- **Côté client :**
  - Interface graphique simple pour interagir avec le serveur.
  - Authentification via un système de login.

## Pré-requis
- **Java Development Kit (JDK)** version 20 ou supérieure.
- **MySQL** version 10.4.32 ou supérieure.
- **mysql-connector-java** version 9.1.0 (inclus dans le projet).
- Un IDE Java (ex. Eclipse, IntelliJ IDEA) ou la ligne de commande pour exécuter le projet.

## Installation

### 1. Configurer la base de données
1. Créez une base de données MySQL nommée `inventaire_db`.
2. Exécutez les scripts SQL fournis dans les fichiers suivants :
   - `schema.sql` : Crée les tables (Produits, Utilisateurs, etc.).
   - `data.sql` : Insère des données de test.
3. Assurez-vous que les paramètres de connexion à la base de données dans `DatabaseConnection.java` correspondent à votre configuration (nom de la base, utilisateur, mot de passe).

### 2. Importer le projet
1. Clonez ou téléchargez ce dépôt dans votre environnement local.
2. Ouvrez le projet dans un IDE Java.
3. Ajoutez le fichier `mysql-connector-j-9.1.0.jar` comme dépendance si ce n'est pas déjà fait.

### 3. Exécuter le serveur
1. Naviguez jusqu'au package `com.gestion.inventaire.Serveur`.
2. Exécutez la classe `Server.java`.
3. Assurez-vous que le serveur RMI est en écoute sur localhost:3000 .

### 4. Exécuter le client
1. Naviguez jusqu'au package `com.gestion.inventaire.Client`.
2. Exécutez la classe `LoginGUI.java`.
3. Connectez-vous en utilisant les identifiants configurés dans la table Utilisateurs .

## Fonctionnement

### Opérations CRUD pour les produits
1. Ajouter un produit : Enregistre un nouveau produit avec ses détails (nom, catégorie, quantité, prix).
2. Modifier un produit : Permet de mettre à jour les informations existantes d'un produit.
3. Supprimer un produit : Retire un produit de la base de données.
4. Rechercher des produits : Filtre les produits par nom, catégorie ou quantité en stock.

### Authentification 
- Les utilisateurs doivent s'authentifier avant d'accéder aux fonctionnalités du client.


## Tests
1. Testez la connexion à la base de données avec `TestDatabaseConnection.java`.
2. Testez les opérations CRUD avec `TestProduitDAO.java`.
3. Lancez le serveur et le client, puis effectuez des opérations pour valider l'ensemble des fonctionnalités.
