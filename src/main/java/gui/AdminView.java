package gui;

import model.Hackathon;
import model.Organizzatore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * La classe AdminView rappresenta l'interfaccia grafica per l'amministratore.
 * Consente di creare e visualizzare gli hackathon associati a un organizzatore.
 */
public class AdminView {
    private JFrame frameAdminView; // Finestra principale della vista amministratore.
    private JPanel panelAdmin; // Pannello principale della vista amministratore.
    private JTextArea adminTextArea; // Area di testo per visualizzare informazioni.
    private JButton creaHackathonButton; // Pulsante per creare un nuovo hackathon.
    private JButton mostraHackathonButton; // Pulsante per mostrare gli hackathon esistenti.

    /**
     * Costruttore della classe AdminView.
     * Inizializza l'interfaccia grafica e gestisce le azioni dei pulsanti.
     *
     * @param adminLogged L'organizzatore attualmente loggato.
     * @param frameCalling Il frame chiamante che ha aperto questa vista.
     */
    public AdminView(Organizzatore adminLogged, JFrame frameCalling) {

        frameAdminView = new JFrame("User View");
        frameAdminView.setContentPane(panelAdmin);
        frameAdminView.pack();

        // Listener per gestire la chiusura della finestra.
        frameAdminView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameCalling.setVisible(true);
                frameAdminView.dispose();
            }
        });

        frameAdminView.setVisible(true);
        frameAdminView.setSize(800, 800);
        frameAdminView.setResizable(false);
        frameAdminView.setLocationRelativeTo(null);

        adminTextArea.setLineWrap(true);
        adminTextArea.setWrapStyleWord(true);
        adminTextArea.setEditable(false);

        // Listener per il pulsante "Crea Hackathon".
        creaHackathonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreaHackathonForm(adminLogged, frameAdminView, frameCalling);
                frameAdminView.setVisible(false);
            }
        });

        // Listener per il pulsante "Mostra Hackathon".
        mostraHackathonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminTextArea.setText(adminLogged.printListaHackathon());
            }
        });
    }
}