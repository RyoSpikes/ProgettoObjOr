package gui;

import controller.Controller;
import controller.ControllerOrganizzatore;
import model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La classe Home rappresenta l'interfaccia grafica principale dell'applicazione.
 * Consente l'accesso alle funzionalità per gli utenti e gli organizzatori,
 * oltre alla visualizzazione delle liste di utenti e organizzatori.
 */
public class Home {
    private JPanel mainPanel; // Pannello principale della finestra.
    private JTextArea homeTextArea; // Area di testo per visualizzare informazioni.
    private JButton stampaUtentiBtn; // Pulsante per stampare la lista degli utenti.
    private JScrollPane textAreaScrollPane; // ScrollPane per l'area di testo.
    private JButton registratiBtn; // Pulsante per accedere come utente.
    private JButton accediBtn; // Pulsante per accedere.
    private JButton stampaOrganizzatoriBtn; // Pulsante per stampare la lista degli organizzatori.
    public static JFrame frame; // Finestra principale dell'applicazione.
    private Controller userController; // Controller per la gestione degli utenti.
    private ControllerOrganizzatore orgController; // Controller per la gestione degli organizzatori.

    /**
     * Metodo principale dell'applicazione.
     * Inizializza e visualizza la finestra principale.
     *
     * @param args Argomenti della riga di comando.
     */
    public static void main(String[] args) {
        frame = new JFrame("Home");
        frame.setContentPane(new Home().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800, 800);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Costruttore della classe Home.
     * Inizializza i controller e configura i listener per i pulsanti.
     */
    public Home() {
        userController = new Controller();
        orgController = new ControllerOrganizzatore();
        
        // Initialize GUI components if not already done
        if (homeTextArea == null) {
            initializeComponents();
        }
        
        homeTextArea.setEditable(false);

        // Listener per il pulsante "Stampa Utenti".
        stampaUtentiBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeTextArea.setText("");
                if(userController.getListaUtenti().isEmpty())
                    homeTextArea.append("Lista Utenti vuota.");
                else {
                    for (Utente u : userController.getListaUtenti()) {
                        homeTextArea.append(
                                "Utente: " + u.getName() + "\n"
                        );
                    }
                }
            }
        });

        // Listener per il pulsante "Accedi".
        accediBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeTextArea.setText("");
                frame.setVisible(false);
                new Login(orgController, userController, frame);
            }
        });

        // Listener per il pulsante "Stampa Organizzatori".
        stampaOrganizzatoriBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeTextArea.setText("");

                if(orgController.getListaOrganizzatori().isEmpty())
                    homeTextArea.append("Lista Organizzatori vuota.");
                else {
                    for (Organizzatore org : orgController.getListaOrganizzatori()) {
                        homeTextArea.append(
                                "Organizzatore: " + org.getName() + "\n"
                        );
                    }
                }
            }
        });
    }
    
    /**
     * Inizializza i componenti GUI manualmente se non sono stati inizializzati automaticamente.
     */
    private void initializeComponents() {
        mainPanel = new JPanel();
        homeTextArea = new JTextArea();
        textAreaScrollPane = new JScrollPane(homeTextArea);
        stampaUtentiBtn = new JButton("Stampa Utenti");
        registratiBtn = new JButton("Accesso Utente");
        accediBtn = new JButton("Accedi");
        stampaOrganizzatoriBtn = new JButton("Stampa Organizzatori");
        
        // Set basic properties
        homeTextArea.setLineWrap(true);
        homeTextArea.setWrapStyleWord(true);
        
        // Add components to panel (basic layout)
        mainPanel.setLayout(new java.awt.BorderLayout());
        
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("HOME"));
        
        JPanel centerPanel = new JPanel();
        textAreaScrollPane.setPreferredSize(new java.awt.Dimension(400, 400));
        centerPanel.add(textAreaScrollPane);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(stampaUtentiBtn);
        buttonPanel.add(stampaOrganizzatoriBtn);
        buttonPanel.add(registratiBtn);
        buttonPanel.add(accediBtn);

        mainPanel.add(topPanel, java.awt.BorderLayout.NORTH);
        mainPanel.add(centerPanel, java.awt.BorderLayout.CENTER);
        mainPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
    }
}
