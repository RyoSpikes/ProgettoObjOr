package gui;

import Database.DAO.Impl.UtenteDAOImpl;
import Database.DAO.Impl.InvitoGiudiceDAOImpl;
import model.Hackathon;
import model.Organizzatore;
import model.Utente;
import controller.ControllerOrganizzatore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
     * @param controllerOrganizzatore Il controller per la gestione degli hackathon
     */
    public AdminView(Organizzatore adminLogged, JFrame frameCalling, ControllerOrganizzatore controllerOrganizzatore) {
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
        
        // Carica e mostra le hackathon nella textArea1
        loadHackathonInTextArea(adminLogged, controllerOrganizzatore);

        // Listener per il pulsante "Crea Hackathon"
        creaHackathonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreaHackathonForm(adminLogged, frameAdminView, frameCalling, controllerOrganizzatore);
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
                    List<Hackathon> hackathonList = controllerOrganizzatore.getHackathonDiOrganizzatore(adminLogged);
                    
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
     * Carica e visualizza le hackathon dell'organizzatore nella textArea1.
     * 
     * @param adminLogged L'organizzatore di cui visualizzare gli hackathon
     * @param controllerOrganizzatore Il controller per recuperare i dati dal database
     */
    private void loadHackathonInTextArea(Organizzatore adminLogged, ControllerOrganizzatore controllerOrganizzatore) {
        if (textArea1 == null) {
            System.out.println("ERRORE: textArea1 è null, impossibile visualizzare le hackathon");
            return;
        }
        
        // Recupera gli hackathon dell'organizzatore
        List<Hackathon> hackathonList = controllerOrganizzatore.getHackathonDiOrganizzatore(adminLogged);
        
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
}
