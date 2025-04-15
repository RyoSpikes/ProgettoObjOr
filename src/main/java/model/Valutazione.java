package model;

import java.util.ArrayList;

public class Valutazione {
    private Documento documentoInValutazione;
    private Giudice giudiceValutante;
    private String valutazione;

    public Valutazione(Documento documentoInValutazione, Giudice giudiceValutante, String valutazione) {
        this.documentoInValutazione = documentoInValutazione;
        this.giudiceValutante = giudiceValutante;
        this.valutazione = valutazione;
    }
}
