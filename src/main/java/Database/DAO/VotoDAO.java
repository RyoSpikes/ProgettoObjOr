package Database.DAO;

import model.Voto;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DAO per la gestione dei voti nel database.
 */
public interface VotoDAO {
    
    /**
     * Salva un nuovo voto nel database.
     *
     * @param usernameGiudice Username del giudice che vota
     * @param titoloHackathon Titolo dell'hackathon
     * @param nomeTeam Nome del team votato
     * @param punteggio Punteggio assegnato (0-10)
     * @return true se l'operazione è andata a buon fine, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean save(String usernameGiudice, String titoloHackathon, String nomeTeam, int punteggio) throws SQLException;
    
    /**
     * Verifica se un giudice ha già votato un team in un hackathon.
     *
     * @param usernameGiudice Username del giudice
     * @param titoloHackathon Titolo dell'hackathon
     * @param nomeTeam Nome del team
     * @return true se il giudice ha già votato il team, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean hasGiudiceVotatoTeam(String usernameGiudice, String titoloHackathon, String nomeTeam) throws SQLException;
    
    /**
     * Recupera tutti i voti di un giudice per un hackathon.
     *
     * @param usernameGiudice Username del giudice
     * @param titoloHackathon Titolo dell'hackathon
     * @return Lista dei voti del giudice
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<Voto> getVotiByGiudice(String usernameGiudice, String titoloHackathon) throws SQLException;
    
    /**
     * Recupera tutti i voti ricevuti da un team in un hackathon.
     *
     * @param nomeTeam Nome del team
     * @param titoloHackathon Titolo dell'hackathon
     * @return Lista dei voti ricevuti dal team
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<Voto> getVotiByTeam(String nomeTeam, String titoloHackathon) throws SQLException;
}
