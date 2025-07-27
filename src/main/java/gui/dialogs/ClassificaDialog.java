package gui.dialogs;

import controller.HackathonController;
import gui.components.ModernButton;
import model.Team;

import javax.swing.*;
import java.awt.*;

/**
 * Dialogo moderno per visualizzare la classifica dell'hackathon.
 */
public class ClassificaDialog extends JDialog {
    private Team team;
    private HackathonController hackathonController;
    private JTextArea classificaArea;
    
    public ClassificaDialog(JFrame parent, Team team, HackathonController hackathonController) {
        super(parent, "Classifica Hackathon", true);
        this.team = team;
        this.hackathonController = hackathonController;
        
        initializeComponents();
        setupLayout();
        loadClassifica();
        
        setSize(650, 550);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Panel principale con gradiente dorato
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(255, 248, 220);
                Color color2 = new Color(250, 240, 200);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Content panel
        JPanel contentPanel = createContentPanel();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        String hackathonTitle = team != null && team.getHackathon() != null ? 
            team.getHackathon().getTitoloIdentificativo() : "Hackathon";
            
        JLabel titleLabel = new JLabel("CLASSIFICA - " + hackathonTitle, JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 3),
            "Risultati",
            0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(184, 134, 11)
        ));
        
        // Area per la classifica
        classificaArea = new JTextArea();
        classificaArea.setEditable(false);
        classificaArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        classificaArea.setBackground(Color.WHITE);
        classificaArea.setForeground(new Color(25, 25, 25));
        classificaArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Loading iniziale
        classificaArea.setText("Caricamento classifica in corso...\n\nPer favore attendi...");
        
        JScrollPane scrollPane = new JScrollPane(classificaArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        
        ModernButton refreshButton = ModernButton.createPrimaryButton("Aggiorna");
        refreshButton.addActionListener(e -> loadClassifica());
        
        ModernButton closeButton = ModernButton.createDangerButton("Chiudi");
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        return buttonPanel;
    }
    
    private void loadClassifica() {
        if (hackathonController == null || team == null || team.getHackathon() == null) {
            classificaArea.setText("Errore: Impossibile accedere alle informazioni necessarie per la classifica.");
            return;
        }
        
        classificaArea.setText("Caricamento classifica in corso...\n\nAttendi un momento...");
        
        // Esegue il caricamento in background per non bloccare l'UI
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return hackathonController.generaClassificaHackathon(team.getHackathon().getTitoloIdentificativo());
            }
            
            @Override
            protected void done() {
                try {
                    String risultatoClassifica = get();
                    displayClassifica(risultatoClassifica);
                } catch (Exception ex) {
                    classificaArea.setText("ERRORE NEL CARICAMENTO DELLA CLASSIFICA\n\n" +
                                         "Dettagli errore: " + ex.getMessage() + "\n\n" +
                                         "Verifica la connessione al database e riprova.");
                }
            }
        };
        
        worker.execute();
    }
    
    private void displayClassifica(String risultatoClassifica) {
        StringBuilder sb = new StringBuilder();
        
        String hackathonTitle = team.getHackathon().getTitoloIdentificativo();
        sb.append("CLASSIFICA HACKATHON: ").append(hackathonTitle).append("\n");
        sb.append("═".repeat(65)).append("\n\n");
        
        // Verifica se il risultato è un errore
        if (risultatoClassifica.startsWith("Errore:")) {
            sb.append("ATTENZIONE: ").append(risultatoClassifica).append("\n\n");
            
            if (risultatoClassifica.contains("prima della fine dell'hackathon")) {
                sb.append("INFO: La classifica sarà disponibile al termine dell'hackathon.\n");
                sb.append("Data fine evento: ").append(team.getHackathon().getDataFine()).append("\n");
            } else if (risultatoClassifica.contains("Mancano") && risultatoClassifica.contains("voti")) {
                sb.append("INFO: I giudici stanno ancora valutando i team.\n");
                sb.append("La classifica sarà disponibile quando tutti i voti saranno stati espressi.\n");
            }
        } else {
            // Classifica generata con successo
            sb.append("Ecco la classifica finale!\n\n");
            
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
                    
                    boolean isTeamCorrente = nomeTeam.equals(team.getNomeTeam());
                    if (isTeamCorrente) {
                        posizioneTeamCorrente = posizione;
                    }
                    
                    String icona = "";
                    if (posizione == 1) {
                        icona = "[1°] ";
                    } else if (posizione == 2) {
                        icona = "[2°] ";
                    } else if (posizione == 3) {
                        icona = "[3°] ";
                    } else {
                        icona = "     ";
                    }
                    
                    String prefisso = isTeamCorrente ? "→ " : "  ";
                    String suffisso = isTeamCorrente ? " ← IL TUO TEAM" : "";
                    
                    sb.append(String.format("%s%s%d°. %-22s Punteggio: %s%s\n", 
                        prefisso, icona, posizione, nomeTeam, punteggio, suffisso));
                }
            }
            
            sb.append("\n").append("═".repeat(65)).append("\n");
            
            // Messaggio personalizzato in base alla posizione
            if (posizioneTeamCorrente > 0) {
                if (posizioneTeamCorrente == 1) {
                    sb.append("CONGRATULAZIONI! Il vostro team è al PRIMO POSTO!\n");
                } else if (posizioneTeamCorrente <= 3) {
                    sb.append("Ottimo lavoro! Il vostro team è sul podio!\n");
                } else {
                    sb.append("Buon lavoro! Posizione del vostro team: ").append(posizioneTeamCorrente).append("°\n");
                }
            }
            
            sb.append("Totale team partecipanti: ").append(righeClassifica.length).append("\n");
        }
        
        classificaArea.setText(sb.toString());
        classificaArea.setCaretPosition(0);
    }
    
    private void setupLayout() {
        // Setup ESC per chiudere
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
            }
        });
    }
}
