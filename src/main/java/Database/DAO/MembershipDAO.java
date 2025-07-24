package Database.DAO;

import model.Utente;
import model.Team;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaccia DAO per la gestione delle membership (tabella associativa).
 * Non utilizza un modello Membership specifico, ma gestisce direttamente i dati.
 */
public interface MembershipDAO {
    
    /**
     * Aggiunge un utente a un team (crea una nuova membership).
     *
     * @param username Il nome utente
     * @param nomeTeam Il nome del team
     * @param titoloHackathon Il titolo dell'hackathon
     * @param dataAdesione La data di adesione
     * @return true se l'operazione è andata a buon fine, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean addUserToTeam(String username, String nomeTeam, String titoloHackathon, LocalDate dataAdesione) throws SQLException;
    
    /**
     * Rimuove un utente da un team (elimina la membership).
     *
     * @param username Il nome utente
     * @param nomeTeam Il nome del team
     * @param titoloHackathon Il titolo dell'hackathon
     * @return true se l'operazione è andata a buon fine, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean removeUserFromTeam(String username, String nomeTeam, String titoloHackathon) throws SQLException;
    
    /**
     * Verifica se un utente è già membro di un team per un hackathon specifico.
     *
     * @param username Il nome utente
     * @param titoloHackathon Il titolo dell'hackathon
     * @return true se l'utente è già membro di un team, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean isUserInTeamForHackathon(String username, String titoloHackathon) throws SQLException;
    
    /**
     * Conta il numero di membri di un team specifico.
     *
     * @param nomeTeam Il nome del team
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Il numero di membri del team
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    int countTeamMembers(String nomeTeam, String titoloHackathon) throws SQLException;
    
    /**
     * Recupera tutti gli utenti membri di un team.
     *
     * @param nomeTeam Il nome del team
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Lista degli utenti membri del team
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<Utente> getTeamMembers(String nomeTeam, String titoloHackathon) throws SQLException;
    
    /**
     * Trova il nome del team a cui appartiene un utente per un hackathon specifico.
     *
     * @param username Il nome utente
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Il nome del team, null se l'utente non appartiene a nessun team
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    String getUserTeamForHackathon(String username, String titoloHackathon) throws SQLException;
    
    /**
     * Recupera tutti i team di un hackathon con il loro numero di membri.
     *
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Lista dei nomi dei team
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<String> getTeamsForHackathon(String titoloHackathon) throws SQLException;
    
    /**
     * Recupera tutti i team a cui partecipa un utente.
     *
     * @param username Il nome utente
     * @return Lista dei team dell'utente
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<Team> getTeamsByUser(String username) throws SQLException;
    
    /**
     * Recupera il team di un utente per un hackathon specifico.
     *
     * @param username Il nome utente
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Il team dell'utente per l'hackathon, null se non partecipa
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    Team getTeamForUserAndHackathon(String username, String titoloHackathon) throws SQLException;
}
