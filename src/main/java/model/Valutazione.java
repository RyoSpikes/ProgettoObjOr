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


    //TODO: Cambiare giudizio -> valutazioneGiudice
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

    /**
     * Restituisce il giudizio autore della valutazione.
     *
     * @return Giudice valutante (non nullo)
     */
    public Giudice getGiudiceValutante() {
        return giudiceValutante;
    }

    /**
     * Restituisce il giudizio testuale.
     *
     * @return Giudizio testuale (non nullo)
     */
    public String getGiudizio() {
        return giudizio;
    }
}