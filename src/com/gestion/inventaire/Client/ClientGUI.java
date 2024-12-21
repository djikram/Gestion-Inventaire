package com.gestion.inventaire.Client;
import com.gestion.inventaire.Serveur.Services.ProduitService;
import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.List;
import java.rmi.RemoteException;

public class ClientGUI extends JFrame {
	private static final long serialVersionUID = 1L;
    private JTextField productNameField, categoryField, quantityField, priceField, searchField;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private ProduitService produitService;

    public ClientGUI() {
        // Configuration de la fenêtre
        setTitle("Gestion d'Inventaire");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
       
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}
        
        // Connexion au service RMI
        try {
            produitService = (ProduitService) Naming.lookup("rmi://localhost:3000/ProduitService");

        } catch (Exception e) {
            showError("Erreur de connexion au serveur : " + e.getMessage());
        }
        
        // Input panel for product details
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Details des produits"));
        inputPanel.add(new JLabel("Nom du produit : "));
        productNameField = new JTextField();
        inputPanel.add(productNameField);

        inputPanel.add(new JLabel("Catégorie : "));
        categoryField = new JTextField();
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Quantité : "));
        quantityField = new JTextField();
        inputPanel.add(quantityField);

        inputPanel.add(new JLabel("Prix : "));
        priceField = new JTextField();
        inputPanel.add(priceField);

        inputPanel.setBackground(new Color(240, 240, 250));
        add(inputPanel, BorderLayout.NORTH);

        // Table to display products
        tableModel = new DefaultTableModel(new String[]{"ID", "Nom", "Catégorie", "Quantité", "Prix"}, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        // Control panel for buttons
        JPanel controlPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(15);
        controlPanel.add(new JLabel("Rechercher : "));
        controlPanel.add(searchField);

        JButton searchButton = new JButton("Rechercher");
        controlPanel.add(searchButton);

        JButton addButton = new JButton("Ajouter");
        controlPanel.add(addButton);

        JButton updateButton = new JButton("Modifier");
        controlPanel.add(updateButton);

        JButton deleteButton = new JButton("Supprimer");
        controlPanel.add(deleteButton);
        controlPanel.setBackground(new Color(220, 220, 240));
        add(controlPanel, BorderLayout.SOUTH);

        // Load products into the table
        getAllProduits();
        
        // Add action listeners
        searchButton.addActionListener(e -> searchProduits());
        addButton.addActionListener(e -> ajouterProduit());
        updateButton.addActionListener(e -> updateProduit());
        deleteButton.addActionListener(e -> deleteProduit());    
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadSelectedRowToFields();
            }
        });
    }
    private void getAllProduits() {
    	 try {
             tableModel.setRowCount(0); // Effacer les lignes existantes
             String query = "SELECT * FROM Produits";
             Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventaire_db", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);

             while (rs.next()) {
                 tableModel.addRow(new Object[]{
                         rs.getInt("id"),
                         rs.getString("nom"),
                         rs.getString("categorie"),
                         rs.getInt("quantite"),
                         rs.getDouble("prix")
                 });
             }
             conn.close();
         } catch (SQLException e) {
             showError("Error loading products: " + e.getMessage());

         }
    }
    //RECHERCHER
    private void searchProduits() {
    	if (!isServerConnected()) {
    	    showError("Le serveur RMI est indisponible. Veuillez vérifier la connexion.");
    	    return;
    	}

        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            showError("Veuillez entrer un critère de recherche.");
            return;
        }
        try {
        	// Appel au service RMI pour rechercher des produits
            List<Produit> produits = produitService.searchProduits(searchText);
            tableModel.setRowCount(0); 
            // Ajouter les résultats de la recherche à la table
            for (Produit produit : produits) {
                tableModel.addRow(new Object[]{
                    produit.getId(),
                    produit.getNom(),
                    produit.getCategorie(),
                    produit.getQuantite(),
                    produit.getPrix()
                });
            }
         // Vérification : si aucun produit trouvé, afficher un message
            if (produits.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun produit trouvé pour le critère : " + searchText);
            }
            
        } catch (RemoteException e) {
            showError("Erreur lors de la recherche des produits : " + e.getMessage());
        }
    }
    //AJOUTER
    private void ajouterProduit() {
    	if (!isServerConnected()) {
    	    showError("Le serveur RMI est indisponible. Veuillez vérifier la connexion.");
    	    return;
    	}

        if (!validateFields()) return;
        
        String name = productNameField.getText().trim();
        String category = categoryField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String priceText = priceField.getText().trim();

        if (name.isEmpty() || category.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);
            // Appel au service RMI
            produitService.ajouterProduit(name, category, quantity, price);
            // Rafraîchir la table
            getAllProduits();
            clearFields();

            productNameField.setText("");
            categoryField.setText("");
            quantityField.setText("");
            priceField.setText("");
            JOptionPane.showMessageDialog(this, "Produit ajouté avec succès.");
        } catch (RemoteException e) {
            showError("Erreur lors de l'ajout du produit : " + e.getMessage());
        }
    }
    //MODIFIER
    private void updateProduit() {
    	if (!isServerConnected()) {
    	    showError("Le serveur RMI est indisponible. Veuillez vérifier la connexion.");
    	    return;
    	}

        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String name = productNameField.getText().trim();
                String category = categoryField.getText().trim();
                String quantityText = quantityField.getText().trim();
                String priceText = priceField.getText().trim();
                
                if (!validateFields()) return;

                if (name.isEmpty() || category.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
                    showError("Veuillez remplir tous les champs.");
                    return;
                }

                int quantity = Integer.parseInt(quantityText);
                double price = Double.parseDouble(priceText);
             // Appel au service RMI
                produitService.updateProduit(id, name, category, quantity, price);
                // Rafraîchir la table
                getAllProduits();
                clearFields();
                JOptionPane.showMessageDialog(this, "Produit mis à jour avec succès.");
            } catch (RemoteException e) {
                showError("Erreur lors de la mise à jour du produit : " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit à mettre à jour.");
        }
        //if (!validateFields()) return;
    }
    //SUPPRIMER
    private void deleteProduit() {
    	if (!isServerConnected()) {
    	    showError("Le serveur RMI est indisponible. Veuillez vérifier la connexion.");
    	    return;
    	}
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int id = (int) tableModel.getValueAt(selectedRow, 0);              
                // Appel au service RMI
                produitService.deleteProduit(id);
                // Rafraîchir la table
                getAllProduits();
                clearFields();

                JOptionPane.showMessageDialog(this, "Produit supprimé avec succès.");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit à supprimer.");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    
    private void loadSelectedRowToFields() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            productNameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            categoryField.setText((String) tableModel.getValueAt(selectedRow, 2));
            quantityField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
            priceField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4)));
        }
    }
    private boolean validateFields() {
        if (productNameField.getText().trim().isEmpty() ||
            categoryField.getText().trim().isEmpty() ||
            quantityField.getText().trim().isEmpty() ||
            priceField.getText().trim().isEmpty()) {
            showError("All fields are required.");
            return false;
        }
        try {
            Integer.parseInt(quantityField.getText().trim());
            Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Quantity and Price must be numeric.");
            return false;
        }
        return true;
    }
    private void clearFields() {
        productNameField.setText("");
        categoryField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }
    private boolean isServerConnected() {
        try {
            produitService.getAllProduits(); // Test d'une méthode simple pour vérifier la connexion
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }
    private void reconnectToServer() {
        try {
            produitService = (ProduitService) Naming.lookup("rmi://localhost:3000/ProduitService");
            JOptionPane.showMessageDialog(this, "Connexion au serveur rétablie avec succès.");
        } catch (Exception e) {
            showError("Impossible de se reconnecter au serveur : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientGUI clientGUI = new ClientGUI();
            clientGUI.setVisible(true);
        });
    }
}
