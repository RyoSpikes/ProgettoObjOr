package gui;

import Database.DAO.Impl.UtenteDAOImpl;
import Database.DAO.Impl.InvitoGiudiceDAOImpl;
import model.Hackathon;
import model.Organizzatore;
import model.Utente;
import controller.ControllerOrganizzatore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * La classe AdminView rappresenta l'interfaccia grafica per l'amministratore.
 * Consente di creare e visualizzare gli hackathon associati a un organizzatore.
 */
public class AdminView {
    private JFrame frameAdminView; // Finestra principale della vista amministratore.
    private JPanel panelAdmin; // Pannello principale della vista amministratore.
    private JTextArea adminTextArea; // Area di testo per visualizzare informazioni.
    private JButton creaHackathonButton; // Pulsante per creare un nuovo hackathon.
    private JButton invitaGiudiceButton;
    private JTextArea textArea1;
    private JPanel hackathonContentPanel; // Pannello interno per i contenuti dinamici degli hackathon

    /**
     * Costruttore della classe AdminView.
     * Inizializza l'interfaccia grafica e gestisce le azioni dei pulsanti.
     *
     * @param adminLogged L'organizzatore attualmente loggato.
     * @param frameCalling Il frame chiamante che ha aperto questa vista.
     * @param controllerOrganizzatore Il controller per la gestione degli hackathon.
     */
    public AdminView(Organizzatore adminLogged, JFrame frameCalling, ControllerOrganizzatore controllerOrganizzatore) {
        frameAdminView = new JFrame("Pannello Organizzatore - " + adminLogged.getName() + " (I tuoi hackathon)");
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
        initializeHackathonButtonsPanel(adminLogged, controllerOrganizzatore);

        // Listener per il pulsante "Crea Hackathon"
        creaHackathonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreaHackathonForm(adminLogged, frameAdminView, frameCalling, controllerOrganizzatore);
                frameAdminView.setVisible(false);
                // Nota: quando si torna alla AdminView, i pulsanti verranno aggiornati automaticamente
                // perché viene creata una nuova istanza della vista
            }
        });

        // Listener per il pulsante "Invita Giudice"
        invitaGiudiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Recupera tutti gli utenti dal database
                    UtenteDAOImpl utenteDAO = new UtenteDAOImpl();
                    List<Utente> tuttiUtenti = utenteDAO.findAll();
                    
                    if (tuttiUtenti.isEmpty()) {
                        JOptionPane.showMessageDialog(frameAdminView, 
                            "Non ci sono utenti registrati nel sistema.", 
                            "Nessun utente", 
                            JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    
                    // Recupera gli hackathon dell'organizzatore
                    var hackathonList = controllerOrganizzatore.getHackathonDiOrganizzatore(adminLogged);
                    
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
                    
                    // Seleziona l'hackathon
                    String hackathonSelezionato = (String) JOptionPane.showInputDialog(
                        frameAdminView,
                        "Seleziona uno dei TUOI hackathon per cui invitare il giudice:\n" +
                        "(Puoi invitare giudici solo per gli hackathon di cui sei organizzatore)",
                        "Selezione Hackathon - Solo i tuoi eventi",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        hackathonTitoli,
                        hackathonTitoli[0]
                    );
                    
                    if (hackathonSelezionato == null) {
                        return; // Utente ha cancellato
                    }
                    
                    // Crea array per la selezione dell'utente
                    String[] utentiNomi = new String[tuttiUtenti.size()];
                    for (int i = 0; i < tuttiUtenti.size(); i++) {
                        utentiNomi[i] = tuttiUtenti.get(i).getName();
                    }
                    
                    // Mostra lista scrollabile degli utenti
                    JList<String> utentiList = new JList<>(utentiNomi);
                    utentiList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    utentiList.setVisibleRowCount(10);
                    
                    JScrollPane scrollPane = new JScrollPane(utentiList);
                    scrollPane.setPreferredSize(new java.awt.Dimension(300, 200));
                    
                    int result = JOptionPane.showConfirmDialog(
                        frameAdminView,
                        scrollPane,
                        "Seleziona l'utente da invitare come giudice",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                    );
                    
                    if (result == JOptionPane.OK_OPTION) {
                        String utenteSelezionato = utentiList.getSelectedValue();
                        
                        if (utenteSelezionato == null) {
                            JOptionPane.showMessageDialog(frameAdminView, 
                                "Devi selezionare un utente dalla lista.", 
                                "Nessuna selezione", 
                                JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        
                        // Invia l'invito con verifica di autorizzazione
                        try {
                            InvitoGiudiceDAOImpl invitoDAO = new InvitoGiudiceDAOImpl();
                            boolean invitato = invitoDAO.creaInvitoConVerifica(
                                adminLogged.getName(), 
                                utenteSelezionato, 
                                hackathonSelezionato
                            );
                            
                            if (invitato) {
                                JOptionPane.showMessageDialog(frameAdminView, 
                                    "Invito inviato con successo a " + utenteSelezionato + 
                                    " per l'hackathon \"" + hackathonSelezionato + "\"!", 
                                    "Invito inviato", 
                                    JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(frameAdminView, 
                                    "Errore nell'invio dell'invito. L'utente potrebbe essere già stato invitato.", 
                                    "Errore invito", 
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (SecurityException secEx) {
                            JOptionPane.showMessageDialog(frameAdminView, 
                                "Errore di autorizzazione: " + secEx.getMessage(), 
                                "Non autorizzato", 
                                JOptionPane.ERROR_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frameAdminView, 
                                "Errore durante l'invio dell'invito: " + ex.getMessage(), 
                                "Errore", 
                                JOptionPane.ERROR_MESSAGE);
                        }
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
     * Inizializza il pannello con i pulsanti dinamici per ogni hackathon organizzato.
     */
    private void initializeHackathonButtonsPanel(Organizzatore adminLogged, ControllerOrganizzatore controllerOrganizzatore) {
        // Pulisce il pannello esistente
        hackathonContentPanel.removeAll();
        hackathonContentPanel.setLayout(new BoxLayout(hackathonContentPanel, BoxLayout.Y_AXIS));
        
        // Recupera gli hackathon dell'organizzatore
        var hackathonList = controllerOrganizzatore.getHackathonDiOrganizzatore(adminLogged);
        
        if (hackathonList.isEmpty()) {
            // Se non ci sono hackathon, mostra un messaggio
            JLabel noHackathonLabel = new JLabel("Non hai ancora creato nessun hackathon.");
            noHackathonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            hackathonContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            hackathonContentPanel.add(noHackathonLabel);
            
            JLabel instructionLabel = new JLabel("Utilizza 'Crea Hackathon' per organizzare il tuo primo evento!");
            instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            hackathonContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            hackathonContentPanel.add(instructionLabel);
        } else {
            // Crea un pulsante per ogni hackathon
            JLabel titleLabel = new JLabel("I TUOI HACKATHON (clicca per i dettagli):");
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
            hackathonContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            hackathonContentPanel.add(titleLabel);
            hackathonContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            
            for (int i = 0; i < hackathonList.size(); i++) {
                Hackathon hackathon = hackathonList.get(i);
                
                // Crea il pulsante per questo hackathon
                JButton hackathonButton = new JButton();
                hackathonButton.setText("<html><div style='text-align: center;'>" +
                    "<b>" + hackathon.getTitoloIdentificativo() + "</b><br>" +
                    "Data: " + hackathon.getDataInizio() + " - " + hackathon.getDataFine() + "<br>" +
                    "Sede: " + hackathon.getSede() + "</div></html>");
                
                hackathonButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                hackathonButton.setMaximumSize(new Dimension(500, 80));
                hackathonButton.setPreferredSize(new Dimension(500, 80));
                
                // Aggiungi listener per mostrare i dettagli
                final Hackathon finalHackathon = hackathon;
                hackathonButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showHackathonDetails(finalHackathon);
                    }
                });
                
                hackathonContentPanel.add(hackathonButton);
                hackathonContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        // Aggiorna la visualizzazione
        hackathonContentPanel.revalidate();
        hackathonContentPanel.repaint();
    }
    
    /**
     * Mostra i dettagli di un hackathon in una finestra di dialogo.
     */
    private void showHackathonDetails(Hackathon hackathon) {
        String details = hackathon.printInfoEvento();
        
        JTextArea detailsArea = new JTextArea(details);
        detailsArea.setEditable(false);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setLineWrap(true);
        detailsArea.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(frameAdminView, 
            scrollPane, 
            "Dettagli Hackathon: " + hackathon.getTitoloIdentificativo(), 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Aggiorna la visualizzazione degli hackathon dopo modifiche.
     */
    public void refreshHackathonList(Organizzatore adminLogged, ControllerOrganizzatore controllerOrganizzatore) {
        initializeHackathonButtonsPanel(adminLogged, controllerOrganizzatore);
    }
}
