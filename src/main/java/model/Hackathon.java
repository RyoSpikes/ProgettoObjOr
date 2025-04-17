package model;

import java.util.ArrayList;

/**
 * Classe che rappresenta un evento Hackathon con tutte le informazioni relative
 * all'organizzazione, tempistiche e partecipanti.
 */
public class Hackathon {
    /** Identificativo univoco numerico dell'evento */
    private final String idNum;
    /** Luogo di svolgimento dell'hackathon */
    private String sede;
    /** Data di inizio dell'evento */
    private String dataInizio;
    /** Data di conclusione dell'evento */
    private String dataFine;
    /** Data di apertura registrazioni */
    private String dataInizioRegistrazioni;
    /** Data di chiusura registrazioni */
    private String dataFineRegistrazioni;
    /** Titolo dell'evento */
    private String titolo;
    /** Numero massimo di membri per team */
    private int maxMembriTeam;
    /** Numero massimo di partecipanti totali */
    private int maxNumIscritti;
    /** Testo del problema/contest */
    private String descrizioneProblema;
    /** Lista dei giudici assegnati all'evento */
    private ArrayList<Giudice> giudiciEvento;

    /**
     * Costruttore principale per creare un nuovo evento Hackathon.
     *
     * @param idNum                   Identificativo univoco
     * @param sede                    Luogo di svolgimento
     * @param dataInizio              Data inizio evento
     * @param dataFine                Data fine evento
     * @param dataInizioRegistrazioni Data apertura iscrizioni
     * @param dataFineRegistrazioni   Data chiusura iscrizioni
     * @param titolo                  Titolo dell'evento
     * @param maxMembriTeam           Massimo membri per team
     * @param maxNumIscritti          Massimo partecipanti totali
     */
    public Hackathon(String idNum, String sede, String dataInizio, String dataFine,
                     String dataInizioRegistrazioni, String dataFineRegistrazioni,
                     String titolo, int maxMembriTeam, int maxNumIscritti) {
        this.idNum = idNum;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.dataInizioRegistrazioni = dataInizioRegistrazioni;
        this.dataFineRegistrazioni = dataFineRegistrazioni;
        this.titolo = titolo;
        this.maxMembriTeam = maxMembriTeam;
        this.maxNumIscritti = maxNumIscritti;
        giudiciEvento = new ArrayList<>();
    }

    /**
     * Imposta la descrizione del problema per l'hackathon.
     *
     * @param descrizioneProblema Testo completo del problema
     */
    public void setDescrizioneProblema(String descrizioneProblema) {
        this.descrizioneProblema = descrizioneProblema;
    }

    /**
     * Restituisce il numero massimo di membri per team.
     *
     * @return Numero massimo di membri consentiti
     */
    public int getMaxMembriTeam() {
        return maxMembriTeam;
    }

    /**
     * Stampa a console tutte le informazioni dell'evento formattate.
     */
    public void printInfoEvento() {
        System.out.println("- Titolo: " + titolo +
                "\n- Sede dell'evento: " + sede +
                "\n- Data Inizio dell'evento: " + dataInizio +
                "\n- Data Fine dell'evento: " + dataFine +
                "\n- Data Inizio iscrizioni: " + dataInizioRegistrazioni +
                "\n- Data Fine iscrizioni: " + dataFineRegistrazioni +
                "\n- Descrizione del problema: " +
                "\t" + descrizioneProblema);
    }

    /**
     * Aggiunge un giudice alla lista dei giudici assegnati all'evento.
     *
     * @param newGiudice Oggetto Giudice da aggiungere
     */
    public void aggiungiGiudice(Giudice newGiudice) {
        giudiciEvento.add(newGiudice);
    }

    /**
     * Restituisce l'identificativo numerico dell'evento.
     *
     * @return ID numerico dell'hackathon
     */
    public String getIdNum() {
        return idNum;
    }
}