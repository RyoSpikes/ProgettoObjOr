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
 * La classe InfoTeam rappresenta l'interfaccia grafica per visualizzare e gestire le informazioni di un team.
 * Permette di partecipare o abbandonare un team.
 */
public class InfoTeam {
    private JTextField textField1;
    private JButton partecipaButton;
    private JButton abbandonaButton;
    private JPanel mainPanel;
    private JTextArea infoTextArea;
    
    private JFrame infoTeamFrame;
    private Team team;
    private Utente userLogged;
    private JFrame parentFrame;
    private TeamController teamController;

    /**
     * Costruttore della classe InfoTeam.
     * 
     * @param team Il team di cui mostrare le informazioni
     * @param userLogged L'utente attualmente loggato
     * @param parentFrame Il frame genitore
     * @param teamController Il controller per la gestione dei team
     */
    public InfoTeam(Team team, Utente userLogged, JFrame parentFrame, TeamController teamController) {
        this.team = team;
        this.userLogged = userLogged;
        this.parentFrame = parentFrame;
        this.teamController = teamController;
        
        // Inizializza i componenti GUI se non sono stati inizializzati automaticamente
        if (mainPanel == null) {
            initializeComponents();
        }
        
        infoTeamFrame = new JFrame("Info Team - " + team.getNomeTeam());
        infoTeamFrame.setContentPane(mainPanel);
        infoTeamFrame.pack();
        infoTeamFrame.setSize(500, 400);
        infoTeamFrame.setLocationRelativeTo(parentFrame);
        
        // Listener per gestire la chiusura della finestra
        infoTeamFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parentFrame.setVisible(true);
                infoTeamFrame.dispose();
            }
        });
        
        // Carica le informazioni del team
        caricaInformazioniTeam();
        
        // Configura i listener dei pulsanti
        setupListeners();
        
        // Verifica se l'utente è già nel team per abilitare/disabilitare i pulsanti
        verificaStatoUtente();
        
        infoTeamFrame.setVisible(true);
    }
    
    /**
     * Carica e mostra le informazioni del team.
     */
    private void caricaInformazioniTeam() {
        if (infoTextArea != null) {
            infoTextArea.setText("");
            infoTextArea.append("=== INFORMAZIONI TEAM ===\n\n");
            infoTextArea.append("Nome Team: " + team.getNomeTeam() + "\n");
            
            if (team.getHackathon() != null) {
                infoTextArea.append("Hackathon: " + team.getHackathon().getTitoloIdentificativo() + "\n");
                infoTextArea.append("Data inizio: " + team.getHackathon().getDataInizio() + "\n");
                infoTextArea.append("Data fine: " + team.getHackathon().getDataFine() + "\n\n");
            }
            
            try {
                // Carica i membri del team
                var membri = teamController.getMembershipDAO().getTeamMembers(
                    team.getNomeTeam(), 
                    team.getHackathon().getTitoloIdentificativo()
                );
                
                infoTextArea.append("=== MEMBRI DEL TEAM ===\n");
                if (membri != null && !membri.isEmpty()) {
                    for (var membro : membri) {
                        infoTextArea.append("- " + membro.getName() + "\n");
                    }
                    infoTextArea.append("\nNumero membri: " + membri.size() + "\n");
                } else {
                    infoTextArea.append("Nessun membro trovato.\n");
                }
            } catch (Exception e) {
                infoTextArea.append("Errore nel caricamento dei membri: " + e.getMessage() + "\n");
            }
        }
        
        if (textField1 != null) {
            textField1.setText(team.getNomeTeam());
            textField1.setEditable(false);
        }
    }
    
    /**
     * Verifica se l'utente è già membro del team e aggiorna lo stato dei pulsanti.
     */
    private void verificaStatoUtente() {
        try {
            boolean isUserInTeam = teamController.getMembershipDAO().isUserInTeamForHackathon(
                userLogged.getName(),
                team.getHackathon().getTitoloIdentificativo()
            );
            
            if (partecipaButton != null) {
                partecipaButton.setEnabled(!isUserInTeam);
                partecipaButton.setText(isUserInTeam ? "Già nel team" : "Partecipa");
            }
            
            if (abbandonaButton != null) {
                abbandonaButton.setEnabled(isUserInTeam);
            }
        } catch (Exception e) {
            // In caso di errore, mantieni i pulsanti abilitati
            System.err.println("Errore nella verifica dello stato utente: " + e.getMessage());
        }
    }
    
    /**
     * Configura i listener dei pulsanti.
     */
    private void setupListeners() {
        // Listener per il pulsante "Partecipa"
        if (partecipaButton != null) {
            partecipaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        teamController.aggiungiUtenteATeam(userLogged, team);
                        
                        JOptionPane.showMessageDialog(infoTeamFrame,
                            "Ti sei unito al team '" + team.getNomeTeam() + "' con successo!",
                            "Partecipazione confermata",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Aggiorna lo stato dei pulsanti e le informazioni
                        verificaStatoUtente();
                        caricaInformazioniTeam();
                        
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(infoTeamFrame,
                            "Errore durante la partecipazione al team: " + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
        
        // Listener per il pulsante "Abbandona"
        if (abbandonaButton != null) {
            abbandonaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = JOptionPane.showConfirmDialog(
                        infoTeamFrame,
                        "Sei sicuro di voler abbandonare il team '" + team.getNomeTeam() + "'?",
                        "Conferma abbandono",
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
                                JOptionPane.showMessageDialog(infoTeamFrame,
                                    "Hai abbandonato il team con successo.",
                                    "Team abbandonato",
                                    JOptionPane.INFORMATION_MESSAGE);
                                
                                // Aggiorna lo stato dei pulsanti e le informazioni
                                verificaStatoUtente();
                                caricaInformazioniTeam();
                            } else {
                                JOptionPane.showMessageDialog(infoTeamFrame,
                                    "Errore durante l'abbandono del team.",
                                    "Errore",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(infoTeamFrame,
                                "Errore durante l'abbandono del team: " + ex.getMessage(),
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
        }
    }

    /**
     * Inizializza manualmente i componenti GUI quando il file .form non è disponibile.
     */
    private void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new java.awt.BorderLayout());
        
        // Pannello superiore con il campo di testo
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Nome Team: "));
        textField1 = new JTextField(20);
        topPanel.add(textField1);
        
        // Area di testo per le informazioni del team
        infoTextArea = new JTextArea(15, 40);
        infoTextArea.setEditable(false);
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        
        // Crea i pulsanti
        partecipaButton = new JButton("Partecipa");
        abbandonaButton = new JButton("Abbandona");
        
        // Pannello per i pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(partecipaButton);
        buttonPanel.add(abbandonaButton);
        
        // Aggiungi i componenti al pannello principale
        mainPanel.add(topPanel, java.awt.BorderLayout.NORTH);
        mainPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
        mainPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
    }
}
