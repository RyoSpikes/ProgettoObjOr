package gui.dialogs;

import controller.HackathonController;
import gui.components.ModernButton;
import model.Team;
import model.Utente;
import utilities.DynamicSearchHelper;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialogo per mostrare e cercare team di un hackathon - Stile coerente con HackathonInfoDialog.
 */
public class TeamListDialog {
    
    /**
     * Mostra un dialog con i team dell'hackathon, identico allo stile dell'AdminView.
     */
    public static void showTeamListDialog(JFrame parent, String titoloHackathon, HackathonController hackathonController) {
        try {
            // Crea il dialog modale
            JDialog dialog = new JDialog(parent, "Team Partecipanti - " + titoloHackathon, true);
            dialog.setSize(650, 450);
            dialog.setLocationRelativeTo(parent);
            
            // Pannello principale con layout a schede (per coerenza con HackathonInfoDialog)
            JTabbedPane tabbedPane = new JTabbedPane();
            
            // === SCHEDA UNICA: TEAM PARTECIPANTI ===
            JPanel teamPanel = creaSchemaTeam(titoloHackathon, hackathonController, dialog);
            tabbedPane.addTab("Team Partecipanti", teamPanel);
            
            // Pannello inferiore con pulsante di chiusura
            JPanel bottomPanel = new JPanel(new FlowLayout());
            ModernButton btnChiudi = ModernButton.createSecondaryButton("Chiudi");
            btnChiudi.addActionListener(_ -> dialog.dispose());
            bottomPanel.add(btnChiudi);
            
            // Layout principale
            dialog.setLayout(new BorderLayout());
            dialog.add(tabbedPane, BorderLayout.CENTER);
            dialog.add(bottomPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            MessageDialog.showErrorMessage(parent, 
                "Errore nell'apertura della finestra team:\n" + e.getMessage());
        }
    }
    
    /**
     * Crea il pannello con i team partecipanti e ricerca dinamica - IDENTICO a HackathonInfoDialog.
     */
    private static JPanel creaSchemaTeam(String titoloHackathon, HackathonController hackathonController, 
                                        JDialog parentDialog) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Recupera i team partecipanti
        List<Team> teamList;
        try {
            teamList = hackathonController.getTeamsByHackathon(titoloHackathon);
        } catch (Exception e) {
            teamList = List.of();
        }
        
        // Pannello per la ricerca
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Cerca Team"));
        searchPanel.add(new JLabel("Nome Team:"), BorderLayout.WEST);
        JTextField searchField = new JTextField(20);
        searchField.setToolTipText("Digita per cercare team in tempo reale");
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        // Pannello centrale per la lista dei team
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Team Partecipanti (" + teamList.size() + ")"));
        
        // Lista e modello
        DefaultListModel<Team> listModel = new DefaultListModel<>();
        JList<Team> teamJList = new JList<>(listModel);
        teamJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Renderer personalizzato per i team - IDENTICO a HackathonInfoDialog
        teamJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Team) {
                    Team team = (Team) value;
                    // Ottieni il numero di membri direttamente dal database
                    int numMembri = hackathonController.getNumeroMembriTeam(team.getNomeTeam(), titoloHackathon);
                    
                    // Ottieni il punteggio finale
                    String punteggio;
                    if (team.getVotoFinale() == 0) {
                        punteggio = "Non assegnato";
                    } else {
                        punteggio = String.valueOf(team.getVotoFinale()) + " punti";
                    }
                    
                    setText(team.getNomeTeam() + " (" + numMembri + " membri) - Punteggio: " + punteggio);
                }
                return this;
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(teamJList);
        listScrollPane.setPreferredSize(new Dimension(300, 200));
        centerPanel.add(listScrollPane, BorderLayout.CENTER);
        
        // Label informativa
        JLabel infoLabel = new JLabel("Seleziona un team per vedere i dettagli");
        infoLabel.setOpaque(true);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        centerPanel.add(infoLabel, BorderLayout.SOUTH);
        
        // Pannello per i pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout());
        ModernButton btnDettagliTeam = ModernButton.createPrimaryButton("Dettagli Team");
        btnDettagliTeam.setEnabled(false);
        buttonPanel.add(btnDettagliTeam);
        
        // Inizializza l'helper per la ricerca dinamica - IDENTICO a HackathonInfoDialog
        DynamicSearchHelper<Team> searchHelper = new DynamicSearchHelper<>(
            searchField,
            teamJList,
            listModel,
            infoLabel,
            teamList,
            team -> team.getNomeTeam(), // Estrae il nome per la ricerca
            () -> {
                // Callback per quando la selezione cambia
                Team selectedTeam = teamJList.getSelectedValue();
                if (selectedTeam != null) {
                    int numMembri = hackathonController.getNumeroMembriTeam(selectedTeam.getNomeTeam(), titoloHackathon);
                    infoLabel.setText(String.format(
                        "Team selezionato: %s (%d membri)",
                        selectedTeam.getNomeTeam(),
                        numMembri
                    ));
                    btnDettagliTeam.setEnabled(true);
                } else {
                    infoLabel.setText("Seleziona un team per vedere i dettagli");
                    btnDettagliTeam.setEnabled(false);
                }
            }
        );
        
        // Listener per il pulsante dettagli team
        btnDettagliTeam.addActionListener(_ -> {
            Team selectedTeam = searchHelper.getSelectedItem();
            if (selectedTeam != null) {
                mostraDettagliTeam(parentDialog, selectedTeam, hackathonController);
            }
        });
        
        // Layout
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Mostra un dialog con i dettagli di un team specifico - IDENTICO a HackathonInfoDialog.
     */
    private static void mostraDettagliTeam(JDialog parentDialog, Team team, HackathonController hackathonController) {
        JDialog teamDialog = new JDialog(parentDialog, "Dettagli Team - " + team.getNomeTeam(), true);
        teamDialog.setSize(450, 350);
        teamDialog.setLocationRelativeTo(parentDialog);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Titolo
        JLabel titleLabel = new JLabel(team.getNomeTeam(), JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Informazioni del team
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        addInfoRow(infoPanel, gbc, 0, "Hackathon:", 
                  team.getHackathon() != null ? team.getHackathon().getTitoloIdentificativo() : "N/A");
        
        // Ottieni il numero di membri direttamente dal database
        int numMembri = hackathonController.getNumeroMembriTeam(team.getNomeTeam(), 
            team.getHackathon() != null ? team.getHackathon().getTitoloIdentificativo() : "");
        addInfoRow(infoPanel, gbc, 1, "Numero Membri:", String.valueOf(numMembri));
        
        // Aggiungi il punteggio finale
        String punteggio;
        if (team.getVotoFinale() == 0) {
            punteggio = "Non ancora assegnato";
        } else {
            punteggio = String.valueOf(team.getVotoFinale()) + " punti";
        }
        addInfoRow(infoPanel, gbc, 2, "Punteggio Finale:", punteggio);
        
        // Lista dei membri - caricata dal database
        JPanel membersPanel = new JPanel(new BorderLayout(5, 5));
        membersPanel.setBorder(BorderFactory.createTitledBorder("Membri del Team"));
        
        DefaultListModel<String> memberListModel = new DefaultListModel<>();
        try {
            // Carica i membri dal database usando il controller
            List<Utente> membri = hackathonController.getMembriTeam(
                team.getNomeTeam(), 
                team.getHackathon() != null ? team.getHackathon().getTitoloIdentificativo() : ""
            );
            
            for (Utente membro : membri) {
                memberListModel.addElement(membro.getName());
            }
        } catch (Exception e) {
            memberListModel.addElement("Errore nel caricamento dei membri");
        }
        
        JList<String> memberList = new JList<>(memberListModel);
        memberList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof String) {
                    setText(value.toString());
                }
                return this;
            }
        });
        
        JScrollPane memberScrollPane = new JScrollPane(memberList);
        memberScrollPane.setPreferredSize(new Dimension(350, 120));
        membersPanel.add(memberScrollPane, BorderLayout.CENTER);
        
        // Pulsante chiudi
        JPanel buttonPanel = new JPanel(new FlowLayout());
        ModernButton btnChiudi = ModernButton.createSecondaryButton("Chiudi");
        btnChiudi.addActionListener(_ -> teamDialog.dispose());
        buttonPanel.add(btnChiudi);
        
        // Pannello contenuto centrale che contiene info e membri
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.add(infoPanel, BorderLayout.NORTH);
        contentPanel.add(membersPanel, BorderLayout.CENTER);
        
        // Layout principale
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        teamDialog.add(mainPanel, BorderLayout.CENTER);
        
        teamDialog.setVisible(true);
    }
    
    /**
     * Metodo di utilità per aggiungere una riga di informazioni al pannello - IDENTICO a HackathonInfoDialog.
     */
    private static void addInfoRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        // Label
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(labelComponent, gbc);
        
        // Value
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(valueComponent, gbc);
    }
}
