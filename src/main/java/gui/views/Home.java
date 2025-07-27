package gui.views;

import controller.HackathonController;
import gui.components.ModernButton;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import gui.dialogs.HackathonInfoDialog;
import java.util.List;

/**
 * Vista moderna per la schermata Home dell'applicazione.
 * Interfaccia completamente rinnovata con componenti moderni e des                if(hackathonController.getListaUtenti().isEmpty()) {
                    informationArea.append("Lista Utenti vuota.\n\n");
                    informationArea.append("Nessun utente è attualmente registrato nel sistema.");
                } else {
                    informationArea.append("LISTA UTENTI REGISTRATI:\n");
                    informationArea.append("-".repeat(40) + "\n\n");
                    
                    int count = 1;
                    for (Utente user : hackathonController.getListaUtenti()) {
                        informationArea.append(String.format("%d. %s\n", count++, user.getName()));
                    }
                    
                    informationArea.append("\n" + "-".repeat(40));
                    informationArea.append(String.format("\nTotale utenti: %d", hackathonController.getListaUtenti().size()));
                }
 * Segue lo stesso pattern di design utilizzato in ModernTeamView.
 */
public class Home {
    private JPanel mainPanel;
    private JPanel centralContentPanel; // Sostituisce informationArea
    private JScrollPane informationScrollPane;
    private ModernButton stampaUtentiBtn;
    private ModernButton stampaOrganizzatoriBtn;
    private ModernButton accediBtn;
    
    // Componenti per header moderno
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    
    // Lista interattiva per organizzatori (ora nella parte principale)
    private JList<String> organizzatoriList;
    private DefaultListModel<String> organizzatoriListModel;
    private JScrollPane organizzatoriScrollPane;
    
    public static JFrame frame;
    private final HackathonController hackathonController;

    /**
     * Metodo principale dell'applicazione.
     */
    public static void main(String[] args) {
        // Pulisce eventuali risorse precedenti
        cleanupPreviousResources();
        
        // Imposta look and feel moderno
        setupModernLookAndFeel();
        
        SwingUtilities.invokeLater(() -> {
            try {
                Home homeInstance = new Home();
                
                // Controllo di sicurezza per evitare il problema "contentpane cannot be set to null"
                if (homeInstance.mainPanel == null) {
                    System.err.println("Errore: mainPanel non inizializzato correttamente");
                    homeInstance.createModernComponents(); // Forza la creazione dei componenti
                }
                
                frame = new JFrame("Hackathon Management System");
                
                // Controllo di sicurezza prima di settare il content pane
                if (homeInstance.mainPanel != null) {
                    frame.setContentPane(homeInstance.mainPanel);
                } else {
                    System.err.println("Errore critico: impossibile inizializzare mainPanel");
                    return;
                }
                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1000, 750);
                frame.setResizable(true);
                frame.setLocationRelativeTo(null);
                
                // Icona personalizzata se disponibile
                try {
                    frame.setIconImage(Toolkit.getDefaultToolkit().getImage("resources/icon.png"));
                } catch (Exception e) {
                    // Ignora se l'icona non è disponibile
                }
                
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Errore durante l'inizializzazione dell'applicazione: " + e.getMessage());
                
                // Fallback: crea una finestra di errore semplice
                JFrame errorFrame = new JFrame("Errore");
                JLabel errorLabel = new JLabel("Errore durante l'avvio dell'applicazione. Prova a cancellare la cartella target.", SwingConstants.CENTER);
                errorFrame.add(errorLabel);
                errorFrame.setSize(400, 100);
                errorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                errorFrame.setLocationRelativeTo(null);
                errorFrame.setVisible(true);
            }
        });
    }
    
    private static void setupModernLookAndFeel() {
        try {
            // Prova prima Nimbus per un look più moderno
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            // Fallback - usa il default senza chiamate
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Pulisce eventuali risorse precedenti per evitare conflitti
     * quando l'applicazione viene riavviata senza cancellare target.
     */
    private static void cleanupPreviousResources() {
        try {
            // Forza la garbage collection
            System.gc();
            
            // Chiudi eventuali frame precedenti
            if (frame != null && frame.isDisplayable()) {
                frame.dispose();
                frame = null;
            }
            
            // Reset delle proprietà UI se necessario
            UIManager.getDefaults().clear();
            
        } catch (Exception e) {
            System.err.println("Avviso: errore durante la pulizia delle risorse precedenti: " + e.getMessage());
        }
    }

    /**
     * Costruttore della classe Home - Implementazione moderna.
     */
    public Home() {
        hackathonController = new HackathonController();
        
        try {
            createModernComponents();
            setupModernLayout();
            setupEventListeners();
            
            // Controllo finale che tutti i componenti siano stati inizializzati
            if (mainPanel == null) {
                throw new RuntimeException("Errore: mainPanel non è stato inizializzato correttamente");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Errore nell'inizializzazione di Home: " + e.getMessage());
            
            // Crea un panel di fallback minimo
            mainPanel = new JPanel(new BorderLayout());
            JLabel errorLabel = new JLabel("Errore durante l'inizializzazione", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Arial", Font.BOLD, 16));
            mainPanel.add(errorLabel, BorderLayout.CENTER);
        }
    }
    
    /**
     * Crea tutti i componenti con stile moderno.
     */
    private void createModernComponents() {
        try {
            // Panel principale con gradiente come ModernTeamView
            mainPanel = new JPanel(new BorderLayout(20, 20)) {
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
            mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
            
            // Componenti header
            createHeaderComponents();
            
            // Area informazioni con stile moderno
            createInformationArea();
            
            // Lista interattiva organizzatori
            createInteractiveOrganizersList();
            
            // Pulsanti moderni
            createModernButtons();
            
            // Verifica che tutti i componenti essenziali siano stati creati
            if (mainPanel == null) {
                throw new RuntimeException("mainPanel non inizializzato");
            }
            
        } catch (Exception e) {
            System.err.println("Errore in createModernComponents: " + e.getMessage());
            e.printStackTrace();
            
            // Crea un panel di emergenza semplice
            mainPanel = new JPanel(new BorderLayout());
            JLabel errorLabel = new JLabel("Errore nell'inizializzazione dei componenti", SwingConstants.CENTER);
            mainPanel.add(errorLabel, BorderLayout.CENTER);
        }
    }
    
    /**
     * Crea i componenti dell'header con stile coordinato.
     */
    private void createHeaderComponents() {
        titleLabel = new JLabel("Hackathon Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112)); // Midnight Blue
        
        subtitleLabel = new JLabel("Sistema di gestione eventi e competizioni tecnologiche", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(105, 105, 105)); // Dim Gray
    }
    
    /**
     * Crea l'area centrale con liste cliccabili invece della JTextArea.
     */
    private void createInformationArea() {
        // Panel container principale con gradiente
        centralContentPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(252, 252, 255); // Very light lavender
                Color color2 = new Color(248, 250, 255); // Light blue tint
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        
        // Header moderno per l'area informazioni
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Dashboard Sistema");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(70, 130, 180));
        
        JLabel statusLabel = new JLabel("Sistema Attivo");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(34, 139, 34)); // Forest Green
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(statusLabel);
        
        // Area centrale per il contenuto dinamico (inizialmente con messaggio di benvenuto)
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setOpaque(false);
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Messaggio di benvenuto iniziale
        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2>BENVENUTO NEL SISTEMA DI GESTIONE HACKATHON</h2>" +
                "<br>Seleziona una delle opzioni sottostanti per iniziare:<br><br>" +
                "- Visualizza Utenti: Lista degli utenti registrati<br>" +
                "- Visualizza Organizzatori: Lista interattiva degli organizzatori<br>" +
                "- Accedi: Entra nel sistema con le tue credenziali" +
                "</div></html>");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        welcomeLabel.setForeground(new Color(45, 45, 45));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        contentArea.add(welcomeLabel, BorderLayout.CENTER);
        
        // Footer con informazioni aggiuntive
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        footerPanel.setOpaque(false);
        
        JLabel versionLabel = new JLabel("v2.0 - Interfaccia Rivisitata");
        versionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        versionLabel.setForeground(new Color(128, 128, 128));
        
        footerPanel.add(versionLabel);
        
        // Assemblaggio container
        centralContentPanel.add(headerPanel, BorderLayout.NORTH);
        centralContentPanel.add(contentArea, BorderLayout.CENTER);
        centralContentPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Bordo moderno con ombra
        centralContentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 240), 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // ScrollPane finale che contiene tutto
        informationScrollPane = new JScrollPane(centralContentPanel);
        informationScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));
        informationScrollPane.setOpaque(false);
        informationScrollPane.getViewport().setOpaque(false);
        informationScrollPane.setPreferredSize(new Dimension(700, 350));
        informationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        informationScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    /**
     * Sostituisce il contenuto centrale con una lista cliccabile.
     */
    private void showUsersList() {
        // Rimuove il contenuto attuale
        centralContentPanel.removeAll();
        
        // Header
        JPanel headerPanel = createDashboardHeader();
        
        // Lista degli utenti
        DefaultListModel<Utente> userListModel = new DefaultListModel<>();
        JList<Utente> userList = new JList<>(userListModel);
        
        // Popola la lista
        for (Utente user : hackathonController.getListaUtenti()) {
            userListModel.addElement(user);
        }
        
        // Stile della lista
        userList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userList.setBackground(new Color(255, 255, 255));
        userList.setSelectionBackground(new Color(70, 130, 180, 80));
        userList.setSelectionForeground(new Color(25, 25, 112));
        userList.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        userList.setFixedCellHeight(35);
        
        // Renderer personalizzato per gli utenti
        userList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Utente) {
                    Utente user = (Utente) value;
                    setText(String.format("%d. %s", index + 1, user.getName()));
                }
                return this;
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(userList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Utenti Registrati (" + userListModel.size() + ")"));
        
        // Footer
        JPanel footerPanel = createDashboardFooter();
        
        // Assemblaggio
        centralContentPanel.add(headerPanel, BorderLayout.NORTH);
        centralContentPanel.add(listScrollPane, BorderLayout.CENTER);
        centralContentPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Refresh del componente
        centralContentPanel.revalidate();
        centralContentPanel.repaint();
    }
    
    /**
     * Mostra la lista degli organizzatori con funzionalità interattiva.
     */
    private void showOrganizersList() {
        // Rimuove il contenuto attuale
        centralContentPanel.removeAll();
        
        // Header
        JPanel headerPanel = createDashboardHeader();
        
        // Lista degli organizzatori
        DefaultListModel<Organizzatore> organizerListModel = new DefaultListModel<>();
        JList<Organizzatore> organizerList = new JList<>(organizerListModel);
        
        // Popola la lista
        for (Organizzatore org : hackathonController.getListaOrganizzatori()) {
            organizerListModel.addElement(org);
        }
        
        // Stile della lista
        organizerList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        organizerList.setBackground(new Color(255, 255, 255));
        organizerList.setSelectionBackground(new Color(70, 130, 180, 80));
        organizerList.setSelectionForeground(new Color(25, 25, 112));
        organizerList.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        organizerList.setFixedCellHeight(35);
        
        // Renderer personalizzato per gli organizzatori
        organizerList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Organizzatore) {
                    Organizzatore org = (Organizzatore) value;
                    setText(String.format("%d. %s", index + 1, org.getName()));
                }
                return this;
            }
        });
        
        // Listener per doppio click - mostra hackathon dell'organizzatore
        organizerList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Organizzatore selectedOrg = organizerList.getSelectedValue();
                    if (selectedOrg != null) {
                        showHackathonsForOrganizer(selectedOrg);
                    }
                }
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(organizerList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Organizzatori (Doppio click per vedere hackathon) - (" + organizerListModel.size() + ")"));
        
        // Footer
        JPanel footerPanel = createDashboardFooter();
        
        // Assemblaggio
        centralContentPanel.add(headerPanel, BorderLayout.NORTH);
        centralContentPanel.add(listScrollPane, BorderLayout.CENTER);
        centralContentPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Refresh del componente
        centralContentPanel.revalidate();
        centralContentPanel.repaint();
    }
    
    /**
     * Mostra le hackathon di un organizzatore specifico.
     */
    private void showHackathonsForOrganizer(Organizzatore organizzatore) {
        // Rimuove il contenuto attuale
        centralContentPanel.removeAll();
        
        // Header personalizzato
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Hackathon di " + organizzatore.getName());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(70, 130, 180));
        
        ModernButton backButton = ModernButton.createSecondaryButton("← Torna agli Organizzatori");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.setPreferredSize(new Dimension(200, 30)); // Imposta dimensione minima per evitare il taglio del testo
        backButton.addActionListener(e -> showOrganizersList());
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalStrut(20));
        headerPanel.add(backButton);
        
        // Lista delle hackathon
        List<Hackathon> hackathons = hackathonController.getHackathonDiOrganizzatore(organizzatore);
        DefaultListModel<Hackathon> hackathonListModel = new DefaultListModel<>();
        JList<Hackathon> hackathonList = new JList<>(hackathonListModel);
        
        // Popola la lista
        for (Hackathon hackathon : hackathons) {
            hackathonListModel.addElement(hackathon);
        }
        
        // Stile della lista
        hackathonList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hackathonList.setBackground(new Color(255, 255, 255));
        hackathonList.setSelectionBackground(new Color(70, 130, 180, 80));
        hackathonList.setSelectionForeground(new Color(25, 25, 112));
        hackathonList.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        hackathonList.setFixedCellHeight(40);
        
        // Renderer personalizzato per le hackathon
        hackathonList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Hackathon) {
                    Hackathon hackathon = (Hackathon) value;
                    setText(String.format("<html><b>%s</b><br><small>%s - %s</small></html>", 
                        hackathon.getTitoloIdentificativo(), 
                        hackathon.getSede(),
                        hackathon.getDataInizio().toLocalDate()));
                }
                return this;
            }
        });
        
        // Listener per doppio click - apre dialog con dettagli hackathon
        hackathonList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Hackathon selectedHackathon = hackathonList.getSelectedValue();
                    if (selectedHackathon != null) {
                        HackathonInfoDialog.mostraDialog(frame, selectedHackathon, hackathonController);
                    }
                }
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(hackathonList);
        String title = hackathons.isEmpty() ? "Nessuna hackathon trovata" : 
                      "Hackathon (" + hackathons.size() + ") - Doppio click per dettagli";
        listScrollPane.setBorder(BorderFactory.createTitledBorder(title));
        
        // Footer
        JPanel footerPanel = createDashboardFooter();
        
        // Assemblaggio
        centralContentPanel.add(headerPanel, BorderLayout.NORTH);
        centralContentPanel.add(listScrollPane, BorderLayout.CENTER);
        centralContentPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Refresh del componente
        centralContentPanel.revalidate();
        centralContentPanel.repaint();
    }
    
    /**
     * Crea l'header standard per la dashboard.
     */
    private JPanel createDashboardHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Dashboard Sistema");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(70, 130, 180));
        
        JLabel statusLabel = new JLabel("Sistema Attivo");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(34, 139, 34));
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(statusLabel);
        
        return headerPanel;
    }
    
    /**
     * Crea il footer standard per la dashboard.
     */
    private JPanel createDashboardFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        footerPanel.setOpaque(false);
        
        JLabel versionLabel = new JLabel("v2.0 - Interfaccia Rivisitata");
        versionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        versionLabel.setForeground(new Color(128, 128, 128));
        
        footerPanel.add(versionLabel);
        
        return footerPanel;
    }
    
    /**
     * Crea la lista interattiva degli organizzatori.
     */
    private void createInteractiveOrganizersList() {
        organizzatoriListModel = new DefaultListModel<>();
        organizzatoriList = new JList<>(organizzatoriListModel);
        
        // Stile moderno della lista
        organizzatoriList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        organizzatoriList.setBackground(new Color(255, 255, 255));
        organizzatoriList.setSelectionBackground(new Color(70, 130, 180, 80));
        organizzatoriList.setSelectionForeground(new Color(25, 25, 112));
        organizzatoriList.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        organizzatoriList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Container panel per la lista con header personalizzato
        JPanel listContainer = new JPanel(new BorderLayout(0, 8));
        listContainer.setOpaque(false);
        
        // Header moderno per la lista
        JLabel headerLabel = new JLabel("Organizzatori Interattivi");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerLabel.setForeground(new Color(70, 130, 180));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(240, 248, 255));
        
        JLabel instructionLabel = new JLabel("Doppio click per visualizzare hackathon");
        instructionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        instructionLabel.setForeground(new Color(105, 105, 105));
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 8, 12));
        
        // ScrollPane interno con bordo moderno
        JScrollPane innerScrollPane = new JScrollPane(organizzatoriList);
        innerScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        innerScrollPane.setBackground(new Color(255, 255, 255));
        innerScrollPane.getViewport().setBackground(new Color(255, 255, 255));
        
        // Assemblaggio del container
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(instructionLabel, BorderLayout.SOUTH);
        
        listContainer.add(headerPanel, BorderLayout.NORTH);
        listContainer.add(innerScrollPane, BorderLayout.CENTER);
        
        // Container principale con bordo
        listContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 1, true),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        // ScrollPane esterno (quello usato nel layout)
        organizzatoriScrollPane = new JScrollPane(listContainer);
        organizzatoriScrollPane.setBorder(null);
        organizzatoriScrollPane.setOpaque(false);
        organizzatoriScrollPane.getViewport().setOpaque(false);
        organizzatoriScrollPane.setPreferredSize(new Dimension(340, 270));
        organizzatoriScrollPane.setVisible(false); // Inizialmente nascosta
    }
    
    /**
     * Crea i pulsanti moderni usando ModernButton come in ModernTeamView.
     */
    private void createModernButtons() {
        // Pulsante Stampa Utenti - Stile Primary (Blu)
        stampaUtentiBtn = ModernButton.createPrimaryButton("Visualizza Utenti");
        stampaUtentiBtn.setPreferredSize(new Dimension(180, 50));
        stampaUtentiBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Pulsante Stampa Organizzatori - Stile Warning (Arancione)
        stampaOrganizzatoriBtn = ModernButton.createWarningButton("Visualizza Organizzatori");
        stampaOrganizzatoriBtn.setPreferredSize(new Dimension(200, 50));
        stampaOrganizzatoriBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Pulsante Accedi - Stile Success (Verde)
        accediBtn = ModernButton.createSuccessButton("Accedi al Sistema");
        accediBtn.setPreferredSize(new Dimension(180, 50));
        accediBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
    
    /**
     * Configura il layout moderno seguendo il pattern di ModernTeamView.
     */
    private void setupModernLayout() {
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Panel centrale
        JPanel centralPanel = createCentralPanel();
        
        // Panel pulsanti
        JPanel buttonPanel = createButtonPanel();
        
        // Assemblaggio finale
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centralPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Crea il panel header con stile moderno.
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        // Panel per i titoli
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 8, 8));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Crea il panel centrale con l'area informazioni e la lista organizzatori.
     */
    private JPanel createCentralPanel() {
        JPanel centralPanel = new JPanel(new BorderLayout(15, 15));
        centralPanel.setOpaque(false);
        centralPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Area informazioni al centro
        centralPanel.add(informationScrollPane, BorderLayout.CENTER);
        
        // Lista organizzatori a destra
        centralPanel.add(organizzatoriScrollPane, BorderLayout.EAST);
        
        return centralPanel;
    }
    
    /**
     * Crea il panel dei pulsanti con layout moderno.
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        buttonPanel.add(stampaUtentiBtn);
        buttonPanel.add(stampaOrganizzatoriBtn);
        buttonPanel.add(accediBtn);
        
        return buttonPanel;
    }
    
    /**
     * Configura i listener per gli eventi dei pulsanti.
     */
    private void setupEventListeners() {

        // Listener per il pulsante "Visualizza Utenti"
        stampaUtentiBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hackathonController.getListaUtenti().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, 
                        "Lista Utenti vuota.\nNessun utente è attualmente registrato nel sistema.", 
                        "Informazione", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showUsersList();
                }
            }
        });

        // Listener per il pulsante "Accedi"
        accediBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chiude la finestra corrente e avvia il sistema di login
                frame.setVisible(false);
                new Login(hackathonController, frame);
            }
        });

        // Listener per il pulsante "Visualizza Organizzatori"
        stampaOrganizzatoriBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hackathonController.getListaOrganizzatori().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, 
                        "Lista Organizzatori vuota.\nNessun organizzatore è attualmente registrato nel sistema.", 
                        "Informazione", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showOrganizersList();
                }
            }
        });
    }
}
