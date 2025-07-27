package gui.views;

import controller.HackathonController;
import gui.dialogs.*;
import model.Utente;
import model.Documento;
import model.Team;

import javax.swing.*;
import java.awt.*;
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

    /**
     * Costruttore della classe JudgeView.
     * 
     * @param titoloHackathon Il titolo dell'hackathon da giudicare
     * @param giudice L'utente che funge da giudice
     * @param parentFrame Il frame genitore per la gestione delle finestre
     * @param hackathonController Il controller per le operazioni business
     */
    public JudgeView(String titoloHackathon, Utente giudice, JFrame parentFrame, HackathonController hackathonController) {
        this.titoloHackathon = titoloHackathon;
        this.giudice = giudice;
        this.hackathonController = hackathonController;
        
        // Inizializza l'interfaccia utente dal form
        $$$setupUI$$$();
        
        // Applica stile moderno ai pulsanti
        setupModernButtonStyles();
        
        // Inizializza i DAO
        if (this.hackathonController == null) {
            throw new IllegalStateException("HackathonController non può essere null");
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
     * Carica le informazioni iniziali nell'area di testo con stile moderno.
     */
    private void caricaInformazioniIniziali() {
        if (MENUGIUDICETextArea != null) {
            // Configura il font e i colori per un aspetto più moderno
            MENUGIUDICETextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            MENUGIUDICETextArea.setBackground(new java.awt.Color(248, 249, 250));
            MENUGIUDICETextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            
            MENUGIUDICETextArea.setText("");
            MENUGIUDICETextArea.append("PANNELLO GIUDICE\n\n");
            MENUGIUDICETextArea.append("Hackathon: " + titoloHackathon + "\n");
            MENUGIUDICETextArea.append("Giudice: " + giudice.getName() + "\n\n");
            
            // Sezione istruzioni con stile più chiaro
            MENUGIUDICETextArea.append("==================================\n");
            MENUGIUDICETextArea.append("OPERAZIONI DISPONIBILI\n");
            MENUGIUDICETextArea.append("==================================\n\n");
            
            MENUGIUDICETextArea.append("TEAM PARTECIPANTI\n");
            MENUGIUDICETextArea.append("   - Visualizza tutti i team dell'hackathon\n");
            MENUGIUDICETextArea.append("   - Ricerca team con filtro in tempo reale\n");
            MENUGIUDICETextArea.append("   - Visualizza dettagli e membri dei team\n\n");
            
            MENUGIUDICETextArea.append("DOCUMENTI\n");
            MENUGIUDICETextArea.append("   - Consulta i documenti caricati dai team\n");
            MENUGIUDICETextArea.append("   - Lascia valutazioni sui documenti\n\n");
            
            MENUGIUDICETextArea.append("CLASSIFICA\n");
            MENUGIUDICETextArea.append("   - Visualizza la classifica finale\n");
            MENUGIUDICETextArea.append("   - Consulta i punteggi assegnati\n\n");
            
            MENUGIUDICETextArea.append("VOTO FINALE\n");
            MENUGIUDICETextArea.append("   - Assegna il voto finale ai team\n");
            MENUGIUDICETextArea.append("   - Range di voto: 0-10 punti\n\n");
            
            MENUGIUDICETextArea.append("==================================\n");
            MENUGIUDICETextArea.append("Seleziona un'operazione per iniziare...\n");

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
     * Mostra tutti i team dell'hackathon in un dialogo dedicato con ricerca.
     */
    private void mostraTeamHackathon() {
        try {
            System.out.println("Debug: Chiamata mostraTeamHackathon() per hackathon: " + titoloHackathon);
            TeamListDialog.showTeamListDialog(judgeViewFrame, titoloHackathon, hackathonController);
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.showErrorMessage(judgeViewFrame,
                "Errore nell'apertura della finestra team:\n" + e.getMessage());
        }
    }

    /**
     * Mostra una finestra per selezionare e valutare un documento.
     */
    private void mostraDocumentiHackathon() {
        try {
            // Usa il controller già disponibile nella classe
            List<Documento> documenti = hackathonController.getDocumentiHackathon(titoloHackathon);

            if (documenti == null || documenti.isEmpty()) {
                MessageDialog.showInfoMessage(judgeViewFrame,
                    "Nessun documento trovato per questo hackathon.\n" +
                    "I team potrebbero non aver ancora caricato documenti.");
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
                            MessageDialog.showInfoMessage(judgeViewFrame, 
                                "Hai già valutato questo documento.\nSeleziona un altro documento per continuare.");
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
                            giudiceModel,
                            hackathonController
                        );
                        valutazioneDialog.setVisible(true);
                        
                        // Il dialog di valutazione è modale, quindi quando si chiude, 
                        // assumiamo che l'operazione sia completata (successo o annullamento)
                        // Per ora manteniamo aperto il dialog di ricerca per permettere altre valutazioni
                        return false; // Non chiude il dialog di ricerca
                        
                    } catch (Exception ex) {
                        MessageDialog.showErrorMessage(judgeViewFrame,
                            "Errore durante il controllo della valutazione:\n" + ex.getMessage());
                        return false; // Non chiude il dialog in caso di errore
                    }
                }
            });

        } catch (Exception ex) {
            MessageDialog.showErrorMessage(judgeViewFrame,
                "Errore durante il caricamento dei documenti:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Mostra la classifica dell'hackathon utilizzando il dialogo dedicato.
     */
    private void mostraClassificaHackathon() {
        try {
            // Crea un team fittizio per il dialogo (usa il costruttore semplificato)
            Team teamFittizio = new Team("Judge_View_Access");
            new ClassificaDialog(judgeViewFrame, teamFittizio, hackathonController);
        } catch (Exception ex) {
            MessageDialog.showErrorMessage(judgeViewFrame,
                "Errore nell'apertura della classifica:\n" + ex.getMessage());
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
                    MessageDialog.showInfoMessage(judgeViewFrame,
                        "Non ci sono team registrati per questo hackathon.");
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
                        MessageDialog.showWarningMessage(judgeViewFrame,
                            "Hai già votato il team '" + teamSelezionato + "'.\n\n" +
                            "MOTIVO: Ogni giudice può assegnare un solo voto per team.\n" +
                            "Non è possibile modificare o riassegnare il voto una volta confermato.\n\n" +
                            "Se hai commesso un errore, contatta l'amministratore del sistema.");
                        return;
                    }

                    // Ottieni l'ultimo documento del team
                    List<Documento> documenti = hackathonController.getDocumentiByTeam(teamSelezionato, titoloHackathon);

                    if (documenti == null || documenti.isEmpty()) {
                        MessageDialog.showWarningMessage(judgeViewFrame,
                            "Il team '" + teamSelezionato + "' non ha caricato alcun documento.\n\n" +
                            "MOTIVO: Non è possibile assegnare un voto a un team senza documenti.\n" +
                            "Il team deve caricare almeno un documento prima di poter ricevere una valutazione.");
                        return;
                    }

                    // Ottieni l'ultimo documento (il più recente)
                    Documento ultimoDocumento = documenti.get(documenti.size() - 1);

                    // Mostra dialog moderno per inserire il voto
                    int voto = VotoFinaleDialog.showVotoDialog(
                        judgeViewFrame, 
                        teamSelezionato, 
                        ultimoDocumento.getTitle()
                    );

                    if (voto != -1) { // -1 significa annullato
                        // Salva il voto nel database usando il controller
                        boolean success = hackathonController.assegnaVotoFinale(titoloHackathon, teamSelezionato, voto, giudice);

                        if (success) {
                            MessageDialog.showSuccessMessage(judgeViewFrame,
                                "Voto assegnato con successo!\n" +
                                "Team: " + teamSelezionato + "\n" +
                                "Voto: " + voto + "/10");

                            // Aggiorna la vista mostrando un messaggio nella text area
                            if (MENUGIUDICETextArea != null) {
                                MENUGIUDICETextArea.append("\n" + "=".repeat(50) + "\n");
                                MENUGIUDICETextArea.append("VOTO ASSEGNATO\n");
                                MENUGIUDICETextArea.append("Team: " + teamSelezionato + "\n");
                                MENUGIUDICETextArea.append("Voto: " + voto + "/10\n");
                                MENUGIUDICETextArea.append("Documento valutato: " + ultimoDocumento.getTitle() + "\n");
                                MENUGIUDICETextArea.append("=".repeat(50) + "\n");
                            }
                        } else {
                            MessageDialog.showErrorMessage(judgeViewFrame,
                                "Errore durante il salvataggio del voto.\n" +
                                "Riprova più tardi.");
                        }
                    }
                }

            } else {
                // Hackathon non terminato
                MessageDialog.showWarningMessage(judgeViewFrame,
                    "Non è possibile assegnare voti finali.\n" +
                    "L'hackathon '" + titoloHackathon + "' non è ancora terminato.\n\n" +
                    "I voti finali possono essere assegnati solo dopo la fine dell'evento.");
            }

        } catch (Exception ex) {
            MessageDialog.showErrorMessage(judgeViewFrame,
                "Errore durante l'operazione: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Applica stile moderno ai pulsanti per essere coerente con la home.
     */
    private void setupModernButtonStyles() {
        if (mostraTeamButton != null) {
            applyModernStyle(mostraTeamButton, new Color(70, 130, 180)); // Blu
        }
        
        if (mostraDocumentiButton != null) {
            applyModernStyle(mostraDocumentiButton, new Color(76, 175, 80)); // Verde
        }
        
        if (mostraClassificaButton != null) {
            applyModernStyle(mostraClassificaButton, new Color(255, 152, 0)); // Arancione
        }
        
        if (assegnaVotoFinaleButton != null) {
            applyModernStyle(assegnaVotoFinaleButton, new Color(244, 67, 54)); // Rosso
        }
    }
    
    /**
     * Applica lo stile moderno a un singolo pulsante.
     */
    private void applyModernStyle(JButton button, Color backgroundColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Dimensioni consistenti
        button.setPreferredSize(new Dimension(140, 35));
        
        // Effetti hover
        Color hoverColor = backgroundColor.brighter();
        Color pressedColor = backgroundColor.darker();
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(backgroundColor);
            }
            
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                button.setBackground(pressedColor);
            }
            
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }
        });
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

