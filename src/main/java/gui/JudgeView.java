package gui;

import Database.DAO.Impl.DocumentoDAOImpl;
import Database.DAO.Impl.MembershipDAOImpl;
import model.Utente;
import model.Documento;

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
    
    private JFrame judgeViewFrame;
    private String titoloHackathon;
    private Utente giudice;
    private JFrame parentFrame;
    private DocumentoDAOImpl documentoDAO;
    private MembershipDAOImpl membershipDAO;

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
     * Mostra tutti i documenti dell'hackathon.
     */
    private void mostraDocumentiHackathon() {
        if (MENUGIUDICETextArea != null && documentoDAO != null) {
            try {
                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("📄 DOCUMENTI DELL'HACKATHON: " + titoloHackathon + "\n");
                MENUGIUDICETextArea.append("=" + "=".repeat(50) + "\n\n");
                
                // Ottieni tutti i documenti per questo hackathon
                List<Documento> documenti = documentoDAO.getDocumentiByHackathon(titoloHackathon);
                
                if (documenti != null && !documenti.isEmpty()) {
                    String currentTeam = "";
                    int docCount = 1;
                    
                    for (Documento documento : documenti) {
                        String teamName = documento.getSource().getNomeTeam();
                        
                        // Raggruppa per team
                        if (!teamName.equals(currentTeam)) {
                            if (!currentTeam.isEmpty()) {
                                MENUGIUDICETextArea.append("\n");
                            }
                            currentTeam = teamName;
                            MENUGIUDICETextArea.append("🏆 TEAM: " + teamName + "\n");
                            MENUGIUDICETextArea.append("   " + "-".repeat(40) + "\n");
                        }
                        
                        MENUGIUDICETextArea.append("   " + docCount + ". Documento: " + documento.getTitle() + "\n");
                        
                        // Mostra contenuto limitato per evitare overflow
                        String contenuto = documento.getText();
                        if (contenuto.length() > 200) {
                            contenuto = contenuto.substring(0, 200) + "...";
                        }
                        MENUGIUDICETextArea.append("      Contenuto: " + contenuto + "\n\n");
                        docCount++;
                    }
                    
                    MENUGIUDICETextArea.append("📊 Totale documenti: " + documenti.size() + "\n");
                } else {
                    MENUGIUDICETextArea.append("❌ Nessun documento trovato per questo hackathon.\n");
                    MENUGIUDICETextArea.append("\nI team potrebbero non aver ancora caricato documenti\n");
                    MENUGIUDICETextArea.append("o potrebbero esserci problemi di connessione al database.\n");
                }
                
                // Posiziona il cursore all'inizio
                MENUGIUDICETextArea.setCaretPosition(0);
                
            } catch (Exception ex) {
                MENUGIUDICETextArea.setText("");
                MENUGIUDICETextArea.append("❌ ERRORE NEL CARICAMENTO DEI DOCUMENTI\n\n");
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
     * Metodo generato automaticamente da IntelliJ IDEA per inizializzare i componenti del form.
     * Questo metodo viene generato dal file JudgeView.form.
     */
    private void $$$setupUI$$$() {
        // Questo metodo sarà generato automaticamente da IntelliJ IDEA
        // dal file JudgeView.form quando il progetto viene compilato
    }
}