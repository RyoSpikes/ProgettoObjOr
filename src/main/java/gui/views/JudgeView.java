package gui.views;

import controller.HackathonController;
import gui.dialogs.*;
import model.Utente;
import model.Documento;
import model.Team;
import utilities.ErrorMessageTranslator;

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
        
        // Inizializza i DAO
        if (this.hackathonController == null) {
            throw new IllegalStateException("HackathonController non può essere null");
        }
        
        judgeViewFrame = new JFrame("Hackathon Management - Area Giudice");
        
        // Crea l'interfaccia moderna invece di usare solo il form
        createModernInterface();
        
        judgeViewFrame.pack();
        judgeViewFrame.setSize(700, 550); // Leggermente più alta per header
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
        
        judgeViewFrame.setVisible(true);
    }
    
    /**
     * Crea l'interfaccia moderna con header stilizzato.
     */
    private void createModernInterface() {
        // Panel principale con gradiente
        JPanel modernMainPanel = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(245, 250, 255); // Light Alice Blue
                Color color2 = new Color(230, 245, 255); // Lighter Steel Blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        modernMainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header con titolo moderno
        JPanel headerPanel = createModernHeader();
        
        // Panel centrale con i pulsanti e area di testo
        JPanel contentPanel = createJudgeContentPanel();
        
        modernMainPanel.add(headerPanel, BorderLayout.NORTH);
        modernMainPanel.add(contentPanel, BorderLayout.CENTER);
        
        judgeViewFrame.setContentPane(modernMainPanel);
        
        // Applica stile moderno ai pulsanti
        setupModernButtonStyles();
    }

    /**
     * Crea l'header moderno con titolo e informazioni hackathon.
     */
    private JPanel createModernHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Titolo principale
        JLabel titleLabel = new JLabel("Pannello Giudice", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112)); // Midnight Blue
        
        // Sottotitolo con hackathon e giudice
        JLabel subtitleLabel = new JLabel("Hackathon: " + titoloHackathon + " | Giudice: " + giudice.getName(), SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(70, 130, 180)); // Steel Blue
        
        // Panel per centrare i titoli
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        // Linea separatrice
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        headerPanel.add(separator, BorderLayout.SOUTH);
        
        return headerPanel;
    }

    /**
     * Crea il panel centrale con i pulsanti e area informazioni per il giudice.
     */
    private JPanel createJudgeContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        
        // Panel per i pulsanti del giudice
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Crea i pulsanti se non esistono (compatibilità con form)
        if (mostraTeamButton == null) mostraTeamButton = new JButton("Mostra Team");
        if (mostraDocumentiButton == null) mostraDocumentiButton = new JButton("Mostra Documenti");
        if (mostraClassificaButton == null) mostraClassificaButton = new JButton("Mostra Classifica");
        if (assegnaVotoFinaleButton == null) assegnaVotoFinaleButton = new JButton("Assegna Voto Finale");
        
        // Configura i listener dei pulsanti
        mostraTeamButton.addActionListener(e -> mostraTeamHackathon());
        mostraDocumentiButton.addActionListener(e -> mostraDocumentiHackathon());
        mostraClassificaButton.addActionListener(e -> mostraClassificaHackathon());
        assegnaVotoFinaleButton.addActionListener(e -> aggiungiVotoFinale());
        
        // Aggiungi i pulsanti al panel
        buttonsPanel.add(mostraTeamButton);
        buttonsPanel.add(mostraDocumentiButton);
        buttonsPanel.add(mostraClassificaButton);
        buttonsPanel.add(assegnaVotoFinaleButton);
        
        contentPanel.add(buttonsPanel, BorderLayout.NORTH);
        
        // Area di testo informativa per il giudice
        if (MENUGIUDICETextArea == null) {
            MENUGIUDICETextArea = new JTextArea(8, 50);
        }
        MENUGIUDICETextArea.setEditable(false);
        MENUGIUDICETextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        MENUGIUDICETextArea.setOpaque(false);
        MENUGIUDICETextArea.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)), 
            "Informazioni Hackathon",
            0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(70, 130, 180)));
        
        JScrollPane scrollPane = new JScrollPane(MENUGIUDICETextArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 40));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
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
            MENUGIUDICETextArea.append("INFORMAZIONI GENERALI\n\n");
            MENUGIUDICETextArea.append("Hackathon: " + titoloHackathon + "\n");
            MENUGIUDICETextArea.append("Giudice: " + giudice.getName() + "\n\n");
            MENUGIUDICETextArea.append("FUNZIONI DISPONIBILI:\n");
            MENUGIUDICETextArea.append("• Visualizza i team partecipanti\n");
            MENUGIUDICETextArea.append("• Consulta i documenti caricati\n");
            MENUGIUDICETextArea.append("• Mostra la classifica attuale\n");
            MENUGIUDICETextArea.append("• Assegna voti finali ai team\n\n");
            MENUGIUDICETextArea.append("Seleziona un'opzione dal menu per iniziare la valutazione.");

            // Posiziona il cursore all'inizio
            MENUGIUDICETextArea.setCaretPosition(0);
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
            System.out.println("DEBUG: Inizio aggiungiVotoFinale per hackathon: " + titoloHackathon);
            
            // Verifica che l'hackathon sia terminato
            if (hackathonController.isHackathonTerminato(titoloHackathon)) {
                System.out.println("DEBUG: Hackathon terminato, procedo con assegnazione voto");

                // Ottieni tutti i team per questo hackathon
                List<String> teamNames = hackathonController.getNomiTeamHackathon(titoloHackathon);
                System.out.println("DEBUG: Trovati " + (teamNames != null ? teamNames.size() : 0) + " team");

                if (teamNames == null || teamNames.isEmpty()) {
                    MessageDialog.showInfoMessage(judgeViewFrame,
                        "Non ci sono team registrati per questo hackathon.\n\n" +
                        "Attendi che dei team si registrino all'evento.");
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

                System.out.println("DEBUG: Team selezionato: " + teamSelezionato);

                if (teamSelezionato != null) {
                    // Verifica se il giudice ha già votato questo team
                    boolean haGiaVotato = hackathonController.hasGiudiceVotatoTeam(giudice.getName(), titoloHackathon, teamSelezionato);
                    System.out.println("DEBUG: Il giudice ha già votato il team? " + haGiaVotato);
                    
                    if (haGiaVotato) {
                        MessageDialog.showWarningMessage(judgeViewFrame,
                            "Hai già votato il team '" + teamSelezionato + "'.\n\n" +
                            "Ogni giudice può votare un team una sola volta.\n" +
                            "Non è possibile modificare il voto.\n\n" +
                            "Se c'è un errore, contatta l'amministratore.");
                        return;
                    }

                    // Ottieni l'ultimo documento del team
                    List<Documento> documenti = hackathonController.getDocumentiByTeam(teamSelezionato, titoloHackathon);
                    System.out.println("DEBUG: Trovati " + (documenti != null ? documenti.size() : 0) + " documenti per il team");

                    if (documenti == null || documenti.isEmpty()) {
                        System.out.println("DEBUG: Nessun documento trovato, mostro warning");
                        MessageDialog.showWarningMessage(judgeViewFrame,
                            "Il team '" + teamSelezionato + "' non ha caricato documenti.\n\n" +
                            "Non è possibile votare senza documenti da valutare.\n\n" +
                            "Il team deve caricare almeno un documento.");
                        return;
                    }

                    // Ottieni l'ultimo documento (il più recente)
                    Documento ultimoDocumento = documenti.get(documenti.size() - 1);
                    System.out.println("DEBUG: Ultimo documento: " + ultimoDocumento.getTitle());

                    // Mostra dialog moderno per inserire il voto
                    System.out.println("DEBUG: Apro dialog per inserire voto");
                    int voto = VotoFinaleDialog.showVotoDialog(
                        judgeViewFrame, 
                        teamSelezionato, 
                        ultimoDocumento.getTitle()
                    );
                    System.out.println("DEBUG: Voto inserito: " + voto);

                    if (voto != -1) { // -1 significa annullato
                        System.out.println("DEBUG: Procedo con salvataggio voto");
                        try {
                            // Salva il voto nel database usando il controller
                            boolean success = hackathonController.assegnaVotoFinale(titoloHackathon, teamSelezionato, voto, giudice);
                            System.out.println("DEBUG: Risultato salvataggio: " + success);

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
                                    "Il voto non è stato salvato correttamente.\n\n" +
                                    "Riprova più tardi o contatta l'amministratore.");
                            }
                        } catch (RuntimeException dbEx) {
                            // Gestisce le eccezioni dal database con messaggio già tradotto
                            System.out.println("DEBUG: Errore database: " + dbEx.getMessage());
                            MessageDialog.showErrorMessage(judgeViewFrame,
                                "Errore durante l'assegnazione del voto:\n\n" +
                                dbEx.getMessage());
                        }
                    }
                }

            } else {
                // Hackathon non terminato
                System.out.println("DEBUG: Hackathon non terminato, mostro warning");
                MessageDialog.showWarningMessage(judgeViewFrame,
                    "L'hackathon '" + titoloHackathon + "' è ancora in corso.\n\n" +
                    "I voti finali possono essere assegnati solo\n" +
                    "dopo la conclusione dell'evento.\n\n" +
                    "Attendi che l'hackathon termini.");
            }

        } catch (Exception ex) {
            System.out.println("DEBUG: Eccezione catturata: " + ex.getMessage());
            ex.printStackTrace();
            String userFriendlyMessage = ErrorMessageTranslator.translateError(ex.getMessage());
            MessageDialog.showErrorMessage(judgeViewFrame,
                "Errore durante l'operazione:\n\n" + userFriendlyMessage);
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

}

