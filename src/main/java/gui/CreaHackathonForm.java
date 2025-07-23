package gui;

import model.Organizzatore;
import controller.ControllerOrganizzatore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.sql.SQLException;
import java.time.DateTimeException;

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
    private JComboBox<Integer> comboBoxNumMaxMembri; // ComboBox per il numero massimo di membri per team.
    private JButton btnCreaHackathon; // Pulsante per creare l'hackathon.
    private JComboBox<Integer> comboBoxGiornoFineEvento; // ComboBox per il giorno di fine dell'evento.
    private JComboBox<Integer> comboBoxMeseFineEvento; // ComboBox per il mese di fine dell'evento.
    private JComboBox<Integer> comboBoxAnnoFineEvento; // ComboBox per l'anno di fine dell'evento.
    private JComboBox<Integer> comboBoxGiornoInizioRegistrazioniEvento; // ComboBox per il giorno di inizio delle registrazioni.
    private JComboBox<Integer> comboBoxMeseInizioRegistrazioniEvento; // ComboBox per il mese di inizio delle registrazioni.
    private JComboBox<Integer> comboBoxAnnoInizioRegistrazioniEvento; // ComboBox per l'anno di inizio delle registrazioni.
    private JComboBox<Integer> comboBoxNumMaxIscritti; // ComboBox per il numero massimo di iscritti.
    private JTextField fieldTitolo; // Campo di testo per il titolo dell'hackathon.
    private JTextArea textAreaDescrizioneProblema; // Area di testo per la descrizione del problema.
    private JScrollPane scrollPaneDescr; // ScrollPane per l'area di testo.
    private JComboBox<Integer> comboBoxGiornoFineRegistrazioniEvento; // ComboBox per il giorno di fine registrazioni

    /**
     * Costruttore della classe CreaHackathonForm.
     * Inizializza l'interfaccia grafica e gestisce le azioni degli elementi.
     *
     * @param adminLogged L'organizzatore attualmente loggato.
     * @param frameCalling Il frame chiamante che ha aperto questa finestra.
     * @param frameMain Il frame principale dell'applicazione.
     * @param controllerOrganizzatore Il controller per la gestione degli hackathon.
     */
    public CreaHackathonForm(Organizzatore adminLogged, JFrame frameCalling, JFrame frameMain, ControllerOrganizzatore controllerOrganizzatore) {
        // ⚠️ VERIFICA DI SICUREZZA: Controllo che tutti i componenti siano inizializzati
        if (panelCreaHackathon == null) {
            JOptionPane.showMessageDialog(null,
                "❌ Errore critico: Il form non è stato inizializzato correttamente.\n\n" +
                "Possibili cause:\n" +
                "• Il file CreaHackathonForm.form non è compatibile\n" +
                "• Problemi con il Form Designer di IntelliJ\n\n" +
                "💡 Ricompila il progetto (Build → Rebuild Project) e riprova.",
                "Errore Inizializzazione Form",
                JOptionPane.ERROR_MESSAGE);
            frameCalling.setVisible(true);
            return;
        }

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
        frame.setSize(650, 550);
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
                    int maxNumMembriTeam = (Integer) comboBoxNumMaxMembri.getSelectedItem();
                    int maxNumIscritti = (Integer) comboBoxNumMaxIscritti.getSelectedItem();

                    // ✅ CREAZIONE HACKATHON NEL DATABASE
                    boolean successo = controllerOrganizzatore.creaHackathon(
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

                    if (successo) {
                        JOptionPane.showMessageDialog(null,
                                "✅ Hackathon '" + titoloIdentificativo + "' creato con successo!\n\n" +
                                "📅 Periodo evento: " + formatData(dataInizioEvento) + " → " + formatData(dataFineEvento) + "\n" +
                                "📝 Registrazioni: " + formatData(dataInizioRegistrazione) + " → " + formatData(dataFineRegistrazione) + "\n" +
                                "👥 Max partecipanti: " + maxNumIscritti + "\n" +
                                "🎯 Max membri per team: " + maxNumMembriTeam + "\n\n" +
                                "💾 L'hackathon è stato salvato nel database PostgreSQL.",
                                "🎉 Hackathon Creato",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Torna alla finestra precedente
                        frameCalling.setVisible(true);
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "❌ Errore durante la creazione dell'hackathon.\n\n" +
                                "L'hackathon non è stato salvato nel database.\n" +
                                "Controlla che tutti i dati siano corretti e riprova.",
                                "💥 Errore Creazione",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    String messaggioErrore = gestisciErroreDatabase(ex);
                    JOptionPane.showMessageDialog(null,
                        messaggioErrore,
                        "❌ Errore Database",
                        JOptionPane.ERROR_MESSAGE);

                } catch (DateTimeException ex) {
                    JOptionPane.showMessageDialog(null,
                            "📅 Errore nelle date inserite:\n\n" + ex.getMessage() + "\n\n" +
                            "Controlla che le date siano valide (es. 30 febbraio non esiste).",
                            "❌ Date Non Valide",
                            JOptionPane.ERROR_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "⚠️ Errore imprevisto durante la creazione dell'hackathon:\n\n" + ex.getMessage() + "\n\n" +
                            "Se il problema persiste, contatta il supporto tecnico.",
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
     * Gestisce e formatta gli errori del database per l'utente.
     */
    private String gestisciErroreDatabase(SQLException ex) {
        String sqlState = ex.getSQLState();

        if ("23505".equals(sqlState)) { // Violazione unique constraint
            return "❌ Esiste già un hackathon con questo titolo!\n\n" +
                   "Titolo: '" + fieldTitolo.getText().trim() + "'\n\n" +
                   "💡 Suggerimento: Prova con un titolo diverso o aggiungi un suffisso (es. 'v2', '2025', etc.).";

        } else if ("23503".equals(sqlState)) { // Violazione foreign key
            return "❌ Organizzatore non valido!\n\n" +
                   "L'organizzatore specificato non esiste nel database.\n\n" +
                   "💡 Questo è un errore interno, contatta il supporto tecnico.";

        } else if ("23514".equals(sqlState)) { // Violazione check constraint
            return "📅 Le date inserite violano i vincoli del database:\n\n" +
                   "• La registrazione deve terminare almeno 2 giorni prima dell'evento\n" +
                   "• Tutte le date devono essere coerenti tra loro\n" +
                   "• Le date non possono essere nel passato\n\n" +
                   "💡 Controlla nuovamente le date e riprova.";

        } else {
            return "❌ Errore del database:\n\n" + ex.getMessage() + "\n\n" +
                   "Codice errore: " + (sqlState != null ? sqlState : "Sconosciuto") + "\n\n" +
                   "💡 Se il problema persiste, contatta il supporto tecnico.";
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
        // Popola i ComboBox con valori predefiniti.
        comboBoxNumMaxMembri.addItem(3);
        comboBoxNumMaxMembri.addItem(6);
        comboBoxNumMaxMembri.addItem(12);
        comboBoxNumMaxMembri.addItem(18);

        comboBoxNumMaxIscritti.addItem(30);
        comboBoxNumMaxIscritti.addItem(60);
        comboBoxNumMaxIscritti.addItem(90);
        comboBoxNumMaxIscritti.addItem(120);
        comboBoxNumMaxIscritti.addItem(150);
        comboBoxNumMaxIscritti.addItem(180);
        comboBoxNumMaxIscritti.addItem(210);

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
}
