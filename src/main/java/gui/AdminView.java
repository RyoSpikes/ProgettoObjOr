package gui;

import model.Hackathon;
import model.Organizzatore;
import controller.ControllerOrganizzatore;
import gui.CreaHackathonForm;

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
     * @param controllerOrganizzatore Il controller per la gestione degli hackathon.
     */
    public AdminView(Organizzatore adminLogged, JFrame frameCalling, ControllerOrganizzatore controllerOrganizzatore) {
        frameAdminView = new JFrame("Admin View");
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

        // Listener per il pulsante "Crea Hackathon"
        creaHackathonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreaHackathonForm(adminLogged, frameAdminView, frameCalling, controllerOrganizzatore);
                frameAdminView.setVisible(false);
            }
        });

        // Listener per il pulsante "Mostra Hackathon"
        mostraHackathonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminTextArea.setText("");

                // Recupera gli hackathon dell'organizzatore dal database
                var hackathonList = controllerOrganizzatore.getHackathonDiOrganizzatore(adminLogged);

                if (hackathonList.isEmpty()) {
                    adminTextArea.append("Non hai ancora creato nessun hackathon.\n\n");
                    adminTextArea.append("Utilizza il pulsante 'Crea Hackathon' per organizzare il tuo primo evento!");
                } else {
                    adminTextArea.append("=== I TUOI HACKATHON ===\n\n");

                    for (int i = 0; i < hackathonList.size(); i++) {
                        Hackathon h = hackathonList.get(i);
                        adminTextArea.append((i + 1) + ". " + h.printInfoEvento());
                        adminTextArea.append("\n" + "=".repeat(50) + "\n\n");
                    }

                    adminTextArea.append("Totale hackathon organizzati: " + hackathonList.size());
                }

                // Scorri automaticamente all'inizio del testo
                adminTextArea.setCaretPosition(0);
            }
        });
    }
}
