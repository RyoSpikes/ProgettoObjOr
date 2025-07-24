package model;

import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * Classe che rappresenta un organizzatore di hackathon, estendendo la classe Utente.
 * Un organizzatore può creare hackathon e invitare giudici a partecipare.
 */
public class Organizzatore extends Utente {
    /**
     * Lista di Hackathon realizzate dall'organizzatore.
     */
    private ArrayList<Hackathon> hackathonOrganizzate;

    /**
     * Costruttore per creare un nuovo Organizzatore.
     *
     * @param login    il nome utente dell'organizzatore
     * @param password la password dell'organizzatore
     */
    public Organizzatore(String login, String password) {
        super(login, password);
        hackathonOrganizzate = new ArrayList<Hackathon>();
    }

    /**
     * Costruttore per creare un nuovo Organizzatore con solo il login.
     * La password sarà impostata a "admin" per default.
     *
     * @param login il nome utente dell'organizzatore
     */
    public Organizzatore(String login) {
        super(login, "admin");
        hackathonOrganizzate = new ArrayList<Hackathon>();
    }

    //TODO: idNum -> Titolo_id
    //TODO: Eliminare titolo
    /**
     * Registra un nuovo hackathon organizzato da questo organizzatore.
     *
     * @param idNum                   l'ID numerico dell'hackathon
     * @param sede                    la sede dell'hackathon
     * @param dataInizio              la data di inizio dell'hackathon
     * @param dataFine                la data di fine dell'hackathon
     * @param dataInizioRegistrazioni la data di inizio delle registrazioni
     * @param titolo                  il titolo dell'hackathon
     * @param maxMembriTeam           il numero massimo di membri per team
     * @param maxNumIscritti          il numero massimo di partecipanti
     */
    public void registrazioneHackathon(
            String idNum,
            String sede,
            LocalDateTime dataInizio,
            LocalDateTime dataFine,
            LocalDateTime dataInizioRegistrazioni,
            String titolo,
            int maxMembriTeam,
            int maxNumIscritti
    ) {
        hackathonOrganizzate.add(
                new Hackathon(
                    titolo,                  // titoloIdentificativo
                    this.getName(),          // organizzatore
                    sede,                    // sede
                    dataInizio,              // dataInizio
                    dataFine,                // dataFine
                    dataInizioRegistrazioni, // dataInizioRegistrazioni
                    maxMembriTeam,           // maxMembriTeam
                    maxNumIscritti,          // maxNumIscritti
                    "Problema da definire"   // descrizioneProblema (placeholder)
                )
        );
    }

    /**
     * Verifica l'accesso dell'organizzatore stampando un messaggio di esito.
     *
     * Se la password è corretta, stampa "Accesso consentito", altrimenti "Accesso negato".
     * Il controllo viene effettuato tramite il metodo ereditato {@link Utente#getLogin(String)}.
     *
     * @param password La password da verificare per l'autenticazione (non può essere null o vuota).
     * @throws IllegalArgumentException Se la password è null o vuota.
     * @throws SecurityException Se l'autenticazione fallisce (password errata).
     */
    public void getLoginOrganizzatore(String password) throws IllegalArgumentException, SecurityException {
        // Controllo precondizioni
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La password non può essere null o vuota.");
        }

        // Verifica accesso con if + eccezione per sicurezza
        if (!super.getLogin(password)) {
            throw new SecurityException("Accesso negato: password errata.");
        }
        System.out.println("Accesso consentito");
    }

    /**
     * Cerca l'indice di un hackathon nella lista degli hackathon organizzati, in base al suo ID.
     *
     * @param idHackathon L'ID univoco dell'hackathon da cercare (case-sensitive)
     * @return L'indice dell'hackathon se trovato, altrimenti -1
     * @throws IllegalArgumentException Se idHackathon è null o vuoto
     */
    public int getIndexHackathonOrganizzata(String idHackathon) {
        // Controllo validità input
        if (idHackathon == null || idHackathon.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID hackathon non può essere null o vuoto");
        }

        int index = 0;
        // Itera sulla lista degli hackathon organizzati
        for (Hackathon hack : hackathonOrganizzate) {
            if (idHackathon.equals(hack.getTitoloIdentificativo())) {
                return index; // Trovato: restituisce l'indice immediatamente
            }
            index++; // Incrementa l'indice se non trovato
        }
        return -1; // Non trovato
    }

    /**
     * Restituisce l'hackathon organizzata in base all'indice specificato.
     *
     * @param index L'indice dell'hackathon nella lista
     * @return L'hackathon corrispondente
     * @throws IndexOutOfBoundsException Se l'indice è fuori dai limiti della lista
     */
    public Hackathon getHackathonOrganizzata(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= hackathonOrganizzate.size()) {
            throw new IndexOutOfBoundsException("Indice " + index + " non valido. La lista contiene "
                    + hackathonOrganizzate.size() + " elementi.");
        }
        return hackathonOrganizzate.get(index);
    }

    public Hackathon getHackathonOrganizzata()
    {
        return hackathonOrganizzate.getLast();
    }

    public ArrayList<Hackathon> getHackathonOrganizzate() { return hackathonOrganizzate; }

    public String printListaHackathon()
    {
        StringBuilder listaStampata = new StringBuilder();
        for(Hackathon h : hackathonOrganizzate)
        {
            listaStampata.append(h.printInfoEvento()).append("\n================================================\n");
        }
        return listaStampata.toString();
    }

    public Team getTeam() {
        // Non implementato, l'organizzatore non ha un team
        throw new UnsupportedOperationException("L'organizzatore non ha un team.");
    }

    public void entrataTeam(Team team) throws UnsupportedOperationException {
        // Non implementato, l'organizzatore non entra nei team
        throw new UnsupportedOperationException("L'organizzatore non può entrare in un team.");
    }

    public Team creaTeam(Hackathon hackathon, String nomeTeam) throws UnsupportedOperationException {
        // Non implementato, l'organizzatore non crea team
        throw new UnsupportedOperationException("L'organizzatore non può creare un team.");
    }
}