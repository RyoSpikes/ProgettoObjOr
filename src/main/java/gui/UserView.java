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
        // Inizializza i componenti GUI se non sono stati inizializzati automaticamente
        if (userPanel == null) {
            initializeComponents();
        }
        
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
                userTextArea.setText("Non ci sono inviti da mostrare.");
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
    
    /**
     * Inizializza i componenti GUI manualmente se non sono stati inizializzati automaticamente.
     */
    private void initializeComponents() {
        userPanel = new JPanel();
        userTextArea = new JTextArea(15, 40);
        creaTeam = new JButton("Crea Team");
        mostraInvitiButton = new JButton("Mostra Inviti");
        scegliTeamButton = new JButton("Scegli Team");
        visualizzaTeamButton = new JButton("Visualizza Team");
        
        // Imposta proprietà dell'area di testo
        userTextArea.setEditable(false);
        userTextArea.setLineWrap(true);
        userTextArea.setWrapStyleWord(true);
        
        // Crea scroll pane per l'area di testo
        JScrollPane scrollPane = new JScrollPane(userTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Imposta layout del pannello
        userPanel.setLayout(new java.awt.BorderLayout());
        
        // Pannello superiore con titolo
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Area Utente"));
        
        // Pannello centrale con area di testo
        JPanel centerPanel = new JPanel();
        centerPanel.add(scrollPane);
        
        // Pannello inferiore con pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new java.awt.FlowLayout());
        buttonPanel.add(creaTeam);
        buttonPanel.add(mostraInvitiButton);
        buttonPanel.add(scegliTeamButton);
        buttonPanel.add(visualizzaTeamButton);
        
        // Aggiungi i pannelli al pannello principale
        userPanel.add(topPanel, java.awt.BorderLayout.NORTH);
        userPanel.add(centerPanel, java.awt.BorderLayout.CENTER);
        userPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
    }
}