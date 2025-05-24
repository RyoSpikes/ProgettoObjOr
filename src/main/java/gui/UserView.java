package gui;

import controller.Controller;
import controller.ControllerOrganizzatore;
import model.Utente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * La classe UserView rappresenta l'interfaccia grafica per la visualizzazione delle funzionalità disponibili per un utente.
 * Consente di creare un team, visualizzare inviti, scegliere un team e visualizzare il team attuale.
 */
public class UserView {
    public JFrame userViewFrame; // Finestra principale della vista utente.
    private JPanel userPanel; // Pannello principale della finestra.
    private JTextArea userTextArea; // Area di testo per visualizzare informazioni.
    private JButton creaTeam; // Pulsante per creare un nuovo team.
    private JButton mostraInvitiButton; // Pulsante per mostrare gli inviti ricevuti.
    private JButton scegliTeamButton; // Pulsante per scegliere un team.
    private JButton visualizzaTeamButton; // Pulsante per visualizzare il team attuale.

    /**
     * Costruttore della classe UserView.
     * Inizializza l'interfaccia grafica e gestisce le azioni dei pulsanti.
     *
     * @param userLogged L'utente attualmente loggato.
     * @param frameHome Il frame principale dell'applicazione.
     * @param controllerOrganizzatore Il controller per la gestione degli organizzatori.
     * @param controllerUtente Il controller per la gestione degli utenti.
     */
    public UserView(Utente userLogged, JFrame frameHome, ControllerOrganizzatore controllerOrganizzatore, Controller controllerUtente) {
        userViewFrame = new JFrame("User View");
        userViewFrame.setContentPane(userPanel);
        userViewFrame.pack();

        // Listener per gestire la chiusura della finestra.
        userViewFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameHome.setVisible(true);
                userViewFrame.dispose();
            }
        });

        userViewFrame.setVisible(true);
        userViewFrame.setSize(800, 800);
        userViewFrame.setResizable(false);
        userViewFrame.setLocationRelativeTo(null);
        userTextArea.setEditable(false);

        // Listener per il pulsante "Crea Team".
        creaTeam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userViewFrame.setVisible(false);
                new CreaTeamForm(userLogged, userViewFrame, controllerOrganizzatore);
            }
        });

        // Listener per il pulsante "Mostra Inviti".
        mostraInvitiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userTextArea.append("");
                userTextArea.append("Non ci sono inviti da mostrare.");
            }
        });

        // Listener per il pulsante "Scegli Team".
        scegliTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ScegliTeam(userLogged, userViewFrame, controllerOrganizzatore, controllerUtente);
                userViewFrame.setVisible(false);
            }
        });

        // Listener per il pulsante "Visualizza Team".
        visualizzaTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userTextArea.setText("");
                try {
                    userTextArea.append(
                            "Nome Team: " + userLogged.getTeam().getNomeTeam() +
                                    "\nHackathon: \n" + userLogged.getTeam().getHackathon().printInfoEvento()
                    );
                } catch (NullPointerException ex) {
                    userTextArea.append("Non sei in nessun team.");
                }
            }
        });
    }
}