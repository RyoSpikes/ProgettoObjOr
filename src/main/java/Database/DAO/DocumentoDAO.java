package Database.DAO;

import model.Documento;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DAO per la gestione dei documenti.
 */
public interface DocumentoDAO {
    
    /**
     * Salva un nuovo documento nel database.
     *
     * @param documento Il documento da salvare
     * @return true se l'operazione è andata a buon fine, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean save(Documento documento) throws SQLException;
    
    /**
     * Recupera tutti i documenti di un team per un hackathon.
     *
     * @param nomeTeam Il nome del team
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Lista dei documenti del team
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<Documento> getDocumentiByTeam(String nomeTeam, String titoloHackathon) throws SQLException;
    
    /**
     * Recupera tutti i documenti per un hackathon (per i giudici).
     *
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Lista di tutti i documenti dell'hackathon
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<Documento> getDocumentiByHackathon(String titoloHackathon) throws SQLException;
    
    /**
     * Elimina un documento dal database.
     *
     * @param idDocumento L'ID del documento da eliminare
     * @return true se l'operazione è andata a buon fine, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean delete(int idDocumento) throws SQLException;
}
