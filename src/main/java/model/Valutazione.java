package model;

import java.util.ArrayList;

/**
 * Classe che rappresenta una valutazione assegnata da un giudice a un documento.
 */
public class Valutazione {
    /**
     * Documento sottoposto a valutazione.
     */
    private Documento documentoInValutazione;

    /**
     * Giudice che ha effettuato la valutazione.
     */
    private Giudice giudiceValutante;

    /**
     * Commento o giudizio testuale del giudice.
     */
    private String giudizio;

    /**
     * Costruttore della classe Valutazione.
     *
     * @param documentoInValutazione Documento da valutare
     * @param giudiceValutante Giudice che effettua la valutazione
     * @param valutazione Giudizio testuale sulla valutazione
     */
    public Valutazione(Documento documentoInValutazione, Giudice giudiceValutante, String valutazione) {
        this.documentoInValutazione = documentoInValutazione;
        this.giudiceValutante = giudiceValutante;
        this.giudizio = valutazione;
    }
}