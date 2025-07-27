package gui.views;

import model.Hackathon;
import model.Organizzatore;
import model.Utente;
import gui.views.CreaHackathonForm;
import gui.dialogs.HackathonInfoDialog;
import controller.HackathonController;
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
    private JButton creaHackathonButton; // Pulsante per creare un nuovo hackathon.
    private JButton invitaGiudiceButton; // Pulsante per invitare giudici.
    private JPanel hackathonContentPanel; // Pannello per la visualizzazione dinamica degli hackathon.
    
    // Timer per gestire il tooltip con ritardo personalizzato
    private Timer tooltipTimer;
    private JComponent currentTooltipComponent;

    /**
     * Costruttore della classe AdminView.
     * Inizializza l'interfaccia grafica e gestisce le azioni dei pulsanti.
     *
     * @param adminLogged L'organizzatore attualmente loggato
     * @param frameCalling Il frame chiamante che ha aperto questa vista
     * @param hackathonController Il controller per la gestione degli hackathon
     */
    public AdminView(Organizzatore adminLogged, JFrame frameCalling, HackathonController hackathonController) {
        frameAdminView = new JFrame("Pannello Organizzatore - " + adminLogged.getName());
        
        // Crea l'interfaccia moderna
        createModernInterface(adminLogged, hackathonController);
        setupListeners(adminLogged, frameCalling, hackathonController);
        
        // Listener per gestire la chiusura della finestra
        frameAdminView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanup();
                frameCalling.setVisible(true);
                frameAdminView.dispose();
            }
        });

        frameAdminView.setSize(800, 700);
        frameAdminView.setLocationRelativeTo(null);
        frameAdminView.setVisible(true);
    }
    
    /**
     * Crea l'interfaccia moderna per l'AdminView.
     */
    private void createModernInterface(Organizzatore adminLogged, HackathonController hackathonController) {
        // Panel principale con gradiente
        JPanel modernMainPanel = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(240, 248, 255); // Alice Blue
                Color color2 = new Color(225, 245, 254); // Light Sky Blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        
        // Header moderno
        JPanel headerPanel = createModernHeader(adminLogged);
        modernMainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel
        JPanel contentPanel = createAdminContentPanel(adminLogged, hackathonController);
        modernMainPanel.add(contentPanel, BorderLayout.CENTER);
        
        frameAdminView.setContentPane(modernMainPanel);
    }
    
    /**
     * Crea l'header moderno per l'AdminView.
     */
    private JPanel createModernHeader(Organizzatore adminLogged) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        // Titolo principale
        JLabel titleLabel = new JLabel("Pannello Organizzatore");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(30, 58, 138));
        
        // Sottotitolo con nome organizzatore
        JLabel subtitleLabel = new JLabel("Benvenuto, " + adminLogged.getName());
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(75, 85, 99));
        
        // Panel per i testi
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        headerPanel.add(textPanel, BorderLayout.WEST);
        
        // Separatore
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        headerPanel.add(separator, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    /**
     * Crea il content panel per l'AdminView.
     */
    private JPanel createAdminContentPanel(Organizzatore adminLogged, HackathonController hackathonController) {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 25, 25));
        
        // Panel per i pulsanti azioni
        JPanel actionPanel = createActionButtonsPanel();
        contentPanel.add(actionPanel, BorderLayout.NORTH);
        
        // Panel per la lista hackathon
        JPanel hackathonListPanel = createHackathonListPanel(adminLogged, hackathonController);
        contentPanel.add(hackathonListPanel, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    /**
     * Crea il panel con i pulsanti delle azioni principali.
     */
    private JPanel createActionButtonsPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setOpaque(false);
        
        // Crea Hackathon Button
        creaHackathonButton = new JButton("Crea Hackathon");
        creaHackathonButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        creaHackathonButton.setPreferredSize(new Dimension(150, 40));
        applyModernButtonStyle(creaHackathonButton, "primary");
        creaHackathonButton.setToolTipText("Crea un nuovo hackathon di cui sarai l'organizzatore");
        
        // Invita Giudice Button
        invitaGiudiceButton = new JButton("Invita Giudice");
        invitaGiudiceButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        invitaGiudiceButton.setPreferredSize(new Dimension(150, 40));
        applyModernButtonStyle(invitaGiudiceButton, "success");
        invitaGiudiceButton.setToolTipText("Invita un giudice per uno dei tuoi hackathon");
        
        actionPanel.add(creaHackathonButton);
        actionPanel.add(invitaGiudiceButton);
        
        return actionPanel;
    }
    
    /**
     * Crea il panel per la lista degli hackathon.
     */
    private JPanel createHackathonListPanel(Organizzatore adminLogged, HackathonController hackathonController) {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setOpaque(false);
        
        // Titolo sezione
        JLabel titleLabel = new JLabel("I tuoi Hackathon:");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 58, 138));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        listPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Scroll panel per la lista
        hackathonContentPanel = new JPanel();
        hackathonContentPanel.setLayout(new BoxLayout(hackathonContentPanel, BoxLayout.Y_AXIS));
        hackathonContentPanel.setBackground(Color.WHITE);
        hackathonContentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JScrollPane scrollPane = new JScrollPane(hackathonContentPanel);
        scrollPane.setPreferredSize(new Dimension(740, 400));
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        listPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Carica gli hackathon
        loadHackathonList(adminLogged, hackathonController);
        
        return listPanel;
    }
    
    /**
     * Configura i listener dei pulsanti.
     */
    private void setupListeners(Organizzatore adminLogged, JFrame frameCalling, HackathonController hackathonController) {
        // Listener per il pulsante "Crea Hackathon"
        creaHackathonButton.addActionListener(e -> {
            frameAdminView.setVisible(false);
            new CreaHackathonForm(adminLogged, frameAdminView, frameCalling, hackathonController);
        });
        
        // Listener per il pulsante "Invita Giudice"
        invitaGiudiceButton.addActionListener(e -> invitaGiudice(adminLogged, hackathonController));
    }
    
    /**
     * Carica la lista degli hackathon dell'organizzatore.
     */
    private void loadHackathonList(Organizzatore adminLogged, HackathonController hackathonController) {
        hackathonContentPanel.removeAll();
        
        try {
            List<Hackathon> hackathons = hackathonController.getHackathonDiOrganizzatore(adminLogged);
            
            if (hackathons.isEmpty()) {
                JLabel noHackathonLabel = new JLabel("Nessun hackathon trovato. Crea il tuo primo hackathon!");
                noHackathonLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                noHackathonLabel.setForeground(new Color(107, 114, 128));
                noHackathonLabel.setHorizontalAlignment(SwingConstants.CENTER);
                hackathonContentPanel.add(Box.createVerticalGlue());
                hackathonContentPanel.add(noHackathonLabel);
                hackathonContentPanel.add(Box.createVerticalGlue());
            } else {
                for (Hackathon hackathon : hackathons) {
                    JPanel hackathonPanel = createHackathonCard(hackathon, hackathonController);
                    hackathonContentPanel.add(hackathonPanel);
                    hackathonContentPanel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Errore nel caricamento degli hackathon: " + e.getMessage());
            errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            errorLabel.setForeground(Color.RED);
            hackathonContentPanel.add(errorLabel);
        }
        
        hackathonContentPanel.revalidate();
        hackathonContentPanel.repaint();
    }
    
    /**
     * Crea una card per un hackathon.
     */
    private JPanel createHackathonCard(Hackathon hackathon, HackathonController hackathonController) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        // Informazioni hackathon
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        infoPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(hackathon.getTitoloIdentificativo());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(30, 58, 138));
        
        JLabel infoLabel = new JLabel(String.format("📍 %s • 📅 %s → %s", 
            hackathon.getSede(),
            hackathon.getDataInizio().toLocalDate(),
            hackathon.getDataFine().toLocalDate()));
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(107, 114, 128));
        
        infoPanel.add(titleLabel);
        infoPanel.add(infoLabel);
        
        // Pulsante dettagli
        JButton detailsButton = new JButton("Dettagli");
        detailsButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        detailsButton.setPreferredSize(new Dimension(80, 30));
        applyModernButtonStyle(detailsButton, "secondary");
        
        detailsButton.addActionListener(e -> {
            HackathonInfoDialog.mostraDialog(frameAdminView, hackathon, hackathonController);
        });
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(detailsButton, BorderLayout.EAST);
        
        // Effetto hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(248, 250, 252));
                card.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.repaint();
            }
        });
        
        return card;
    }
    
    /**
     * Metodo per invitare un giudice.
     */
    private void invitaGiudice(Organizzatore adminLogged, HackathonController hackathonController) {
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
        
        // Applica lo stile moderno ai pulsanti
        applyModernButtonStyle(btnSeleziona, "primary");
        applyModernButtonStyle(btnAnnulla, "secondary");
        
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
        if (hackathonContentPanel == null) {
            System.out.println("ERRORE: hackathonContentPanel è null, impossibile visualizzare le hackathon");
            return;
        }
        
        // Recupera gli hackathon dell'organizzatore
        List<Hackathon> hackathonList = hackathonController.getHackathonDiOrganizzatore(adminLogged);
        
        // Pulisce e nasconde il panel
        hackathonContentPanel.removeAll();
        hackathonContentPanel.setVisible(false);
        
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
                
                // Applica lo stile moderno
                applyModernButtonStyle(hackathonButton, "special");
                
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
                
                // Effetti hover e click con tooltip personalizzato
                hackathonButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hackathonButton.setBackground(hoverColor);
                        // Mostra il tooltip con ritardo personalizzato
                        showTooltipWithDelay(hackathonButton, "Clicca per vedere i dettagli di " + hackathon.getTitoloIdentificativo());
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hackathonButton.setBackground(defaultColor);
                        // Nasconde immediatamente il tooltip quando si esce
                        hideTooltipImmediately();
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
                
                // Non impostare il tooltip standard, usiamo quello personalizzato
                // hackathonButton.setToolTipText("Clicca per vedere i dettagli di " + hackathon.getTitoloIdentificativo());
                
                // Listener per aprire il dialog dei dettagli
                hackathonButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Apri il dialog con i dettagli dell'hackathon
                        HackathonInfoDialog.mostraDialog(frameAdminView, hackathon, 
                                                       hackathonController);
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
        
        // Sostituisce il contenuto del pannello hackathon
        // Metodo rimosso - non più necessario con la nuova interfaccia
        
        System.out.println("DEBUG: Metodo obsoleto - usare loadHackathonList");
    }

    /**
     * Nasconde immediatamente il tooltip corrente e ferma il timer.
     */
    private void hideTooltipImmediately() {
        if (tooltipTimer != null && tooltipTimer.isRunning()) {
            tooltipTimer.stop();
        }
        
        // Forza la chiusura del tooltip corrente
        ToolTipManager.sharedInstance().setEnabled(false);
        ToolTipManager.sharedInstance().setEnabled(true);
        
        currentTooltipComponent = null;
    }
    
    /**
     * Mostra il tooltip con un ritardo personalizzato.
     * 
     * @param component Il componente per cui mostrare il tooltip
     * @param tooltipText Il testo del tooltip
     */
    private void showTooltipWithDelay(JComponent component, String tooltipText) {
        // Ferma il timer precedente se in esecuzione
        if (tooltipTimer != null && tooltipTimer.isRunning()) {
            tooltipTimer.stop();
        }
        
        currentTooltipComponent = component;
        
        // Imposta immediatamente il testo del tooltip ma non lo mostrare ancora
        component.setToolTipText(tooltipText);
        
        // Crea un nuovo timer con ritardo di 800ms
        tooltipTimer = new Timer(800, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentTooltipComponent == component) {
                    // Mostra il tooltip solo se il mouse è ancora sullo stesso componente
                    Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                    SwingUtilities.convertPointFromScreen(mouseLocation, component);
                    
                    if (component.contains(mouseLocation)) {
                        // Forza la visualizzazione del tooltip simulando un movimento del mouse
                        ToolTipManager.sharedInstance().mouseMoved(
                            new MouseEvent(component, MouseEvent.MOUSE_MOVED, 
                                         System.currentTimeMillis(), 0, 
                                         mouseLocation.x, mouseLocation.y, 0, false));
                    }
                }
            }
        });
        
        tooltipTimer.setRepeats(false);
        tooltipTimer.start();
    }
    
    /**
     * Pulisce le risorse prima della chiusura della finestra.
     */
    private void cleanup() {
        if (tooltipTimer != null && tooltipTimer.isRunning()) {
            tooltipTimer.stop();
        }
        currentTooltipComponent = null;
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
        
        // Applica lo stile moderno ai pulsanti
        applyModernButtonStyle(btnSeleziona, "primary");
        applyModernButtonStyle(btnAnnulla, "secondary");
        
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
    
    /**
     * Applica lo stile ModernButton a un JButton esistente.
     * 
     * @param button Il JButton da modernizzare
     * @param type Il tipo di stile: "primary", "success", "danger", "warning", "secondary", "special"
     */
    private void applyModernButtonStyle(JButton button, String type) {
        // Rimuove il border di default
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
        // Font moderno
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Colori in base al tipo
        Color backgroundColor, textColor, hoverColor;
        switch (type.toLowerCase()) {
            case "primary":
                backgroundColor = new Color(0, 123, 255);
                textColor = Color.WHITE;
                hoverColor = new Color(0, 86, 179);
                break;
            case "success":
                backgroundColor = new Color(40, 167, 69);
                textColor = Color.WHITE;
                hoverColor = new Color(34, 142, 58);
                break;
            case "danger":
                backgroundColor = new Color(220, 53, 69);
                textColor = Color.WHITE;
                hoverColor = new Color(200, 35, 51);
                break;
            case "warning":
                backgroundColor = new Color(255, 193, 7);
                textColor = new Color(33, 37, 41);
                hoverColor = new Color(227, 172, 6);
                break;
            case "secondary":
                backgroundColor = new Color(108, 117, 125);
                textColor = Color.WHITE;
                hoverColor = new Color(90, 98, 104);
                break;
            case "special":
                backgroundColor = new Color(102, 16, 242);
                textColor = Color.WHITE;
                hoverColor = new Color(81, 13, 193);
                break;
            default:
                backgroundColor = new Color(0, 123, 255);
                textColor = Color.WHITE;
                hoverColor = new Color(0, 86, 179);
        }
        
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        
        // Padding e dimensioni
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        // Effetto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
}
