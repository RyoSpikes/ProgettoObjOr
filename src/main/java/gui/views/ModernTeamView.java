package gui.views;

import controller.HackathonController;
import gui.components.MembriListCellRenderer;
import gui.components.ModernButton;
import gui.dialogs.ClassificaDialog;
import gui.dialogs.ConfirmDialog;
import gui.dialogs.InsertDocumentDialog;
import gui.dialogs.MessageDialog;
import gui.dialogs.ViewDocumentDialog;
import model.Documento;
import model.Team;
import model.Utente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Vista moderna e modulare per la gestione dei team.
 * Interfaccia completamente rinnovata con componenti dinamici e dialoghi dedicati.
 */
public class ModernTeamView {
    private JPanel mainPanel;
    private ModernButton inserisciDocumentoButton;
    private ModernButton mostraClassificaButton;
    private ModernButton abbandonaTeamButton;
    
    // Componenti per la visualizzazione dinamica
    private JList<String> membriList;
    private JTable documentiTable;
    private DefaultTableModel documentiTableModel;
    private JLabel teamInfoLabel;
    private JLabel hackathonInfoLabel;
    private JLabel dataInfoLabel;
    
    private JFrame teamViewFrame;
    private Team team;
    private Utente userLogged;
    private JFrame parentFrame;
    private HackathonController hackathonController;
    
    // Cache per i documenti (per ottimizzare il caricamento)
    private List<Documento> documentiCache;

    public ModernTeamView(Team team, Utente userLogged, JFrame parentFrame, HackathonController hackathonController) {
        this.team = team;
        this.userLogged = userLogged;
        this.parentFrame = parentFrame;
        this.hackathonController = hackathonController;
        
        initializeController();
        createComponents();
        setupLayout();
        loadTeamData();
        setupEventListeners();
        showFrame();
    }
    
    private void initializeController() {
        // Il controller viene sempre passato come parametro - non deve essere istanziato qui
        if (this.hackathonController == null) {
            throw new IllegalStateException("HackathonController non può essere null");
        }
    }
    
    private void createComponents() {
        // Panel principale con gradiente
        mainPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(245, 250, 255);
                Color color2 = new Color(230, 245, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Componenti header
        createHeaderComponents();
        
        // Componenti centrali
        createCentralComponents();
        
        // Pulsanti moderni
        createModernButtons();
    }
    
    private void createHeaderComponents() {
        teamInfoLabel = new JLabel();
        teamInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        teamInfoLabel.setForeground(new Color(25, 25, 112));
        
        hackathonInfoLabel = new JLabel();
        hackathonInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hackathonInfoLabel.setForeground(new Color(105, 105, 105));
        
        dataInfoLabel = new JLabel();
        dataInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dataInfoLabel.setForeground(new Color(105, 105, 105));
    }
    
    private void createCentralComponents() {
        // Lista membri con renderer personalizzato
        membriList = new JList<>();
        membriList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        membriList.setCellRenderer(new MembriListCellRenderer());
        membriList.setBackground(new Color(248, 255, 248));
        
        // Tabella documenti con doppio click per aprire
        String[] colonneDocumenti = {"Titolo", "Anteprima", "Lunghezza", "Azioni"};
        documentiTableModel = new DefaultTableModel(colonneDocumenti, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        documentiTable = new JTable(documentiTableModel);
        documentiTable.setRowHeight(30);
        documentiTable.setShowGrid(true);
        documentiTable.setGridColor(new Color(220, 220, 220));
        documentiTable.setBackground(new Color(255, 250, 240));
        documentiTable.getTableHeader().setBackground(new Color(255, 228, 181));
        documentiTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        documentiTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        // Imposta larghezze colonne
        documentiTable.getColumnModel().getColumn(0).setPreferredWidth(140);
        documentiTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        documentiTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        documentiTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        
        // Aggiungi tooltip informativi
        documentiTable.setToolTipText("Doppio click per aprire un documento");
    }
    
    private void createModernButtons() {
        inserisciDocumentoButton = ModernButton.createPrimaryButton("Nuovo Documento");
        inserisciDocumentoButton.setPreferredSize(new Dimension(160, 40));
        
        mostraClassificaButton = ModernButton.createSpecialButton("Classifica");
        mostraClassificaButton.setPreferredSize(new Dimension(120, 40));
        
        abbandonaTeamButton = ModernButton.createDangerButton("Abbandona Team");
        abbandonaTeamButton.setPreferredSize(new Dimension(140, 40));
    }
    
    private void setupLayout() {
        // Header panel con informazioni team
        JPanel headerPanel = createHeaderPanel();
        
        // Panel centrale con split
        JSplitPane centralSplit = createCentralPanel();
        
        // Panel pulsanti
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centralSplit, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Pannello info principale
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setOpaque(false);
        infoPanel.add(teamInfoLabel);
        infoPanel.add(hackathonInfoLabel);
        infoPanel.add(dataInfoLabel);
        
        headerPanel.add(infoPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JSplitPane createCentralPanel() {
        JSplitPane centralSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        centralSplit.setDividerLocation(320);
        centralSplit.setResizeWeight(0.4);
        centralSplit.setOneTouchExpandable(true);
        
        // Panel membri
        JPanel membriPanel = new JPanel(new BorderLayout());
        membriPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
            "Membri del Team",
            0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(34, 139, 34)
        ));
        membriPanel.setBackground(new Color(240, 255, 240));
        
        JScrollPane membriScroll = new JScrollPane(membriList);
        membriScroll.setPreferredSize(new Dimension(300, 250));
        membriPanel.add(membriScroll, BorderLayout.CENTER);
        
        // Panel documenti
        JPanel documentiPanel = new JPanel(new BorderLayout());
        documentiPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 140, 0), 2),
            "Documenti del Team",
            0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(255, 140, 0)
        ));
        documentiPanel.setBackground(new Color(255, 248, 240));
        
        JScrollPane documentiScroll = new JScrollPane(documentiTable);
        documentiScroll.setPreferredSize(new Dimension(500, 250));
        documentiPanel.add(documentiScroll, BorderLayout.CENTER);
        
        centralSplit.setLeftComponent(membriPanel);
        centralSplit.setRightComponent(documentiPanel);
        
        return centralSplit;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);
        
        // Aggiungiamo solo i pulsanti essenziali
        buttonPanel.add(inserisciDocumentoButton);
        buttonPanel.add(mostraClassificaButton);
        buttonPanel.add(abbandonaTeamButton);
        
        return buttonPanel;
    }
    
    private void loadTeamData() {
        // Aggiorna informazioni header
        updateHeaderInfo();
        
        // Carica membri in background
        loadMembriAsync();
        
        // Carica documenti in background
        loadDocumentiAsync();
    }
    
    private void updateHeaderInfo() {
        if (teamInfoLabel != null) {
            teamInfoLabel.setText("Team: " + team.getNomeTeam());
        }
        
        if (hackathonInfoLabel != null && team.getHackathon() != null) {
            hackathonInfoLabel.setText("Hackathon: " + team.getHackathon().getTitoloIdentificativo());
        }
        
        if (dataInfoLabel != null && team.getHackathon() != null) {
            dataInfoLabel.setText("Dal " + team.getHackathon().getDataInizio() + 
                                " al " + team.getHackathon().getDataFine());
        }
    }
    
    private void loadMembriAsync() {
        SwingWorker<java.util.List<Utente>, Void> worker = new SwingWorker<java.util.List<Utente>, Void>() {
            @Override
            protected java.util.List<Utente> doInBackground() throws Exception {
                return hackathonController.getMembriTeam(
                    team.getNomeTeam(), 
                    team.getHackathon().getTitoloIdentificativo()
                );
            }
            
            @Override
            protected void done() {
                try {
                    var membri = get();
                    updateMembriList(membri);
                } catch (Exception e) {
                    showErrorInMembriList("Errore nel caricamento membri: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }
    
    private void loadDocumentiAsync() {
        SwingWorker<java.util.List<Documento>, Void> worker = new SwingWorker<java.util.List<Documento>, Void>() {
            @Override
            protected java.util.List<Documento> doInBackground() throws Exception {
                return hackathonController.getDocumentiByTeam(
                    team.getNomeTeam(),
                    team.getHackathon().getTitoloIdentificativo()
                );
            }
            
            @Override
            protected void done() {
                try {
                    documentiCache = get();
                    updateDocumentiTable(documentiCache);
                } catch (Exception e) {
                    showErrorInDocumentiTable("Errore nel caricamento documenti: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }
    
    private void updateMembriList(java.util.List<Utente> membri) {
        DefaultListModel<String> membriModel = new DefaultListModel<>();
        if (membri != null && !membri.isEmpty()) {
            for (var membro : membri) {
                membriModel.addElement(membro.getName());
            }
        } else {
            membriModel.addElement("Nessun membro trovato");
        }
        membriList.setModel(membriModel);
    }
    
    private void updateDocumentiTable(java.util.List<Documento> documenti) {
        documentiTableModel.setRowCount(0);
        
        if (documenti != null && !documenti.isEmpty()) {
            for (var documento : documenti) {
                String preview = documento.getText();
                if (preview.length() > 60) {
                    preview = preview.substring(0, 60) + "...";
                }
                preview = preview.replaceAll("\n", " ");
                
                Object[] row = {
                    documento.getTitle(),
                    preview,
                    documento.getText().length() + " caratteri",
                    "Visualizza"
                };
                documentiTableModel.addRow(row);
            }
        } else {
            Object[] emptyRow = {"Nessun documento", "Clicca 'Nuovo Documento' per iniziare", "0 caratteri", ""};
            documentiTableModel.addRow(emptyRow);
        }
    }
    private void showErrorInMembriList(String error) {
        DefaultListModel<String> errorModel = new DefaultListModel<>();
        errorModel.addElement("Errore: " + error);
        membriList.setModel(errorModel);
    }
    
    private void showErrorInDocumentiTable(String error) {
        documentiTableModel.setRowCount(0);
        Object[] errorRow = {"Errore", error, "N/A", ""};
        documentiTableModel.addRow(errorRow);
    }
    
    private void setupEventListeners() {
        // Listener pulsanti
        inserisciDocumentoButton.addActionListener(e -> openInsertDocumentDialog());
        mostraClassificaButton.addActionListener(e -> openClassificaDialog());
        abbandonaTeamButton.addActionListener(e -> handleAbbandonaTeam());
        
        // Doppio click su tabella documenti per aprire
        documentiTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = documentiTable.getSelectedRow();
                    if (selectedRow >= 0 && documentiCache != null && selectedRow < documentiCache.size()) {
                        Documento selectedDoc = documentiCache.get(selectedRow);
                        new ViewDocumentDialog(teamViewFrame, selectedDoc);
                    }
                }
            }
        });
    }
    
    private void openInsertDocumentDialog() {
        InsertDocumentDialog dialog = new InsertDocumentDialog(teamViewFrame, team, hackathonController);
        // Se il documento è stato inserito, ricarica i dati
        if (dialog.isDocumentInserted()) {
            loadTeamData();
        }
    }
    
    private void openClassificaDialog() {
        new ClassificaDialog(teamViewFrame, team, hackathonController);
    }
    
    private void handleAbbandonaTeam() {
        String message = "Sei sicuro di voler abbandonare il team '" + team.getNomeTeam() + "'?\n\n" +
                        "Questa azione non può essere annullata e perderai l'accesso a tutti i documenti e le attività del team.";
        
        boolean confirmed = ConfirmDialog.showConfirmDialog(
            teamViewFrame,
            "Conferma Abbandono Team", 
            message,
            "Abbandona Team",
            "Annulla"
        );
        
        if (confirmed) {
            try {
                boolean success = hackathonController.rimuoviUtenteDaTeam(
                    userLogged.getName(),
                    team.getNomeTeam(),
                    team.getHackathon().getTitoloIdentificativo()
                );
                
                if (success) {
                    MessageDialog.showSuccessMessage(teamViewFrame,
                        "Hai abbandonato il team con successo.\n\nVerrai reindirizzato alla schermata principale.");
                    
                    parentFrame.setVisible(true);
                    teamViewFrame.dispose();
                } else {
                    MessageDialog.showErrorMessage(teamViewFrame,
                        "Errore durante l'abbandono del team.\n\nRiprova più tardi o contatta l'amministratore.");
                }
            } catch (Exception ex) {
                MessageDialog.showErrorMessage(teamViewFrame,
                    "Errore durante l'abbandono del team:\n\n" + ex.getMessage() + 
                    "\n\nVerifica la connessione e riprova.");
            }
        }
    }
    
    private void showFrame() {
        teamViewFrame = new JFrame("Team Manager - " + team.getNomeTeam());
        teamViewFrame.setContentPane(mainPanel);
        teamViewFrame.setSize(900, 700);
        teamViewFrame.setLocationRelativeTo(parentFrame);
        teamViewFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Gestione chiusura finestra
        teamViewFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parentFrame.setVisible(true);
                teamViewFrame.dispose();
            }
        });
        
        teamViewFrame.setVisible(true);
    }
    
    /**
     * Getter per il frame (per compatibilità).
     */
    public JFrame getFrame() {
        return teamViewFrame;
    }
}
