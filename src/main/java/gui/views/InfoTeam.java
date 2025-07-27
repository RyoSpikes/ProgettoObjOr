package gui.views;

import controller.HackathonController;
import gui.components.ModernButton;
import model.Team;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * La classe InfoTeam rappresenta l'interfaccia grafica per visualizzare e gestire le informazioni di un team.
 * Permette di partecipare o abbandonare un team.
 */
public class InfoTeam {
    private JTextField textField1;
    private ModernButton partecipaButton;
    private ModernButton abbandonaButton;
    private JPanel mainPanel;
    private JTextArea infoTextArea;
    
    private JFrame infoTeamFrame;
    private Team team;
    private Utente userLogged;
    private JFrame parentFrame;
    private HackathonController hackathonController;

    /**
     * Costruttore della classe InfoTeam.
     * 
     * @param team Il team di cui mostrare le informazioni
     * @param userLogged L'utente attualmente loggato
     * @param parentFrame Il frame genitore
     * @param hackathonController Il controller principale per la gestione
     */
    public InfoTeam(Team team, Utente userLogged, JFrame parentFrame, HackathonController hackathonController) {
        this.team = team;
        this.userLogged = userLogged;
        this.parentFrame = parentFrame;
        this.hackathonController = hackathonController;
        
        // Inizializza i componenti programmmaticamente
        initializeComponents();
        layoutComponents();
        setupListeners();
        
        infoTeamFrame = new JFrame("Info Team - " + team.getNomeTeam());
        infoTeamFrame.setContentPane(mainPanel);
        infoTeamFrame.pack();
        infoTeamFrame.setSize(500, 400);
        infoTeamFrame.setLocationRelativeTo(parentFrame);
        
        // Listener per gestire la chiusura della finestra
        infoTeamFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parentFrame.setVisible(true);
                infoTeamFrame.dispose();
            }
        });
        
        // Carica le informazioni del team
        caricaInformazioniTeam();
        
        // Verifica se l'utente è già nel team per abilitare/disabilitare i pulsanti
        verificaStatoUtente();
        
        infoTeamFrame.setVisible(true);
    }
    
    /**
     * Inizializza i componenti dell'interfaccia grafica.
     */
    private void initializeComponents() {
        textField1 = new JTextField(20);
        textField1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        textField1.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        textField1.setBackground(Color.WHITE);
        textField1.setForeground(Color.BLACK);
        textField1.setCaretColor(Color.BLACK);
        
        // Assicuriamoci che il campo di testo sia completamente funzionale
        textField1.setEditable(true);
        textField1.setEnabled(true);
        textField1.setFocusable(true);
        textField1.setRequestFocusEnabled(true);
        textField1.setOpaque(true);
        
        // Aggiungi listener per garantire il corretto funzionamento del focus
        textField1.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    textField1.setCaretPosition(textField1.getText().length());
                });
            }
        });
        
        // Mouse listener per garantire focus al click
        textField1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (!textField1.hasFocus()) {
                    textField1.requestFocusInWindow();
                }
            }
        });
        
        infoTextArea = new JTextArea(15, 30);
        infoTextArea.setEditable(false);
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        partecipaButton = ModernButton.createSuccessButton("Partecipa");
        abbandonaButton = ModernButton.createDangerButton("Abbandona");
    }
    
    /**
     * Organizza i componenti nel layout.
     */
    private void layoutComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Pannello superiore con il nome del team
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Nome Team:"));
        topPanel.add(textField1);
        
        // Pannello centrale con le informazioni del team
        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Informazioni Team"));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // Pannello inferiore con i pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(partecipaButton);
        buttonPanel.add(abbandonaButton);
        
        // Assembla il layout principale
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Carica e mostra le informazioni del team.
     */
    private void caricaInformazioniTeam() {
        if (infoTextArea != null) {
            infoTextArea.setText("");
            infoTextArea.append("=== INFORMAZIONI TEAM ===\n\n");
            infoTextArea.append("Nome Team: " + team.getNomeTeam() + "\n");
            
            if (team.getHackathon() != null) {
                infoTextArea.append("Hackathon: " + team.getHackathon().getTitoloIdentificativo() + "\n");
                infoTextArea.append("Data inizio: " + team.getHackathon().getDataInizio() + "\n");
                infoTextArea.append("Data fine: " + team.getHackathon().getDataFine() + "\n\n");
            }
            
            try {
                // Carica i membri del team
                var membri = hackathonController.getMembriTeam(
                    team.getNomeTeam(), 
                    team.getHackathon().getTitoloIdentificativo()
                );
                
                infoTextArea.append("=== MEMBRI DEL TEAM ===\n");
                if (membri != null && !membri.isEmpty()) {
                    for (var membro : membri) {
                        infoTextArea.append("- " + membro.getName() + "\n");
                    }
                    infoTextArea.append("\nNumero membri: " + membri.size() + "\n");
                } else {
                    infoTextArea.append("Nessun membro trovato.\n");
                }
            } catch (Exception e) {
                infoTextArea.append("Errore nel caricamento dei membri: " + e.getMessage() + "\n");
            }
        }
        
        if (textField1 != null) {
            textField1.setText(team.getNomeTeam());
        }
    }
    
    /**
     * Verifica se l'utente è già membro del team e aggiorna lo stato dei pulsanti.
     */
    private void verificaStatoUtente() {
        try {
            // Carica i membri del team e verifica se l'utente corrente è presente
            var membri = hackathonController.getMembriTeam(
                team.getNomeTeam(), 
                team.getHackathon().getTitoloIdentificativo()
            );
            
            boolean isUserInTeam = false;
            if (membri != null) {
                for (var membro : membri) {
                    if (membro.getName().equals(userLogged.getName())) {
                        isUserInTeam = true;
                        break;
                    }
                }
            }
            
            if (isUserInTeam) {
                partecipaButton.setEnabled(false);
                partecipaButton.setText("Sei già nel team");
                abbandonaButton.setEnabled(true);
            } else {
                partecipaButton.setEnabled(true);
                partecipaButton.setText("Partecipa");
                abbandonaButton.setEnabled(false);
            }
        } catch (Exception e) {
            System.err.println("Errore nella verifica dello stato utente: " + e.getMessage());
            // In caso di errore, abilita entrambi i pulsanti per sicurezza
            partecipaButton.setEnabled(true);
            abbandonaButton.setEnabled(true);
        }
    }
    
    /**
     * Configura i listener dei pulsanti.
     */
    private void setupListeners() {
        // Listener per il campo di testo del nome team
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuovoNome = textField1.getText().trim();
                if (!nuovoNome.isEmpty() && !nuovoNome.equals(team.getNomeTeam())) {
                    int confirm = JOptionPane.showConfirmDialog(infoTeamFrame,
                        "Vuoi modificare il nome del team da '" + team.getNomeTeam() + "' a '" + nuovoNome + "'?",
                        "Conferma modifica nome",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                        
                    if (confirm == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(infoTeamFrame,
                            "Nome del team modificato con successo!\n(Nota: questa è una funzionalità demo)",
                            "Successo",
                            JOptionPane.INFORMATION_MESSAGE);
                        // Qui potresti implementare la logica per salvare nel database se necessario
                    } else {
                        // Ripristina il nome originale se l'utente annulla
                        textField1.setText(team.getNomeTeam());
                    }
                }
            }
        });
        
        partecipaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    hackathonController.aggiungiUtenteATeam(userLogged, team);
                    
                    JOptionPane.showMessageDialog(infoTeamFrame,
                        "Ti sei unito al team con successo!",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
                        
                    // Ricarica le informazioni e aggiorna lo stato
                    caricaInformazioniTeam();
                    verificaStatoUtente();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(infoTeamFrame,
                        "Errore: " + ex.getMessage(),
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        abbandonaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(infoTeamFrame,
                    "Sei sicuro di voler abbandonare il team?",
                    "Conferma abbandono",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        boolean success = hackathonController.rimuoviUtenteDaTeam(
                            userLogged.getName(), 
                            team.getNomeTeam(), 
                            team.getHackathon().getTitoloIdentificativo()
                        );
                        
                        if (success) {
                            JOptionPane.showMessageDialog(infoTeamFrame,
                                "Hai abbandonato il team con successo!",
                                "Successo",
                                JOptionPane.INFORMATION_MESSAGE);
                                
                            // Ricarica le informazioni e aggiorna lo stato
                            caricaInformazioniTeam();
                            verificaStatoUtente();
                        } else {
                            JOptionPane.showMessageDialog(infoTeamFrame,
                                "Errore durante l'abbandono del team.",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(infoTeamFrame,
                            "Errore: " + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
}
