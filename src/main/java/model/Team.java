package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Classe che rappresenta un team partecipante a un hackathon.
 * Gestisce la lista dei membri (dinamica), la documentazione e il punteggio.
 */
public class Team {
    /** Lista dinamica dei membri del team. */
    private ArrayList<Utente> membro;
    /** Nome identificativo del team (univoco per hackathon). */
    private final String nomeTeam;
    /** Hackathon a cui il team è iscritto. */
    private final Hackathon eventoPartecipazione;
    /** Lista dei documenti caricati dal team. */
    private ArrayList<Documento> documentazione;
    /** Punteggio totale accumulato dal team. */
    private int votoFinale = 0;
    /** Numero di voti ricevuti */
    private int numeroVoti = 0;

    /**
     * Costruttore del team.
     * @param evento Hackathon di riferimento (non nullo).
     * @param nomeTeam Nome del team (non nullo/vuoto).
     * @throws IllegalArgumentException Se evento o nomeTeam sono null/vuoti.
     */
    public Team(Hackathon evento, String nomeTeam) {
        if (evento == null || nomeTeam == null || nomeTeam.trim().isEmpty()) {
            throw new IllegalArgumentException("Parametri non validi");
        }
        this.membro = new ArrayList<>();
        this.eventoPartecipazione = evento;
        this.nomeTeam = nomeTeam;
        this.documentazione = new ArrayList<>();
    }

    /**
     * Restituisce la lista dei membri.
     *
     * @return Lista di membri del team.
     */
    public ArrayList<Utente> getMembro() {return membro;}

    /**
     * Restituisce l'hackathon a cui il team è registrato.
     *
     * @return Hackathon a cui è registrato il team.
     */
    public Hackathon getHackathon() {return eventoPartecipazione;}

    /**
     * Restituisce i voti ricevuti dal team.
     *
     * @return Voti ricevuti dal team.
     */
    public int getNumeroVoti() {return numeroVoti;}

    /**
     * Incrementa di uno il numero di voti ricevuti dal team.
     */
    public void incrementaNumeroVoti() {this.numeroVoti++;}

    /**
     * Restituisce il punteggio finale del team.
     */
    public int getVotoFinale() {return votoFinale;}

    /**
     * Restituisce il nome del team.
     *
     * @return Nome del team.
     */
    public String getNomeTeam() {return nomeTeam;}

    /**
     * Aggiunta dell'utente in input a this team.
     *
     * @param user Utente da aggiungere al team.
     * @throws IllegalArgumentException Se l'utente è null.
     * @throws IllegalStateException Se il team è già al completo.
     * @throws IllegalStateException Se la data di registrazione non è valida.
     */
    public void aggiungiMembro(Utente user) {
        if (user == null) {
            throw new IllegalArgumentException("L'utente non può essere null");
        }
        // Controllo se l'utente è già iscritto a questo team
        if (user.getTeam() == this) {
            System.out.print("Sei già iscritto a questo team, vuoi abbandonarlo? (si/no): ");
            String risposta = new Scanner(System.in).nextLine();
            if (risposta.equalsIgnoreCase("si")) {
                user.abbandonaTeam();
            } else {
                throw new IllegalStateException("Sei già iscritto a questo team, non puoi iscriverti a un altro team.");
            }
        }
        // Controllo se l'utente è già iscritto a un altro team della stessa hackathon
        if (user.getTeam() != null && user.getTeam().getHackathon() == this.getHackathon()) {
            System.out.print("Sei già iscritto a un team, vuoi abbandonarlo? (si/no): ");
            String risposta = new Scanner(System.in).nextLine();
            if (risposta.equalsIgnoreCase("si")) {
                user.abbandonaTeam();
            } else {
                throw new IllegalStateException("Sei già iscritto a un team della stessa hackaton, non puoi iscriverti a un altro team.");
            }
        }
        if (membro.size() >= eventoPartecipazione.getMaxMembriTeam()) {
            throw new IllegalStateException("Team al completo. Massimo membri: "
                    + eventoPartecipazione.getMaxMembriTeam());
        }
        // Chiamo la funzione per il controllo della validità della data di registrazione e se
        //  il numero massimo di iscritti non è stato ancora raggiunto
        if (eventoPartecipazione.controlloValiditaDataReg() && eventoPartecipazione.controlloMaxIscritti()) {
            // Registro l'utente
            membro.add(user);
            user.setNewTeam(this);
            // Incremento il numero di iscritti
            eventoPartecipazione.incrementaNumIscritti();
        } else {
            // Se la data non è compresa nell'intervallo di registrazione
            throw new IllegalStateException("Impossibile registrarsi in questa data: " + LocalDateTime.now() +
                    "\nData di inizio registrazioni: " + eventoPartecipazione.getDataInizioRegistrazioni() +
                    "\nData di fine registrazioni: " + eventoPartecipazione.getDataFineRegistrazioni());
        }
    }

    /**
     * Stampa i membri del team.
     */
    public void printMembers() {
        System.out.println("Membri del team " + this.nomeTeam + ":");
        for(Utente user : membro) {
            System.out.println(user.getName());
        }
    }

    /**
     * Carica un nuovo documento all'interno della piattaforma
     *
     * @param title Inserisce il titolo del documento.
     * @param text Inserisce il corpo del documento.
     */
    public void uploadDocumento(String title, String text) {documentazione.add(new Documento(this, title, text));}

    /**
     * Restituisce la lista dei documenti caricati dal team.
     *
     * @return Lista di documenti (non nulla).
     */
    public ArrayList<Documento> getDocumentazione() {return documentazione;}

    /**
     * Stampa il documento scelto.
     * @param doc Documento da recuperare per la stampa.
     * @throws NullPointerException Lancia eccezione nel caso in cui il
     *                              documento non ha nessun riferimento ad
     *                              un oggetto esistente.
     */
    public void stampaDocumento(Documento doc) throws NullPointerException {
        if(doc == null) {
            throw new NullPointerException("Non è stato trovato alcun documento!");
        }
            System.out.println("Titolo: " + doc.getTitle() + "\n" +
                    "Testo: " + doc.getText());
    }

    /**
     * Stampa il documento scelto.
     * @param titolo Titolo del documento da cercare.
     * @return doc Restituisce documento da cercare se il titolo
     * corrisponde. Altrimenti restituisce null.
     */
    public Documento cercaDocumento(String titolo) {
        for(Documento doc : documentazione) {
            if(doc.getTitle().toLowerCase().contains(titolo.toLowerCase())) {
                return doc;
            }
        }

        return null;
    }

    /**
     * Incrementa il valore del punteggio finale del team.
     *
     * @param voto Valutazione da aggiungere al punteggio finale.
     */
    public void setVotoFinale(int voto) {
        this.votoFinale += voto;
    }
}
