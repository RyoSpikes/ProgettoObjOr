package Database.DAO;

import model.Organizzatore;
import java.sql.SQLException;
import java.util.List;

public interface OrganizzatoreDAO extends GenericDAO<Organizzatore, String> {

    /**
     * Trova un organizzatore nel database tramite il nome utente.
     *
     * @param username Il nome utente dell'organizzatore da cercare
     * @return L'organizzatore trovato, null se non esiste
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    Organizzatore login(String username, String password) throws SQLException;

    /**
     * Trova un organizzatore nel database tramite il nome.
     *
     * @param nome Il nome dell'organizzatore da cercare
     * @return L'organizzatore trovato, null se non esiste
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    Organizzatore findByNome(String nome) throws SQLException;

    /**
     * Trova l'organizzatore che ha creato un determinato hackathon.
     *
     * @param hackathonId L'ID dell'hackathon
     * @return Lista degli organizzatori che hanno organizzato l'hackathon
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    Organizzatore findByHackathon(int hackathonId) throws SQLException;
}
