package model;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.time.LocalDateTime;

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
    private LocalDateTime dataInizio;
    /** Data di conclusione dell'evento */
    private LocalDateTime dataFine;
    /** Data di apertura registrazioni */
    private LocalDateTime dataInizioRegistrazioni;
    /** Data di chiusura registrazioni */
    private LocalDateTime dataFineRegistrazioni;
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
     * @param titolo                  Titolo dell'evento
     * @param maxMembriTeam           Massimo membri per team
     * @param maxNumIscritti          Massimo partecipanti totali
     * @throws DateTimeException      Messaggio di errore nel caso in cui la data di fine evento
     *                                preceda la data di inizio evento.
     */
    public Hackathon(String idNum, String sede, LocalDateTime dataInizio, LocalDateTime dataFine,
                     LocalDateTime dataInizioRegistrazioni, String titolo,
                     int maxMembriTeam, int maxNumIscritti) {
        this.idNum = idNum;
        this.sede = sede;
        this.dataInizio = dataInizio;
        //Controllo sulla validità della data di fine evento: Non può essere prima dell'inizio dell'evento.
        if((this.dataFine = dataFine).isBefore(this.dataInizio)) {
            throw new DateTimeException("Data non valida\nLa data di fine evento deve essere dopo la data di inizio.");
        }
        //Controllo sulla validità della data di inizio registrazioni:
        //      - Non può corrispondere ad una data da 2 giorni prima della data di inizio evento;
        if((this.dataInizioRegistrazioni = dataInizioRegistrazioni).isAfter(this.dataInizio.minusDays(2)))
        {
            throw new DateTimeException("Data non valida!");
        }
        // Le registrazioni finiscono esattamente 2 giorni prima dell'inizio dell'evento
        this.dataFineRegistrazioni = dataInizio.minusDays(2);
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

    /**
     * Restituisce la data dalla quale sarà possibile registrarsi.
     *
     * @return Data di inizio delle registrazioni
     */
    public LocalDateTime getDataInizioRegistrazioni() {
        return dataInizioRegistrazioni;
    }

    /**
     * Restituisce la data della scadenza delle registrazioni.
     *
     * @return Data di scadenza delle registrazioni
     */
    public LocalDateTime getDataFineRegistrazioni() {
        return dataFineRegistrazioni;
    }
}