package Database.DAO;

import model.Utente;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DAO specifica per l'entità Utente.
 * Estende l'interfaccia generica GenericDAO e aggiunge metodi specifici per gli utenti.
 */
public interface UtenteDAO extends GenericDAO<Utente, String> {
    
    /**
     * Trova un utente nel database tramite il nome utente.
     * 
     * @param username Il nome utente da cercare
     * @return L'utente trovato, null se non esiste
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    Utente findByUsername(String username) throws SQLException;
    
    /**
     * Verifica le credenziali di login di un utente.
     * 
     * @param username Il nome utente
     * @param password La password
     * @return L'utente se le credenziali sono corrette, null altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    Utente login(String username, String password) throws SQLException;
    
    /**
     * Trova tutti gli utenti che partecipano a un determinato hackathon.
     * 
     * @param hackathonId L'ID dell'hackathon
     * @return Lista degli utenti partecipanti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<Utente> findByHackathon(int hackathonId) throws SQLException;
}
