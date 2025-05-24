package gui;

import model.Organizzatore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.DateTimeException;
import java.time.LocalDateTime;


public class CreaHackathonForm {

    //Anno minimo e massimo adottati per le registrazioni
    private final int MIN_YEAR = 2025;
    private final int MAX_YEAR = 2050;

    private JPanel panelCreaHackathon;
    private JTextField fieldId;
    private JTextField fieldSede;
    private JComboBox comboBoxGiornoInizioEvento;
    private JComboBox comboBoxMeseInizioEvento;
    private JComboBox comboBoxAnnoInizioEvento;
    private JComboBox comboBoxNumMaxMembri;
    private JButton btnCreaHackathon;
    private JComboBox comboBoxGiornoFineEvento;
    private JComboBox comboBoxMeseFineEvento;
    private JComboBox comboBoxAnnoFineEvento;
    private JComboBox comboBoxGiornoInizioRegistrazioniEvento;
    private JComboBox comboBoxMeseInizioRegistrazioniEvento;
    private JComboBox comboBoxAnnoInizioRegistrazioniEvento;
    private JComboBox comboBoxNumMaxIscritti;
    private JTextField fieldTitolo;
    private JTextArea textAreaDescrizioneProblema;



    public CreaHackathonForm(Organizzatore adminLogged, JFrame frameCalling, JFrame frameMain) {
        JFrame frame = new JFrame("Creazione Team");
        frame.setContentPane(panelCreaHackathon);
        frame.pack();

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

        // Aggiungi listener per aggiornare i giorni in base al mese/anno selezionati
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

        /*// Mesi
        String[] mesi = {
                "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
        };*/

        Integer[] mesi = {
                        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
                };

        for (Integer mese : mesi) {
            comboBoxMeseInizioEvento.addItem(mese);
            comboBoxMeseFineEvento.addItem(mese);
            comboBoxMeseInizioRegistrazioniEvento.addItem(mese);
        }

// Anni (esempio: 2020–2030)
        for (int i = MIN_YEAR; i <= MAX_YEAR; i++) {
            comboBoxAnnoInizioEvento.addItem(i);
            comboBoxAnnoFineEvento.addItem(i);
            comboBoxAnnoInizioRegistrazioniEvento.addItem(i);
        }

        aggiornaGiorni(comboBoxGiornoInizioEvento, comboBoxMeseInizioEvento, comboBoxAnnoInizioEvento);
        aggiornaGiorni(comboBoxGiornoFineEvento, comboBoxMeseFineEvento, comboBoxAnnoFineEvento);
        aggiornaGiorni(comboBoxGiornoInizioRegistrazioniEvento, comboBoxMeseInizioRegistrazioniEvento, comboBoxAnnoInizioRegistrazioniEvento);


        btnCreaHackathon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    LocalDateTime inizioEvento = LocalDateTime.of((Integer) comboBoxAnnoInizioEvento.getSelectedItem(),
                            (Integer) comboBoxMeseInizioEvento.getSelectedItem(),
                            (Integer) comboBoxGiornoInizioEvento.getSelectedItem(),
                            0,
                            0,
                            0);
                    LocalDateTime fineEvento = LocalDateTime.of((Integer)comboBoxAnnoFineEvento.getSelectedItem(),
                            (Integer)comboBoxMeseFineEvento.getSelectedItem(),
                            (Integer)comboBoxGiornoFineEvento.getSelectedItem(),
                            0,
                            0,
                            0);
                    LocalDateTime inizioRegistrazioni = LocalDateTime.of((Integer)comboBoxAnnoInizioRegistrazioniEvento.getSelectedItem(),
                            (Integer)comboBoxMeseInizioRegistrazioniEvento.getSelectedItem(),
                            (Integer)comboBoxGiornoInizioRegistrazioniEvento.getSelectedItem(),
                            0,
                            0,
                            0);

                    adminLogged.registrazioneHackathon(
                            fieldId.getText(),
                            fieldSede.getText(),
                            inizioEvento, fineEvento, inizioRegistrazioni,
                            fieldTitolo.getText(),
                            (int) comboBoxNumMaxMembri.getSelectedItem(),
                            (int) comboBoxNumMaxIscritti.getSelectedItem()
                    );
                    adminLogged.getHackathonOrganizzata().setDescrizioneProblema(textAreaDescrizioneProblema.getText());
                    frameCalling.setVisible(true);
                    frame.dispose();
                }
                catch(DateTimeException ex)
                {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });
    }

    private void aggiornaGiorni(JComboBox<Integer> giornoBox, JComboBox<String> meseBox, JComboBox<Integer> annoBox) {
        if (meseBox.getSelectedItem() == null || annoBox.getSelectedItem() == null) return;

        int mese = meseBox.getSelectedIndex(); // 0-based
        int anno = (Integer) annoBox.getSelectedItem();

        // Ottieni il numero massimo di giorni del mese
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
