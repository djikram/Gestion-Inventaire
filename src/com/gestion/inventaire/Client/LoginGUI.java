package com.gestion.inventaire.Client;

import javax.swing.*;
import com.gestion.inventaire.Serveur.Services.ProduitService;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;

public class LoginGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
    private JPasswordField passwordField;
    private ProduitService produitService;

    public LoginGUI() {
        setTitle("Connexion - Gestion d'Inventaire");
        setSize(400, 250); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null); 

        // Connexion au service RMI
        try {
            produitService = (ProduitService) Naming.lookup("rmi://localhost:3000/ProduitService");
        } catch (Exception e) {
            showError("Erreur de connexion au serveur : " + e.getMessage());
            return;
        }

        // Header with title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); 
        JLabel titleLabel = new JLabel("Bienvenue");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Panel for input fields and button
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 5, 5)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        
        // Labels and input fields
        JLabel usernameLabel = new JLabel("Nom d'utilisateur :");
        usernameField = new JTextField();
        
        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordField = new JPasswordField();
        
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center-align the button
        JButton loginButton = new JButton("Se connecter");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        // Login button listener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        mainPanel.add(new JLabel());
        mainPanel.add(loginButton);

        add(mainPanel, BorderLayout.CENTER);
        
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
     // Validation for empty fields
        if (username.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        try {
            if (produitService.authenticate(username, password)) {
                JOptionPane.showMessageDialog(this, "Connexion réussie !");
                dispose(); 
                ClientGUI clientGUI = new ClientGUI(); 
                clientGUI.setVisible(true);
            } else {
                showError("Nom d'utilisateur ou mot de passe incorrect.");
            }
            
        } catch (Exception e) {
            showError("Erreur lors de la connexion : " + e.getMessage());
        }
    }
    public void login(String username, String password) {
        try {
            ProduitService produitService = (ProduitService) Naming.lookup("rmi://localhost:1099/produitService");
            
            boolean isAuthenticated = produitService.authenticate(username, password);

            if (isAuthenticated) {
                System.out.println("Connexion réussie !");
            } else {
                System.out.println("Nom d'utilisateur ou mot de passe incorrect.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion : " + e.getMessage());
        }
    }


    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setVisible(true);
        });
    }
}
