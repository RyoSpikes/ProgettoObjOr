package gui;

import model.Organizzatore;
import controller.ControllerOrganizzatore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.sql.SQLException;

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
    private JTextField fieldId; // Campo di testo per l'ID dell'hackathon.
    private JTextField fieldSede; // Campo di testo per la sede dell'hackathon.
    private JComboBox comboBoxGiornoInizioEvento; // ComboBox per il giorno di inizio dell'evento.
    private JComboBox comboBoxMeseInizioEvento; // ComboBox per il mese di inizio dell'evento.
    private JComboBox comboBoxAnnoInizioEvento; // ComboBox per l'anno di inizio dell'evento.
    private JComboBox comboBoxNumMaxMembri; // ComboBox per il numero massimo di membri per team.
    private JButton btnCreaHackathon; // Pulsante per creare l'hackathon.
    private JComboBox comboBoxGiornoFineEvento; // ComboBox per il giorno di fine dell'evento.
    private JComboBox comboBoxMeseFineEvento; // ComboBox per il mese di fine dell'evento.
    private JComboBox comboBoxAnnoFineEvento; // ComboBox per l'anno di fine dell'evento.
    private JComboBox comboBoxGiornoInizioRegistrazioniEvento; // ComboBox per il giorno di inizio delle registrazioni.
    private JComboBox comboBoxMeseInizioRegistrazioniEvento; // ComboBox per il mese di inizio delle registrazioni.
    private JComboBox comboBoxAnnoInizioRegistrazioniEvento; // ComboBox per l'anno di inizio delle registrazioni.
    private JComboBox comboBoxNumMaxIscritti; // ComboBox per il numero massimo di iscritti.
    private JTextField fieldTitolo; // Campo di testo per il titolo dell'hackathon.
    private JTextArea textAreaDescrizioneProblema; // Area di testo per la descrizione del problema.

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
        JFrame frame = new JFrame("Creazione Hackathon");
        frame.setContentPane(panelCreaHackathon);
        frame.pack();

        // Listener per gestire la chiusura della finestra.
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameCalling.setVisible(true);
                frame.dispose();
            }
        });

        frame.setVisible(true);
        textAreaDescrizioneProblema.setLineWrap(true);
        textAreaDescrizioneProblema.setWrapStyleWord(true);

        // Aggiunge listener per aggiornare i giorni in base al mese/anno selezionati.
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

        frame.setSize(600, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

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

        Integer[] mesi = {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
        };

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

        // Listener per il pulsante "Crea Hackathon" - ora utilizza la DAO per salvare nel database.
        btnCreaHackathon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validazione dei campi obbligatori
                if (fieldTitolo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Il titolo dell'hackathon è obbligatorio.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (fieldSede.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "La sede dell'hackathon è obbligatoria.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (textAreaDescrizioneProblema.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "La descrizione del problema è obbligatoria.", "Errore", JOptionPane.ERROR_MESSAGE);
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

                    // Dati dal form
                    String titoloIdentificativo = fieldTitolo.getText().trim();
                    String sede = fieldSede.getText().trim();
                    String descrizioneProblema = textAreaDescrizioneProblema.getText().trim();
                    int maxNumMembriTeam = (Integer) comboBoxNumMaxMembri.getSelectedItem();
                    int maxNumIscritti = (Integer) comboBoxNumMaxIscritti.getSelectedItem();

                    // Utilizza il controller per creare l'hackathon nel database
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
                        JOptionPane.showMessageDialog(frame,
                                "Hackathon '" + titoloIdentificativo + "' creato con successo!",
                                "Successo",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Torna alla finestra precedente
                        frameCalling.setVisible(true);
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Errore durante la creazione dell'hackathon.",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Errore nel database: " + ex.getMessage(),
                            "Errore Database",
                            JOptionPane.ERROR_MESSAGE);
                } catch (DateTimeException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Errore nelle date inserite: " + ex.getMessage(),
                            "Errore Date",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Errore imprevisto: " + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Aggiorna i giorni disponibili in un ComboBox in base al mese e all'anno selezionati.
     *
     * @param giornoBox ComboBox dei giorni da aggiornare.
     * @param meseBox ComboBox del mese selezionato.
     * @param annoBox ComboBox dell'anno selezionato.
     */
    private void aggiornaGiorni(JComboBox<Integer> giornoBox, JComboBox<String> meseBox, JComboBox<Integer> annoBox) {
        if (meseBox.getSelectedItem() == null || annoBox.getSelectedItem() == null) return;

        int mese = meseBox.getSelectedIndex(); // Indice del mese (0-based).
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
