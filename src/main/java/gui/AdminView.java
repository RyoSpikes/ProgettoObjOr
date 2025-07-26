package gui;

import model.Hackathon;
import model.Organizzatore;
import model.Utente;
import controller.HackathonController;
import controller.TeamController;
import utilities.DynamicSearchHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.List;

/**
 * La classe AdminView rappresenta l'interfaccia grafica per l'amministratore/organizzatore.
 * Consente di creare e visualizzare gli hackathon associati a un organizzatore, 
 * oltre a invitare giudici per la valutazione dei progetti.
 */
public class AdminView {
    private JFrame frameAdminView; // Finestra principale della vista amministratore.
    private JPanel panelAdmin; // Pannello principale della vista amministratore.
    private JTextArea adminTextArea; // Area di testo per visualizzare informazioni.
    private JButton creaHackathonButton; // Pulsante per creare un nuovo hackathon.
    private JButton invitaGiudiceButton; // Pulsante per invitare giudici.
    private JTextArea textArea1; // Campo richiesto dal form binding.
    private JScrollPane scrollPane; // ScrollPane per l'area di testo delle hackathon.
    private JPanel hackathonContentPanel; // Pannello per la visualizzazione dinamica degli hackathon.

    /**
     * Costruttore della classe AdminView.
     * Inizializza l'interfaccia grafica e gestisce le azioni dei pulsanti.
     *
     * @param adminLogged L'organizzatore attualmente loggato
     * @param frameCalling Il frame chiamante che ha aperto questa vista
     * @param hackathonController Il controller per la gestione degli hackathon
     */
    public AdminView(Organizzatore adminLogged, JFrame frameCalling, HackathonController hackathonController) {
        frameAdminView = new JFrame("Pannello Organizzatore - " + adminLogged.getName() + " (I tuoi hackathon)");
        
        // Inizializza solo hackathonContentPanel che non è nel form
        hackathonContentPanel = new JPanel();
        hackathonContentPanel.setLayout(new BoxLayout(hackathonContentPanel, BoxLayout.Y_AXIS));
        
        frameAdminView.setContentPane(panelAdmin);

        // Listener per gestire la chiusura della finestra
        frameAdminView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameCalling.setVisible(true);
                frameAdminView.dispose();
            }
        });

        frameAdminView.setVisible(true);
        frameAdminView.setSize(800, 700);
        frameAdminView.setResizable(false);
        frameAdminView.setLocationRelativeTo(null);
        
        // Imposta tooltip per i pulsanti
        invitaGiudiceButton.setToolTipText("Invita un giudice per uno dei tuoi hackathon (solo organizzatore)");
        creaHackathonButton.setToolTipText("Crea un nuovo hackathon di cui sarai l'organizzatore");
        
        // Nascondi l'area di testo se non è necessaria e inizializza i pulsanti dinamici
        if (adminTextArea != null) {
            adminTextArea.setVisible(false);
        }
        
        // Carica e mostra le hackathon come pulsanti cliccabili
        loadHackathonAsButtons(adminLogged, hackathonController);

        // Listener per il pulsante "Crea Hackathon"
        creaHackathonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreaHackathonForm(adminLogged, frameAdminView, frameCalling, hackathonController);
                frameAdminView.setVisible(false);
                // Quando si torna alla AdminView, verrà creata una nuova istanza
                // che caricherà automaticamente tutti gli hackathon aggiornati dal database
            }
        });

        // Listener per il pulsante "Invita Giudice"
        invitaGiudiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Recupera tutti gli utenti dal database tramite il controller
                    List<Utente> tuttiUtenti = hackathonController.getTuttiUtenti();
                    
                    if (tuttiUtenti.isEmpty()) {
                        JOptionPane.showMessageDialog(frameAdminView, 
                            "Non ci sono utenti registrati nel sistema.", 
                            "Nessun utente", 
                            JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    
                    // Recupera gli hackathon dell'organizzatore
                    List<Hackathon> hackathonList = hackathonController.getHackathonDiOrganizzatore(adminLogged);
                    
                    if (hackathonList.isEmpty()) {
                        JOptionPane.showMessageDialog(frameAdminView, 
                            "Devi prima creare un hackathon per poter invitare giudici.", 
                            "Nessun hackathon", 
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    // Crea array per la selezione dell'hackathon
                    String[] hackathonTitoli = new String[hackathonList.size()];
                    for (int i = 0; i < hackathonList.size(); i++) {
                        hackathonTitoli[i] = hackathonList.get(i).getTitoloIdentificativo();
                    }
                    
                    // Mostra dialog personalizzato per la selezione dell'hackathon con ricerca dinamica
                    Hackathon hackathonSelezionato = mostraDialogSelezioneHackathon(hackathonList, frameAdminView);
                    
                    if (hackathonSelezionato == null) {
                        return; // Utente ha cancellato
                    }
                    
                    // Crea e mostra dialog personalizzato per la selezione utente con ricerca dinamica
                    Utente utenteSelezionato = mostraDialogSelezioneUtente(tuttiUtenti, frameAdminView);
                    
                    if (utenteSelezionato == null) {
                        return; // Utente ha cancellato o non selezionato
                    }
                    
                    // Invia l'invito tramite il controller
                    boolean invitato = hackathonController.invitaGiudice(
                        hackathonSelezionato.getTitoloIdentificativo(), 
                        utenteSelezionato.getName(), 
                        adminLogged
                    );
                        
                    if (invitato) {
                        JOptionPane.showMessageDialog(frameAdminView, 
                            "Invito inviato con successo a " + utenteSelezionato.getName() + 
                            " per l'hackathon \"" + hackathonSelezionato.getTitoloIdentificativo() + "\"!", 
                            "Invito inviato", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frameAdminView, 
                            "Errore nell'invio dell'invito. L'utente potrebbe essere già stato invitato.", 
                            "Errore invito", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frameAdminView, 
                        "Errore nel recupero degli utenti: " + ex.getMessage(), 
                        "Errore", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    /**
     * Carica e visualizza le hackathon dell'organizzatore nella textArea1.
     * 
     * @param adminLogged L'organizzatore di cui visualizzare gli hackathon
     * @param hackathonController Il controller per recuperare i dati dal database
     */
    private void loadHackathonInTextArea(Organizzatore adminLogged, HackathonController hackathonController) {
        if (textArea1 == null) {
            System.out.println("ERRORE: textArea1 è null, impossibile visualizzare le hackathon");
            return;
        }
        
        // Recupera gli hackathon dell'organizzatore
        List<Hackathon> hackathonList = hackathonController.getHackathonDiOrganizzatore(adminLogged);
        
        StringBuilder hackathonText = new StringBuilder();
        
        if (hackathonList.isEmpty()) {
            hackathonText.append("═══════════════════════════════════════════════════════════════\n");
            hackathonText.append("                    PANNELLO ORGANIZZATORE\n");
            hackathonText.append("═══════════════════════════════════════════════════════════════\n\n");
            hackathonText.append("Benvenuto ").append(adminLogged.getName()).append("!\n\n");
            hackathonText.append("📋 Non hai ancora creato nessun hackathon.\n\n");
            hackathonText.append("💡 Utilizza il pulsante 'Crea Hackathon' per organizzare\n");
            hackathonText.append("   il tuo primo evento!\n\n");
            hackathonText.append("🔧 Funzionalità disponibili:\n");
            hackathonText.append("   • Crea Hackathon: Organizza un nuovo evento\n");
            hackathonText.append("   • Invita Giudice: Invita esperti per valutare i progetti\n");
        } else {
            hackathonText.append("═══════════════════════════════════════════════════════════════\n");
            hackathonText.append("                    I TUOI HACKATHON\n");
            hackathonText.append("                Organizzatore: ").append(adminLogged.getName()).append("\n");
            hackathonText.append("═══════════════════════════════════════════════════════════════\n\n");
            hackathonText.append("📊 Totale hackathon organizzati: ").append(hackathonList.size()).append("\n\n");
            
            for (int i = 0; i < hackathonList.size(); i++) {
                Hackathon hackathon = hackathonList.get(i);
                
                hackathonText.append("🏆 HACKATHON #").append(i + 1).append("\n");
                hackathonText.append("───────────────────────────────────────────────────────────────\n");
                hackathonText.append("📌 Titolo: ").append(hackathon.getTitoloIdentificativo()).append("\n");
                hackathonText.append("📝 Descrizione: ").append(hackathon.getDescrizioneProblema()).append("\n");
                hackathonText.append("📅 Periodo evento: ").append(hackathon.getDataInizio())
                             .append(" → ").append(hackathon.getDataFine()).append("\n");
                hackathonText.append("📍 Sede: ").append(hackathon.getSede()).append("\n");
                hackathonText.append("👥 Max partecipanti: ").append(hackathon.getMaxNumIscritti()).append("\n");
                hackathonText.append("� Organizzatore: ").append(hackathon.getOrganizzatore()).append("\n");
                
                if (i < hackathonList.size() - 1) {
                    hackathonText.append("\n");
                }
            }
            
            hackathonText.append("\n═══════════════════════════════════════════════════════════════\n");
            hackathonText.append("💡 Utilizza 'Invita Giudice' per aggiungere valutatori ai tuoi eventi\n");
            hackathonText.append("🔄 Le informazioni vengono aggiornate automaticamente\n");
        }
        
        // Imposta il testo nella textArea1
        textArea1.setText(hackathonText.toString());
        textArea1.setEditable(false);
        textArea1.setCaretPosition(0); // Torna all'inizio del testo
        
        // Se il JScrollPane è disponibile, assicurati che venga mostrato dall'inizio
        if (scrollPane != null) {
            SwingUtilities.invokeLater(() -> {
                scrollPane.getVerticalScrollBar().setValue(0);
                scrollPane.getHorizontalScrollBar().setValue(0);
        });
        }
        
        System.out.println("DEBUG: Caricate " + hackathonList.size() + " hackathon in textArea1");
    }
    
    /**
     * Mostra un dialog per la selezione di un utente con ricerca dinamica.
     * 
     * @param utenti Lista di tutti gli utenti disponibili
     * @param parentFrame Frame genitore per il dialog
     * @return L'utente selezionato, null se cancellato
     */
    private Utente mostraDialogSelezioneUtente(List<Utente> utenti, JFrame parentFrame) {
        // Crea il dialog modale
        JDialog dialog = new JDialog(parentFrame, "Seleziona Utente da Invitare", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(parentFrame);
        
        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Pannello per la ricerca
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Ricerca Utente"));
        searchPanel.add(new JLabel("Cerca Nome:"), BorderLayout.WEST);
        JTextField nomeUtenteField = new JTextField(25);
        nomeUtenteField.setToolTipText("Digita per cercare utenti in tempo reale");
        searchPanel.add(nomeUtenteField, BorderLayout.CENTER);
        
        // Pannello centrale per la lista degli utenti
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Utenti Disponibili"));
        
        // Lista e modello
        DefaultListModel<Utente> listModel = new DefaultListModel<>();
        JList<Utente> utenteList = new JList<>(listModel);
        utenteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Renderer personalizzato per mostrare solo il nome
        utenteList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Utente) {
                    Utente utente = (Utente) value;
                    setText(utente.getName());
                }
                return this;
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(utenteList);
        listScrollPane.setPreferredSize(new Dimension(400, 250));
        centerPanel.add(listScrollPane, BorderLayout.CENTER);
        
        // Label informativa
        JLabel infoLabel = new JLabel("Seleziona un utente dalla lista");
        infoLabel.setOpaque(true);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        centerPanel.add(infoLabel, BorderLayout.SOUTH);
        
        // Pannello inferiore per i pulsanti
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton btnSeleziona = new JButton("Seleziona Utente");
        btnSeleziona.setEnabled(false);
        JButton btnAnnulla = new JButton("Annulla");
        bottomPanel.add(btnSeleziona);
        bottomPanel.add(btnAnnulla);
        
        // Variabile per memorizzare l'utente selezionato
        final Utente[] utenteSelezionato = {null};
        
        // Layout
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        dialog.setContentPane(mainPanel);
        
        // Inizializza l'helper per la ricerca dinamica
        DynamicSearchHelper<Utente> searchHelper = new DynamicSearchHelper<>(
            nomeUtenteField,
            utenteList,
            listModel,
            infoLabel,
            utenti,
            utente -> utente.getName(), // Estrae il nome per la ricerca
            () -> {
                // Callback per quando la selezione cambia
                Utente selectedUtente = utenteList.getSelectedValue();
                if (selectedUtente != null) {
                    infoLabel.setText("Utente selezionato: " + selectedUtente.getName());
                    btnSeleziona.setEnabled(true);
                } else {
                    infoLabel.setText("Seleziona un utente dalla lista");
                    btnSeleziona.setEnabled(false);
                }
            }
        );
        
        // Listener per il pulsante "Seleziona Utente"
        btnSeleziona.addActionListener(e -> {
            utenteSelezionato[0] = searchHelper.getSelectedItem();
            dialog.dispose();
        });
        
        // Listener per il pulsante "Annulla"
        btnAnnulla.addActionListener(e -> dialog.dispose());
        
        // Mostra il dialog
        dialog.setVisible(true);
        
        return utenteSelezionato[0];
    }
    
    /**
     * Carica e visualizza le hackathon dell'organizzatore come pulsanti cliccabili.
     * 
     * @param adminLogged L'organizzatore di cui visualizzare gli hackathon
     * @param hackathonController Il controller per recuperare i dati dal database
     */
    private void loadHackathonAsButtons(Organizzatore adminLogged, HackathonController hackathonController) {
        if (textArea1 == null) {
            System.out.println("ERRORE: textArea1 è null, impossibile visualizzare le hackathon");
            return;
        }
        
        // Recupera gli hackathon dell'organizzatore
        List<Hackathon> hackathonList = hackathonController.getHackathonDiOrganizzatore(adminLogged);
        
        // Pulisce l'area di testo e la nasconde
        textArea1.setText("");
        textArea1.setVisible(false);
        
        // Crea un pannello scorrevole per i pulsanti degli hackathon
        JPanel hackathonButtonsPanel = new JPanel();
        hackathonButtonsPanel.setLayout(new BoxLayout(hackathonButtonsPanel, BoxLayout.Y_AXIS));
        hackathonButtonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        if (hackathonList.isEmpty()) {
            // Messaggio quando non ci sono hackathon
            JLabel noHackathonLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2>🏆 Benvenuto " + adminLogged.getName() + "!</h2>" +
                "<p>📋 Non hai ancora creato nessun hackathon.</p>" +
                "<p>💡 Utilizza il pulsante 'Crea Hackathon' per organizzare il tuo primo evento!</p>" +
                "</div></html>");
            noHackathonLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noHackathonLabel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
            hackathonButtonsPanel.add(noHackathonLabel);
        } else {
            // Titolo della sezione
            JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2>🏆 I TUOI HACKATHON</h2>" +
                "<p>Organizzatore: <b>" + adminLogged.getName() + "</b> | " +
                "Totale eventi: <b>" + hackathonList.size() + "</b></p>" +
                "</div></html>");
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
            hackathonButtonsPanel.add(titleLabel);
            
            // Crea un pulsante per ogni hackathon
            for (int i = 0; i < hackathonList.size(); i++) {
                Hackathon hackathon = hackathonList.get(i);
                
                // Crea il pulsante per l'hackathon con design pulito
                JButton hackathonButton = new JButton();
                hackathonButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                hackathonButton.setPreferredSize(new Dimension(600, 50));
                hackathonButton.setHorizontalAlignment(SwingConstants.LEFT);
                
                // Contenuto del pulsante - design minimalista
                String buttonText = "<html><div style='padding: 6px;'>" +
                    "<b>" + hackathon.getTitoloIdentificativo() + "</b><br>" +
                    "<small style='color: #666666;'>" + hackathon.getDataInizio() + " - " + hackathon.getDataFine() + 
                    " | " + hackathon.getSede() + "</small>" +
                    "</div></html>";
                
                hackathonButton.setText(buttonText);
                hackathonButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
                
                // Design pulito senza bordi complessi
                hackathonButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                hackathonButton.setBackground(Color.WHITE);
                hackathonButton.setOpaque(true);
                hackathonButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                
                // Rimuove il focus border (quadrato nero)
                hackathonButton.setFocusPainted(false);
                hackathonButton.setBorderPainted(false);
                
                // Colori per gli stati hover/click
                final Color defaultColor = Color.WHITE;
                final Color hoverColor = new Color(240, 240, 240);
                final Color clickColor = new Color(220, 220, 220);
                
                // Effetti hover e click
                hackathonButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hackathonButton.setBackground(hoverColor);
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hackathonButton.setBackground(defaultColor);
                    }
                    
                    @Override
                    public void mousePressed(MouseEvent e) {
                        hackathonButton.setBackground(clickColor);
                    }
                    
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        hackathonButton.setBackground(hoverColor);
                    }
                });
                
                // Tooltip semplificato
                hackathonButton.setToolTipText("Clicca per vedere i dettagli di " + hackathon.getTitoloIdentificativo());
                
                // Listener per aprire il dialog dei dettagli
                final Hackathon currentHackathon = hackathon; // Per il closure
                hackathonButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Apri il dialog con i dettagli dell'hackathon
                        TeamController teamController = new TeamController();
                        HackathonInfoDialog.mostraDialog(frameAdminView, currentHackathon, 
                                                       hackathonController, teamController);
                    }
                });
                
                hackathonButtonsPanel.add(hackathonButton);
                
                // Aggiungi uno spazio sottile tra i pulsanti
                if (i < hackathonList.size() - 1) {
                    hackathonButtonsPanel.add(Box.createVerticalStrut(2));
                }
            }
        }
        
        // Crea uno scroll pane per i pulsanti
        JScrollPane scrollPane = new JScrollPane(hackathonButtonsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Fix per lo scroll con la rotella del mouse sui pulsanti
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        hackathonButtonsPanel.addMouseWheelListener(e -> {
            scrollPane.dispatchEvent(e);
        });
        
        // Sostituisce il contenuto del pannello che contiene la textArea1
        Container parent = textArea1.getParent();
        if (parent != null) {
            parent.removeAll();
            parent.add(scrollPane);
            parent.revalidate();
            parent.repaint();
        }
        
        System.out.println("DEBUG: Creati " + hackathonList.size() + " pulsanti per gli hackathon");
    }

    /**
     * Mostra un dialog personalizzato per la selezione di un hackathon con ricerca dinamica.
     * 
     * @param hackathonList Lista degli hackathon tra cui scegliere
     * @param parentFrame Frame padre per il dialog
     * @return L'hackathon selezionato o null se l'operazione è stata cancellata
     */
    private Hackathon mostraDialogSelezioneHackathon(List<Hackathon> hackathonList, JFrame parentFrame) {
        // Variabile per memorizzare la selezione
        final Hackathon[] hackathonSelezionato = new Hackathon[1];
        hackathonSelezionato[0] = null;
        
        // Crea il dialog
        JDialog dialog = new JDialog(parentFrame, "Seleziona Hackathon - Solo i tuoi eventi", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(600, 450);
        dialog.setLocationRelativeTo(parentFrame);
        
        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Pannello superiore con istruzioni
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel infoLabel = new JLabel("<html><b>Seleziona uno dei TUOI hackathon per cui invitare il giudice:</b><br>" +
                                    "(Puoi invitare giudici solo per gli hackathon di cui sei organizzatore)</html>");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topPanel.add(infoLabel, BorderLayout.NORTH);
        
        // Pannello per la ricerca
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Ricerca Hackathon"));
        searchPanel.add(new JLabel("Cerca per titolo:"), BorderLayout.WEST);
        JTextField searchField = new JTextField(20);
        searchField.setToolTipText("Digita per cercare hackathon in tempo reale");
        searchPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Lista degli hackathon
        DefaultListModel<Hackathon> listModel = new DefaultListModel<>();
        JList<Hackathon> hackathonListComponent = new JList<>(listModel);
        hackathonListComponent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Renderer personalizzato per mostrare le informazioni dell'hackathon
        hackathonListComponent.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value instanceof Hackathon) {
                    Hackathon hackathon = (Hackathon) value;
                    setText(String.format("<html><b>%s</b><br>Inizio: %s - Fine: %s<br>Partecipanti: %d</html>",
                        hackathon.getTitoloIdentificativo(),
                        hackathon.getDataInizio(),
                        hackathon.getDataFine(),
                        hackathon.getNumIscritti()));
                }
                
                return this;
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(hackathonListComponent);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Hackathon Disponibili"));
        mainPanel.add(listScrollPane, BorderLayout.CENTER);
        
        // Pannello informazioni selezione
        JLabel selectionInfoLabel = new JLabel("Seleziona un hackathon dalla lista");
        selectionInfoLabel.setBorder(BorderFactory.createTitledBorder("Hackathon Selezionato"));
        selectionInfoLabel.setOpaque(true);
        selectionInfoLabel.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(selectionInfoLabel, BorderLayout.SOUTH);
        
        // Pannello pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSeleziona = new JButton("Seleziona");
        JButton btnAnnulla = new JButton("Annulla");
        
        btnSeleziona.setEnabled(false); // Inizialmente disabilitato
        
        buttonPanel.add(btnSeleziona);
        buttonPanel.add(btnAnnulla);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Crea l'helper per la ricerca dinamica
        DynamicSearchHelper<Hackathon> searchHelper = new DynamicSearchHelper<>(
            searchField,
            hackathonListComponent,
            listModel,
            selectionInfoLabel,
            hackathonList,
            hackathon -> hackathon.getTitoloIdentificativo(), // Estrae il titolo per la ricerca
            () -> {
                // Callback per quando la selezione cambia
                Hackathon selected = hackathonListComponent.getSelectedValue();
                if (selected != null) {
                    selectionInfoLabel.setText(String.format(
                        "<html><b>Hackathon:</b> %s<br><b>Date:</b> %s - %s<br><b>Partecipanti:</b> %d</html>",
                        selected.getTitoloIdentificativo(),
                        selected.getDataInizio(),
                        selected.getDataFine(),
                        selected.getNumIscritti()
                    ));
                    btnSeleziona.setEnabled(true);
                } else {
                    selectionInfoLabel.setText("Seleziona un hackathon dalla lista");
                    btnSeleziona.setEnabled(false);
                }
            }
        );
        
        // Listener per i pulsanti
        btnSeleziona.addActionListener(e -> {
            hackathonSelezionato[0] = hackathonListComponent.getSelectedValue();
            dialog.dispose();
        });
        
        btnAnnulla.addActionListener(e -> dialog.dispose());
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
        
        return hackathonSelezionato[0];
    }
}
