package gui;

import controller.HackathonController;
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
    private JButton accediBtn; // Pulsante per accedere.
    private JButton stampaOrganizzatoriBtn; // Pulsante per stampare la lista degli organizzatori.

    public static JFrame frame; // Finestra principale dell'applicazione.
    private final HackathonController hackathonController; // Controller principale dell'applicazione.

    /**
     * Metodo principale dell'applicazione.
     * Inizializza e visualizza la finestra principale.
     *
     * @param args Argomenti della riga di comando.
     */
    public static void main(String[] args) {
        // Crea un'istanza di Home prima di creare il frame
        Home homeInstance = new Home();

        frame = new JFrame("Home");
        frame.setContentPane(homeInstance.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800, 800);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Costruttore della classe Home.
     * Inizializza il controller principale e configura i listener per i pulsanti.
     */
    public Home() {
        hackathonController = new HackathonController();

        // Ensure homeTextArea is not null before using it
        if (homeTextArea != null) {
            homeTextArea.setEditable(false);
        }

        // Listener per il pulsante "Stampa Utenti"
        if (stampaUtentiBtn != null) {
            stampaUtentiBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (homeTextArea != null) {
                        homeTextArea.setText("");
                        if(hackathonController.getListaUtenti().isEmpty()) {
                            homeTextArea.append("Lista Utenti vuota.");
                        } else {
                            for (Utente u : hackathonController.getListaUtenti()) {
                                homeTextArea.append("Utente: " + u.getName() + "\n");
                            }
                        }
                    }
                }
            });
        }

        // Listener per il pulsante "Accedi"
        if (accediBtn != null) {
            accediBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (homeTextArea != null) {
                        homeTextArea.setText("");
                    }
                    frame.setVisible(false);
                    new Login(hackathonController, frame);
                }
            });
        }

        // Listener per il pulsante "Stampa Organizzatori"
        if (stampaOrganizzatoriBtn != null) {
            stampaOrganizzatoriBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (homeTextArea != null) {
                        homeTextArea.setText("");
                        if(hackathonController.getListaOrganizzatori().isEmpty()) {
                            homeTextArea.append("Lista Organizzatori vuota.");
                        } else {
                            for (Organizzatore org : hackathonController.getListaOrganizzatori()) {
                                homeTextArea.append("Organizzatore: " + org.getName() + "\n");
                            }
                        }
                    }
                }
            });
        }
    }
    
    /**
     * Inizializza manualmente i componenti GUI quando il file .form non è disponibile
     */
    private void initializeComponents() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setLayout(new java.awt.BorderLayout());
            
            // Crea l'area di testo
            homeTextArea = new JTextArea(20, 40);
            homeTextArea.setEditable(false);
            textAreaScrollPane = new JScrollPane(homeTextArea);
            
            // Crea i pulsanti
            stampaUtentiBtn = new JButton("Stampa Utenti");
            accediBtn = new JButton("Accedi");
            stampaOrganizzatoriBtn = new JButton("Stampa Organizzatori");
            
            // Crea un pannello per i pulsanti
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(stampaUtentiBtn);
            buttonPanel.add(accediBtn);
            buttonPanel.add(stampaOrganizzatoriBtn);
            
            // Aggiungi i componenti al pannello principale
            mainPanel.add(textAreaScrollPane, java.awt.BorderLayout.CENTER);
            mainPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
        }
    }
}
