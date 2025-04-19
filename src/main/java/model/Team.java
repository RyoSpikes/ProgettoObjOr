package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
    public ArrayList<Utente> getMembro() {
        return membro;
    }

    /**
     * Controllo se la data corrente è valida per la registrazione.
     *
     * @return true se la data corrente è valida
     */
    public Boolean controlloValiditaRegistrazione() {
        // Ricavo la data di oggi
        LocalDateTime now = LocalDateTime.now();

        // Controllo se la data di oggi è compresa tra la data di inizio e la data di fine registrazioni
        if (eventoPartecipazione.getDataInizioRegistrazioni().isBefore(now) &&
                eventoPartecipazione.getDataFineRegistrazioni().isAfter(now)) {
            return true;
        } else {
            return false;
        }
    }

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

        if (membro.size() >= eventoPartecipazione.getMaxMembriTeam()) {
            throw new IllegalStateException("Team al completo. Massimo membri: "
                    + eventoPartecipazione.getMaxMembriTeam());
        }

        // Chiamo la funzione per il controllo della validità della data di registrazione
        if (controlloValiditaRegistrazione()) {
            // TODO: Controllo se l'utente non sia già registrato ad un team o a questo stesso team

            // TODO: Se l'utente è già registrato ad un
            //  team potremmo voler chiedere allo stesso utente se vuole
            //  abbandonare il team corrente per iscriversi a questo team.
            //  N.B.: questo controllo potrebbe essere implementato direttamente
            //  nella funzione entrataTeam nella classe Utente per coerenza.

            // Registro l'utente
            membro.add(user);
            user.setNewTeam(this);
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
    public void printMembers()
    {
        System.out.println("Membri del team " + this.nomeTeam + ":");
        for(Utente user : membro)
        {
            System.out.println(user.getName());
        }
    }

    /**
     * Carica un nuovo documento all'interno della piattaforma
     *
     * @param title Inserisce il titolo del documento.
     * @param text Inserisce il corpo del documento.
     */
    public void uploadDocumento(String title, String text)
    {
        documentazione.add(new Documento(this, title, text));
    }

    /**
     * Restituisce la lista dei documenti caricati dal team.
     *
     * @return Lista di documenti (non nulla).
     */
    public ArrayList<Documento> getDocumentazione() {
        return documentazione;
    }

    /**
     * Stampa il documento scelto.
     * @param doc Documento da recuperare per la stampa.
     * @throws NullPointerException Lancia eccezione nel caso in cui il
     *                              documento non ha nessun riferimento ad
     *                              un oggetto esistente.
     */
    public void stampaDocumento(Documento doc) throws NullPointerException
    {
        if(doc == null)
        {
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
    public Documento cercaDocumento(String titolo)
    {
        for(Documento doc : documentazione)
        {
            if(doc.getTitle().toLowerCase().contains(titolo.toLowerCase()))
            {
                return doc;
            }
        }
        return null;
    }

    // TODO: 18/04/2025
    public void setVotoFinale(int votoFinale) {
        this.votoFinale += votoFinale;
    }
}
