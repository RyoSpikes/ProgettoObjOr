package gui.views;

import model.Organizzatore;
import controller.HackathonController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.DateTimeException;
import java.util.function.Supplier;

/**
 * La classe CreaHackathonForm rappresenta un'interfaccia grafica per la creazione di un nuovo hackathon.
 * Consente all'organizzatore di inserire i dettagli dell'hackathon e di registrarlo.
 */
public class CreaHackathonForm {

    /**
     * Anno minimo adottato per le registrazioni.
     */
    private final int MIN_YEAR = 2025;

    /**
     * Anno massimo adottato per le registrazioni.
     */
    private final int MAX_YEAR = 2050;

    private JPanel panelCreaHackathon; // Pannello principale della finestra.
    private JTextField fieldSede; // Campo di testo per la sede dell'hackathon.
    private JComboBox<Integer> comboBoxGiornoInizioEvento; // ComboBox per il giorno di inizio dell'evento.
    private JComboBox<Integer> comboBoxMeseInizioEvento; // ComboBox per il mese di inizio dell'evento.
    private JComboBox<Integer> comboBoxAnnoInizioEvento; // ComboBox per l'anno di inizio dell'evento.
    private JButton btnCreaHackathon; // Pulsante per creare l'hackathon.
    private JComboBox<Integer> comboBoxGiornoFineEvento; // ComboBox per il giorno di fine dell'evento.
    private JComboBox<Integer> comboBoxMeseFineEvento; // ComboBox per il mese di fine dell'evento.
    private JComboBox<Integer> comboBoxAnnoFineEvento; // ComboBox per l'anno di fine dell'evento.
    private JComboBox<Integer> comboBoxGiornoInizioRegistrazioniEvento; // ComboBox per il giorno di inizio delle registrazioni.
    private JComboBox<Integer> comboBoxMeseInizioRegistrazioniEvento; // ComboBox per il mese di inizio delle registrazioni.
    private JComboBox<Integer> comboBoxAnnoInizioRegistrazioniEvento; // ComboBox per l'anno di inizio delle registrazioni.
    private JTextField fieldTitolo; // Campo di testo per il titolo dell'hackathon.
    private JTextArea textAreaDescrizioneProblema; // Area di testo per la descrizione del problema.
    private JScrollPane scrollPaneDescr; // ScrollPane per l'area di testo.
    
    // Spinner per i parametri numerici
    private JSpinner spinnerNumeroMassimoPartecipanti;
    private JSpinner spinnerNumeroMassimoPartecipantiTeam;

    /**
     * Costruttore della classe CreaHackathonForm.
     * Inizializza l'interfaccia grafica e gestisce le azioni degli elementi.
     *
     * @param adminLogged L'organizzatore attualmente loggato.
     * @param frameCalling Il frame chiamante che ha aperto questa finestra.
     * @param frameMain Il frame principale dell'applicazione.
     * @param hackathonController Il controller per la gestione degli hackathon.
     */
    public CreaHackathonForm(Organizzatore adminLogged, JFrame frameCalling, JFrame frameMain, HackathonController hackathonController) {
        
        // Crea l'interfaccia moderna
        createModernInterface();
        
        JFrame frame = new JFrame("Creazione Hackathon");
        frame.setContentPane(panelCreaHackathon);

        // Listener per gestire la chiusura della finestra.
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameCalling.setVisible(true);
                frame.dispose();
            }
        });

        frame.setVisible(true);
        frame.setSize(900, 700);
        frame.setMinimumSize(new Dimension(700, 500));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Popola le ComboBox con i valori predefiniti.
        populateComboBoxes();

        // Listener per il pulsante "Crea Hackathon"
        btnCreaHackathon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validazione dei campi obbligatori
                if (fieldTitolo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "⚠️ Il titolo dell'hackathon è obbligatorio.",
                        "Campo Mancante",
                        JOptionPane.WARNING_MESSAGE);
                    fieldTitolo.requestFocus();
                    return;
                }
                if (fieldSede.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "⚠️ La sede dell'hackathon è obbligatoria.",
                        "Campo Mancante",
                        JOptionPane.WARNING_MESSAGE);
                    fieldSede.requestFocus();
                    return;
                }
                if (textAreaDescrizioneProblema.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                        "⚠️ La descrizione del problema è obbligatoria.",
                        "Campo Mancante",
                        JOptionPane.WARNING_MESSAGE);
                    textAreaDescrizioneProblema.requestFocus();
                    return;
                }

                try {
                    // Costruzione delle date dal form
                    LocalDateTime dataInizioEvento = LocalDateTime.of(
                            (Integer) comboBoxAnnoInizioEvento.getSelectedItem(),
                            (Integer) comboBoxMeseInizioEvento.getSelectedItem(),
                            (Integer) comboBoxGiornoInizioEvento.getSelectedItem(),
                             0, 0, 0);

                    LocalDateTime dataFineEvento = LocalDateTime.of(
                            (Integer) comboBoxAnnoFineEvento.getSelectedItem(),
                            (Integer) comboBoxMeseFineEvento.getSelectedItem(),
                            (Integer) comboBoxGiornoFineEvento.getSelectedItem(),
                            23, 59, 59);

                    LocalDateTime dataInizioRegistrazione = LocalDateTime.of(
                            (Integer) comboBoxAnnoInizioRegistrazioniEvento.getSelectedItem(),
                            (Integer) comboBoxMeseInizioRegistrazioniEvento.getSelectedItem(),
                            (Integer) comboBoxGiornoInizioRegistrazioniEvento.getSelectedItem(),
                             0, 0, 0);

                    // La data di fine registrazione è automaticamente calcolata: 2 giorni prima dell'evento
                    LocalDateTime dataFineRegistrazione = dataInizioEvento.minusDays(2);

                    // 📅 VALIDAZIONE DELLE DATE LATO CLIENT
                    String erroreValidazione = validaDate(dataInizioRegistrazione, dataFineRegistrazione, dataInizioEvento, dataFineEvento);
                    if (erroreValidazione != null) {
                        JOptionPane.showMessageDialog(null,
                            erroreValidazione,
                            "❌ Date Non Valide",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Dati dal form
                    String titoloIdentificativo = fieldTitolo.getText().trim();
                    String sede = fieldSede.getText().trim();
                    String descrizioneProblema = textAreaDescrizioneProblema.getText().trim();
                    int maxNumMembriTeam = (Integer) spinnerNumeroMassimoPartecipantiTeam.getValue();
                    int maxNumIscritti = (Integer) spinnerNumeroMassimoPartecipanti.getValue();

                    // ✅ CREAZIONE HACKATHON NEL DATABASE
                    HackathonController.CreazioneHackathonRisultato risultato = hackathonController.creaHackathonSicuro(
                            titoloIdentificativo,
                            adminLogged,
                            sede,
                            dataInizioRegistrazione,
                            dataFineRegistrazione,
                            dataInizioEvento,
                            dataFineEvento,
                            descrizioneProblema,
                            maxNumIscritti,
                            maxNumMembriTeam
                    );

                    if (risultato == HackathonController.CreazioneHackathonRisultato.SUCCESSO) {
                        JOptionPane.showMessageDialog(null,
                                "✅ Hackathon '" + titoloIdentificativo + "' creato con successo!\n\n" +
                                "📅 Periodo evento: " + formatData(dataInizioEvento) + " → " + formatData(dataFineEvento) + "\n" +
                                "📝 Registrazioni: " + formatData(dataInizioRegistrazione) + " → " + formatData(dataFineRegistrazione) + "\n" +
                                "👥 Max partecipanti: " + maxNumIscritti + "\n" +
                                "🎯 Max membri per team: " + maxNumMembriTeam + "\n",
                                "🎉 Hackathon Creato",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Crea una nuova AdminView con i dati aggiornati dal database
                        frameCalling.dispose(); // Chiude la vecchia AdminView
                        new AdminView(adminLogged, frameMain, hackathonController); // Crea nuova AdminView aggiornata
                        frame.dispose();
                    } else {
                        // Gestisce gli errori basandosi sul risultato dell'enum
                        String messaggioErrore = gestisciErroreCreazione(risultato, titoloIdentificativo);
                        JOptionPane.showMessageDialog(null,
                            messaggioErrore,
                            "❌ Errore Creazione",
                            JOptionPane.ERROR_MESSAGE);
                    }

                } catch (DateTimeException ex) {
                    JOptionPane.showMessageDialog(null,
                            "📅 Errore nelle date inserite:\n\n" + ex.getMessage() + "\n\n" +
                            "Controlla che le date siano valide (es. 30 febbraio non esiste).",
                            "❌ Date Non Valide",
                            JOptionPane.ERROR_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "⚠️ Errore imprevisto durante la creazione dell'hackathon:\n\n" + ex.getMessage(),
                            "❌ Errore Imprevisto",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        comboBoxMeseInizioEvento.addActionListener(e ->
                aggiornaGiorni(comboBoxGiornoInizioEvento, comboBoxMeseInizioEvento, comboBoxAnnoInizioEvento));
        comboBoxAnnoInizioEvento.addActionListener(e ->
                aggiornaGiorni(comboBoxGiornoInizioEvento, comboBoxMeseInizioEvento, comboBoxAnnoInizioEvento));

        comboBoxMeseFineEvento.addActionListener(e ->
                aggiornaGiorni(comboBoxGiornoFineEvento, comboBoxMeseFineEvento, comboBoxAnnoFineEvento));
        comboBoxAnnoFineEvento.addActionListener(e ->
                aggiornaGiorni(comboBoxGiornoFineEvento, comboBoxMeseFineEvento, comboBoxAnnoFineEvento));

        comboBoxMeseInizioRegistrazioniEvento.addActionListener(e ->
                aggiornaGiorni(comboBoxGiornoInizioRegistrazioniEvento, comboBoxMeseInizioRegistrazioniEvento, comboBoxAnnoInizioRegistrazioniEvento));
        comboBoxAnnoInizioRegistrazioniEvento.addActionListener(e ->
                aggiornaGiorni(comboBoxGiornoInizioRegistrazioniEvento, comboBoxMeseInizioRegistrazioniEvento, comboBoxAnnoInizioRegistrazioniEvento));
    }

    /**
     * Valida le date dell'hackathon secondo le regole di business.
     *
     * @param dataInizioReg Data inizio registrazioni
     * @param dataFineReg Data fine registrazioni
     * @param dataInizioEvento Data inizio evento
     * @param dataFineEvento Data fine evento
     * @return Messaggio di errore se le date non sono valide, null se sono corrette
     */
    private String validaDate(LocalDateTime dataInizioReg, LocalDateTime dataFineReg,
                             LocalDateTime dataInizioEvento, LocalDateTime dataFineEvento) {

        LocalDateTime ora = LocalDateTime.now();

        // 1. Le date non possono essere nel passato
        if (dataInizioReg.isBefore(ora)) {
            return "📅 La data di inizio registrazioni non può essere nel passato.\n" +
                   "Data selezionata: " + formatData(dataInizioReg) + "\n" +
                   "Data attuale: " + formatData(ora);
        }

        // 2. L'inizio registrazioni deve essere prima della fine registrazioni
        if (dataInizioReg.isAfter(dataFineReg) || dataInizioReg.isEqual(dataFineReg)) {
            return "📅 L'inizio delle registrazioni deve essere prima della fine.\n" +
                   "Inizio: " + formatData(dataInizioReg) + "\n" +
                   "Fine: " + formatData(dataFineReg);
        }

        // 3. L'evento deve iniziare dopo la fine delle registrazioni (minimo 2 giorni)
        if (dataFineReg.plusDays(2).isAfter(dataInizioEvento)) {
            return "📅 Le registrazioni devono chiudersi almeno 2 giorni prima dell'evento.\n" +
                   "Fine registrazioni: " + formatData(dataFineReg) + "\n" +
                   "Inizio evento: " + formatData(dataInizioEvento) + "\n" +
                   "Minimo richiesto: " + formatData(dataFineReg.plusDays(2));
        }

        // 4. L'inizio evento deve essere prima della fine evento
        if (dataInizioEvento.isAfter(dataFineEvento) || dataInizioEvento.isEqual(dataFineEvento)) {
            return "📅 L'inizio dell'evento deve essere prima della fine.\n" +
                   "Inizio: " + formatData(dataInizioEvento) + "\n" +
                   "Fine: " + formatData(dataFineEvento);
        }

        // 5. L'evento deve durare almeno 1 giorno
        if (dataInizioEvento.plusDays(1).isAfter(dataFineEvento)) {
            return "📅 L'evento deve durare almeno 1 giorno completo.\n" +
                   "Inizio: " + formatData(dataInizioEvento) + "\n" +
                   "Fine: " + formatData(dataFineEvento);
        }

        return null; // Tutte le validazioni sono passate
    }

    /**
     * Gestisce e formatta gli errori di creazione hackathon per l'utente.
     */
    private String gestisciErroreCreazione(HackathonController.CreazioneHackathonRisultato risultato, String titoloHackathon) {
        switch (risultato) {
            case ERRORE_TITOLO_DUPLICATO:
                return "❌ Esiste già un hackathon con questo titolo!\n\n" +
                       "Titolo: '" + titoloHackathon + "'\n\n" +
                       "💡 Suggerimento: Prova con un titolo diverso o aggiungi un suffisso (es. 'v2', '2025', etc.).";

            case ERRORE_ORGANIZZATORE_NON_VALIDO:
                return "❌ Organizzatore non valido!\n\n" +
                       "L'organizzatore specificato non esiste nel database.";

            case ERRORE_DATE_NON_VALIDE:
                return "📅 Le date inserite non sono valide!\n\n" +
                       "• La registrazione deve terminare almeno 2 giorni prima dell'evento\n" +
                       "• Tutte le date devono essere coerenti tra loro\n" +
                       "• Le date non possono essere nel passato\n\n" +
                       "💡 Controlla nuovamente le date e riprova.";

            case ERRORE_DAO_NON_DISPONIBILE:
                return "❌ Servizio database non disponibile!\n\n" +
                       "Il sistema non riesce a connettersi al database.\n\n" +
                       "💡 Soluzione: Controlla la connessione e riprova tra qualche minuto.";

            case ERRORE_GENERICO:
            default:
                return "❌ Errore durante la creazione dell'hackathon.\n\n" +
                       "L'hackathon non è stato salvato nel database.\n" +
                       "Controlla che tutti i dati siano corretti e riprova.";
        }
    }

    /**
     * Formatta una data per la visualizzazione utente-friendly.
     */
    private String formatData(LocalDateTime data) {
        return data.getDayOfMonth() + "/" +
               data.getMonthValue() + "/" +
               data.getYear() + " " +
               String.format("%02d:%02d", data.getHour(), data.getMinute());
    }

    /**
     * Popola le ComboBox con i valori predefiniti.
     */
    private void populateComboBoxes() {
        // I parametri numerici ora sono gestiti da spinner, non più da ComboBox
        // quindi non c'è bisogno di popolare comboBoxNumMaxMembri e comboBoxNumMaxIscritti
        
        Integer[] mesi = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        for (Integer mese : mesi) {
            comboBoxMeseInizioEvento.addItem(mese);
            comboBoxMeseFineEvento.addItem(mese);
            comboBoxMeseInizioRegistrazioniEvento.addItem(mese);
        }

        // Popola i ComboBox degli anni.
        for (int i = MIN_YEAR; i <= MAX_YEAR; i++) {
            comboBoxAnnoInizioEvento.addItem(i);
            comboBoxAnnoFineEvento.addItem(i);
            comboBoxAnnoInizioRegistrazioniEvento.addItem(i);
        }

        aggiornaGiorni(comboBoxGiornoInizioEvento, comboBoxMeseInizioEvento, comboBoxAnnoInizioEvento);
        aggiornaGiorni(comboBoxGiornoFineEvento, comboBoxMeseFineEvento, comboBoxAnnoFineEvento);
        aggiornaGiorni(comboBoxGiornoInizioRegistrazioniEvento, comboBoxMeseInizioRegistrazioniEvento, comboBoxAnnoInizioRegistrazioniEvento);
    }

    /**
     * Aggiorna i giorni disponibili in un ComboBox in base al mese e all'anno selezionati.
     *
     * @param giornoBox ComboBox dei giorni da aggiornare.
     * @param meseBox ComboBox del mese selezionato.
     * @param annoBox ComboBox dell'anno selezionato.
     */
    private void aggiornaGiorni(JComboBox<Integer> giornoBox, JComboBox<Integer> meseBox, JComboBox<Integer> annoBox) {
        if (meseBox.getSelectedItem() == null || annoBox.getSelectedItem() == null) return;

        int mese = (Integer) meseBox.getSelectedItem() - 1; // Calendar usa 0-based per i mesi
        int anno = (Integer) annoBox.getSelectedItem();

        // Ottiene il numero massimo di giorni del mese.
        java.util.Calendar cal = new java.util.GregorianCalendar(anno, mese, 1);
        int giorniNelMese = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

        Integer giornoSelezionato = (Integer) giornoBox.getSelectedItem();

        giornoBox.removeAllItems();
        for (int i = 1; i <= giorniNelMese; i++) {
            giornoBox.addItem(i);
        }

        if (giornoSelezionato != null && giornoSelezionato <= giorniNelMese) {
            giornoBox.setSelectedItem(giornoSelezionato);
        }
    }
    
    /**
     * Crea l'interfaccia moderna per la creazione hackathon.
     */
    private void createModernInterface() {
        // Panel principale con gradiente
        panelCreaHackathon = new JPanel(new BorderLayout(0, 0)) {
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
        JPanel headerPanel = createModernHeader();
        panelCreaHackathon.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel con form in uno scroll pane
        JPanel contentPanel = createFormPanel();
        
        // Crea scroll pane per il contenuto
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        // Stile moderno per la scrollbar
        styleScrollPane(scrollPane);
        
        panelCreaHackathon.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel con pulsante
        JPanel bottomPanel = createBottomPanel();
        panelCreaHackathon.add(bottomPanel, BorderLayout.SOUTH);
        
        // Popola le ComboBox
        populateComboBoxes();
    }
    
    /**
     * Crea l'header moderno.
     */
    private JPanel createModernHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));
        
        // Titolo principale
        JLabel titleLabel = new JLabel("Crea Nuovo Hackathon");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(30, 58, 138));
        
        // Sottotitolo
        JLabel subtitleLabel = new JLabel("Organizza il tuo evento di programmazione");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
     * Crea il panel del form con tutti i campi.
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Sezione Informazioni Base
        addSectionHeader(formPanel, gbc, row, "📝 Informazioni Base");
        row++;
        
        // Titolo Hackathon
        addFormRow(formPanel, gbc, row, "Titolo:", () -> {
            fieldTitolo = new JTextField();
            styleModernTextField(fieldTitolo, 300);
            return fieldTitolo;
        });
        row++;
        
        // Sede
        addFormRow(formPanel, gbc, row, "Sede:", () -> {
            fieldSede = new JTextField();
            styleModernTextField(fieldSede, 300);
            return fieldSede;
        });
        row++;
        
        // Descrizione
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel descLbl = createModernLabel("Descrizione:");
        formPanel.add(descLbl, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 0.3;
        textAreaDescrizioneProblema = new JTextArea(4, 40);
        styleModernTextArea(textAreaDescrizioneProblema);
        
        scrollPaneDescr = new JScrollPane(textAreaDescrizioneProblema);
        styleModernScrollPane(scrollPaneDescr);
        formPanel.add(scrollPaneDescr, gbc);
        
        row++;
        gbc.weighty = 0;
        
        // Sezione Date
        addSectionHeader(formPanel, gbc, row, "📅 Configurazione Date");
        row++;
        
        // Date section con layout migliorato
        row = addImprovedDateSection(formPanel, gbc, row);
        
        // Sezione Parametri
        addSectionHeader(formPanel, gbc, row, "⚙️ Parametri Evento");
        row++;
        
        // Parametri numerici con layout migliorato
        addImprovedNumericSection(formPanel, gbc, row);
        
        return formPanel;
    }
    
    /**
     * Crea una sezione header con stile moderno.
     */
    private void addSectionHeader(JPanel panel, GridBagConstraints gbc, int row, String title) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 3; 
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(25, 0, 15, 0);
        
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(new Color(30, 144, 255));
        headerLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(30, 144, 255, 100)),
            BorderFactory.createEmptyBorder(0, 0, 8, 0)
        ));
        
        panel.add(headerLabel, gbc);
        
        // Reset insets per i prossimi elementi
        gbc.insets = new Insets(12, 12, 12, 12);
    }
    
    /**
     * Aggiunge una riga del form con label e campo.
     */
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, 
                           java.util.function.Supplier<JComponent> fieldSupplier) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; 
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        
        JLabel label = createModernLabel(labelText);
        panel.add(label, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        
        JComponent field = fieldSupplier.get();
        panel.add(field, gbc);
    }
    
    /**
     * Crea una label con stile moderno.
     */
    private JLabel createModernLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(70, 70, 70));
        return label;
    }
    
    /**
     * Applica stile moderno a un text field con larghezza specifica.
     */
    private void styleModernTextField(JTextField textField, int preferredWidth) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setPreferredSize(new Dimension(preferredWidth, 32));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        
        // Effetto focus
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(30, 144, 255), 2),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)
                ));
            }
        });
    }
    
    /**
     * Applica stile moderno a una text area.
     */
    private void styleModernTextArea(JTextArea textArea) {
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        textArea.setBackground(Color.WHITE);
    }
    
    /**
     * Applica stile moderno a uno scroll pane.
     */
    private void styleModernScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
    }
    
    /**
     * Applica stile moderno alla scroll pane principale.
     */
    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        // Personalizza la scrollbar verticale
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(200, 200, 200);
                this.trackColor = new Color(240, 240, 240);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });
        
        // Imposta la velocità di scroll
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);
    }
    
    /**
     * Crea ComboBox con stile moderno.
     */
    private JComboBox<String> createStyledComboBox(String[] items, int preferredWidth) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboBox.setPreferredSize(new Dimension(preferredWidth, 32));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        
        // Renderer personalizzato per migliorare l'aspetto
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (isSelected) {
                    setBackground(new Color(30, 144, 255, 50));
                    setForeground(new Color(30, 144, 255));
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                
                setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                setFont(new Font("Segoe UI", Font.PLAIN, 12));
                return this;
            }
        });
        
        return comboBox;
    }
    
    /**
     * Crea ComboBox di Integer con stile moderno.
     */
    private JComboBox<Integer> createStyledIntegerComboBox(Integer[] items, int preferredWidth) {
        JComboBox<Integer> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboBox.setPreferredSize(new Dimension(preferredWidth, 32));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        
        // Renderer personalizzato per migliorare l'aspetto
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (isSelected) {
                    setBackground(new Color(30, 144, 255, 50));
                    setForeground(new Color(30, 144, 255));
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                
                setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                setFont(new Font("Segoe UI", Font.PLAIN, 12));
                return this;
            }
        });
        
        return comboBox;
    }
    
    /**
     * Aggiunge la sezione date migliorata con layout più compatto.
     */
    private int addImprovedDateSection(JPanel formPanel, GridBagConstraints gbc, int startRow) {
        int row = startRow;
        
        // Inizio Registrazioni
        row = addCompactDateRow(formPanel, gbc, row, "Inizio Registrazioni:",
            () -> comboBoxGiornoInizioRegistrazioniEvento = createStyledIntegerComboBox(getDaysAsInteger(), 80),
            () -> comboBoxMeseInizioRegistrazioniEvento = createStyledIntegerComboBox(getMonthsAsInteger(), 120),
            () -> comboBoxAnnoInizioRegistrazioniEvento = createStyledIntegerComboBox(getYearsAsInteger(), 100));
        
        // Inizio Evento
        row = addCompactDateRow(formPanel, gbc, row, "Inizio Evento:",
            () -> comboBoxGiornoInizioEvento = createStyledIntegerComboBox(getDaysAsInteger(), 80),
            () -> comboBoxMeseInizioEvento = createStyledIntegerComboBox(getMonthsAsInteger(), 120),
            () -> comboBoxAnnoInizioEvento = createStyledIntegerComboBox(getYearsAsInteger(), 100));
        
        // Fine Evento
        row = addCompactDateRow(formPanel, gbc, row, "Fine Evento:",
            () -> comboBoxGiornoFineEvento = createStyledIntegerComboBox(getDaysAsInteger(), 80),
            () -> comboBoxMeseFineEvento = createStyledIntegerComboBox(getMonthsAsInteger(), 120),
            () -> comboBoxAnnoFineEvento = createStyledIntegerComboBox(getYearsAsInteger(), 100));
        
        // Nota informativa
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 3; 
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 12, 12, 12);
        
        JLabel noteLabel = new JLabel("ℹ️ Nota: La fine registrazione è automaticamente 2 giorni prima dell'inizio evento");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        noteLabel.setForeground(new Color(100, 100, 100));
        formPanel.add(noteLabel, gbc);
        
        // Reset insets
        gbc.insets = new Insets(12, 12, 12, 12);
        
        return row + 1;
    }
    
    /**
     * Aggiunge una riga di date compatta con tre ComboBox affiancate.
     */
    private int addCompactDateRow(JPanel panel, GridBagConstraints gbc, int row, String labelText,
                                 Supplier<JComboBox<Integer>> daySupplier,
                                 Supplier<JComboBox<Integer>> monthSupplier, 
                                 Supplier<JComboBox<Integer>> yearSupplier) {
        
        // Label
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; 
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel label = createModernLabel(labelText);
        panel.add(label, gbc);
        
        // Panel per le tre ComboBox
        gbc.gridx = 1; gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        datePanel.setOpaque(false);
        
        // Giorno
        JComboBox<Integer> dayCombo = daySupplier.get();
        JLabel dayLabel = new JLabel("Giorno:");
        dayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dayLabel.setForeground(new Color(100, 100, 100));
        datePanel.add(dayLabel);
        datePanel.add(dayCombo);
        
        // Mese
        JComboBox<Integer> monthCombo = monthSupplier.get();
        JLabel monthLabel = new JLabel("Mese:");
        monthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        monthLabel.setForeground(new Color(100, 100, 100));
        datePanel.add(monthLabel);
        datePanel.add(monthCombo);
        
        // Anno
        JComboBox<Integer> yearCombo = yearSupplier.get();
        JLabel yearLabel = new JLabel("Anno:");
        yearLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        yearLabel.setForeground(new Color(100, 100, 100));
        datePanel.add(yearLabel);
        datePanel.add(yearCombo);
        
        panel.add(datePanel, gbc);
        
        return row + 1;
    }
    
    /**
     * Aggiunge la sezione parametri numerici migliorata.
     */
    private void addImprovedNumericSection(JPanel formPanel, GridBagConstraints gbc, int startRow) {
        int row = startRow;
        
        // Numero massimo partecipanti all'hackathon
        addNumericRow(formPanel, gbc, row, "Numero Massimo Partecipanti Hackathon:",
            () -> {
                spinnerNumeroMassimoPartecipanti = new JSpinner(new SpinnerNumberModel(30, 10, 500, 10));
                styleModernSpinner(spinnerNumeroMassimoPartecipanti, 120);
                return spinnerNumeroMassimoPartecipanti;
            });
        row++;
        
        // Numero massimo partecipanti per team
        addNumericRow(formPanel, gbc, row, "Numero Massimo Partecipanti per Team:",
            () -> {
                spinnerNumeroMassimoPartecipantiTeam = new JSpinner(new SpinnerNumberModel(5, 2, 20, 1));
                styleModernSpinner(spinnerNumeroMassimoPartecipantiTeam, 120);
                return spinnerNumeroMassimoPartecipantiTeam;
            });
        
        // Nota informativa sul numero minimo
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 3; 
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 12, 12, 12);
        
        JLabel noteLabel = new JLabel("ℹ️ Nota: Il numero minimo di partecipanti per team è automaticamente 2");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        noteLabel.setForeground(new Color(100, 100, 100));
        formPanel.add(noteLabel, gbc);
        
        // Reset insets
        gbc.insets = new Insets(12, 12, 12, 12);
    }
    
    /**
     * Aggiunge una riga per parametri numerici.
     */
    private void addNumericRow(JPanel panel, GridBagConstraints gbc, int row, String labelText,
                              Supplier<JComponent> componentSupplier) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; 
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        
        JLabel label = createModernLabel(labelText);
        panel.add(label, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 1; 
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        
        JComponent component = componentSupplier.get();
        panel.add(component, gbc);
        
        // Spazio vuoto a destra
        gbc.gridx = 2; gbc.gridwidth = 1; 
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(Box.createHorizontalGlue(), gbc);
    }
    
    /**
     * Applica stile moderno a uno spinner.
     */
    private void styleModernSpinner(JSpinner spinner, int preferredWidth) {
        spinner.setPreferredSize(new Dimension(preferredWidth, 32));
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Stile del campo di testo interno
        JTextField textField = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        textField.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Bordo dello spinner
        spinner.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
    }
    
    /**
     * Metodi helper per ottenere array di valori per le ComboBox.
     */
    private String[] getDays() {
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = String.valueOf(i + 1);
        }
        return days;
    }
    
    private String[] getMonths() {
        return new String[]{"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                           "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};
    }
    
    private String[] getYears() {
        String[] years = new String[10];
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        for (int i = 0; i < 10; i++) {
            years[i] = String.valueOf(currentYear + i);
        }
        return years;
    }
    
    /**
     * Metodi helper per ottenere array di Integer.
     */
    private Integer[] getDaysAsInteger() {
        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) {
            days[i] = i + 1;
        }
        return days;
    }
    
    private Integer[] getMonthsAsInteger() {
        Integer[] months = new Integer[12];
        for (int i = 0; i < 12; i++) {
            months[i] = i + 1;
        }
        return months;
    }
    
    private Integer[] getYearsAsInteger() {
        Integer[] years = new Integer[10];
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        for (int i = 0; i < 10; i++) {
            years[i] = currentYear + i;
        }
        return years;
    }
    
    /**
     * Crea il panel inferiore con il pulsante di creazione.
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setOpaque(false);
        
        btnCreaHackathon = new JButton("🚀 Crea Hackathon");
        btnCreaHackathon.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCreaHackathon.setPreferredSize(new Dimension(200, 45));
        
        // Stile moderno per il pulsante principale
        btnCreaHackathon.setBackground(new Color(34, 197, 94)); // Verde
        btnCreaHackathon.setForeground(Color.WHITE);
        btnCreaHackathon.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        btnCreaHackathon.setFocusPainted(false);
        btnCreaHackathon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Effetti hover
        btnCreaHackathon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCreaHackathon.setBackground(new Color(22, 163, 74));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnCreaHackathon.setBackground(new Color(34, 197, 94));
            }
        });
        
        bottomPanel.add(btnCreaHackathon);
        
        return bottomPanel;
    }
    
    /**
     * Applica stile moderno ai text field.
     */
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
    }
    
    /**
     * Applica stile moderno alle combo box.
     */
    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
    }
}
