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
    private JButton accediBtn; // Pulsante per accedere.
    private JButton stampaOrganizzatoriBtn; // Pulsante per stampare la lista degli organizzatori.

    public static JFrame frame; // Finestra principale dell'applicazione.
    private final Controller userController; // Controller per la gestione degli utenti.
    private final ControllerOrganizzatore orgController; // Controller per la gestione degli organizzatori.

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
     * Inizializza i controller e configura i listener per i pulsanti.
     */
    public Home() {
        userController = new Controller();
        orgController = new ControllerOrganizzatore();

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
                        if(userController.getListaUtenti().isEmpty()) {
                            homeTextArea.append("Lista Utenti vuota.");
                        } else {
                            for (Utente u : userController.getListaUtenti()) {
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
                    new Login(orgController, userController, frame);
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
                        if(orgController.getListaOrganizzatori().isEmpty()) {
                            homeTextArea.append("Lista Organizzatori vuota.");
                        } else {
                            for (Organizzatore org : orgController.getListaOrganizzatori()) {
                                homeTextArea.append("Organizzatore: " + org.getName() + "\n");
                            }
                        }
                    }
                }
            });
        }
    }
}
