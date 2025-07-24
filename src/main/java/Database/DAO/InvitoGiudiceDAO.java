package Database.DAO;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DAO per la gestione degli inviti a giudice.
 */
public interface InvitoGiudiceDAO {
    
    /**
     * Recupera tutti gli inviti ricevuti da un utente.
     *
     * @param username Il nome utente
     * @return Lista degli inviti (come stringhe descrittive)
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<String> getInvitiByUser(String username) throws SQLException;
    
    /**
     * Accetta un invito a giudice.
     *
     * @param username Il nome utente che accetta
     * @param titoloHackathon Il titolo dell'hackathon
     * @return true se l'operazione è andata a buon fine, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean accettaInvito(String username, String titoloHackathon) throws SQLException;
    
    /**
     * Rifiuta un invito a giudice.
     *
     * @param username Il nome utente che rifiuta
     * @param titoloHackathon Il titolo dell'hackathon
     * @return true se l'operazione è andata a buon fine, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean rifiutaInvito(String username, String titoloHackathon) throws SQLException;
    
    /**
     * Recupera tutte le hackathon di cui un utente è giudice.
     *
     * @param username Il nome utente
     * @return Lista dei titoli delle hackathon
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<String> getHackathonAsGiudice(String username) throws SQLException;
    
    /**
     * Crea un nuovo invito a giudice.
     *
     * @param username Il nome utente da invitare
     * @param titoloHackathon Il titolo dell'hackathon
     * @return true se l'invito è stato creato con successo, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean creaInvito(String username, String titoloHackathon) throws SQLException;
    
    /**
     * Crea un nuovo invito a giudice con verifica di autorizzazione dell'organizzatore.
     *
     * @param usernameOrganizzatore Il nome utente dell'organizzatore che invia l'invito
     * @param username Il nome utente da invitare
     * @param titoloHackathon Il titolo dell'hackathon
     * @return true se l'invito è stato creato con successo, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     * @throws SecurityException Se l'organizzatore non è autorizzato per questo hackathon
     */
    boolean creaInvitoConVerifica(String usernameOrganizzatore, String username, String titoloHackathon) throws SQLException, SecurityException;
}
