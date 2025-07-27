package gui;

import javax.swing.*;
import model.*;
import controller.HackathonController;

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
    private JButton accediBtn; // Pulsante per effettuare il login.
    private JButton registratiBtn; // Pulsante per registrarsi.
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
        logFrame = new JFrame("Login");
        logFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        logFrame.setSize(400, 250);
        logFrame.setResizable(false);
        logFrame.setLocationRelativeTo(null);

        // Listener per il pulsante "Accedi" - gestisce sia utenti che organizzatori
        accediBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = fieldUsername.getText();
                String password = new String(fieldPassword.getPassword());

                if (username == null || username.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Inserire un nome utente valido.",
                            "Campo vuoto",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Inserire una password.",
                            "Campo vuoto",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    if (adminCheckBox.isSelected()) {
                        // Login come organizzatore
                        Organizzatore orgLoggato = hackathonController.loginOrganizzatore(username, password);

                        if (orgLoggato != null) {
                            System.out.println("Login riuscito per organizzatore: " + username);
                            new AdminView(orgLoggato, frameCalling, hackathonController);
                            logFrame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Credenziali organizzatore non valide. Riprova.",
                                    "Login Fallito",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Login come utente normale
                        Utente utenteLoggato = hackathonController.loginUtente(username, password);

                        if (utenteLoggato != null) {
                            System.out.println("Login riuscito per utente: " + username);
                            new UserView(utenteLoggato, frameCalling, hackathonController);
                            logFrame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Credenziali utente non valide. Riprova.",
                                    "Login Fallito",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Errore durante il login: " + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        // Listener per il pulsante "Registrati" - gestisce utenti e organizzatori
        registratiBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = fieldUsername.getText();
                String password = new String(fieldPassword.getPassword());

                if (username == null || username.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Inserire un nome utente valido.",
                            "Campo vuoto",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Inserire una password.",
                            "Campo vuoto",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    if (adminCheckBox.isSelected()) {
                        // ❌ REGISTRAZIONE ORGANIZZATORE DISABILITATA PER SICUREZZA
                        JOptionPane.showMessageDialog(null,
                                "Registrazione come organizzatore non permessa.\n" +
                                "Gli organizzatori devono essere creati direttamente nel database per motivi di sicurezza.",
                                "Accesso Negato",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        // Registrazione come utente normale
                        hackathonController.aggiungiUtente(username, password);
                        JOptionPane.showMessageDialog(null,
                                "Registrazione utente completata con successo!",
                                "Successo",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    fieldUsername.setText("");
                    fieldPassword.setText("");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Errore durante la registrazione: " + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
