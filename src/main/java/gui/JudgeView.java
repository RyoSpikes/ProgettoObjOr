package gui;

import Database.DAO.Impl.DocumentoDAOImpl;
import Database.DAO.Impl.MembershipDAOImpl;
import Database.DAO.Impl.HackathonDAOImpl;
import model.Utente;
import model.Documento;
import model.Giudice;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * La classe JudgeView rappresenta l'interfaccia grafica per la gestione delle funzionalità da giudice.
 * Permette di visualizzare team e documenti per un hackathon specifico.
 */
public class JudgeView {
    private JButton mostraTeamButton;
    private JButton mostraDocumentiButton;
    private JTextArea MENUGIUDICETextArea;
    private JPanel mainPanel;
    private JButton mostraClassificaButton;

    private JFrame judgeViewFrame;
    private String titoloHackathon;
    private Utente giudice;
    private JFrame parentFrame;
    private DocumentoDAOImpl documentoDAO;
    private MembershipDAOImpl membershipDAO;
    private HackathonDAOImpl hackathonDAO;

    /**
     * Costruttore della classe JudgeView.
     * 
     * @param titoloHackathon Il titolo dell'hackathon da giudicare
     * @param giudice L'utente che funge da giudice
     * @param parentFrame Il frame genitore
     */
    public JudgeView(String titoloHackathon, Utente giudice, JFrame parentFrame) {
        this.titoloHackathon = titoloHackathon;
        this.giudice = giudice;
        this.parentFrame = parentFrame;
        
        // Inizializza l'interfaccia utente dal form
        $$$setupUI$$$();
        
        // Inizializza i DAO
        try {
            this.documentoDAO = new DocumentoDAOImpl();
            this.membershipDAO = new MembershipDAOImpl();
            this.hackathonDAO = new HackathonDAOImpl();
        } catch (Exception e) {
            System.err.println("Errore nell'inizializzazione dei DAO: " + e.getMessage());
        }
        
        judgeViewFrame = new JFrame("Giudice - " + titoloHackathon);
        judgeViewFrame.setContentPane(mainPanel);
        judgeViewFrame.pack();
        judgeViewFrame.setSize(700, 500);
        judgeViewFrame.setLocationRelativeTo(parentFrame);
        
        // Listener per gestire la chiusura della finestra
        judgeViewFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
                judgeViewFrame.dispose();
            }
        });
        
        // Carica le informazioni iniziali
        caricaInformazioniIniziali();
        
        // Configura i listener dei pulsanti
        setupListeners();
        
        judgeViewFrame.setVisible(true);
    }
    
    /**
     * Carica le informazioni iniziali nell'area di testo.
     */
    private void caricaInformazioniIniziali() {
        if (MENUGIUDICETextArea != null) {
            MENUGIUDICETextArea.setText("");
            MENUGIUDICETextArea.append("=== VISTA GIUDICE ===\n\n");
            MENUGIUDICETextArea.append("Hackathon: " + titoloHackathon + "\n");
            MENUGIUDICETextArea.append("Giudice: " + giudice.getName() + "\n\n");
            MENUGIUDICETextArea.append("📋 Istruzioni:\n");
            MENUGIUDICETextArea.append("• Usa 'Visualizza Team' per aprire la vista di un team\n");
            MENUGIUDICETextArea.append("• Usa 'Mostra Documenti' per vedere i documenti caricati\n\n");
            MENUGIUDICETextArea.append("Seleziona un'opzione per iniziare...\n");
            
            // Posiziona il cursore all'inizio
            MENUGIUDICETextArea.setCaretPosition(0);
        }
    }
    
    /**
     * Configura i listener dei pulsanti.
     */
    private void setupListeners() {
        // Listener per il pulsante "Mostra Team"
        if (mostraTeamButton != null) {
            mostraTeamButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mostraTeamHackathon();
                }
            });
        }
        
        // Listener per il pulsante "Mostra Documenti"
        if (mostraDocumentiButton != null) {
            mostraDocumentiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mostraDocumentiHackathon();
                }
            });
        }
        
        // Listener per il pulsante "Mostra Classifica"
        if (mostraClassificaButton != null) {
            mostraClassificaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mostraClassificaHackathon();
                }
            });
        }
    }
    
    /**
     * Mostra tutti i team dell'hackathon nella textarea.
     */
    private void mostraTeamHackathon() {
        if (MENUGIUDICETextArea != null && membershipDAO != null) {
            try {
                // Ottieni tutti i team per questo hackathon
                List<String> teamNames = membershipDAO.getTeamsForHackathon(titoloHackathon);
                
                if (teamNames != null && !teamNames.isEmpty()) {
                    MENUGIUDICETextArea.setText("");
                    MENUGIUDICETextArea.append("🏆 TEAM DELL'HACKATHON: " + titoloHackathon + "\n");
                    MENUGIUDICETextArea.append("═".repeat(60) + "\n\n");
                    
                    int teamNumber = 1;
                    for (String teamName : teamNames) {
                        MENUGIUDICETextArea.append("🔸 Team " + teamNumber + ": " + teamName + "\n");
                        
                        // Ottieni i membri del team
                        List<Utente> membri = membershipDAO.getTeamMembers(teamName, titoloHackathon);
                        if (membri != null && !membri.isEmpty()) {
                            MENUGIUDICETextArea.append("   👥 Membri:\n");
                            for (Utente membro : membri) {
                                MENUGIUDICETextArea.append("      • " + membro.getName() + "\n");
                            }
                        } else {
                            MENUGIUDICETextArea.append("   ❌ Nessun membro trovato\n");
                        }
                        MENUGIUDICETextArea.append("\n");
                        teamNumber++;
                    }
                    
                    MENUGIUDICETextArea.append("═".repeat(60) + "\n");
                    MENUGIUDICETextArea.append("📊 Totale team: " + teamNames.size() + "\n");
                    MENUGIUDICETextArea.setCaretPosition(0);
                } else {
                    // Mostra messaggio nell'area di testo se non ci sono team
                    MENUGIUDICETextArea.setText("");
                    MENUGIUDICETextArea.append("❌ Nessun team trovato per questo hackathon.\n");
                    MENUGIUDICETextArea.append("\nI team potrebbero non essersi ancora iscritti\n");
                    MENUGIUDICETextArea.append("o potrebbero esserci problemi di connessione al database.\n");
                    MENUGIUDICETextArea.setCaretPosition(0);
                }
                
            } catch (Exception ex) {
                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("❌ ERRORE NEL CARICAMENTO DEI TEAM\n\n");
                MENUGIUDICETextArea.append("Dettagli errore: " + ex.getMessage() + "\n");
                MENUGIUDICETextArea.append("\nVerifica la connessione al database e riprova.");
            }
        } else {
            if (MENUGIUDICETextArea != null) {
                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("❌ ERRORE DI INIZIALIZZAZIONE\n\n");
                MENUGIUDICETextArea.append("Il sistema non è stato inizializzato correttamente.\n");
            }
        }
    }
    
    /**
     * Mostra una finestra per selezionare e valutare un documento.
     */
    private void mostraDocumentiHackathon() {
        if (documentoDAO == null) {
            JOptionPane.showMessageDialog(judgeViewFrame, 
                "Errore di inizializzazione del sistema.", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Ottieni tutti i documenti per questo hackathon
            List<Documento> documenti = documentoDAO.getDocumentiByHackathon(titoloHackathon);
            
            if (documenti == null || documenti.isEmpty()) {
                JOptionPane.showMessageDialog(judgeViewFrame, 
                    "Nessun documento trovato per questo hackathon.\n" +
                    "I team potrebbero non aver ancora caricato documenti.", 
                    "Nessun Documento", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Crea array di opzioni per il dropdown
            String[] opzioniDocumenti = new String[documenti.size()];
            for (int i = 0; i < documenti.size(); i++) {
                Documento doc = documenti.get(i);
                opzioniDocumenti[i] = String.format("[%s] %s", 
                    doc.getSource().getNomeTeam(), 
                    doc.getTitle());
            }
            
            // Mostra dialog di selezione documento
            String documentoSelezionato = (String) JOptionPane.showInputDialog(
                judgeViewFrame,
                "Seleziona il documento da valutare:",
                "Selezione Documento",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opzioniDocumenti,
                opzioniDocumenti[0]
            );
            
            if (documentoSelezionato == null) {
                return; // Utente ha annullato
            }
            
            // Trova il documento selezionato
            int indiceSelezionato = -1;
            for (int i = 0; i < opzioniDocumenti.length; i++) {
                if (opzioniDocumenti[i].equals(documentoSelezionato)) {
                    indiceSelezionato = i;
                    break;
                }
            }
            
            if (indiceSelezionato != -1) {
                Documento docDaValutare = documenti.get(indiceSelezionato);
                
                // Importiamo e creiamo il giudice dal modello esistente
                model.Giudice giudiceModel = new model.Giudice(
                    giudice.getName(), 
                    giudice.getPassword(), 
                    docDaValutare.getSource().getHackathon()
                );
                
                // Apri dialog di valutazione
                ValutazioneDialog valutazioneDialog = new ValutazioneDialog(
                    judgeViewFrame, 
                    docDaValutare, 
                    giudiceModel
                );
                valutazioneDialog.setVisible(true);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(judgeViewFrame, 
                "Errore durante il caricamento dei documenti:\n" + ex.getMessage(), 
                "Errore Database", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Mostra la classifica dell'hackathon utilizzando la funzione del database.
     */
    private void mostraClassificaHackathon() {
        if (MENUGIUDICETextArea != null && hackathonDAO != null) {
            try {
                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("🏆 GENERAZIONE CLASSIFICA HACKATHON: " + titoloHackathon + "\n");
                MENUGIUDICETextArea.append("═".repeat(70) + "\n\n");
                MENUGIUDICETextArea.append("⏳ Elaborazione in corso...\n\n");
                
                // Chiama la funzione del database per generare la classifica
                String risultatoClassifica = hackathonDAO.generaClassificaHackathon(titoloHackathon);
                
                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("🏆 CLASSIFICA HACKATHON: " + titoloHackathon + "\n");
                MENUGIUDICETextArea.append("═".repeat(70) + "\n\n");
                
                // Verifica se il risultato è un errore
                if (risultatoClassifica.startsWith("Errore:")) {
                    MENUGIUDICETextArea.append("❌ " + risultatoClassifica + "\n\n");
                    
                    // Aggiungi suggerimenti basati sul tipo di errore
                    if (risultatoClassifica.contains("prima della fine dell'hackathon")) {
                        MENUGIUDICETextArea.append("💡 Suggerimento: La classifica può essere generata solo dopo la fine dell'hackathon.\n");
                    } else if (risultatoClassifica.contains("Mancano") && risultatoClassifica.contains("voti")) {
                        MENUGIUDICETextArea.append("💡 Suggerimento: Tutti i giudici devono votare tutti i team prima di generare la classifica.\n");
                        MENUGIUDICETextArea.append("📋 Verifica che ogni giudice abbia espresso il proprio voto per ogni team partecipante.\n");
                    } else if (risultatoClassifica.contains("non trovato")) {
                        MENUGIUDICETextArea.append("💡 Suggerimento: Verifica che il nome dell'hackathon sia corretto.\n");
                    }
                    
                } else {
                    // Classifica generata con successo - formatta e mostra
                    MENUGIUDICETextArea.append("✅ Classifica generata con successo!\n\n");
                    
                    String[] righeClassifica = risultatoClassifica.split("\n");
                    
                    for (String riga : righeClassifica) {
                        if (riga.trim().isEmpty()) continue;
                        
                        String[] parti = riga.trim().split(" ");
                        if (parti.length >= 3) {
                            int posizione = Integer.parseInt(parti[0]);
                            String nomeTeam = parti[1];
                            String punteggio = parti[2];
                            
                            // Icone per le prime posizioni
                            String icona = "";
                            if (posizione == 1) {
                                icona = "🥇 ";
                            } else if (posizione == 2) {
                                icona = "🥈 ";
                            } else if (posizione == 3) {
                                icona = "🥉 ";
                            } else {
                                icona = "   ";
                            }
                            
                            MENUGIUDICETextArea.append(String.format("%s%d°. %-25s Punteggio: %s\n", 
                                icona, posizione, nomeTeam, punteggio));
                        }
                    }
                    
                    MENUGIUDICETextArea.append("\n" + "═".repeat(70) + "\n");
                    MENUGIUDICETextArea.append("📊 Classifica aggiornata nel database\n");
                    MENUGIUDICETextArea.append("🎯 Totale team classificati: " + righeClassifica.length + "\n");
                }
                
                // Posiziona il cursore all'inizio
                MENUGIUDICETextArea.setCaretPosition(0);
                
            } catch (Exception ex) {
                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("❌ ERRORE NELLA GENERAZIONE DELLA CLASSIFICA\n\n");
                MENUGIUDICETextArea.append("Dettagli errore: " + ex.getMessage() + "\n\n");
                MENUGIUDICETextArea.append("Possibili cause:\n");
                MENUGIUDICETextArea.append("• Problemi di connessione al database\n");
                MENUGIUDICETextArea.append("• L'hackathon non esiste\n");
                MENUGIUDICETextArea.append("• Errore nella funzione del database\n");
                MENUGIUDICETextArea.append("\nVerifica la connessione e riprova.");
                ex.printStackTrace(); // Per debug
            }
        } else {
            if (MENUGIUDICETextArea != null) {
                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("❌ ERRORE DI INIZIALIZZAZIONE\n\n");
                MENUGIUDICETextArea.append("Il sistema non è stato inizializzato correttamente.\n");
            }
        }
    }

    /**
     * Metodo generato automaticamente da IntelliJ IDEA per inizializzare i componenti del form.
     * Questo metodo viene generato dal file JudgeView.form.
     */
    private void $$$setupUI$$$() {
        // Questo metodo sarà generato automaticamente da IntelliJ IDEA
        // dal file JudgeView.form quando il progetto viene compilato
    }
}