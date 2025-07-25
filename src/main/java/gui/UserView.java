package gui;

import controller.UserController;
import controller.HackathonController;
import controller.TeamController;
import model.Utente;
import model.Team;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

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
    private JButton gestisciTeamButton; // Pulsante per gestire il team (aprire TeamView).
    private JButton menuGiudiceButton;
    private TeamController teamController; // Controller per la gestione dei team.

    /**
     * Costruttore della classe UserView.
     * Inizializza l'interfaccia grafica e gestisce le azioni dei pulsanti.
     *
     * @param userLogged L'utente attualmente loggato.
     * @param frameHome Il frame principale dell'applicazione.
     * @param hackathonController Il controller per la gestione degli organizzatori.
     * @param controllerUtente Il controller per la gestione degli utenti.
    /**
     * Costruttore della classe UserView.
     * Inizializza l'interfaccia grafica e gestisce le azioni dei pulsanti.
     *
     * @param userLogged L'utente attualmente loggato
     * @param frameHome Il frame principale dell'applicazione
     * @param hackathonController Il controller per la gestione degli organizzatori
     * @param controllerUtente Il controller per la gestione degli utenti
     */
    public UserView(Utente userLogged, JFrame frameHome, HackathonController hackathonController, UserController controllerUtente) {
        // Inizializza il TeamController
        this.teamController = new TeamController();
        
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

        // Listener per il pulsante "Crea Team".
        creaTeam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userViewFrame.setVisible(false);
                new CreaTeamForm(userLogged, userViewFrame, hackathonController);
            }
        });

        // Listener per il pulsante "Mostra Inviti".
        mostraInvitiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<String> inviti = hackathonController.getInvitiGiudice(userLogged.getName());
                    
                    if (inviti != null && !inviti.isEmpty()) {
                        String[] invitiArray = inviti.toArray(new String[0]);
                        
                        String selectedInvito = (String) JOptionPane.showInputDialog(
                            userViewFrame,
                            "Seleziona un invito a giudice:",
                            "I tuoi inviti",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            invitiArray,
                            invitiArray[0]
                        );
                        
                        if (selectedInvito != null) {
                            // Estrai il titolo dell'hackathon dall'invito
                            String titoloHackathon = selectedInvito.split("'")[1]; // Estrae il titolo tra apici
                            
                            int result = JOptionPane.showConfirmDialog(
                                userViewFrame,
                                "Vuoi accettare l'invito a giudicare l'hackathon '" + titoloHackathon + "'?",
                                "Conferma invito",
                                JOptionPane.YES_NO_OPTION
                            );
                            
                            if (result == JOptionPane.YES_OPTION) {
                                boolean success = hackathonController.accettaInvito(userLogged.getName(), titoloHackathon);
                                if (success) {
                                    JOptionPane.showMessageDialog(userViewFrame,
                                        "Invito accettato! Ora sei un giudice per l'hackathon '" + titoloHackathon + "'",
                                        "Invito accettato",
                                        JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(userViewFrame,
                                        "Errore nell'accettazione dell'invito.",
                                        "Errore",
                                        JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                boolean success = hackathonController.rifiutaInvito(userLogged.getName(), titoloHackathon);
                                if (success) {
                                    JOptionPane.showMessageDialog(userViewFrame,
                                        "Invito rifiutato.",
                                        "Invito rifiutato",
                                        JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(userViewFrame, 
                            "Non hai inviti a giudice pendenti.", 
                            "Nessun invito", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(userViewFrame, 
                        "Errore nel recupero degli inviti: " + ex.getMessage(), 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Listener per il pulsante "Scegli Team".
        scegliTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ScegliTeam(userLogged, userViewFrame, hackathonController, controllerUtente);
                // Rimosso userViewFrame.setVisible(false); - UserView deve rimanere visibile
            }
        });

        // Listener per il pulsante "Visualizza Team".
        visualizzaTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Ottiene tutti i team dell'utente usando la DAO
                    List<Team> teamsUtente = teamController.getMembershipDAO().getTeamsByUser(userLogged.getName());
                    
                    if (teamsUtente != null && !teamsUtente.isEmpty()) {
                        // Crea una lista cliccabile dei team
                        String[] teamNames = new String[teamsUtente.size()];
                        for (int i = 0; i < teamsUtente.size(); i++) {
                            Team team = teamsUtente.get(i);
                            String hackathonName = team.getHackathon() != null ? 
                                team.getHackathon().getTitoloIdentificativo() : "Hackathon sconosciuto";
                            teamNames[i] = team.getNomeTeam() + " - " + hackathonName;
                        }
                        
                        // Mostra la lista in un dialogo
                        String selectedTeam = (String) JOptionPane.showInputDialog(
                            userViewFrame,
                            "Seleziona un team per visualizzare i dettagli:",
                            "I tuoi team",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            teamNames,
                            teamNames[0]
                        );
                        
                        if (selectedTeam != null) {
                            // Trova il team selezionato
                            Team teamSelezionato = null;
                            for (Team team : teamsUtente) {
                                String hackathonName = team.getHackathon() != null ? 
                                    team.getHackathon().getTitoloIdentificativo() : "Hackathon sconosciuto";
                                if (selectedTeam.equals(team.getNomeTeam() + " - " + hackathonName)) {
                                    teamSelezionato = team;
                                    break;
                                }
                            }
                            
                            if (teamSelezionato != null) {
                                // Apri TeamView form invece di InfoTeam
                                new TeamView(teamSelezionato, userLogged, userViewFrame, teamController);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(userViewFrame, 
                            "Non sei in nessun team.", 
                            "Nessun team", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(userViewFrame, 
                        "Errore nel recupero delle informazioni dei team: " + ex.getMessage(), 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Listener per il pulsante "Gestisci Team" (se presente nel form).
        if (gestisciTeamButton != null) {
            gestisciTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Ottiene tutti i team dell'utente usando la DAO
                    List<Team> teamsUtente = teamController.getMembershipDAO().getTeamsByUser(userLogged.getName());
                    
                    if (teamsUtente != null && !teamsUtente.isEmpty()) {
                        // Crea una lista cliccabile dei team
                        String[] teamNames = new String[teamsUtente.size()];
                        for (int i = 0; i < teamsUtente.size(); i++) {
                            Team team = teamsUtente.get(i);
                            String hackathonName = team.getHackathon() != null ? 
                                team.getHackathon().getTitoloIdentificativo() : "Hackathon sconosciuto";
                            teamNames[i] = team.getNomeTeam() + " - " + hackathonName;
                        }
                        
                        // Mostra la lista in un dialogo
                        String selectedTeam = (String) JOptionPane.showInputDialog(
                            userViewFrame,
                            "Seleziona un team da gestire:",
                            "Gestisci Team",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            teamNames,
                            teamNames[0]
                        );
                        
                        if (selectedTeam != null) {
                            // Trova il team selezionato
                            Team teamSelezionato = null;
                            for (Team team : teamsUtente) {
                                String hackathonName = team.getHackathon() != null ? 
                                    team.getHackathon().getTitoloIdentificativo() : "Hackathon sconosciuto";
                                if (selectedTeam.equals(team.getNomeTeam() + " - " + hackathonName)) {
                                    teamSelezionato = team;
                                    break;
                                }
                            }
                            
                            if (teamSelezionato != null) {
                                // Apri TeamView per gestire il team
                                userViewFrame.setVisible(false);
                                new TeamView(teamSelezionato, userLogged, userViewFrame, teamController);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(userViewFrame, 
                            "Non sei in nessun team da gestire.", 
                            "Nessun team", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(userViewFrame, 
                        "Errore nel recupero delle informazioni dei team: " + ex.getMessage(), 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        } // Fine del controllo if (gestisciTeamButton != null)

        // Listener per il pulsante "Menu Giudice".
        menuGiudiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<String> hackathons = hackathonController.getHackathonAsGiudice(userLogged.getName());
                    
                    if (hackathons != null && !hackathons.isEmpty()) {
                        String[] hackathonsArray = hackathons.toArray(new String[0]);
                        
                        String selectedHackathon = (String) JOptionPane.showInputDialog(
                            userViewFrame,
                            "Seleziona un hackathon di cui sei giudice:",
                            "Menu Giudice",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            hackathonsArray,
                            hackathonsArray[0]
                        );
                        
                        if (selectedHackathon != null) {
                            // Estrai il titolo dell'hackathon (prima del primo " - ")
                            String titoloHackathon = selectedHackathon.split(" - ")[0];
                            
                            // Apri JudgeView
                            try {
                                userViewFrame.setVisible(false);
                                new JudgeView(titoloHackathon, userLogged, userViewFrame);
                            } catch (Exception ex) {
                                userViewFrame.setVisible(true); // Ripristina la visibilità in caso di errore
                                JOptionPane.showMessageDialog(userViewFrame, 
                                    "Errore nell'apertura della vista giudice:\n" + ex.getMessage(), 
                                    "Errore", 
                                    JOptionPane.ERROR_MESSAGE);
                                ex.printStackTrace();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(userViewFrame, 
                            "Non sei giudice di nessun hackathon.", 
                            "Nessun hackathon", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(userViewFrame, 
                        "Errore nel recupero degli hackathon: " + ex.getMessage(), 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
}

