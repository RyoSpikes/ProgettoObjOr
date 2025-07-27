package gui.dialogs;

import gui.components.ModernButton;

import javax.swing.*;
import java.awt.*;

/**
 * Dialogo di messaggio moderno e stilizzato.
 */
public class MessageDialog extends JDialog {
    
    public enum MessageType {
        INFO, SUCCESS, ERROR, WARNING
    }
    
    public MessageDialog(JFrame parent, String title, String message, MessageType type) {
        super(parent, title, true);
        
        initializeComponents(message, type);
        setupLayout();
        setupProperties();
    }
    
    private void initializeComponents(String message, MessageType type) {
        // Panel principale pulito e moderno
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 25, 30));
        
        // Header minimalista
        JPanel headerPanel = createHeaderPanel(type);
        
        // Messaggio
        JPanel messagePanel = createMessagePanel(message, type);
        
        // Pulsante
        JPanel buttonPanel = createButtonPanel(type);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel(MessageType type) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        
        String titleText;
        Color titleColor;
        
        switch (type) {
            case SUCCESS:
                titleText = "Operazione completata";
                titleColor = new Color(34, 139, 34);
                break;
            case ERROR:
                titleText = "Si è verificato un errore";
                titleColor = new Color(220, 20, 60);
                break;
            case WARNING:
                titleText = "Attenzione";
                titleColor = new Color(255, 140, 0);
                break;
            default: // INFO
                titleText = "Informazione";
                titleColor = new Color(70, 130, 180);
                break;
        }
        
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(titleColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createMessagePanel(String message, MessageType type) {
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setOpaque(false);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Usa JTextArea invece di JLabel per gestire meglio i messaggi lunghi
        JTextArea messageArea = new JTextArea(message);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageArea.setForeground(new Color(85, 85, 85));
        messageArea.setBackground(Color.WHITE);
        messageArea.setEditable(false);
        messageArea.setOpaque(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(null);
        
        // Centra il testo
        messageArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(350, 110)); // Aumentata l'altezza da 80 a 110
        
        messagePanel.add(scrollPane, BorderLayout.CENTER);
        
        return messagePanel;
    }
    
    private JPanel createButtonPanel(MessageType type) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        
        ModernButton okButton;
        switch (type) {
            case SUCCESS:
                okButton = ModernButton.createSuccessButton("OK");
                break;
            case ERROR:
                okButton = ModernButton.createDangerButton("OK");
                break;
            case WARNING:
                okButton = ModernButton.createWarningButton("OK");
                break;
            default: // INFO
                okButton = ModernButton.createPrimaryButton("OK");
                break;
        }
        
        okButton.setPreferredSize(new Dimension(100, 35));
        okButton.addActionListener(e -> dispose());
        
        buttonPanel.add(okButton);
        
        return buttonPanel;
    }
    
    private void setupLayout() {
        // Imposta ESC e ENTER per chiudere
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "CLOSE");
        
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ENTER");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterKeyStroke, "CLOSE");
        
        getRootPane().getActionMap().put("CLOSE", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void setupProperties() {
        // Calcola dimensioni automatiche basate sul contenuto
        pack(); // Calcola la dimensione ottimale
        
        // Imposta dimensioni minime e massime (aumentata l'altezza)
        setMinimumSize(new Dimension(350, 200));
        setMaximumSize(new Dimension(600, 450));
        
        // Se la finestra è troppo piccola dopo pack(), usa dimensioni di default (più alta)
        if (getWidth() < 350 || getHeight() < 200) {
            setSize(420, 300);
        }
        
        setLocationRelativeTo(getParent());
        setResizable(true); // Permettere ridimensionamento
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    /**
     * Metodi statici per mostrare rapidamente diversi tipi di messaggio
     */
    public static void showInfoMessage(JFrame parent, String message) {
        MessageDialog dialog = new MessageDialog(parent, "Informazione", message, MessageType.INFO);
        dialog.setVisible(true);
    }
    
    public static void showSuccessMessage(JFrame parent, String message) {
        MessageDialog dialog = new MessageDialog(parent, "Successo", message, MessageType.SUCCESS);
        dialog.setVisible(true);
    }
    
    public static void showErrorMessage(JFrame parent, String message) {
        MessageDialog dialog = new MessageDialog(parent, "Errore", message, MessageType.ERROR);
        dialog.setVisible(true);
    }
    
    public static void showWarningMessage(JFrame parent, String message) {
        MessageDialog dialog = new MessageDialog(parent, "Attenzione", message, MessageType.WARNING);
        dialog.setVisible(true);
    }
}
