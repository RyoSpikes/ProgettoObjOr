package gui.dialogs;

import controller.HackathonController;
import model.*;
import utilities.DynamicSearchHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * La classe CercaDocumentoDialog rappresenta un'interfaccia grafica per consentire a un giudice di cercare
 * e selezionare un documento tramite ricerca dinamica con lista cliccabile.
 */
public class CercaDocumentoDialog {
    private JTextField titoloDocumentoField; // Campo di testo per inserire il titolo del documento.
    private JList<Documento> documentoList; // Lista per mostrare i documenti trovati.
    private DefaultListModel<Documento> listModel; // Modello per la lista dei documenti.
    private JButton btnSeleziona; // Pulsante per confermare la selezione del documento.
    private JLabel infoLabel; // Label per mostrare informazioni sui risultati.
    private DynamicSearchHelper<Documento> searchHelper; // Helper per la ricerca dinamica.

    /**
     * Costruttore della classe CercaDocumentoDialog.
     *
     * @param parentFrame Il frame genitore che ha aperto questa finestra.
     * @param titoloHackathon Il titolo dell'hackathon per filtrare i documenti.
     * @param giudice Il giudice che sta effettuando la ricerca.
     * @param hackathonController Il controller per gestire i documenti.
     * @param callback Callback da chiamare quando un documento viene selezionato.
     */
    public CercaDocumentoDialog(JFrame parentFrame, String titoloHackathon, Utente giudice, 
                               HackathonController hackathonController, DocumentoSelectionCallback callback) {

        // Ottiene tutti i documenti dall'hackathon tramite il controller
        List<Documento> documentiDisponibili = hackathonController.getDocumentiHackathon(titoloHackathon);
        
        if (documentiDisponibili == null || documentiDisponibili.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame,
                "Nessun documento trovato per questo hackathon.\n" +
                "I team potrebbero non aver ancora caricato documenti.",
                "Nessun Documento",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Crea la finestra di dialogo personalizzata
        JDialog dialog = new JDialog(parentFrame, "Cerca Documento - " + titoloHackathon, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(700, 600);
        dialog.setLocationRelativeTo(parentFrame);

        // Crea il pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Pannello superiore per informazioni
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Hackathon: " + titoloHackathon));
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(new JLabel("Giudice: " + giudice.getName()));

        // Pannello per la ricerca
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Ricerca Documento"));
        searchPanel.add(new JLabel("Cerca Titolo:"), BorderLayout.WEST);
        titoloDocumentoField = new JTextField(25);
        titoloDocumentoField.setToolTipText("Digita per cercare documenti in tempo reale");
        searchPanel.add(titoloDocumentoField, BorderLayout.CENTER);

        // Pannello centrale per la lista dei documenti
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Documenti Disponibili"));

        // Inizializza la lista e il modello
        listModel = new DefaultListModel<>();
        documentoList = new JList<>(listModel);
        documentoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Renderer personalizzato per la lista
        documentoList.setCellRenderer(new DocumentoListCellRenderer(hackathonController, giudice));

        // ScrollPane per la lista
        JScrollPane listScrollPane = new JScrollPane(documentoList);
        listScrollPane.setPreferredSize(new Dimension(600, 300));
        centerPanel.add(listScrollPane, BorderLayout.CENTER);

        // Label informativa
        infoLabel = new JLabel();
        infoLabel.setOpaque(true);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        centerPanel.add(infoLabel, BorderLayout.SOUTH);

        // Pannello inferiore per i pulsanti
        JPanel bottomPanel = new JPanel(new FlowLayout());
        btnSeleziona = new JButton("Seleziona Documento");
        btnSeleziona.setEnabled(false);
        JButton btnChiudi = new JButton("Chiudi");
        bottomPanel.add(btnSeleziona);
        bottomPanel.add(btnChiudi);

        // Layout
        JPanel combinedCenter = new JPanel(new BorderLayout());
        combinedCenter.add(searchPanel, BorderLayout.NORTH);
        combinedCenter.add(centerPanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(combinedCenter, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);

        // Inizializza l'helper per la ricerca dinamica
        searchHelper = new DynamicSearchHelper<>(
            titoloDocumentoField,
            documentoList,
            listModel,
            infoLabel,
            documentiDisponibili,
            documento -> documento.getTitle(), // Estrae il titolo per la ricerca
            () -> {
                // Callback per quando la selezione cambia
                Documento documentoSelezionato = documentoList.getSelectedValue();
                if (documentoSelezionato != null) {
                    infoLabel.setText(String.format(
                        "<html><b>Documento selezionato:</b> %s<br><b>Team:</b> %s<br><b>Stato:</b> Pronto per la valutazione</html>",
                        documentoSelezionato.getTitle(),
                        documentoSelezionato.getSource().getNomeTeam()
                    ));
                    infoLabel.setBackground(new Color(200, 200, 255)); // Blu chiaro
                    btnSeleziona.setEnabled(true);
                } else {
                    btnSeleziona.setEnabled(false);
                }
            }
        );

        // Listener per il pulsante "Seleziona Documento"
        btnSeleziona.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Documento documentoSelezionato = searchHelper.getSelectedItem();
                if (documentoSelezionato == null) {
                    JOptionPane.showMessageDialog(dialog, "Seleziona un documento dalla lista!",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Chiama il callback con il documento selezionato
                boolean chiudiDialog = false;
                if (callback != null) {
                    chiudiDialog = callback.onDocumentoSelected(documentoSelezionato);
                }

                // Chiude il dialog solo se il callback lo richiede
                if (chiudiDialog) {
                    dialog.dispose();
                }
            }
        });

        // Listener per il pulsante "Chiudi"
        btnChiudi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Chiude il dialog
            }
        });

        dialog.setVisible(true);
    }

    /**
     * Interface per il callback di selezione documento
     */
    public interface DocumentoSelectionCallback {
        /**
         * Chiamato quando un documento viene selezionato.
         * @param documento Il documento selezionato
         * @return true se il dialog deve essere chiuso, false per mantenerlo aperto
         */
        boolean onDocumentoSelected(Documento documento);
    }
}

/**
 * Renderer personalizzato per la lista dei documenti
 */
class DocumentoListCellRenderer extends DefaultListCellRenderer {
    
    private HackathonController hackathonController;
    private Utente giudice;
    
    public DocumentoListCellRenderer(HackathonController hackathonController, Utente giudice) {
        this.hackathonController = hackathonController;
        this.giudice = giudice;
    }
    
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Documento) {
            Documento documento = (Documento) value;
            
            // Controlla se il documento è già stato valutato dal giudice
            boolean giaValutato = false;
            if (hackathonController != null && giudice != null) {
                giaValutato = hackathonController.hasGiudiceValutatoDocumento(
                    giudice.getName(), 
                    documento.getIdDocumento()
                );
            }

            // Testo da mostrare con informazioni dettagliate
            String status = giaValutato ? " ✓ VALUTATO" : "";
            setText(String.format("<html><b>%s</b>%s<br><small>Team: %s</small></html>",
                documento.getTitle(),
                status,
                documento.getSource().getNomeTeam()));

            // Imposta colori in base allo stato
            if (giaValutato) {
                if (isSelected) {
                    setForeground(Color.WHITE);
                    setBackground(new Color(100, 150, 100)); // Verde scuro quando selezionato
                } else {
                    setForeground(new Color(0, 120, 0)); // Verde scuro
                    setBackground(new Color(240, 255, 240)); // Verde chiaro come sfondo
                }
            } else {
                setForeground(isSelected ? Color.WHITE : Color.BLACK);
                setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            }

            // Imposta tooltip con informazioni aggiuntive
            String tooltipStatus = giaValutato ? "<br><b>Stato:</b> Già valutato da te" : "<br><b>Stato:</b> Non ancora valutato";
            setToolTipText(String.format(
                "<html><b>Titolo:</b> %s<br><b>Team:</b> %s<br><b>Hackathon:</b> %s%s</html>",
                documento.getTitle(),
                documento.getSource().getNomeTeam(),
                documento.getSource().getHackathon().getTitoloIdentificativo(),
                tooltipStatus
            ));
        }

        return this;
    }
}
