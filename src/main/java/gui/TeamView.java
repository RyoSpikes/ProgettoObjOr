package gui;

import controller.TeamController;
import model.Team;
import model.Utente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * La classe TeamView rappresenta l'interfaccia grafica per la visualizzazione e gestione di un team.
 * Permette di inserire documenti, abbandonare il team e visualizzare informazioni del team.
 */
public class TeamView {
    private JPanel panel1;
    private JButton inserisciDocumentoButton;
    private JButton FOZZANAPOLIButton;
    private JButton abbandonaTeamButton1;
    private JTextArea TeamTextArea;
    
    private JFrame teamViewFrame;
    private Team team;
    private Utente userLogged;
    private JFrame parentFrame;
    private TeamController teamController;

    /**
     * Costruttore della classe TeamView.
     * 
     * @param team Il team da visualizzare/gestire
     * @param userLogged L'utente attualmente loggato
     * @param parentFrame Il frame genitore
     * @param teamController Il controller per la gestione dei team
     */
    public TeamView(Team team, Utente userLogged, JFrame parentFrame, TeamController teamController) {
        this.team = team;
        this.userLogged = userLogged;
        this.parentFrame = parentFrame;
        this.teamController = teamController;
        
        teamViewFrame = new JFrame("Team View - " + team.getNomeTeam());
        teamViewFrame.setContentPane(panel1);
        teamViewFrame.pack();
        teamViewFrame.setSize(600, 400);
        teamViewFrame.setLocationRelativeTo(parentFrame);
        
        // Listener per gestire la chiusura della finestra
        teamViewFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parentFrame.setVisible(true);
                teamViewFrame.dispose();
            }
        });
        
        // Carica le informazioni del team
        caricaInformazioniTeam();
        
        // Configura i listener dei pulsanti
        setupListeners();
        
        teamViewFrame.setVisible(true);
    }
    
    /**
     * Carica e mostra le informazioni del team nell'area di testo.
     */
    private void caricaInformazioniTeam() {
        if (TeamTextArea != null) {
            TeamTextArea.setText("");
            TeamTextArea.append("=== INFORMAZIONI TEAM ===\n\n");
            TeamTextArea.append("Nome Team: " + team.getNomeTeam() + "\n");
            
            if (team.getHackathon() != null) {
                TeamTextArea.append("Hackathon: " + team.getHackathon().getTitoloIdentificativo() + "\n");
                TeamTextArea.append("Data inizio: " + team.getHackathon().getDataInizio() + "\n");
                TeamTextArea.append("Data fine: " + team.getHackathon().getDataFine() + "\n\n");
            }
            
            try {
                // Carica i membri del team
                var membri = teamController.getMembershipDAO().getTeamMembers(
                    team.getNomeTeam(), 
                    team.getHackathon().getTitoloIdentificativo()
                );
                
                TeamTextArea.append("=== MEMBRI DEL TEAM ===\n");
                if (membri != null && !membri.isEmpty()) {
                    for (var membro : membri) {
                        TeamTextArea.append("- " + membro.getName() + "\n");
                    }
                } else {
                    TeamTextArea.append("Nessun membro trovato.\n");
                }
            } catch (Exception e) {
                TeamTextArea.append("Errore nel caricamento dei membri: " + e.getMessage() + "\n");
            }
        }
    }
    
    /**
     * Configura i listener dei pulsanti.
     */
    private void setupListeners() {
        // Listener per il pulsante "Inserisci Documento"
        if (inserisciDocumentoButton != null) {
            inserisciDocumentoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO: Implementare l'inserimento di documenti
                    JOptionPane.showMessageDialog(teamViewFrame, 
                        "Funzionalità di inserimento documenti non ancora implementata.", 
                        "In sviluppo", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
        
        // Listener per il pulsante "FOZZA NAPOLI" (lasciato vuoto come richiesto)
        if (FOZZANAPOLIButton != null) {
            FOZZANAPOLIButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Lasciato vuoto come richiesto
                }
            });
        }
        
        // Listener per il pulsante "Abbandona Team"
        if (abbandonaTeamButton1 != null) {
            abbandonaTeamButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = JOptionPane.showConfirmDialog(
                        teamViewFrame,
                        "Sei sicuro di voler abbandonare il team '" + team.getNomeTeam() + "'?",
                        "Conferma abbandono team",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    
                    if (result == JOptionPane.YES_OPTION) {
                        try {
                            boolean success = teamController.getMembershipDAO().removeUserFromTeam(
                                userLogged.getName(),
                                team.getNomeTeam(),
                                team.getHackathon().getTitoloIdentificativo()
                            );
                            
                            if (success) {
                                JOptionPane.showMessageDialog(teamViewFrame,
                                    "Hai abbandonato il team con successo.",
                                    "Team abbandonato",
                                    JOptionPane.INFORMATION_MESSAGE);
                                
                                // Chiudi la finestra e torna al frame genitore
                                parentFrame.setVisible(true);
                                teamViewFrame.dispose();
                            } else {
                                JOptionPane.showMessageDialog(teamViewFrame,
                                    "Errore durante l'abbandono del team.",
                                    "Errore",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(teamViewFrame,
                                "Errore durante l'abbandono del team: " + ex.getMessage(),
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
        }
    }
}

