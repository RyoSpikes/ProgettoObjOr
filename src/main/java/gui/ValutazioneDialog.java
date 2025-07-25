package gui;

import Database.DAO.Impl.ValutazioneDAOImpl;
import model.Documento;
import model.Giudice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Dialog per la valutazione testuale di un documento da parte di un giudice.
 * Permette ai giudici di inserire commenti e valutazioni sui documenti caricati dai team.
 */
public class ValutazioneDialog extends JDialog {
    
    private final Documento documento;
    private final Giudice giudice;
    private JTextArea textAreaGiudizio;
    private JButton buttonSalva;
    private JButton buttonAnnulla;
    
    /**
     * Costruttore del dialog di valutazione.
     *
     * @param parent     Finestra padre
     * @param documento  Documento da valutare
     * @param giudice    Giudice che effettua la valutazione
     */
    public ValutazioneDialog(JFrame parent, Documento documento, Giudice giudice) {
        super(parent, "Valutazione Documento", true);
        this.documento = documento;
        this.giudice = giudice;
        
        initializeComponents();
        layoutComponents();
        setupEventHandlers();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(parent);
    }
    
    /**
     * Inizializza i componenti della GUI.
     */
    private void initializeComponents() {
        textAreaGiudizio = new JTextArea(10, 40);
        textAreaGiudizio.setLineWrap(true);
        textAreaGiudizio.setWrapStyleWord(true);
        textAreaGiudizio.setBorder(BorderFactory.createTitledBorder("Inserisci la tua valutazione"));
        
        buttonSalva = new JButton("Salva Valutazione");
        buttonAnnulla = new JButton("Annulla");
    }
    
    /**
     * Organizza i componenti nella finestra.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Panel informazioni documento
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informazioni Documento"));
        infoPanel.add(new JLabel("Titolo:"));
        infoPanel.add(new JLabel(documento.getTitle()));
        infoPanel.add(new JLabel("Team:"));
        infoPanel.add(new JLabel(documento.getSource().getNomeTeam()));
        infoPanel.add(new JLabel("Hackathon:"));
        infoPanel.add(new JLabel(documento.getSource().getHackathon().getTitoloIdentificativo()));
        
        // Panel contenuto documento
        JTextArea textAreaContenuto = new JTextArea(documento.getText());
        textAreaContenuto.setEditable(false);
        textAreaContenuto.setLineWrap(true);
        textAreaContenuto.setWrapStyleWord(true);
        JScrollPane scrollContenuto = new JScrollPane(textAreaContenuto);
        scrollContenuto.setBorder(BorderFactory.createTitledBorder("Contenuto Documento"));
        scrollContenuto.setPreferredSize(new Dimension(480, 150));
        
        // Panel valutazione testuale
        JScrollPane scrollValutazione = new JScrollPane(textAreaGiudizio);
        scrollValutazione.setPreferredSize(new Dimension(480, 120));
        
        // Panel bottoni
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(buttonSalva);
        buttonPanel.add(buttonAnnulla);
        
        // Aggiungi tutti i panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(infoPanel, BorderLayout.NORTH);
        topPanel.add(scrollContenuto, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollValutazione, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Configura i gestori degli eventi.
     */
    private void setupEventHandlers() {
        buttonSalva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvaValutazione();
            }
        });
        
        buttonAnnulla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Salva la valutazione nel database.
     */
    private void salvaValutazione() {
        String giudizio = textAreaGiudizio.getText().trim();
        String titoloHackathon = documento.getSource().getHackathon().getTitoloIdentificativo();
        
        if (giudizio.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Inserisci una valutazione prima di salvare.", 
                "Errore", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            ValutazioneDAOImpl valutazioneDAO = new ValutazioneDAOImpl();
            
            // Controlla se il giudice ha già valutato questo documento
            if (valutazioneDAO.hasGiudiceValutatoDocumento(giudice.getName(), documento.getIdDocumento())) {
                JOptionPane.showMessageDialog(this, 
                    "Hai già valutato questo documento.", 
                    "Attenzione", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Salva la valutazione testuale
            boolean successValutazione = valutazioneDAO.save(giudizio, documento.getIdDocumento(), 
                                                giudice.getName(), titoloHackathon);
            
            if (successValutazione) {
                JOptionPane.showMessageDialog(this, 
                    "Valutazione salvata con successo!", 
                    "Successo", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Errore durante il salvataggio della valutazione.", 
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Errore database: " + ex.getMessage(), 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
