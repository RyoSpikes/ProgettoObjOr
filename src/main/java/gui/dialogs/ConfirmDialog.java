package gui.dialogs;

import gui.components.ModernButton;

import javax.swing.*;
import java.awt.*;

/**
 * Dialogo di conferma moderno e stilizzato.
 */
public class ConfirmDialog extends JDialog {
    private boolean confirmed = false;
    
    public ConfirmDialog(JFrame parent, String title, String message, String confirmText, String cancelText) {
        super(parent, title, true);
        
        initializeComponents(message, confirmText, cancelText);
        setupLayout();
        setupProperties();
    }
    
    private void initializeComponents(String message, String confirmText, String cancelText) {
        // Panel principale con stile pulito
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 25, 30));
        
        // Header minimalista
        JPanel headerPanel = createHeaderPanel();
        
        // Messaggio
        JPanel messagePanel = createMessagePanel(message);
        
        // Pulsanti
        JPanel buttonPanel = createButtonPanel(confirmText, cancelText);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        
        JLabel titleLabel = new JLabel("Conferma operazione");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createMessagePanel(String message) {
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setOpaque(false);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center; line-height: 1.4;'>" + 
                                       message.replace("\n", "<br>") + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(85, 85, 85));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        return messagePanel;
    }
    
    private JPanel createButtonPanel(String confirmText, String cancelText) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        ModernButton cancelButton = ModernButton.createPrimaryButton(cancelText);
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        ModernButton confirmButton = ModernButton.createDangerButton(confirmText);
        confirmButton.setPreferredSize(new Dimension(140, 40));
        confirmButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        
        return buttonPanel;
    }
    
    private void setupLayout() {
        // Imposta ESC per annullare
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                confirmed = false;
                dispose();
            }
        });
        
        // Imposta ENTER per confermare (solo se non è un'azione pericolosa)
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ENTER");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterKeyStroke, "ENTER");
        getRootPane().getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Per sicurezza, ENTER non conferma azioni pericolose
                confirmed = false;
                dispose();
            }
        });
    }
    
    private void setupProperties() {
        setSize(480, 220);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        // Gestione chiusura finestra
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmed = false;
                dispose();
            }
        });
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    /**
     * Metodo statico per mostrare rapidamente un dialogo di conferma
     */
    public static boolean showConfirmDialog(JFrame parent, String title, String message, 
                                          String confirmText, String cancelText) {
        ConfirmDialog dialog = new ConfirmDialog(parent, title, message, confirmText, cancelText);
        dialog.setVisible(true);
        return dialog.isConfirmed();
    }
    
    /**
     * Metodo statico per dialoghi di conferma standard
     */
    public static boolean showConfirmDialog(JFrame parent, String title, String message) {
        return showConfirmDialog(parent, title, message, "Conferma", "Annulla");
    }
}
