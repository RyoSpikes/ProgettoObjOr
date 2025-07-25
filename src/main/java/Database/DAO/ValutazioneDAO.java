package Database.DAO;

import model.Valutazione;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DAO per la gestione delle valutazioni dei documenti.
 */
public interface ValutazioneDAO {
    
    /**
     * Salva una nuova valutazione nel database.
     *
     * @param valutazione La valutazione da salvare
     * @param idDocumento L'ID del documento valutato
     * @param nomeTeam Il nome del team proprietario del documento
     * @param titoloHackathon Il titolo dell'hackathon
     * @return true se l'operazione è andata a buon fine, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean save(Valutazione valutazione, int idDocumento, String nomeTeam, String titoloHackathon) throws SQLException;
    
    /**
     * Salva una nuova valutazione nel database (metodo semplificato).
     *
     * @param valutazioneTestuale Il testo della valutazione
     * @param idDocumento L'ID del documento valutato
     * @param usernameGiudice Username del giudice che valuta
     * @param titoloHackathon Il titolo dell'hackathon
     * @return true se l'operazione è andata a buon fine, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean save(String valutazioneTestuale, int idDocumento, String usernameGiudice, String titoloHackathon) throws SQLException;
    
    /**
     * Recupera tutte le valutazioni di un documento.
     *
     * @param idDocumento L'ID del documento
     * @return Lista delle valutazioni del documento
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<Valutazione> getValutazioniByDocumento(int idDocumento) throws SQLException;
    
    /**
     * Verifica se un giudice ha già valutato un determinato documento.
     *
     * @param usernameGiudice Username del giudice
     * @param idDocumento ID del documento
     * @param titoloHackathon Titolo dell'hackathon
     * @return true se il giudice ha già valutato il documento, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean hasGiudiceValutatoDocumento(String usernameGiudice, int idDocumento, String titoloHackathon) throws SQLException;
    
    /**
     * Verifica se un giudice ha già valutato un determinato documento (metodo semplificato).
     *
     * @param usernameGiudice Username del giudice
     * @param idDocumento ID del documento
     * @return true se il giudice ha già valutato il documento, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean hasGiudiceValutatoDocumento(String usernameGiudice, int idDocumento) throws SQLException;
    
    /**
     * Recupera tutte le valutazioni effettuate da un giudice per un hackathon.
     *
     * @param usernameGiudice Username del giudice
     * @param titoloHackathon Titolo dell'hackathon
     * @return Lista delle valutazioni del giudice
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<Valutazione> getValutazioniByGiudice(String usernameGiudice, String titoloHackathon) throws SQLException;
}
