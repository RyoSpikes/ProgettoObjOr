package gui.views;

import controller.HackathonController;
import gui.components.ModernButton;
import model.*;
import utilities.DynamicSearchHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

/**
 * La classe ScegliTeam rappresenta un'interfaccia grafica per consentire a un utente di cercare
 * e selezionare un team tramite ricerca dinamica con lista cliccabile.
 */
public class ScegliTeam {
    private JComboBox<Hackathon> hackathonComboBox; // ComboBox per selezionare un hackathon.
    private JTextField nomeTeamField; // Campo di testo per inserire il nome del team.
    private JList<Team> teamList; // Lista per mostrare i team trovati.
    private DefaultListModel<Team> listModel; // Modello per la lista dei team.
    private ModernButton btnInvio; // Pulsante per confermare la selezione del team.
    private JLabel infoLabel; // Label per mostrare informazioni sui risultati.
    private DynamicSearchHelper<Team> searchHelper; // Helper per la ricerca dinamica.

    /**
     * Costruttore della classe ScegliTeam.
     *
     * @param userLogged L'utente attualmente loggato.
     * @param frameCalling Il frame chiamante che ha aperto questa finestra.
     * @param hackathonController Il controller principale dell'applicazione.
     */
    public ScegliTeam(Utente userLogged, JFrame frameCalling, HackathonController hackathonController) {

        // Crea la finestra di dialogo personalizzata
        JDialog dialog = new JDialog(frameCalling, "Scegli Team", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(frameCalling);

        // Crea il pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Pannello superiore per la selezione dell'hackathon
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Seleziona Hackathon:"));
        hackathonComboBox = new JComboBox<>();
        topPanel.add(hackathonComboBox);

        // Pannello per la ricerca
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Ricerca Team"));
        searchPanel.add(new JLabel("Cerca Nome Team:"), BorderLayout.WEST);
        nomeTeamField = new JTextField(20);
        nomeTeamField.setToolTipText("Digita per cercare team in tempo reale");
        searchPanel.add(nomeTeamField, BorderLayout.CENTER);

        // Pannello centrale per la lista dei team
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Team Disponibili"));

        // Inizializza la lista e il modello
        listModel = new DefaultListModel<>();
        teamList = new JList<>(listModel);
        teamList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teamList.setEnabled(false);

        // Renderer personalizzato per la lista
        teamList.setCellRenderer(new TeamListCellRenderer(hackathonController, userLogged));

        // ScrollPane per la lista
        JScrollPane listScrollPane = new JScrollPane(teamList);
        listScrollPane.setPreferredSize(new Dimension(500, 200));
        centerPanel.add(listScrollPane, BorderLayout.CENTER);

        // Label informativa
        infoLabel = new JLabel("Seleziona un hackathon e digita per cercare");
        infoLabel.setOpaque(true);
        infoLabel.setBackground(Color.LIGHT_GRAY);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        centerPanel.add(infoLabel, BorderLayout.SOUTH);

        // Pannello inferiore per i pulsanti
        JPanel bottomPanel = new JPanel(new FlowLayout());
        btnInvio = ModernButton.createSuccessButton("Entra nel Team");
        btnInvio.setEnabled(false);
        ModernButton btnAnnulla = ModernButton.createSecondaryButton("Annulla");
        bottomPanel.add(btnInvio);
        bottomPanel.add(btnAnnulla);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Layout manager per il centro
        JPanel combinedCenter = new JPanel(new BorderLayout());
        combinedCenter.add(searchPanel, BorderLayout.NORTH);
        combinedCenter.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(combinedCenter, BorderLayout.CENTER);

        dialog.setContentPane(mainPanel);

        // Popola il ComboBox con la lista degli hackathon disponibili.
        for(Hackathon h : hackathonController.getListaHackathon()) {
            hackathonComboBox.addItem(h);
        }

        // Imposta il renderer per visualizzare i titoli degli hackathon nel ComboBox.
        hackathonComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Hackathon) {
                    setText(((Hackathon) value).getTitoloIdentificativo());
                }
                return this;
            }
        });

        // Inizializza l'helper per la ricerca dinamica (sarà configurato quando si seleziona un hackathon)
        searchHelper = null;

        // Listener per il cambio di hackathon
        hackathonComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            Hackathon hackathonSelezionato = (Hackathon) hackathonComboBox.getSelectedItem();
            
            System.out.println("DEBUG ScegliTeam: Cambiato hackathon a: " + 
                (hackathonSelezionato != null ? hackathonSelezionato.getTitoloIdentificativo() : "null"));
            
            if (hackathonSelezionato == null) {
                listModel.clear();
                teamList.setEnabled(false);
                btnInvio.setEnabled(false);
                infoLabel.setText("Seleziona un hackathon");
                infoLabel.setBackground(Color.LIGHT_GRAY);
                searchHelper = null;
                return;
            }

            // Ottieni tutti i team per l'hackathon selezionato
            List<Team> teamDisponibili = hackathonController.getTeamsByHackathon(hackathonSelezionato.getTitoloIdentificativo());
            
            System.out.println("DEBUG ScegliTeam: Trovati " + (teamDisponibili != null ? teamDisponibili.size() : 0) + " team per hackathon: " + hackathonSelezionato.getTitoloIdentificativo());
            if (teamDisponibili != null) {
                for (Team t : teamDisponibili) {
                    System.out.println("  - Team: " + t.getNomeTeam());
                }
            }
            
            if (teamDisponibili == null || teamDisponibili.isEmpty()) {
                listModel.clear();
                teamList.setEnabled(false);
                btnInvio.setEnabled(false);
                infoLabel.setText("Nessun team trovato per questo hackathon");
                infoLabel.setBackground(new Color(255, 200, 200)); // Rosso chiaro
                searchHelper = null;
                nomeTeamField.setText(""); // Reset campo ricerca
                return;
            }

            // Reset del campo di ricerca per evitare confusione
            nomeTeamField.setText("");
            
            // Inizializza sempre un nuovo helper per la ricerca dinamica
            searchHelper = new DynamicSearchHelper<>(
                nomeTeamField,
                teamList,
                listModel,
                infoLabel,
                teamDisponibili,
                team -> team.getNomeTeam(), // Estrae il nome per la ricerca
                () -> {
                    // Callback per quando la selezione cambia
                    Team teamSelezionato = teamList.getSelectedValue();
                    if (teamSelezionato != null) {
                        int numMembri = hackathonController.contaMembriTeam(
                            teamSelezionato.getNomeTeam(),
                            teamSelezionato.getHackathon().getTitoloIdentificativo()
                        );

                        // Controlla se l'utente può unirsi al team
                        boolean puo_unirsi = hackathonController.puoUtenterUnirsiAlTeam(teamSelezionato, userLogged);

                        infoLabel.setText(String.format(
                            "<html><b>Team selezionato:</b> %s<br><b>Membri attuali:</b> %d<br><b>Stato:</b> %s</html>",
                            teamSelezionato.getNomeTeam(),
                            numMembri,
                            puo_unirsi ? "Disponibile" : "Non disponibile"
                        ));

                        btnInvio.setEnabled(puo_unirsi);
                    } else {
                        btnInvio.setEnabled(false);
                    }
                }
            );
            }
        });

        // Listener per il pulsante "Entra nel Team"
        btnInvio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Team teamSelezionato = searchHelper != null ? searchHelper.getSelectedItem() : null;
                if (teamSelezionato == null) {
                    JOptionPane.showMessageDialog(dialog, "Seleziona un team dalla lista!",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Aggiunge l'utente al team utilizzando hackathonController
                    hackathonController.aggiungiUtenteATeam(userLogged, teamSelezionato);

                    // Notifica successo
                    JOptionPane.showMessageDialog(dialog,
                            "Sei stato aggiunto con successo al team '" + teamSelezionato.getNomeTeam() + "'!",
                            "Adesione completata", JOptionPane.INFORMATION_MESSAGE);

                    dialog.dispose(); // Chiude solo il dialog, non l'applicazione
                }
                catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Errore nell'adesione al team:\n" + ex.getMessage(),
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
                catch (IllegalStateException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Operazione non consentita:\n" + ex.getMessage(),
                            "Avviso", JOptionPane.WARNING_MESSAGE);
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Errore imprevisto:\n" + ex.getMessage(),
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        // Listener per il pulsante "Annulla"
        btnAnnulla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Chiude solo il dialog, non l'applicazione
            }
        });

        dialog.setVisible(true);
    }

}

/**
 * Renderer personalizzato per la lista dei team
 */
class TeamListCellRenderer extends DefaultListCellRenderer {
    private HackathonController hackathonController;
    private Utente utente;

    public TeamListCellRenderer(HackathonController hackathonController, Utente utente) {
        this.hackathonController = hackathonController;
        this.utente = utente;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Team) {
            Team team = (Team) value;

            int membri = hackathonController.contaMembriTeam(
                team.getNomeTeam(),
                team.getHackathon().getTitoloIdentificativo()
            );
            int maxMembri = team.getHackathon().getMaxMembriTeam();

            // Testo da mostrare
            setText(String.format("%s (%d/%d membri)",
                team.getNomeTeam(), membri, maxMembri));

            // Verifica lo stato del team usando il metodo sicuro del controller
            boolean puo_unirsi = hackathonController.puoUtenterUnirsiAlTeam(team, utente);
            String motivazione = "";

            // Verifica se il periodo di registrazione è ancora aperto
            LocalDate dataFineRegistrazione = team.getHackathon().getDataFineRegistrazioni().toLocalDate();
            if (LocalDate.now().isAfter(dataFineRegistrazione)) {
                puo_unirsi = false;
                motivazione = "Registrazioni chiuse";
            }

            // Verifica se l'utente è già in un team per questo hackathon
            String teamCorrente = hackathonController.getTeamCorrenteUtente(utente.getName(), team.getHackathon().getTitoloIdentificativo());

            if (teamCorrente != null) {
                puo_unirsi = false;
                if (teamCorrente.equals(team.getNomeTeam())) {
                    motivazione = "Sei già membro di questo team";
                    setForeground(isSelected ? Color.WHITE : new Color(0, 100, 0)); // Verde scuro
                } else {
                    motivazione = "Sei già in un altro team";
                }
            }

            // Verifica se il team è al completo
            if (membri >= maxMembri && puo_unirsi) {
                puo_unirsi = false;
                motivazione = "Team al completo";
            }

            // Imposta colori e tooltip
            if (!puo_unirsi) {
                if (!motivazione.equals("Sei già membro di questo team")) {
                    setForeground(isSelected ? Color.WHITE : Color.GRAY);
                }
                setToolTipText(motivazione);
            } else {
                setForeground(isSelected ? Color.WHITE : Color.BLACK);
                setToolTipText("Clicca per selezionare questo team");
            }
        }

        return this;
    }
}
