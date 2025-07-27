package gui.dialogs;

import gui.components.ModernButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialogo per l'inserimento del voto finale di un team.
 */
public class VotoFinaleDialog extends JDialog {
    private int voto = -1;
    private boolean confermato = false;
    private JSpinner votoSpinner;
    
    public VotoFinaleDialog(JFrame parent, String teamName, String documentTitle) {
        super(parent, "Voto Finale - " + teamName, true);
        
        initializeComponents(teamName, documentTitle);
        setupLayout();
        setupProperties();
    }
    
    private void initializeComponents(String teamName, String documentTitle) {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        // Header
        JPanel headerPanel = createHeaderPanel(teamName);
        
        // Info documento
        JPanel infoPanel = createInfoPanel(documentTitle);
        
        // Voto selector
        JPanel votoPanel = createVotoPanel();
        
        // Pulsanti
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(votoPanel, BorderLayout.SOUTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(contentPanel);
    }
    
    private JPanel createHeaderPanel(String teamName) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("Assegna Voto Finale");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel teamLabel = new JLabel("Team: " + teamName);
        teamLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        teamLabel.setForeground(new Color(51, 51, 51));
        teamLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(Box.createVerticalStrut(8), BorderLayout.CENTER);
        headerPanel.add(teamLabel, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    private JPanel createInfoPanel(String documentTitle) {
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JLabel infoLabel = new JLabel("<html><div style='text-align: center; line-height: 1.4;'>" +
                                    "<b>Ultimo documento caricato:</b><br>" +
                                    documentTitle + "</div></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(85, 85, 85));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        
        return infoPanel;
    }
    
    private JPanel createVotoPanel() {
        JPanel votoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        votoPanel.setOpaque(false);
        
        JLabel votoLabel = new JLabel("Voto (1-10):");
        votoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        votoLabel.setForeground(new Color(70, 130, 180));
        
        votoSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        votoSpinner.setFont(new Font("Segoe UI", Font.BOLD, 18));
        votoSpinner.setPreferredSize(new Dimension(80, 35));
        
        // Personalizza l'aspetto dello spinner
        JComponent editor = votoSpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setHorizontalAlignment(SwingConstants.CENTER);
            ((JSpinner.DefaultEditor) editor).getTextField().setFont(new Font("Segoe UI", Font.BOLD, 18));
        }
        
        votoPanel.add(votoLabel);
        votoPanel.add(votoSpinner);
        
        return votoPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        
        ModernButton annullaButton = ModernButton.createSecondaryButton("Annulla");
        annullaButton.setPreferredSize(new Dimension(120, 40));
        annullaButton.addActionListener(e -> {
            confermato = false;
            dispose();
        });
        
        ModernButton confermaButton = ModernButton.createPrimaryButton("Conferma Voto");
        confermaButton.setPreferredSize(new Dimension(140, 40));
        confermaButton.addActionListener(e -> {
            voto = (Integer) votoSpinner.getValue();
            confermato = true;
            dispose();
        });
        
        buttonPanel.add(annullaButton);
        buttonPanel.add(confermaButton);
        
        return buttonPanel;
    }
    
    private void setupLayout() {
        // ESC per annullare
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confermato = false;
                dispose();
            }
        });
        
        // ENTER per confermare
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ENTER");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterKeyStroke, "ENTER");
        getRootPane().getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voto = (Integer) votoSpinner.getValue();
                confermato = true;
                dispose();
            }
        });
    }
    
    private void setupProperties() {
        setSize(450, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confermato = false;
                dispose();
            }
        });
        
        // Dai il focus allo spinner quando si apre
        SwingUtilities.invokeLater(() -> votoSpinner.requestFocusInWindow());
    }
    
    public int getVoto() {
        return voto;
    }
    
    public boolean isConfermato() {
        return confermato;
    }
    
    /**
     * Metodo statico per mostrare rapidamente il dialogo
     */
    public static int showVotoDialog(JFrame parent, String teamName, String documentTitle) {
        VotoFinaleDialog dialog = new VotoFinaleDialog(parent, teamName, documentTitle);
        dialog.setVisible(true);
        
        if (dialog.isConfermato()) {
            return dialog.getVoto();
        }
        return -1; // Annullato
    }
}
