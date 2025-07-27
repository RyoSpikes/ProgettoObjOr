package gui.dialogs;

import controller.HackathonController;
import gui.components.ModernButton;
import model.Documento;
import model.Team;

import javax.swing.*;
import java.awt.*;

/**
 * Dialogo moderno per l'inserimento di nuovi documenti.
 */
public class InsertDocumentDialog extends JDialog {
    private Team team;
    private HackathonController hackathonController;
    private JTextField titleField;
    private JTextArea contentArea;
    private boolean documentInserted = false;
    
    public InsertDocumentDialog(JFrame parent, Team team, HackathonController hackathonController) {
        super(parent, "Inserisci Nuovo Documento", true);
        this.team = team;
        this.hackathonController = hackathonController;
        
        initializeComponents();
        setupLayout();
        
        setSize(550, 450);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Panel principale con gradiente
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(248, 255, 248);
                Color color2 = new Color(240, 250, 240);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Form panel
        JPanel formPanel = createFormPanel();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("Inserimento Nuovo Documento", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(25, 25, 112));
        
        String teamInfo = "Team: " + team.getNomeTeam();
        if (team.getHackathon() != null) {
            teamInfo += " | Hackathon: " + team.getHackathon().getTitoloIdentificativo();
        }
        JLabel infoLabel = new JLabel(teamInfo, JLabel.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(105, 105, 105));
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(infoLabel, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout(10, 15));
        formPanel.setOpaque(false);
        
        // Panel per il titolo
        JPanel titlePanel = new JPanel(new BorderLayout(5, 5));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Titolo del Documento",
            0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(70, 130, 180)
        ));
        
        titleField = new JTextField();
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JLabel titleHint = new JLabel("Massimo 30 caratteri");
        titleHint.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        titleHint.setForeground(new Color(128, 128, 128));
        
        titlePanel.add(titleField, BorderLayout.CENTER);
        titlePanel.add(titleHint, BorderLayout.SOUTH);
        
        // Panel per il contenuto
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
            "Contenuto del Documento",
            0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(34, 139, 34)
        ));
        
        contentArea = new JTextArea(10, 40);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentArea.setBackground(new Color(255, 255, 255));
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        formPanel.add(titlePanel, BorderLayout.NORTH);
        formPanel.add(contentPanel, BorderLayout.CENTER);
        
        return formPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        
        ModernButton saveButton = ModernButton.createSuccessButton("Salva Documento");
        saveButton.addActionListener(e -> saveDocument());
        
        ModernButton cancelButton = ModernButton.createDangerButton("Annulla");
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        return buttonPanel;
    }
    
    private void saveDocument() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();
        
        // Validazione input
        if (title.isEmpty()) {
            showErrorMessage("Il titolo del documento è obbligatorio.");
            titleField.requestFocus();
            return;
        }
        
        if (content.isEmpty()) {
            showErrorMessage("Il contenuto del documento è obbligatorio.");
            contentArea.requestFocus();
            return;
        }
        
        if (title.length() > 30) {
            showErrorMessage("Il titolo non può superare i 30 caratteri.");
            titleField.requestFocus();
            return;
        }
        
        try {
            // Crea il documento
            Documento nuovoDocumento = new Documento(team, title, content);
            
            // Salva nel database
            if (hackathonController.salvaDocumento(nuovoDocumento)) {
                showSuccessMessage("Documento inserito con successo!");
                documentInserted = true;
                dispose();
            } else {
                showErrorMessage("Errore durante l'inserimento del documento.");
            }
            
        } catch (Exception ex) {
            showErrorMessage("Errore durante l'inserimento: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, 
            "⚠️ " + message, 
            "Attenzione", 
            JOptionPane.WARNING_MESSAGE);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, 
            "✅ " + message, 
            "Successo", 
            JOptionPane.INFORMATION_MESSAGE);
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
        
        // Focus iniziale sul campo titolo
        SwingUtilities.invokeLater(() -> titleField.requestFocus());
    }
    
    /**
     * Verifica se il documento è stato inserito con successo.
     */
    public boolean isDocumentInserted() {
        return documentInserted;
    }
}
