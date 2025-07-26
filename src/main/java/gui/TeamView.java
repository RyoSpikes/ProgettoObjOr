package gui;

import controller.TeamController;
import controller.HackathonController;
import model.Team;
import model.Utente;
import model.Documento;

import javax.swing.*;
import java.awt.*;
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
    
    // Controller per gestire la logica di business
    private TeamController teamController;
    private HackathonController hackathonController;

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
        
        // Inizializza i DAO
        try {
            this.teamController = new TeamController();
            this.hackathonController = new HackathonController();
        } catch (Exception e) {
            System.err.println("Errore nell'inizializzazione dei DAO: " + e.getMessage());
        }
        
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
                
                TeamTextArea.append("\n");
                
                // Carica i documenti del team
                if (hackathonController != null) {
                    var documenti = hackathonController.getDocumentiByTeam(
                            team.getNomeTeam(),
                            team.getHackathon().getTitoloIdentificativo()
                    );                    TeamTextArea.append("=== DOCUMENTI CARICATI ===\n");
                    if (documenti != null && !documenti.isEmpty()) {
                        for (var documento : documenti) {
                            TeamTextArea.append("📄 " + documento.getTitle() + "\n");
                            // Mostra una preview del contenuto (prime 100 caratteri)
                            String preview = documento.getText();
                            if (preview.length() > 100) {
                                preview = preview.substring(0, 100) + "...";
                            }
                            TeamTextArea.append("   " + preview.replaceAll("\n", " ") + "\n\n");
                        }
                        TeamTextArea.append("📊 Totale documenti: " + documenti.size() + "\n");
                    } else {
                        TeamTextArea.append("Nessun documento caricato ancora.\n");
                        TeamTextArea.append("💡 Usa il bottone 'Inserisci Documento' per aggiungerne uno.\n");
                    }
                }
                
            } catch (Exception e) {
                TeamTextArea.append("Errore nel caricamento delle informazioni: " + e.getMessage() + "\n");
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
                    apriFinstraInserimentoDocumento();
                }
            });
        }
        
        // Listener per il pulsante "FOZZA NAPOLI" - Utilizzato per mostrare la classifica
        if (FOZZANAPOLIButton != null) {
            FOZZANAPOLIButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mostraClassificaHackathon();
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
    
    /**
     * Apre una finestra di dialogo per l'inserimento di un nuovo documento.
     */
    private void apriFinstraInserimentoDocumento() {
        // Crea un dialogo personalizzato
        JDialog dialogoInserimento = new JDialog(teamViewFrame, "Inserisci Nuovo Documento", true);
        dialogoInserimento.setSize(500, 400);
        dialogoInserimento.setLocationRelativeTo(teamViewFrame);
        
        // Panel principale
        JPanel panelPrincipale = new JPanel();
        panelPrincipale.setLayout(new BoxLayout(panelPrincipale, BoxLayout.Y_AXIS));
        panelPrincipale.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Titolo
        JLabel labelTitolo = new JLabel("📄 Inserimento Nuovo Documento");
        labelTitolo.setFont(labelTitolo.getFont().deriveFont(16f));
        labelTitolo.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        // Info team
        JLabel labelTeam = new JLabel("Team: " + team.getNomeTeam() + " | Hackathon: " + team.getHackathon().getTitoloIdentificativo());
        labelTeam.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        // Campo titolo documento
        JLabel labelTitoloDoc = new JLabel("Titolo del documento:");
        JTextField campoTitolo = new JTextField();
        campoTitolo.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, campoTitolo.getPreferredSize().height));
        
        // Campo contenuto documento
        JLabel labelContenuto = new JLabel("Contenuto del documento:");
        JTextArea areaContenuto = new JTextArea(8, 40);
        areaContenuto.setLineWrap(true);
        areaContenuto.setWrapStyleWord(true);
        JScrollPane scrollContenuto = new JScrollPane(areaContenuto);
        
        // Panel per i bottoni
        JPanel panelBottoni = new JPanel();
        panelBottoni.setLayout(new FlowLayout());
        
        JButton bottoneInserisci = new JButton("💾 Inserisci Documento");
        JButton bottoneAnnulla = new JButton("❌ Annulla");
        
        // Listener per il bottone Inserisci
        bottoneInserisci.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titolo = campoTitolo.getText().trim();
                String contenuto = areaContenuto.getText().trim();
                
                // Validazione input
                if (titolo.isEmpty()) {
                    JOptionPane.showMessageDialog(dialogoInserimento, 
                        "Il titolo del documento è obbligatorio.", 
                        "Campo mancante", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (contenuto.isEmpty()) {
                    JOptionPane.showMessageDialog(dialogoInserimento, 
                        "Il contenuto del documento è obbligatorio.", 
                        "Campo mancante", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Controlla lunghezza massima (in base allo schema DB)
                if (titolo.length() > 30) {
                    JOptionPane.showMessageDialog(dialogoInserimento, 
                        "Il titolo non può superare i 30 caratteri.", 
                        "Titolo troppo lungo", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    // Crea il documento
                    Documento nuovoDocumento = new Documento(team, titolo, contenuto);
                    
                    // Salva nel database
                    if (hackathonController.salvaDocumento(nuovoDocumento)) {
                        JOptionPane.showMessageDialog(dialogoInserimento, 
                            "Documento inserito con successo!", 
                            "Successo", 
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        dialogoInserimento.dispose();
                        
                        // Ricarica le informazioni del team per mostrare il nuovo documento
                        caricaInformazioniTeam();
                        
                    } else {
                        JOptionPane.showMessageDialog(dialogoInserimento, 
                            "Errore durante l'inserimento del documento.", 
                            "Errore", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialogoInserimento, 
                        "Errore durante l'inserimento: " + ex.getMessage(), 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        
        // Listener per il bottone Annulla
        bottoneAnnulla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogoInserimento.dispose();
            }
        });
        
        // Aggiungi componenti al panel
        panelPrincipale.add(labelTitolo);
        panelPrincipale.add(Box.createVerticalStrut(10));
        panelPrincipale.add(labelTeam);
        panelPrincipale.add(Box.createVerticalStrut(20));
        panelPrincipale.add(labelTitoloDoc);
        panelPrincipale.add(Box.createVerticalStrut(5));
        panelPrincipale.add(campoTitolo);
        panelPrincipale.add(Box.createVerticalStrut(15));
        panelPrincipale.add(labelContenuto);
        panelPrincipale.add(Box.createVerticalStrut(5));
        panelPrincipale.add(scrollContenuto);
        panelPrincipale.add(Box.createVerticalStrut(20));
        
        panelBottoni.add(bottoneInserisci);
        panelBottoni.add(bottoneAnnulla);
        panelPrincipale.add(panelBottoni);
        
        dialogoInserimento.add(panelPrincipale);
        dialogoInserimento.setVisible(true);
    }
    
    /**
     * Mostra la classifica dell'hackathon utilizzando la funzione del database.
     */
    private void mostraClassificaHackathon() {
        if (TeamTextArea != null && hackathonController != null && team != null && team.getHackathon() != null) {
            try {
                String titoloHackathon = team.getHackathon().getTitoloIdentificativo();
                
                TeamTextArea.setText("");
                TeamTextArea.append("🏆 CLASSIFICA HACKATHON: " + titoloHackathon + "\n");
                TeamTextArea.append("═".repeat(70) + "\n\n");
                TeamTextArea.append("⏳ Elaborazione in corso...\n\n");
                
                // Chiama la funzione del database per generare la classifica
                String risultatoClassifica = hackathonController.generaClassificaHackathon(titoloHackathon);
                
                TeamTextArea.setText("");
                TeamTextArea.append("🏆 CLASSIFICA HACKATHON: " + titoloHackathon + "\n");
                TeamTextArea.append("═".repeat(70) + "\n\n");
                
                // Verifica se il risultato è un errore
                if (risultatoClassifica.startsWith("Errore:")) {
                    TeamTextArea.append("❌ " + risultatoClassifica + "\n\n");
                    
                    // Aggiungi suggerimenti basati sul tipo di errore
                    if (risultatoClassifica.contains("prima della fine dell'hackathon")) {
                        TeamTextArea.append("💡 La classifica sarà disponibile al termine dell'hackathon.\n");
                        TeamTextArea.append("📅 Data fine evento: " + team.getHackathon().getDataFine() + "\n");
                    } else if (risultatoClassifica.contains("Mancano") && risultatoClassifica.contains("voti")) {
                        TeamTextArea.append("💡 I giudici stanno ancora valutando i team.\n");
                        TeamTextArea.append("⏳ La classifica sarà disponibile quando tutti i voti saranno stati espressi.\n");
                    }
                    
                } else {
                    // Classifica generata con successo - formatta e mostra
                    TeamTextArea.append("✅ Ecco la classifica finale!\n\n");
                    
                    String[] righeClassifica = risultatoClassifica.split("\n");
                    int posizioneTeamCorrente = -1;
                    
                    for (int i = 0; i < righeClassifica.length; i++) {
                        String riga = righeClassifica[i];
                        if (riga.trim().isEmpty()) continue;
                        
                        String[] parti = riga.trim().split(" ");
                        if (parti.length >= 3) {
                            int posizione = Integer.parseInt(parti[0]);
                            String nomeTeam = parti[1];
                            String punteggio = parti[2];
                            
                            // Verifica se questo è il team dell'utente
                            boolean isTeamCorrente = nomeTeam.equals(team.getNomeTeam());
                            if (isTeamCorrente) {
                                posizioneTeamCorrente = posizione;
                            }
                            
                            // Icone per le prime posizioni
                            String icona = "";
                            if (posizione == 1) {
                                icona = "🥇 ";
                            } else if (posizione == 2) {
                                icona = "🥈 ";
                            } else if (posizione == 3) {
                                icona = "🥉 ";
                            } else {
                                icona = "   ";
                            }
                            
                            // Evidenzia il team dell'utente
                            String prefisso = isTeamCorrente ? "➤ " : "  ";
                            String suffisso = isTeamCorrente ? " ⬅ IL TUO TEAM" : "";
                            
                            TeamTextArea.append(String.format("%s%s%d°. %-25s Punteggio: %s%s\n", 
                                prefisso, icona, posizione, nomeTeam, punteggio, suffisso));
                        }
                    }
                    
                    TeamTextArea.append("\n" + "═".repeat(70) + "\n");
                    
                    // Messaggio personalizzato in base alla posizione
                    if (posizioneTeamCorrente > 0) {
                        if (posizioneTeamCorrente == 1) {
                            TeamTextArea.append("🎉 CONGRATULAZIONI! Il vostro team è al PRIMO POSTO! 🎉\n");
                        } else if (posizioneTeamCorrente <= 3) {
                            TeamTextArea.append("🏆 Ottimo lavoro! Il vostro team è sul podio!\n");
                        } else {
                            TeamTextArea.append("👏 Buon lavoro! Posizione del vostro team: " + posizioneTeamCorrente + "°\n");
                        }
                    }
                    
                    TeamTextArea.append("📊 Totale team partecipanti: " + righeClassifica.length + "\n");
                }
                
                // Posiziona il cursore all'inizio
                TeamTextArea.setCaretPosition(0);
                
            } catch (Exception ex) {
                TeamTextArea.setText("");
                TeamTextArea.append("❌ ERRORE NELLA VISUALIZZAZIONE DELLA CLASSIFICA\n\n");
                TeamTextArea.append("Dettagli errore: " + ex.getMessage() + "\n\n");
                TeamTextArea.append("Verifica la connessione al database e riprova.");
                ex.printStackTrace(); // Per debug
            }
        } else {
            if (TeamTextArea != null) {
                TeamTextArea.setText("");
                TeamTextArea.append("❌ ERRORE DI INIZIALIZZAZIONE\n\n");
                TeamTextArea.append("Impossibile accedere alle informazioni necessarie.\n");
            }
        }
    }
}

