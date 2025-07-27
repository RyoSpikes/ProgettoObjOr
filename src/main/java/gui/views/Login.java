package gui.views;

import javax.swing.*;
import gui.components.ModernButton;
import model.*;
import controller.HackathonController;
import controller.UserController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * La classe Login rappresenta l'interfaccia grafica per il login degli utenti e degli organizzatori.
 * Consente di autenticarsi o registrarsi come nuovo utente o organizzatore.
 */
public class Login {
    private JPanel loginPanel; // Pannello principale della finestra di login.
    private JTextField fieldUsername; // Campo di testo per l'inserimento del nome utente.
    private JPasswordField fieldPassword; // Campo di testo per l'inserimento della password.
    private ModernButton accediBtn; // Pulsante per effettuare il login.
    private ModernButton registratiBtn; // Pulsante per registrarsi.
    private JCheckBox adminCheckBox; // Checkbox per indicare se è un admin.
    public JFrame logFrame; // Finestra principale della vista di login.

    /**
     * Costruttore della classe Login.
     * Inizializza l'interfaccia grafica e gestisce le azioni per il login degli utenti.
     *
     * @param hackathonController Il controller principale per la gestione di tutti i componenti.
     * @param frameCalling Il frame chiamante che ha aperto questa finestra.
     */
    public Login(HackathonController hackathonController, JFrame frameCalling) {
        logFrame = new JFrame("Login - Sistema Hackathon");
        logFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Inizializa i componenti programmaticamente
        initializeComponents();  
        layoutComponents();
        setupEventHandlers(hackathonController, frameCalling);
        
        logFrame.setContentPane(loginPanel);

        logFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameCalling.setVisible(true);
                logFrame.dispose();
            }
        });

        logFrame.pack();
        logFrame.setVisible(true);
        logFrame.setSize(450, 350);
        logFrame.setResizable(false);
        logFrame.setLocationRelativeTo(null);
        
        // Imposta il focus sul campo username quando la finestra si apre
        SwingUtilities.invokeLater(() -> {
            fieldUsername.requestFocusInWindow();
            fieldUsername.setCaretPosition(0);
        });
    }
    
    /**
     * Inizializza i componenti dell'interfaccia grafica.
     */
    private void initializeComponents() {
        // Crea il campo username con configurazione completa e sicura
        fieldUsername = new JTextField(20);
        fieldUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fieldUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fieldUsername.setBackground(Color.WHITE);
        fieldUsername.setForeground(Color.BLACK);
        fieldUsername.setCaretColor(Color.BLACK);
        
        // Configurazioni di base per assicurarsi che il campo sia completamente funzionale
        fieldUsername.setEditable(true);
        fieldUsername.setEnabled(true);
        fieldUsername.setFocusable(true);
        fieldUsername.setRequestFocusEnabled(true);
        fieldUsername.setOpaque(true);
        
        // Aggiungi listener per garantire il corretto funzionamento del focus
        fieldUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    fieldUsername.setCaretPosition(fieldUsername.getText().length());
                });
            }
        });
        
        // Mouse listener per garantire focus al click
        fieldUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (!fieldUsername.hasFocus()) {
                    fieldUsername.requestFocusInWindow();
                }
            }
        });
        
        // Crea il campo password con configurazione completa e sicura
        fieldPassword = new JPasswordField(20);
        fieldPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fieldPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        fieldPassword.setBackground(Color.WHITE);
        fieldPassword.setForeground(Color.BLACK);
        fieldPassword.setCaretColor(Color.BLACK);
        
        // Configurazioni di base per assicurarsi che il campo sia completamente funzionale
        fieldPassword.setEditable(true);
        fieldPassword.setEnabled(true);
        fieldPassword.setFocusable(true);
        fieldPassword.setRequestFocusEnabled(true);
        fieldPassword.setOpaque(true);
        
        // Aggiungi listener per garantire il corretto funzionamento del focus
        fieldPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    fieldPassword.setCaretPosition(fieldPassword.getPassword().length);
                });
            }
        });
        
        // Mouse listener per garantire focus al click
        fieldPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (!fieldPassword.hasFocus()) {
                    fieldPassword.requestFocusInWindow();
                }
            }
        });
        
        // Aggiungi supporto per ENTER su entrambi i campi
        java.awt.event.KeyListener enterKeyListener = new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    accediBtn.doClick();
                }
            }
        };
        fieldUsername.addKeyListener(enterKeyListener);
        fieldPassword.addKeyListener(enterKeyListener);
        
        accediBtn = ModernButton.createPrimaryButton("Accedi");
        accediBtn.setPreferredSize(new Dimension(120, 35));
        accediBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        registratiBtn = ModernButton.createSuccessButton("Registrati");
        registratiBtn.setPreferredSize(new Dimension(120, 35));
        registratiBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        adminCheckBox = new JCheckBox("Login come Organizzatore");
        adminCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        adminCheckBox.setBackground(Color.WHITE);
        adminCheckBox.setFocusable(false); // Evita conflitti di focus
    }
    
    /**
     * Organizza i componenti nel layout.
     */
    private void layoutComponents() {
        loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Pannello header con titolo
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("Accesso Sistema Hackathon");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        headerPanel.add(titleLabel);
        
        // Pannello centrale con form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setLabelFor(fieldUsername); // Associa la label al campo
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(fieldUsername, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passLabel.setLabelFor(fieldPassword); // Associa la label al campo
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(fieldPassword, gbc);
        
        // Checkbox admin
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(adminCheckBox, gbc);
        
        // Pannello pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(accediBtn);
        buttonPanel.add(registratiBtn);
        
        // Assembla il layout
        loginPanel.add(headerPanel, BorderLayout.NORTH);
        loginPanel.add(formPanel, BorderLayout.CENTER);
        loginPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Configura i gestori degli eventi.
     */
    private void setupEventHandlers(HackathonController hackathonController, JFrame frameCalling) {
        // Listener per il pulsante "Accedi" - gestisce sia utenti che organizzatori
        accediBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = fieldUsername.getText();
                String password = new String(fieldPassword.getPassword());

                if (username == null || username.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(logFrame,
                            "⚠️ Inserire un nome utente valido.",
                            "Campo Obbligatorio",
                            JOptionPane.WARNING_MESSAGE);
                    fieldUsername.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(logFrame,
                            "⚠️ Inserire una password.",
                            "Campo Obbligatorio", 
                            JOptionPane.WARNING_MESSAGE);
                    fieldPassword.requestFocus();
                    return;
                }

                // Aggiungi indicatore di caricamento
                accediBtn.setText("Accesso in corso...");
                accediBtn.setEnabled(false);

                try {
                    if (adminCheckBox.isSelected()) {
                        // Login come organizzatore
                        Organizzatore orgLoggato = hackathonController.loginOrganizzatore(username, password);

                        if (orgLoggato != null) {
                            System.out.println("✅ Login riuscito per organizzatore: " + username);
                            JOptionPane.showMessageDialog(logFrame,
                                    "✅ Benvenuto/a, " + orgLoggato.getName() + "!",  
                                    "Login Riuscito",
                                    JOptionPane.INFORMATION_MESSAGE);
                            new AdminView(orgLoggato, frameCalling, hackathonController);
                            logFrame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(logFrame,
                                    "❌ Credenziali organizzatore non valide.\nControlla username e password.",
                                    "Login Fallito",
                                    JOptionPane.ERROR_MESSAGE);
                            fieldPassword.setText("");
                            fieldPassword.requestFocus();
                        }
                    } else {
                        // Login come utente normale - usa UserController direttamente
                        UserController userController = new UserController();
                        boolean loginRiuscito = userController.checkLogin(username, password);

                        if (loginRiuscito) {
                            System.out.println("✅ Login riuscito per utente: " + username);
                            
                            // Recupera l'oggetto utente dalla lista
                            Utente utenteLoggato = null;
                            for (Utente u : userController.getListaUtenti()) {
                                if (u.getName().equals(username)) {
                                    utenteLoggato = u;
                                    break;
                                }
                            }
                            
                            JOptionPane.showMessageDialog(logFrame,
                                    "✅ Benvenuto/a, " + username + "!",
                                    "Login Riuscito", 
                                    JOptionPane.INFORMATION_MESSAGE);
                            new UserView(utenteLoggato, frameCalling, hackathonController);
                            logFrame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(logFrame,
                                    "❌ Credenziali utente non valide.\nControlla username e password.",
                                    "Login Fallito",
                                    JOptionPane.ERROR_MESSAGE);
                            fieldPassword.setText("");
                            fieldPassword.requestFocus();
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(logFrame,
                            "❌ Errore durante il login:\n" + ex.getMessage(),
                            "Errore di Sistema",
                            JOptionPane.ERROR_MESSAGE);  
                } finally {
                    // Ripristina il pulsante
                    accediBtn.setText("Accedi");
                    accediBtn.setEnabled(true);
                }
            }
        });

        // Listener per il pulsante "Registrati"
        registratiBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = fieldUsername.getText();
                String password = new String(fieldPassword.getPassword());

                if (username == null || username.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(logFrame,
                            "⚠️ Inserire un nome utente valido.",
                            "Campo Obbligatorio",
                            JOptionPane.WARNING_MESSAGE);
                    fieldUsername.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(logFrame,
                            "⚠️ Inserire una password.",
                            "Campo Obbligatorio",
                            JOptionPane.WARNING_MESSAGE);
                    fieldPassword.requestFocus();
                    return;
                }

                // Aggiungi indicatore di caricamento
                registratiBtn.setText("Registrazione...");
                registratiBtn.setEnabled(false);

                try {
                    if (adminCheckBox.isSelected()) {
                        // Registrazione come organizzatore - usa hackathonController.aggiungiUtente (che crea organizzatori)
                        hackathonController.aggiungiUtente(username, password);
                        JOptionPane.showMessageDialog(logFrame,
                                "✅ Registrazione organizzatore completata con successo!\n" +
                                "Ora puoi effettuare il login come organizzatore.",
                                "Registrazione Riuscita", 
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Registrazione come utente normale - usa UserController per creare utenti
                        UserController userController = new UserController();
                        userController.aggiungiUtente(username, password);
                        JOptionPane.showMessageDialog(logFrame,
                                "✅ Registrazione utente completata con successo!\n" +
                                "Ora puoi effettuare il login come utente.",
                                "Registrazione Riuscita",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    fieldUsername.setText("");
                    fieldPassword.setText("");
                    adminCheckBox.setSelected(false);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(logFrame,
                            "❌ Errore durante la registrazione:\n" + ex.getMessage(),
                            "Errore di Registrazione",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    // Ripristina il pulsante
                    registratiBtn.setText("Registrati");
                    registratiBtn.setEnabled(true);
                }
            }
        });
    }
}
