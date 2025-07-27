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
        logFrame.setSize(500, 550); // Finestra più alta per contenere tutto
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
        fieldUsername = new JTextField(25); // Più lungo
        fieldUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        fieldUsername.setPreferredSize(new Dimension(280, 40)); // Dimensioni specifiche
        fieldUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true), // Bordo più spesso e colorato
            BorderFactory.createEmptyBorder(10, 15, 10, 15) // Padding maggiore
        ));
        fieldUsername.setBackground(Color.WHITE);
        fieldUsername.setForeground(new Color(50, 50, 50));
        fieldUsername.setCaretColor(new Color(70, 130, 180));
        
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
                    // Cambia colore del bordo quando ha il focus
                    fieldUsername.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(34, 139, 34), 2, true),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                    ));
                });
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                // Ripristina il colore del bordo quando perde il focus
                fieldUsername.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
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
        fieldPassword = new JPasswordField(25); // Più lungo
        fieldPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        fieldPassword.setPreferredSize(new Dimension(280, 40)); // Dimensioni specifiche
        fieldPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true), // Bordo più spesso e colorato
            BorderFactory.createEmptyBorder(10, 15, 10, 15) // Padding maggiore
        ));
        fieldPassword.setBackground(Color.WHITE);
        fieldPassword.setForeground(new Color(50, 50, 50));
        fieldPassword.setCaretColor(new Color(70, 130, 180));
        
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
                    // Cambia colore del bordo quando ha il focus
                    fieldPassword.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(34, 139, 34), 2, true),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                    ));
                });
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                // Ripristina il colore del bordo quando perde il focus
                fieldPassword.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
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
        
        // Pulsanti con stile moderno e dimensioni maggiori
        accediBtn = ModernButton.createPrimaryButton("Accedi");
        accediBtn.setPreferredSize(new Dimension(130, 40));
        accediBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        registratiBtn = ModernButton.createSuccessButton("Registrati");
        registratiBtn.setPreferredSize(new Dimension(130, 40));
        registratiBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Checkbox con stile migliorato
        adminCheckBox = new JCheckBox("Login come Organizzatore");
        adminCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        adminCheckBox.setForeground(new Color(70, 130, 180));
        adminCheckBox.setBackground(Color.WHITE);
        adminCheckBox.setFocusable(false); // Evita conflitti di focus
    }
    
    /**
     * Organizza i componenti nel layout.
     */
    private void layoutComponents() {
        loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(new Color(245, 248, 252));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Pannello header con titolo
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(245, 248, 252));
        
        JLabel titleLabel = new JLabel("Sistema Hackathon");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(70, 130, 180));
        headerPanel.add(titleLabel);
        
        // Pannello centrale con form semplificato
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(userLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(fieldUsername, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(new Color(70, 130, 180));
        formPanel.add(passLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(fieldPassword, gbc);
        
        // Checkbox admin
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(25, 10, 20, 10);
        formPanel.add(adminCheckBox, gbc);
        
        // Pannello pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        buttonPanel.setBackground(new Color(245, 248, 252));
        buttonPanel.add(accediBtn);
        buttonPanel.add(registratiBtn);
        
        // Assembla il layout principale
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
