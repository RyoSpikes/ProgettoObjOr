package gui;

import controller.HackathonController;
import controller.TeamController;
import model.Utente;
import model.Documento;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * La classe JudgeView rappresenta l'interfaccia grafica per la gestione delle funzionalità da giudice.
 * Permette di visualizzare team e documenti per un hackathon specifico.
 */
public class JudgeView {
    private JButton mostraTeamButton;
    private JButton mostraDocumentiButton;
    private JTextArea MENUGIUDICETextArea;
    private JPanel mainPanel;
    private JButton mostraClassificaButton;
    private JButton assegnaVotoFinaleButton;

    private JFrame judgeViewFrame;
    private String titoloHackathon;
    private Utente giudice;
    
    // Controller per gestire la logica di business
    private HackathonController hackathonController;
    private TeamController teamController;

    /**
     * Costruttore della classe JudgeView.
     * 
     * @param titoloHackathon Il titolo dell'hackathon da giudicare
     * @param giudice L'utente che funge da giudice
     * @param parentFrame Il frame genitore per la gestione delle finestre
     */
    public JudgeView(String titoloHackathon, Utente giudice, JFrame parentFrame) {
        this.titoloHackathon = titoloHackathon;
        this.giudice = giudice;
        
        // Inizializza l'interfaccia utente dal form
        $$$setupUI$$$();
        
        // Inizializza i DAO
        try {
            this.hackathonController = new HackathonController();
            this.teamController = new TeamController();
        } catch (Exception e) {
            System.err.println("Errore nell'inizializzazione dei DAO: " + e.getMessage());
        }
        
        judgeViewFrame = new JFrame("Giudice - " + titoloHackathon);
        judgeViewFrame.setContentPane(mainPanel);
        judgeViewFrame.pack();
        judgeViewFrame.setSize(700, 500);
        judgeViewFrame.setLocationRelativeTo(null);
        
        // Listener per gestire la chiusura della finestra
        judgeViewFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                judgeViewFrame.dispose();
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
            }
        });
        
        // Carica le informazioni iniziali
        caricaInformazioniIniziali();
        
        // Configura i listener dei pulsanti
        setupListeners();
        
        judgeViewFrame.setVisible(true);
    }
    
    /**
     * Carica le informazioni iniziali nell'area di testo.
     */
    private void caricaInformazioniIniziali() {
        if (MENUGIUDICETextArea != null) {
            MENUGIUDICETextArea.setText("");
            MENUGIUDICETextArea.append("=== VISTA GIUDICE ===\n\n");
            MENUGIUDICETextArea.append("Hackathon: " + titoloHackathon + "\n");
            MENUGIUDICETextArea.append("Giudice: " + giudice.getName() + "\n\n");
            MENUGIUDICETextArea.append("📋 Istruzioni:\n");
            MENUGIUDICETextArea.append("• Usa 'Visualizza Team' per aprire la vista di un team\n");
            MENUGIUDICETextArea.append("• Usa 'Mostra Documenti' per vedere i documenti caricati\n\n");
            MENUGIUDICETextArea.append("Seleziona un'opzione per iniziare...\n");

            // Posiziona il cursore all'inizio
            MENUGIUDICETextArea.setCaretPosition(0);
        }
    }

    /**
     * Configura i listener dei pulsanti.
     */
    private void setupListeners() {
        // Listener per il pulsante "Mostra Team"
        if (mostraTeamButton != null) {
            mostraTeamButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mostraTeamHackathon();
                }
            });
        }

        // Listener per il pulsante "Mostra Documenti"
        if (mostraDocumentiButton != null) {
            mostraDocumentiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mostraDocumentiHackathon();
                }
            });
        }

        // Listener per il pulsante "Mostra Classifica"
        if (mostraClassificaButton != null) {
            mostraClassificaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mostraClassificaHackathon();
                }
            });
        }

        // Listener per il pulsante "Aggiungi Voto Finale"
        if (assegnaVotoFinaleButton != null) {
            assegnaVotoFinaleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    aggiungiVotoFinale();
                }
            });
        }
    }

    /**
     * Mostra tutti i team dell'hackathon nella textarea.
     */
    private void mostraTeamHackathon() {
        if (MENUGIUDICETextArea != null && hackathonController != null) {
            try {
                // Ottieni tutti i team per questo hackathon
                List<String> teamNames = hackathonController.getNomiTeamHackathon(titoloHackathon);

                if (teamNames != null && !teamNames.isEmpty()) {
                    MENUGIUDICETextArea.setText("");
                    MENUGIUDICETextArea.append("🏆 TEAM DELL'HACKATHON: " + titoloHackathon + "\n");
                    MENUGIUDICETextArea.append("═".repeat(60) + "\n\n");

                    int teamNumber = 1;
                    for (String teamName : teamNames) {
                        MENUGIUDICETextArea.append("🔸 Team " + teamNumber + ": " + teamName + "\n");

                        // Ottieni i membri del team
                        List<Utente> membri = teamController.getMembershipDAO().getTeamMembers(teamName, titoloHackathon);
                        if (membri != null && !membri.isEmpty()) {
                            MENUGIUDICETextArea.append("   👥 Membri:\n");
                            for (Utente membro : membri) {
                                MENUGIUDICETextArea.append("      • " + membro.getName() + "\n");
                            }
                        } else {
                            MENUGIUDICETextArea.append("   ❌ Nessun membro trovato\n");
                        }
                        MENUGIUDICETextArea.append("\n");
                        teamNumber++;
                    }

                    MENUGIUDICETextArea.append("═".repeat(60) + "\n");
                    MENUGIUDICETextArea.append("📊 Totale team: " + teamNames.size() + "\n");
                    MENUGIUDICETextArea.setCaretPosition(0);
                } else {
                    // Mostra messaggio nell'area di testo se non ci sono team
                    MENUGIUDICETextArea.setText("");
                    MENUGIUDICETextArea.append("❌ Nessun team trovato per questo hackathon.\n");
                    MENUGIUDICETextArea.append("\nI team potrebbero non essersi ancora iscritti\n");
                    MENUGIUDICETextArea.append("o potrebbero esserci problemi di connessione al database.\n");
                    MENUGIUDICETextArea.setCaretPosition(0);
                }

            } catch (Exception ex) {
                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("❌ ERRORE NEL CARICAMENTO DEI TEAM\n\n");
                MENUGIUDICETextArea.append("Dettagli errore: " + ex.getMessage() + "\n");
                MENUGIUDICETextArea.append("\nVerifica la connessione al database e riprova.");
            }
        } else {
            if (MENUGIUDICETextArea != null) {
                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("❌ ERRORE DI INIZIALIZZAZIONE\n\n");
                MENUGIUDICETextArea.append("Il sistema non è stato inizializzato correttamente.\n");
            }
        }
    }

    /**
     * Mostra una finestra per selezionare e valutare un documento.
     */
    private void mostraDocumentiHackathon() {
        try {
            // Crea un controller per gestire i documenti
            controller.HackathonController hackathonController = new controller.HackathonController();
            
            // Verifica se ci sono documenti per questo hackathon
            List<Documento> documenti = hackathonController.getDocumentiHackathon(titoloHackathon);

            if (documenti == null || documenti.isEmpty()) {
                JOptionPane.showMessageDialog(judgeViewFrame,
                    "Nessun documento trovato per questo hackathon.\n" +
                    "I team potrebbero non aver ancora caricato documenti.",
                    "Nessun Documento",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Apri il dialog di ricerca documento con ricerca dinamica
            new CercaDocumentoDialog(judgeViewFrame, titoloHackathon, giudice, hackathonController, 
                new CercaDocumentoDialog.DocumentoSelectionCallback() {
                @Override
                public boolean onDocumentoSelected(Documento docDaValutare) {
                    try {
                        // Controlla se il giudice ha già valutato questo documento tramite il controller
                        boolean giaValutato = hackathonController.hasGiudiceValutatoDocumento(
                            giudice.getName(), 
                            docDaValutare.getIdDocumento()
                        );
                        
                        if (giaValutato) {
                            JOptionPane.showMessageDialog(judgeViewFrame, 
                                "Hai già valutato questo documento.\nSeleziona un altro documento per continuare.", 
                                "Documento già valutato", 
                                JOptionPane.INFORMATION_MESSAGE);
                            return false; // Non chiude il dialog, permette di selezionare altri documenti
                        }
                        
                        // Crea il giudice dal modello esistente
                        model.Giudice giudiceModel = new model.Giudice(
                            giudice.getName(),
                            giudice.getPassword(),
                            docDaValutare.getSource().getHackathon()
                        );

                        // Apri dialog di valutazione
                        ValutazioneDialog valutazioneDialog = new ValutazioneDialog(
                            judgeViewFrame,
                            docDaValutare,
                            giudiceModel
                        );
                        valutazioneDialog.setVisible(true);
                        
                        // Il dialog di valutazione è modale, quindi quando si chiude, 
                        // assumiamo che l'operazione sia completata (successo o annullamento)
                        // Per ora manteniamo aperto il dialog di ricerca per permettere altre valutazioni
                        return false; // Non chiude il dialog di ricerca
                        
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(judgeViewFrame,
                            "Errore durante il controllo della valutazione:\n" + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                        return false; // Non chiude il dialog in caso di errore
                    }
                }
            });

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(judgeViewFrame,
                "Errore durante il caricamento dei documenti:\n" + ex.getMessage(),
                "Errore Database",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Mostra la classifica dell'hackathon utilizzando la funzione del database.
     */
    private void mostraClassificaHackathon() {
        if (MENUGIUDICETextArea != null && hackathonController != null) {
            try {
                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("🏆 GENERAZIONE CLASSIFICA HACKATHON: " + titoloHackathon + "\n");
                MENUGIUDICETextArea.append("═".repeat(70) + "\n\n");
                MENUGIUDICETextArea.append("⏳ Elaborazione in corso...\n\n");

                // Chiama la funzione del database per generare la classifica
                String risultatoClassifica = hackathonController.generaClassificaHackathon(titoloHackathon);

                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("🏆 CLASSIFICA HACKATHON: " + titoloHackathon + "\n");
                MENUGIUDICETextArea.append("═".repeat(70) + "\n\n");

                // Verifica se il risultato è un errore
                if (risultatoClassifica.startsWith("Errore:")) {
                    MENUGIUDICETextArea.append("❌ " + risultatoClassifica + "\n\n");

                    // Aggiungi suggerimenti basati sul tipo di errore
                    if (risultatoClassifica.contains("prima della fine dell'hackathon")) {
                        MENUGIUDICETextArea.append("💡 Suggerimento: La classifica può essere generata solo dopo la fine dell'hackathon.\n");
                    } else if (risultatoClassifica.contains("Mancano") && risultatoClassifica.contains("voti")) {
                        MENUGIUDICETextArea.append("💡 Suggerimento: Tutti i giudici devono votare tutti i team prima di generare la classifica.\n");
                        MENUGIUDICETextArea.append("📋 Verifica che ogni giudice abbia espresso il proprio voto per ogni team partecipante.\n");
                    } else if (risultatoClassifica.contains("non trovato")) {
                        MENUGIUDICETextArea.append("💡 Suggerimento: Verifica che il nome dell'hackathon sia corretto.\n");
                    }

                } else {
                    // Classifica generata con successo - formatta e mostra
                    MENUGIUDICETextArea.append("✅ Classifica generata con successo!\n\n");

                    String[] righeClassifica = risultatoClassifica.split("\n");

                    for (String riga : righeClassifica) {
                        if (riga.trim().isEmpty()) continue;

                        String[] parti = riga.trim().split(" ");
                        if (parti.length >= 3) {
                            int posizione = Integer.parseInt(parti[0]);
                            String nomeTeam = parti[1];
                            String punteggio = parti[2];

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

                            MENUGIUDICETextArea.append(String.format("%s%d°. %-25s Punteggio: %s\n",
                                icona, posizione, nomeTeam, punteggio));
                        }
                    }

                    MENUGIUDICETextArea.append("\n" + "═".repeat(70) + "\n");
                    MENUGIUDICETextArea.append("📊 Classifica aggiornata nel database\n");
                    MENUGIUDICETextArea.append("🎯 Totale team classificati: " + righeClassifica.length + "\n");
                }

                // Posiziona il cursore all'inizio
                MENUGIUDICETextArea.setCaretPosition(0);

            } catch (Exception ex) {
                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("❌ ERRORE NELLA GENERAZIONE DELLA CLASSIFICA\n\n");
                MENUGIUDICETextArea.append("Dettagli errore: " + ex.getMessage() + "\n\n");
                MENUGIUDICETextArea.append("Possibili cause:\n");
                MENUGIUDICETextArea.append("• Problemi di connessione al database\n");
                MENUGIUDICETextArea.append("• L'hackathon non esiste\n");
                MENUGIUDICETextArea.append("• Errore nella funzione del database\n");
                MENUGIUDICETextArea.append("\nVerifica la connessione e riprova.");
                ex.printStackTrace(); // Per debug
            }
        } else {
            if (MENUGIUDICETextArea != null) {
                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("❌ ERRORE DI INIZIALIZZAZIONE\n\n");
                MENUGIUDICETextArea.append("Il sistema non è stato inizializzato correttamente.\n");
            }
        }
    }

    /**
     * Permette al giudice di selezionare un team e assegnare un voto finale.
     * Il voto può essere assegnato solo se l'hackathon è terminato.
     */
    private void aggiungiVotoFinale() {
        try {
            // Verifica che l'hackathon sia terminato
            if (hackathonController.isHackathonTerminato(titoloHackathon)) {

                // Ottieni tutti i team per questo hackathon
                List<String> teamNames = hackathonController.getNomiTeamHackathon(titoloHackathon);

                if (teamNames == null || teamNames.isEmpty()) {
                    JOptionPane.showMessageDialog(judgeViewFrame,
                        "Non ci sono team registrati per questo hackathon.",
                        "Nessun team",
                        JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Crea un array per la selezione
                String[] teamArray = teamNames.toArray(new String[0]);

                // Mostra dialog per selezionare il team
                String teamSelezionato = (String) JOptionPane.showInputDialog(
                    judgeViewFrame,
                    "Seleziona il team da votare:",
                    "Aggiungi Voto Finale",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    teamArray,
                    teamArray[0]
                );

                if (teamSelezionato != null) {
                    // Verifica se il giudice ha già votato questo team
                    if (hackathonController.hasGiudiceVotatoTeam(giudice.getName(), titoloHackathon, teamSelezionato)) {
                        JOptionPane.showMessageDialog(judgeViewFrame,
                            "Hai già votato il team '" + teamSelezionato + "'.\n" +
                            "Non è possibile modificare il voto.",
                            "Voto già assegnato",
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Ottieni l'ultimo documento del team
                    List<Documento> documenti = hackathonController.getDocumentiByTeam(teamSelezionato, titoloHackathon);

                    if (documenti == null || documenti.isEmpty()) {
                        JOptionPane.showMessageDialog(judgeViewFrame,
                            "Il team '" + teamSelezionato + "' non ha caricato alcun documento.\n" +
                            "Non è possibile assegnare un voto.",
                            "Nessun documento",
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Ottieni l'ultimo documento (il più recente)
                    Documento ultimoDocumento = documenti.get(documenti.size() - 1);

                    // Mostra dialog per inserire il voto
                    String votoString = JOptionPane.showInputDialog(
                        judgeViewFrame,
                        "Ultimo documento del team '" + teamSelezionato + "':\n" +
                        "Titolo: " + ultimoDocumento.getTitle() + "\n\n" +
                        "Inserisci il voto finale (1-10):",
                        "Voto Finale per " + teamSelezionato,
                        JOptionPane.QUESTION_MESSAGE
                    );

                    if (votoString != null && !votoString.trim().isEmpty()) {
                        try {
                            int voto = Integer.parseInt(votoString.trim());

                            if (voto < 1 || voto > 10) {
                                JOptionPane.showMessageDialog(judgeViewFrame,
                                    "Il voto deve essere compreso tra 1 e 10.",
                                    "Voto non valido",
                                    JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            // Salva il voto nel database usando il controller
                            boolean success = hackathonController.assegnaVotoFinale(titoloHackathon, teamSelezionato, voto, giudice);

                            if (success) {
                                JOptionPane.showMessageDialog(judgeViewFrame,
                                    "Voto assegnato con successo!\n" +
                                    "Team: " + teamSelezionato + "\n" +
                                    "Voto: " + voto + "/10",
                                    "Voto salvato",
                                    JOptionPane.INFORMATION_MESSAGE);

                                // Aggiorna la vista mostrando un messaggio nella text area
                                if (MENUGIUDICETextArea != null) {
                                    MENUGIUDICETextArea.append("\n" + "═".repeat(50) + "\n");
                                    MENUGIUDICETextArea.append("✅ VOTO ASSEGNATO\n");
                                    MENUGIUDICETextArea.append("Team: " + teamSelezionato + "\n");
                                    MENUGIUDICETextArea.append("Voto: " + voto + "/10\n");
                                    MENUGIUDICETextArea.append("Documento valutato: " + ultimoDocumento.getTitle() + "\n");
                                    MENUGIUDICETextArea.append("═".repeat(50) + "\n");
                                }
                            } else {
                                JOptionPane.showMessageDialog(judgeViewFrame,
                                    "Errore durante il salvataggio del voto.\n" +
                                    "Riprova più tardi.",
                                    "Errore",
                                    JOptionPane.ERROR_MESSAGE);
                            }

                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(judgeViewFrame,
                                "Inserisci un numero valido (1-10).",
                                "Formato non valido",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

            } else {
                // Hackathon non terminato
                JOptionPane.showMessageDialog(judgeViewFrame,
                    "Non è possibile assegnare voti finali.\n" +
                    "L'hackathon '" + titoloHackathon + "' non è ancora terminato.\n\n" +
                    "I voti finali possono essere assegnati solo dopo la fine dell'evento.",
                    "Hackathon in corso",
                    JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(judgeViewFrame,
                "Errore durante l'operazione: " + ex.getMessage(),
                "Errore",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Metodo generato automaticamente da IntelliJ IDEA per inizializzare i componenti del form.
     * Questo metodo viene generato dal file JudgeView.form.
     */
    private void $$$setupUI$$$() {
        // Questo metodo sarà generato automaticamente da IntelliJ IDEA
        // dal file JudgeView.form quando il progetto viene compilato
    }
}

