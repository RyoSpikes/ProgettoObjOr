package utilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.function.Function;

/**
 * Utility class per implementare ricerca dinamica in componenti GUI.
 * Fornisce funzionalità comuni per filtrare liste di oggetti in tempo reale
 * mentre l'utente digita in un campo di testo.
 * 
 * @param <T> Il tipo degli oggetti da filtrare nella lista
 */
public class DynamicSearchHelper<T> {
    
    private final JTextField searchField;
    private final JList<T> resultList;
    private final DefaultListModel<T> listModel;
    private final JLabel infoLabel;
    private final List<T> allItems;
    private final Function<T, String> textExtractor;
    private final Runnable onSelectionChanged;

    /**
     * Costruttore per DynamicSearchHelper.
     * 
     * @param searchField Campo di testo per la ricerca
     * @param resultList Lista che mostra i risultati
     * @param listModel Modello della lista
     * @param infoLabel Label per informazioni sui risultati
     * @param allItems Lista completa di tutti gli elementi disponibili
     * @param textExtractor Funzione per estrarre il testo da cercare dall'oggetto
     * @param onSelectionChanged Callback chiamato quando la selezione cambia
     */
    public DynamicSearchHelper(JTextField searchField, JList<T> resultList, 
                             DefaultListModel<T> listModel, JLabel infoLabel,
                             List<T> allItems, Function<T, String> textExtractor,
                             Runnable onSelectionChanged) {
        this.searchField = searchField;
        this.resultList = resultList;
        this.listModel = listModel;
        this.infoLabel = infoLabel;
        this.allItems = allItems;
        this.textExtractor = textExtractor;
        this.onSelectionChanged = onSelectionChanged;
        
        setupDynamicSearch();
        loadAllItems(); // Carica tutti gli elementi all'inizio
    }

    /**
     * Configura la ricerca dinamica aggiungendo i listener necessari.
     */
    private void setupDynamicSearch() {
        // DocumentListener per la ricerca dinamica
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }
        });

        // Listener per i cambi di selezione
        resultList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && onSelectionChanged != null) {
                onSelectionChanged.run();
            }
        });
    }

    /**
     * Carica tutti gli elementi nella lista all'inizio.
     */
    private void loadAllItems() {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            if (allItems != null && !allItems.isEmpty()) {
                for (T item : allItems) {
                    listModel.addElement(item);
                }
                resultList.setEnabled(true);
                updateInfoLabel(allItems.size(), false);
            } else {
                resultList.setEnabled(false);
                infoLabel.setText("Nessun elemento disponibile");
                infoLabel.setBackground(Color.LIGHT_GRAY);
            }
        });
    }

    /**
     * Esegue la ricerca filtrando gli elementi in base al testo inserito.
     */
    private void performSearch() {
        SwingUtilities.invokeLater(() -> {
            String searchText = searchField.getText().trim();
            
            listModel.clear();
            
            if (searchText.isEmpty()) {
                // Se non c'è testo di ricerca, mostra tutti gli elementi
                if (allItems != null) {
                    for (T item : allItems) {
                        listModel.addElement(item);
                    }
                }
                if (allItems != null && !allItems.isEmpty()) {
                    resultList.setEnabled(true);
                    updateInfoLabel(allItems.size(), false);
                } else {
                    resultList.setEnabled(false);
                    infoLabel.setText("Nessun elemento disponibile");
                    infoLabel.setBackground(Color.LIGHT_GRAY);
                }
                return;
            }

            // Filtra gli elementi che contengono il testo cercato
            int foundCount = 0;
            if (allItems != null) {
                for (T item : allItems) {
                    String itemText = textExtractor.apply(item);
                    if (itemText != null && itemText.toLowerCase().contains(searchText.toLowerCase())) {
                        listModel.addElement(item);
                        foundCount++;
                    }
                }
            }

            if (foundCount > 0) {
                resultList.setEnabled(true);
                updateInfoLabel(foundCount, true);
            } else {
                resultList.setEnabled(false);
                infoLabel.setText("Nessun elemento trovato con questo criterio");
                infoLabel.setBackground(new Color(255, 200, 200)); // Rosso chiaro
            }
        });
    }

    /**
     * Aggiorna il label informativo con il numero di risultati.
     * 
     * @param count Numero di elementi trovati
     * @param isFiltered Se true, indica che i risultati sono filtrati
     */
    private void updateInfoLabel(int count, boolean isFiltered) {
        if (isFiltered) {
            infoLabel.setText(String.format("Trovati %d elementi corrispondenti", count));
            infoLabel.setBackground(new Color(200, 255, 200)); // Verde chiaro
        } else {
            infoLabel.setText(String.format("Mostrando tutti i %d elementi disponibili", count));
            infoLabel.setBackground(Color.LIGHT_GRAY);
        }
    }

    /**
     * Aggiorna la lista di tutti gli elementi disponibili e ricarica la vista.
     * 
     * @param newItems Nuova lista di elementi
     */
    public void updateAllItems(List<T> newItems) {
        // Sostituisce completamente la lista corrente
        allItems.clear();
        if (newItems != null) {
            allItems.addAll(newItems);
        }
        
        // Ricarica la vista
        String currentSearch = searchField.getText().trim();
        if (currentSearch.isEmpty()) {
            loadAllItems();
        } else {
            performSearch();
        }
    }

    /**
     * Ottiene l'elemento attualmente selezionato nella lista.
     * 
     * @return L'elemento selezionato o null se nessun elemento è selezionato
     */
    public T getSelectedItem() {
        return resultList.getSelectedValue();
    }

    /**
     * Pulisce il campo di ricerca e ricarica tutti gli elementi.
     */
    public void clearSearch() {
        searchField.setText("");
        loadAllItems();
    }
}
