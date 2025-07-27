package gui.dialogs;

import gui.components.ModernButton;
import model.Documento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Dialogo per visualizzare il contenuto completo di un documento.
 */
public class ViewDocumentDialog extends JDialog {
    private Documento documento;
    private JFrame parentFrame;
    
    public ViewDocumentDialog(JFrame parent, Documento documento) {
        super(parent, "Visualizza Documento - " + documento.getTitle(), true);
        this.documento = documento;
        this.parentFrame = parent;
        
        initializeComponents();
        setupLayout();
        setupListeners();
        
        setSize(700, 500);
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
                Color color1 = new Color(240, 248, 255);
                Color color2 = new Color(230, 240, 250);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header con informazioni del documento
        JPanel headerPanel = createHeaderPanel();
        
        // Area contenuto
        JPanel contentPanel = createContentPanel();
        
        // Panel bottoni
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        headerPanel.setBackground(new Color(255, 255, 255, 200));
        
        // Titolo documento
        JLabel titleLabel = new JLabel(documento.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(25, 25, 112));
        
        // Info team
        String teamInfo = "Documento del team";
        if (documento.getSource() != null) {
            teamInfo = "Team: " + documento.getSource().getNomeTeam();
        }
        JLabel teamLabel = new JLabel(teamInfo);
        teamLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        teamLabel.setForeground(new Color(105, 105, 105));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setOpaque(false);
        infoPanel.add(titleLabel);
        infoPanel.add(teamLabel);
        
        headerPanel.add(infoPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
            "Contenuto del Documento",
            0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(34, 139, 34)
        ));
        
        // Area di testo per il contenuto
        JTextArea contentArea = new JTextArea(documento.getText());
        contentArea.setEditable(false);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBackground(new Color(248, 255, 248));
        contentArea.setForeground(new Color(25, 25, 25));
        contentArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentArea.setCaretPosition(0);
        
        // Scroll pane con styling personalizzato
        JScrollPane scrollPane = new JScrollPane(contentArea);
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
        
        // Bottone chiudi con stile moderno
        ModernButton closeButton = ModernButton.createDangerButton("Chiudi");
        closeButton.addActionListener(e -> dispose());
        
        // Bottone copia contenuto
        ModernButton copyButton = ModernButton.createPrimaryButton("Copia Contenuto");
        copyButton.addActionListener(e -> copyToClipboard());
        
        buttonPanel.add(copyButton);
        buttonPanel.add(closeButton);
        
        return buttonPanel;
    }
    
    private void copyToClipboard() {
        try {
            java.awt.datatransfer.StringSelection stringSelection = 
                new java.awt.datatransfer.StringSelection(documento.getText());
            java.awt.datatransfer.Clipboard clipboard = 
                java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            
            MessageDialog.showSuccessMessage(parentFrame, 
                "Contenuto copiato negli appunti!");
        } catch (Exception e) {
            MessageDialog.showErrorMessage(parentFrame, 
                "Errore durante la copia: " + e.getMessage());
        }
    }
    
    private void setupLayout() {
        // Layout già configurato in initializeComponents()
    }
    
    private void setupListeners() {
        // ESC per chiudere
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
