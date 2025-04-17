package model;

import java.awt.*;
import java.util.ArrayList;

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
     * Registra un nuovo hackathon organizzato da questo organizzatore.
     *
     * @param idNum                   l'ID numerico dell'hackathon
     * @param sede                    la sede dell'hackathon
     * @param dataInizio              la data di inizio dell'hackathon
     * @param dataFine                la data di fine dell'hackathon
     * @param dataInizioRegistrazioni la data di inizio delle registrazioni
     * @param dataFineRegistrazioni   la data di fine delle registrazioni
     * @param titolo                  il titolo dell'hackathon
     * @param maxMembriTeam           il numero massimo di membri per team
     * @param maxNumIscritti          il numero massimo di partecipanti
     */
    public void registrazioneHackathon(
            String idNum,
            String sede,
            String dataInizio,
            String dataFine,
            String dataInizioRegistrazioni,
            String dataFineRegistrazioni,
            String titolo,
            int maxMembriTeam,
            int maxNumIscritti
    ) {
        hackathonOrganizzate.add(new Hackathon(
                idNum, sede, dataInizio,
                dataFine, dataInizioRegistrazioni, dataFineRegistrazioni,
                titolo, maxMembriTeam, maxNumIscritti
        ));
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
            if (idHackathon.equals(hack.getIdNum())) {
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
     * @throws IndexOutOfBoundsException Se l'indice è fuori dai limiti della lista (index < 0 || index >= size)
     */
    public Hackathon getHackathonOrganizzata(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= hackathonOrganizzate.size()) {
            throw new IndexOutOfBoundsException("Indice " + index + " non valido. La lista contiene "
                    + hackathonOrganizzate.size() + " elementi.");
        }
        return hackathonOrganizzate.get(index);
    }

    //TO DO
    public void invitoGiudice(Utente giudiceAggiunto, String idNum) {
        //getHackathonOrganizzata(getIndexHackathonOrganizzata(idNum)).aggiungiGiudice(giudiceAggiunto.getInvite(idNum));
    }
}
