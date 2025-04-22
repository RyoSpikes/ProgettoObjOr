package model;

import java.util.ArrayList;

/**
 * Classe (provvisoria) che rappresenta un documento caricato da un team durante un hackathon.
 * Contiene il contenuto testuale, il titolo del documento ed il riferimento al team proprietario.
 */
public class Documento {
    /**
     * Contenuto testuale del documento (non nullo).
     */
    private final String text;

    /**
     * Titolo univoco del documento (non nullo).
     */
    private final String title;

    /**
     * Team che ha caricato il documento (non nullo).
     */
    private final Team source;

    /**
     * Lista delle valutazioni associate al documento.
     */
    private ArrayList<Valutazione> valutazioni;

    /**
     * Costruttore del documento.
     *
     * @param source Team proprietario del documento (non nullo)
     * @param title  Titolo univoco del documento (non nullo)
     * @param text   Contenuto testuale del documento (non nullo/vuoto)
     * @throws IllegalArgumentException Se source o text sono null, o se text è vuoto
     */
    public Documento(Team source, String title, String text) {
        if (title == null) {
            throw new IllegalArgumentException("Il titolo del documento non può essere nullo");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Il testo del documento non può essere nullo o vuoto");
        }
        this.text = text;
        this.title = title;
        this.source = source;
        this.valutazioni = new ArrayList<>();
    }

    /**
     * Restituisce il contenuto testuale del documento.
     *
     * @return Stringa contenente il testo (mai nullo/vuota)
     */
    public String getText() {
        return text;
    }

    /**
     * Restituisce il titolo del documento.
     *
     * @return Stringa contenente il titolo (mai nullo/vuota)
     */
    public String getTitle() {
        return title;
    }

    /**
     * Restituisce il team proprietario del documento.
     *
     * @return Riferimento al Team (mai nullo)
     */
    public Team getSource() {
        return source;
    }

    /**
     * Recupera le valutazioni di un documento.
     *
     * @return Valutazioni del documento (mai nulla se esistente)
     */
    public ArrayList<Valutazione> getValutazioni() {
        return valutazioni;
    }

    /**
     * Stampa le valutazioni del documento con i rispettivi giudici.
     */
    public void stampaValutazioni() {
        if (valutazioni == null || valutazioni.isEmpty()) {
            System.out.println("Nessuna valutazione disponibile per questo documento.");
            return;
        }

        System.out.println("Valutazioni del documento \"" + title + "\":");
        for (Valutazione valutazione : valutazioni) {
            System.out.println("Giudice: " + valutazione.getGiudiceValutante().getName() +
                    "\nCommento: " + valutazione.getGiudizio());
        }
    }
}