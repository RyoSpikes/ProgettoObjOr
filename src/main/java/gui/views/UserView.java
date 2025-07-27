package gui.views;

import controller.HackathonController;
import gui.views.CreaTeamForm;
import model.Utente;
import model.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * La classe UserView rappresenta l'interfaccia grafica per la visualizzazione delle funzionalità disponibili per un utente.
 * Consente di creare un team, visualizzare inviti, scegliere un team e visualizzare il team attuale.
 */
public class UserView {
    public JFrame userViewFrame; // Finestra principale della vista utente.
    private JPanel userPanel; // Pannello principale della finestra.
    private JTextArea userTextArea; // Area di testo per visualizzare informazioni.
    private JButton creaTeam; // Pulsante per creare un nuovo team.
    private JButton mostraInvitiButton; // Pulsante per mostrare gli inviti ricevuti.
    private JButton scegliTeamButton; // Pulsante per scegliere un team.
    private JButton gestisciTeamButton; // Pulsante per gestire il team (aprire TeamView).
    private JButton menuGiudiceButton;

    /**
     * Costruttore della classe UserView.
     * Inizializza l'interfaccia grafica e gestisce le azioni dei pulsanti.
     *
     * @param userLogged L'utente attualmente loggato.
     * @param frameHome Il frame principale dell'applicazione.
     * @param hackathonController Il controller principale dell'applicazione.
     */
    public UserView(Utente userLogged, JFrame frameHome, HackathonController hackathonController) {
        
        userViewFrame = new JFrame("Hackathon Management - Area Utente");
        
        // Crea l'interfaccia moderna invece di usare solo il form
        createModernInterface(userLogged);
        
        userViewFrame.pack();

        // Listener per gestire la chiusura della finestra.
        userViewFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameHome.setVisible(true);
                userViewFrame.dispose();
            }
        });

        userViewFrame.setVisible(true);
        userViewFrame.setSize(600, 500); // Ridotta da 800x800 a 600x500
        userViewFrame.setResizable(false);
        userViewFrame.setLocationRelativeTo(null);

        // Applica stile moderno ai pulsanti
        setupModernButtonStyles();

        // Listener per il pulsante "Crea Team".
        creaTeam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userViewFrame.setVisible(false);
                new CreaTeamForm(userLogged, userViewFrame, hackathonController);
            }
        });

        // Listener per il pulsante "Mostra Inviti".
        mostraInvitiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<String> inviti = hackathonController.getInvitiGiudice(userLogged.getName());
                    
                    if (inviti != null && !inviti.isEmpty()) {
                        String[] invitiArray = inviti.toArray(new String[0]);
                        
                        String selectedInvito = (String) JOptionPane.showInputDialog(
                            userViewFrame,
                            "Seleziona un invito a giudice:",
                            "I tuoi inviti",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            invitiArray,
                            invitiArray[0]
                        );
                        
                        if (selectedInvito != null) {
                            // Estrai il titolo dell'hackathon dall'invito
                            String titoloHackathon = selectedInvito.split("'")[1]; // Estrae il titolo tra apici
                            
                            // Controllo preventivo per conflitti di date
                            boolean puoAccettare = hackathonController.puoAccettareInvito(userLogged.getName(), titoloHackathon);
                            String messaggioConferma = "Vuoi accettare l'invito a giudicare l'hackathon '" + titoloHackathon + "'?";
                            
                            if (!puoAccettare) {
                                messaggioConferma += "\n\n⚠️ ATTENZIONE: Potresti avere conflitti di date con altri hackathon per cui sei già giudice.";
                            }
                            
                            int result = JOptionPane.showConfirmDialog(
                                userViewFrame,
                                messaggioConferma,
                                puoAccettare ? "Conferma invito" : "⚠️ Conferma invito (Conflitto rilevato)",
                                JOptionPane.YES_NO_OPTION,
                                puoAccettare ? JOptionPane.QUESTION_MESSAGE : JOptionPane.WARNING_MESSAGE
                            );
                            
                            if (result == JOptionPane.YES_OPTION) {
                                // Usa il metodo dettagliato per gestire gli errori specifici
                                HackathonController.AccettazioneInvitoRisultato risultato = 
                                    hackathonController.accettaInvitoDettagliato(userLogged.getName(), titoloHackathon);
                                
                                // Ottieni il messaggio appropriato per il risultato
                                String[] messaggio = hackathonController.getMessaggioAccettazioneInvito(risultato, titoloHackathon);
                                
                                // Determina il tipo di messaggio
                                int messageType;
                                switch (risultato) {
                                    case SUCCESSO:
                                        messageType = JOptionPane.INFORMATION_MESSAGE;
                                        break;
                                    case ERRORE_DATE_SOVRAPPOSTE:
                                        messageType = JOptionPane.WARNING_MESSAGE;
                                        break;
                                    case ERRORE_INVITO_NON_ESISTENTE:
                                    case ERRORE_GENERICO:
                                    default:
                                        messageType = JOptionPane.ERROR_MESSAGE;
                                        break;
                                }
                                
                                // Mostra la finestra di dialogo con il messaggio appropriato
                                JOptionPane.showMessageDialog(userViewFrame,
                                    messaggio[1], // messaggio
                                    messaggio[0], // titolo
                                    messageType);
                            } else {
                                boolean success = hackathonController.rifiutaInvito(userLogged.getName(), titoloHackathon);
                                if (success) {
                                    JOptionPane.showMessageDialog(userViewFrame,
                                        "Invito rifiutato.",
                                        "Invito rifiutato",
                                        JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(userViewFrame, 
                            "Non hai inviti a giudice pendenti.", 
                            "Nessun invito", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(userViewFrame, 
                        "Errore nel recupero degli inviti: " + ex.getMessage(), 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Listener per il pulsante "Scegli Team".
        scegliTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ScegliTeam(userLogged, userViewFrame, hackathonController);
                // Rimosso userViewFrame.setVisible(false); - UserView deve rimanere visibile
            }
        });

        // Listener per il pulsante "Gestisci Team" (se presente nel form).
        if (gestisciTeamButton != null) {
            gestisciTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Ottiene tutti i team dell'utente usando hackathonController
                    List<Team> teamsUtente = hackathonController.getTeamsByUser(userLogged.getName());
                    
                    if (teamsUtente != null && !teamsUtente.isEmpty()) {
                        // Crea una lista cliccabile dei team
                        String[] teamNames = new String[teamsUtente.size()];
                        for (int i = 0; i < teamsUtente.size(); i++) {
                            Team team = teamsUtente.get(i);
                            String hackathonName = team.getHackathon() != null ? 
                                team.getHackathon().getTitoloIdentificativo() : "Hackathon sconosciuto";
                            teamNames[i] = team.getNomeTeam() + " - " + hackathonName;
                        }
                        
                        // Mostra la lista in un dialogo
                        String selectedTeam = (String) JOptionPane.showInputDialog(
                            userViewFrame,
                            "Seleziona un team da gestire:",
                            "Gestisci Team",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            teamNames,
                            teamNames[0]
                        );
                        
                        if (selectedTeam != null) {
                            // Trova il team selezionato
                            Team teamSelezionato = null;
                            for (Team team : teamsUtente) {
                                String hackathonName = team.getHackathon() != null ? 
                                    team.getHackathon().getTitoloIdentificativo() : "Hackathon sconosciuto";
                                if (selectedTeam.equals(team.getNomeTeam() + " - " + hackathonName)) {
                                    teamSelezionato = team;
                                    break;
                                }
                            }
                            
                            if (teamSelezionato != null) {
                                // Apri ModernTeamView per gestire il team
                                userViewFrame.setVisible(false);
                                new ModernTeamView(teamSelezionato, userLogged, userViewFrame, hackathonController);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(userViewFrame, 
                            "Non sei in nessun team da gestire.", 
                            "Nessun team", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(userViewFrame, 
                        "Errore nel recupero delle informazioni dei team: " + ex.getMessage(), 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        } // Fine del controllo if (gestisciTeamButton != null)

        // Listener per il pulsante "Menu Giudice".
        menuGiudiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<String> hackathons = hackathonController.getHackathonAsGiudice(userLogged.getName());
                    
                    if (hackathons != null && !hackathons.isEmpty()) {
                        String[] hackathonsArray = hackathons.toArray(new String[0]);
                        
                        String selectedHackathon = (String) JOptionPane.showInputDialog(
                            userViewFrame,
                            "Seleziona un hackathon di cui sei giudice:",
                            "Menu Giudice",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            hackathonsArray,
                            hackathonsArray[0]
                        );
                        
                        if (selectedHackathon != null) {
                            // Estrai il titolo dell'hackathon (prima del primo " - ")
                            String titoloHackathon = selectedHackathon.split(" - ")[0];
                            
                            // Apri JudgeView
                            try {
                                userViewFrame.setVisible(false);
                                new JudgeView(titoloHackathon, userLogged, userViewFrame, hackathonController);
                            } catch (Exception ex) {
                                userViewFrame.setVisible(true); // Ripristina la visibilità in caso di errore
                                JOptionPane.showMessageDialog(userViewFrame, 
                                    "Errore nell'apertura della vista giudice:\n" + ex.getMessage(), 
                                    "Errore", 
                                    JOptionPane.ERROR_MESSAGE);
                                ex.printStackTrace();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(userViewFrame, 
                            "Non sei giudice di nessun hackathon.", 
                            "Nessun hackathon", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(userViewFrame, 
                        "Errore nel recupero degli hackathon: " + ex.getMessage(), 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Crea l'interfaccia moderna con header stilizzato.
     */
    private void createModernInterface(Utente userLogged) {
        // Panel principale con gradiente
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(245, 250, 255); // Light Alice Blue
                Color color2 = new Color(230, 245, 255); // Lighter Steel Blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header con titolo moderno
        JPanel headerPanel = createModernHeader(userLogged);
        
        // Panel centrale con i pulsanti
        JPanel contentPanel = createContentPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        userViewFrame.setContentPane(mainPanel);
    }

    /**
     * Crea l'header moderno con titolo e informazioni utente.
     */
    private JPanel createModernHeader(Utente userLogged) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Titolo principale
        JLabel titleLabel = new JLabel("Area Utente", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112)); // Midnight Blue
        
        // Sottotitolo con nome utente
        JLabel subtitleLabel = new JLabel("Benvenuto, " + userLogged.getName(), SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(70, 130, 180)); // Steel Blue
        
        // Panel per centrare i titoli
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        // Linea separatrice
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        headerPanel.add(separator, BorderLayout.SOUTH);
        
        return headerPanel;
    }

    /**
     * Crea il panel centrale con i pulsanti organizzati.
     */
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        
        // Panel per i pulsanti principali (ridotto a 3x1 + 2 in basso)
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Crea i pulsanti se non esistono (compatibilità con form)
        if (creaTeam == null) creaTeam = new JButton("Crea Team");
        if (mostraInvitiButton == null) mostraInvitiButton = new JButton("Mostra Inviti");
        if (scegliTeamButton == null) scegliTeamButton = new JButton("Scegli Team");
        if (gestisciTeamButton == null) gestisciTeamButton = new JButton("Gestisci Team");
        if (menuGiudiceButton == null) menuGiudiceButton = new JButton("Menu Giudice");
        
        // Aggiungi i pulsanti al panel (solo 5 pulsanti invece di 6)
        buttonsPanel.add(creaTeam);
        buttonsPanel.add(mostraInvitiButton);
        buttonsPanel.add(scegliTeamButton);
        buttonsPanel.add(gestisciTeamButton);
        buttonsPanel.add(menuGiudiceButton);
        
        // Cella vuota per bilanciare il layout 2x3
        buttonsPanel.add(new JLabel()); // Placeholder vuoto
        
        contentPanel.add(buttonsPanel, BorderLayout.CENTER);
        
        // Area di testo informativa (opzionale)
        if (userTextArea == null) {
            userTextArea = new JTextArea(3, 40);
        }
        userTextArea.setEditable(false);
        userTextArea.setText("Seleziona un'opzione dal menu per iniziare.\nPuoi creare team, gestire inviti e partecipare agli hackathon.");
        userTextArea.setOpaque(false);
        userTextArea.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)), 
            "Informazioni",
            0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(70, 130, 180)));
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 40));
        infoPanel.add(userTextArea, BorderLayout.CENTER);
        
        contentPanel.add(infoPanel, BorderLayout.SOUTH);
        
        return contentPanel;
    }

    /**
     * Applica stile moderno ai pulsanti per essere coerente con la home.
     */
    private void setupModernButtonStyles() {
        // Configura il font dell'area di testo se presente
        if (userTextArea != null) {
            userTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            userTextArea.setLineWrap(true);
            userTextArea.setWrapStyleWord(true);
        }
        
        // Colori coordinati con la home
        if (creaTeam != null) {
            applyModernStyle(creaTeam, new Color(33, 150, 243)); // Blu
        }
        if (mostraInvitiButton != null) {
            applyModernStyle(mostraInvitiButton, new Color(76, 175, 80)); // Verde
        }
        if (scegliTeamButton != null) {
            applyModernStyle(scegliTeamButton, new Color(255, 152, 0)); // Arancione
        }
        if (gestisciTeamButton != null) {
            applyModernStyle(gestisciTeamButton, new Color(96, 125, 139)); // Grigio-blu
        }
        if (menuGiudiceButton != null) {
            applyModernStyle(menuGiudiceButton, new Color(244, 67, 54)); // Rosso
        }
    }

    /**
     * Applica lo stile moderno a un singolo pulsante.
     */
    private void applyModernStyle(JButton button, Color backgroundColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Dimensioni consistenti
        button.setPreferredSize(new Dimension(140, 35));
        
        // Effetti hover
        Color hoverColor = backgroundColor.brighter();
        Color pressedColor = backgroundColor.darker();
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(backgroundColor);
            }
            
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                button.setBackground(pressedColor);
            }
            
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }
        });
    }
}

