package model;

import java.time.DateTimeException;
import java.util.*;
import java.time.LocalDateTime;

import utilities.RandomStringGenerator;

/**
 * Classe che rappresenta un evento Hackathon con tutte le informazioni relative
 * all'organizzazione, tempistiche e partecipanti.
 */
public class Hackathon {
    /** Titolo identificativo dell'hackathon (chiave primaria nel DB) */
    private final String titoloIdentificativo;
    /** Organizzatore dell'hackathon */
    private String organizzatore;
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

    //TODO: eliminare titolo, è stabilito come chiave primaria dal DB
    /** Numero massimo di membri per team */
    private int maxMembriTeam;
    /** Numero massimo di partecipanti totali */
    private int maxNumIscritti;
    /** Testo del problema/contest */
    private String descrizioneProblema;
    /** Lista dei giudici assegnati all'evento */
    private ArrayList<Giudice> giudiciEvento;
    /** Numero corrente degli iscritti all'hackathon */
    private int numIscritti;        //Ricavabile tramite Team
    /** Lista dei team iscritti all'evento che verrà ordinata per punteggio a fine evento */
    private ArrayList<Team> classifica;

    /**
     * Costruttore principale per creare un nuovo evento Hackathon.
     *
     * @param titoloIdentificativo    Titolo identificativo dell'hackathon (chiave primaria)
     * @param organizzatore           Username dell'organizzatore
     * @param sede                    Luogo di svolgimento
     * @param dataInizio              Data inizio evento
     * @param dataFine                Data fine evento
     * @param dataInizioRegistrazioni Data apertura iscrizioni
     * @param maxMembriTeam           Massimo membri per team
     * @param maxNumIscritti          Massimo partecipanti totali
     * @param descrizioneProblema     Descrizione del problema da risolvere
     * @throws DateTimeException      Messaggio di errore nel caso in cui la data di fine evento
     *                                preceda la data di inizio evento.
     */
    public Hackathon(String titoloIdentificativo, String organizzatore, String sede, LocalDateTime dataInizio, LocalDateTime dataFine,
                     LocalDateTime dataInizioRegistrazioni, int maxMembriTeam, int maxNumIscritti, String descrizioneProblema) throws DateTimeException {
        this.titoloIdentificativo = titoloIdentificativo;
        this.organizzatore = organizzatore;
        this.sede = sede;
        this.dataInizio = dataInizio;
        //Controllo sulla validità della data di fine evento: Non può essere prima dell'inizio dell'evento.
        if((this.dataFine = dataFine).isBefore(this.dataInizio)) {
            throw new DateTimeException("Data non valida!\nLa data di fine evento deve essere dopo la data di inizio.");
        }
        //Controllo sulla validità della data di inizio registrazioni:
        //      - Non può corrispondere ad una data da 2 giorni prima della data di inizio evento;
        if((this.dataInizioRegistrazioni = dataInizioRegistrazioni).isAfter(this.dataInizio.minusDays(2))) {
            throw new DateTimeException("Data non valida!\nLa data di inizio registrazioni non può essere 2 giorni prima dell'inizio dell'evento.");
        }
        // Le registrazioni finiscono esattamente 2 giorni prima dell'inizio dell'evento
        this.dataFineRegistrazioni = dataInizio.minusDays(2);
        this.maxMembriTeam = maxMembriTeam;
        this.maxNumIscritti = maxNumIscritti;
        this.descrizioneProblema = descrizioneProblema;
        giudiciEvento = new ArrayList<>();
        this.classifica = new ArrayList<>();
    }

    //Constructor for testing purposes
    public Hackathon(String id)
    {
        Random random = new Random();
        this.titoloIdentificativo = id;
        this.organizzatore = "testUser";
        this.sede = RandomStringGenerator.generateRandomString(12);

        this.dataInizio = LocalDateTime.of(2025, 12, 2 ,0, 0);
        this.dataFine = dataInizio.plusDays(365);
        this.dataInizioRegistrazioni = dataInizio.minusDays(365);
        this.dataFineRegistrazioni = dataInizio.minusDays(2);

        this.maxMembriTeam = 4;
        this.maxNumIscritti = random.nextInt(10, 100);
        this.descrizioneProblema = "Test problem description";
        giudiciEvento = new ArrayList<>();
        this.classifica = new ArrayList<>();

    }

    public String getTitoloIdentificativo() {
        return titoloIdentificativo;
    }
    
    public String getOrganizzatore() {
        return organizzatore;
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
    public String printInfoEvento() {
        return "- Titolo: " + titoloIdentificativo +
                "\n- Organizzatore: " + organizzatore +
                "\n- Sede dell'evento: " + sede +
                "\n- Data Inizio dell'evento: " + dataInizio +
                "\n- Data Fine dell'evento: " + dataFine +
                "\n- Data Inizio iscrizioni: " + dataInizioRegistrazioni +
                "\n- Data Fine iscrizioni: " + dataFineRegistrazioni +
                "\n- Descrizione del problema: " +
                "\n" + descrizioneProblema + "\n";
    }

    /**
     * Aggiunge un giudice alla lista dei giudici assegnati all'evento.
     *
     * @param newGiudice Oggetto Giudice da aggiungere
     */
    public void aggiungiGiudice(Giudice newGiudice) {
        giudiciEvento.add(newGiudice);
    }


    /** Restituisce la data della fine dell'evento.
     *
     * @return Data di fine evento
     */
    public LocalDateTime getDataFine() {return dataFine;}

    /** Restituisce la data dell'inizio dell'evento.
     *
     * @return Data di inizio evento
     */
    public LocalDateTime getDataInizio() {
        return dataInizio;
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
    public LocalDateTime getDataFineRegistrazioni() {return dataFineRegistrazioni;}

    /**
     * Restituisce la classifica dell'evento.
     *
     * @return Lista dei team ordinata per punteggio
     */
    public ArrayList<Team> getClassifica() {return classifica;}


    //TODO: funzione incrementaNumIscritti() va eliminata, in quanto gestita tramite TRIGGER
    /**
     * Incrementa di 1 il numero di iscritti all'evento.
     */
    public void incrementaNumIscritti() {this.numIscritti++;}

    /**
     * Restituisce la sede dell'hackathon.
     *
     * @return Sede dell'evento
     */
    public String getSede() {
        return sede;
    }

    /**
     * Restituisce la descrizione del problema dell'hackathon.
     *
     * @return Descrizione del problema
     */
    public String getDescrizioneProblema() {
        return descrizioneProblema;
    }

    /**
     * Restituisce il numero massimo di iscritti all'hackathon.
     *
     * @return Numero massimo di iscritti
     */
    public int getMaxNumIscritti() {
        return maxNumIscritti;
    }

    /**
     * Restituisce il numero corrente di iscritti all'hackathon.
     *
     * @return Numero corrente di iscritti
     */
    public int getNumIscritti() {
        return numIscritti;
    }

    /**
     * Imposta il numero corrente di iscritti all'hackathon.
     *
     * @param numIscritti Numero corrente di iscritti
     */
    public void setNumIscritti(int numIscritti) {
        this.numIscritti = numIscritti;
    }

    // I SEGUENTI METODI SONO STATI RIMOSSI PERCHÉ LA LORO LOGICA
    // È ORA GESTITA DIRETTAMENTE DAL DATABASE TRAMITE TRIGGER E FUNZIONI:
    //
    // - controlloValiditaDataReg(): Gestito dal trigger 'verifica_adesione_valida'.
    // - controlloMaxIscritti(): Gestito dal trigger 'verifica_adesione_valida'.
    // - ordinaStampaClassifica(): Gestito dalla funzione 'genera_classifica_hackathon'.
}
