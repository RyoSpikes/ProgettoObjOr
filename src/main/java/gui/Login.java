package gui;

import javax.swing.*;
import model.*;
import controller.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * La classe Login rappresenta l'interfaccia grafica per il login degli utenti e degli organizzatori.
 * Consente di autenticarsi o registrarsi come nuovo utente o organizzatore.
 */
public class Login {
    private JPanel loginPanel; // Pannello principale della finestra di login.
    private JTextField fieldUsername; // Campo di testo per l'inserimento del nome utente.
    private JPasswordField fieldPassword; // Campo di testo per l'inserimento della password.
    private JButton getLoginBtn; // Pulsante per effettuare il login.
    public JFrame logFrame; // Finestra principale della vista di login.

    /**
     * Metodo per trovare un utente nella lista degli utenti in base alle credenziali fornite.
     *
     * @param utenti La lista degli utenti registrati.
     * @param username Il nome utente da cercare.
     * @param password La password da verificare.
     * @return L'utente trovato, o null se non esiste.
     * @throws IllegalArgumentException se il nome utente è vuoto o la password è errata.
     */
    private Utente foundLogin(ArrayList<Utente> utenti, String username, String password) throws IllegalArgumentException {
        if(!utenti.isEmpty()) {
            for(Utente u : utenti) {
                if(username == null || username.isEmpty())
                    throw new IllegalArgumentException("Non è stato inserito alcun nome utente.");

                if(username.equals(u.getName())) {
                    if(!u.getLogin(password))
                        throw new IllegalArgumentException("Password errata.");
                    else {
                        return u;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Metodo per trovare un organizzatore nella lista degli organizzatori in base alle credenziali fornite.
     *
     * @param utenti La lista degli organizzatori registrati.
     * @param username Il nome utente da cercare.
     * @param password La password da verificare.
     * @return L'organizzatore trovato, o null se non esiste.
     * @throws IllegalArgumentException se il nome utente è vuoto o la password è errata.
     */
    private Organizzatore foundLoginOrg(ArrayList<Organizzatore> utenti, String username, String password) throws IllegalArgumentException {
        if(!utenti.isEmpty()) {
            for(Organizzatore u : utenti) {
                if(username == null || username.isEmpty())
                    throw new IllegalArgumentException("Non è stato inserito alcun nome utente.");

                if(username.equals(u.getName())) {
                    if(!u.getLogin(password))
                        throw new IllegalArgumentException("Password errata.");
                    else {
                        return u;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Costruttore della classe Login per gli utenti.
     * Inizializza l'interfaccia grafica e gestisce le azioni per il login degli utenti.
     *
     * @param controllerOrganizzatore Il controller per la gestione degli organizzatori.
     * @param controller Il controller per la gestione degli utenti.
     * @param frameCalling Il frame chiamante che ha aperto questa finestra.
     */
    public Login(ControllerOrganizzatore controllerOrganizzatore, Controller controller, JFrame frameCalling) {
        // Inizializza i componenti GUI se non sono stati inizializzati automaticamente
        if (loginPanel == null) {
            initializeComponents();
        }
        
        logFrame = new JFrame("Login");
        logFrame.setContentPane(loginPanel);

        logFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameCalling.setVisible(true);
                logFrame.dispose();
            }
        });

        logFrame.pack();
        logFrame.setVisible(true);
        logFrame.setSize(600, 300);
        logFrame.setResizable(false);
        logFrame.setLocationRelativeTo(null);

        getLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = fieldUsername.getText();
                String password = new String(fieldPassword.getPassword());

                try {
                    // Usa il nuovo metodo loginUtente che restituisce l'utente
                    Utente utenteLoggato = controller.loginUtente(username, password);
                    
                    if (utenteLoggato != null) {
                        // Login riuscito
                        System.out.println("Login riuscito per utente: " + username);
                        new UserView(utenteLoggato, frameCalling, controllerOrganizzatore, controller);
                        logFrame.dispose();
                    } else {
                        // Login fallito
                        JOptionPane.showMessageDialog(null, 
                            "Credenziali non valide. Riprova.", 
                            "Login Fallito", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Errore durante il login: " + ex.getMessage(), 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Costruttore della classe Login per gli organizzatori.
     * Inizializza l'interfaccia grafica e gestisce le azioni per il login degli organizzatori.
     *
     * @param controller Il controller per la gestione degli organizzatori.
     * @param frameCalling Il frame chiamante che ha aperto questa finestra.
     */
    public Login(ControllerOrganizzatore controller, JFrame frameCalling) {
        // Inizializza i componenti GUI se non sono stati inizializzati automaticamente
        if (loginPanel == null) {
            initializeComponents();
        }
        
        logFrame = new JFrame("Login");
        logFrame.setContentPane(loginPanel);

        logFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameCalling.setVisible(true);
                logFrame.dispose();
            }
        });

        logFrame.pack();
        logFrame.setVisible(true);
        logFrame.setSize(600, 300);
        logFrame.setResizable(false);
        logFrame.setLocationRelativeTo(null);

        getLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = fieldUsername.getText();
                String password = new String(fieldPassword.getPassword());

                try
                {
                    Organizzatore org = foundLoginOrg(controller.getListaOrganizzatori(), username, password);
                    if(org == null)
                    {
                        controller.aggiungiUtente(username, password);
                        new AdminView(controller.getListaOrganizzatori().getLast(), frameCalling);
                        logFrame.dispose();
                    }
                    else
                    {
                        new AdminView(org,frameCalling);
                        logFrame.dispose();
                    }
                }
                catch (IllegalArgumentException ex)
                {
                    JOptionPane.showMessageDialog(null, ex);
                }

            }
        });
    }
    
    /**
     * Inizializza i componenti GUI manualmente se non sono stati inizializzati automaticamente.
     */
    private void initializeComponents() {
        loginPanel = new JPanel();
        fieldUsername = new JTextField(20);
        fieldPassword = new JPasswordField(20);
        getLoginBtn = new JButton("Login");
        
        // Imposta layout del pannello
        loginPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        
        // Aggiunge etichetta username
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new java.awt.Insets(10, 10, 5, 5);
        loginPanel.add(new JLabel("Username:"), gbc);
        
        // Aggiunge campo username
        gbc.gridx = 1; gbc.gridy = 0; gbc.insets = new java.awt.Insets(10, 5, 5, 10);
        loginPanel.add(fieldUsername, gbc);
        
        // Aggiunge etichetta password
        gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new java.awt.Insets(5, 10, 5, 5);
        loginPanel.add(new JLabel("Password:"), gbc);
        
        // Aggiunge campo password
        gbc.gridx = 1; gbc.gridy = 1; gbc.insets = new java.awt.Insets(5, 5, 5, 10);
        loginPanel.add(fieldPassword, gbc);
        
        // Aggiunge pulsante login
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; 
        gbc.insets = new java.awt.Insets(15, 10, 10, 10);
        loginPanel.add(getLoginBtn, gbc);
    }
}