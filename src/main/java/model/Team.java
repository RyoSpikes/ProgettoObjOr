package model;

import java.util.ArrayList;

/**
 * Classe che rappresenta un team partecipante a un hackathon.
 * Gestisce la lista dei membri (dinamica), la documentazione e il punteggio.
 */
public class Team {

    /** Lista dinamica dei membri del team. */
    private ArrayList<Utente> membro;

    /** Nome identificativo del team (univoco per hackathon). */
    private String nomeTeam;

    /** Hackathon a cui il team è iscritto. */
    private Hackathon eventoPartecipazione;

    /** Lista dei documenti caricati dal team. */
    private ArrayList<Documento> documentazione;

    /** Punteggio totale accumulato dal team. */
    private int votoFinale = 0;

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
     * Aggiunge un membro al team se ci sono posti disponibili.
     * @param user Utente da aggiungere (non nullo).
     * @throws IllegalArgumentException Se user è null.
     * @throws IllegalStateException Se il team è al completo.
     */
    public void aggiungiMembro(Utente user) {
        if (user == null) {
            throw new IllegalArgumentException("L'utente non può essere null");
        }
        if (membro.size() >= eventoPartecipazione.getMaxMembriTeam()) {
            throw new IllegalStateException("Team al completo. Massimo membri: "
                    + eventoPartecipazione.getMaxMembriTeam());
        }
        membro.add(user);
    }

    /**
     * Stampa i membri del team.
     */
    public void printMembers()
    {
        System.out.println("Membri del team " + this.nomeTeam + ":");
        for(Utente user : membro)
        {
            System.out.println(user.getName());
        }
    }

    //TO DO
    public void uploadDocumento(String doc)
    {
        documentazione.add(new Documento(this, doc));
    }

    //TO DO
    public void setVotoFinale(int votoFinale) {
        this.votoFinale += votoFinale;
    }
}
