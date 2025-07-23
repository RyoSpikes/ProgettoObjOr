package model;

import Database.ConnessioneDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public ArrayList<Utente> getMembro() { return membro; }

    /**
     * Restituisce l'hackathon a cui il team è registrato.
     *
     * @return Hackathon a cui è registrato il team.
     */
    public Hackathon getHackathon() { return eventoPartecipazione; }

    /**
     * Restituisce i voti ricevuti dal team.
     *
     * @return Voti ricevuti dal team.
     */
    public int getNumeroVoti() { return numeroVoti; }

    /**
     * Incrementa di uno il numero di voti ricevuti dal team.
     */
    public void incrementaNumeroVoti() { this.numeroVoti++; }

    /**
     * Restituisce il punteggio finale del team.
     *
     * @return Punteggio finale.
     */
    public int getVotoFinale() { return votoFinale; }

    /**
     * Restituisce il nome del team.
     *
     * @return Nome del team.
     */
    public String getNomeTeam() { return nomeTeam; }

    /**
     * Aggiunge un nuovo membro al team.
     * Inserisce una nuova riga nella tabella MEMBERSHIP del database.
     * I trigger del database gestiranno automaticamente le validazioni:
     * - Verifica che le registrazioni siano ancora aperte
     * - Verifica che il team non abbia raggiunto il numero massimo di membri
     * - Verifica che l'hackathon non abbia superato il limite di partecipanti
     *
     * @param utente L'utente da aggiungere al team
     * @throws IllegalArgumentException Se l'aggiunta non è possibile per i vincoli del database
     * @throws IllegalStateException Se l'utente è già in un team per questo hackathon
     */
    public void aggiungiMembro(Utente utente) throws IllegalArgumentException, IllegalStateException {
        if (utente == null) {
            throw new IllegalArgumentException("L'utente non può essere null");
        }

        String sql = "INSERT INTO MEMBERSHIP (Username_utente, Team_appartenenza, Titolo_hackathon, Data_adesione) VALUES (?, ?, ?, CURRENT_DATE)";

        try (Connection conn = ConnessioneDatabase.getInstance().connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utente.getName());
            stmt.setString(2, this.nomeTeam);
            stmt.setString(3, this.eventoPartecipazione.getTitoloIdentificativo());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Aggiorna la lista locale dei membri
                this.membro.add(utente);
                // Aggiorna il team corrente dell'utente
                utente.setNewTeam(this);
                System.out.println("Utente " + utente.getName() + " aggiunto al team " + this.nomeTeam);
            } else {
                throw new IllegalArgumentException("Non è stato possibile aggiungere l'utente al team");
            }

        } catch (SQLException e) {
            // Il database restituisce errori specifici tramite i trigger
            if (e.getMessage().contains("duplicate key")) {
                throw new IllegalStateException("L'utente è già membro di un team per questo hackathon");
            }
            throw new IllegalArgumentException("Errore nell'aggiunta dell'utente al team: " + e.getMessage());
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
    public void uploadDocumento(String title, String text) { documentazione.add(new Documento(this, title, text)); }

    /**
     * Restituisce la lista dei documenti caricati dal team.
     *
     * @return Lista di documenti (non nulla).
     */
    public ArrayList<Documento> getDocumentazione() { return documentazione; }

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
