package model;

/**
 * Classe che rappresenta un documento caricato da un team durante un hackathon.
 * Contiene il contenuto testuale e il riferimento al team proprietario.
 */
public class Documento {
    /** Contenuto testuale del documento (non nullo). */
    private final String text;

    /** Team che ha caricato il documento (non nullo). */
    private final Team source;

    /**
     * Costruttore del documento.
     *
     * @param source Team proprietario del documento (non nullo)
     * @param text Contenuto testuale del documento (non nullo/vuoto)
     * @throws IllegalArgumentException Se source o text sono null, o se text è vuoto
     */
    public Documento(Team source, String text) {
        if (source == null) {
            throw new IllegalArgumentException("Il team sorgente non può essere nullo");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Il testo del documento non può essere nullo o vuoto");
        }
        this.text = text;
        this.source = source;
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
     * Restituisce il team proprietario del documento.
     *
     * @return Riferimento al Team (mai nullo)
     */
    public Team getSource() {
        return source;
    }
}