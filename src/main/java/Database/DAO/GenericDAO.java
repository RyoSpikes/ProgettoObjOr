package Database.DAO;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia generica per le operazioni CRUD (Create, Read, Update, Delete) sui database.
 * 
 * @param <T> Il tipo di entità gestita dal DAO
 * @param <K> Il tipo della chiave primaria dell'entità
 */
public interface GenericDAO<T, K> {
    
    /**
     * Salva una nuova entità nel database.
     * 
     * @param entity L'entità da salvare
     * @return true se l'operazione è andata a buon fine, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean save(T entity) throws SQLException;
    
    /**
     * Aggiorna un'entità esistente nel database.
     * 
     * @param entity L'entità da aggiornare
     * @return true se l'operazione è andata a buon fine, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean update(T entity) throws SQLException;
    
    /**
     * Elimina un'entità dal database tramite la sua chiave primaria.
     * 
     * @param key La chiave primaria dell'entità da eliminare
     * @return true se l'operazione è andata a buon fine, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean delete(K key) throws SQLException;
    
    /**
     * Trova un'entità nel database tramite la sua chiave primaria.
     * 
     * @param key La chiave primaria dell'entità da cercare
     * @return L'entità trovata, null se non esiste
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    T findByKey(K key) throws SQLException;
    
    /**
     * Recupera tutte le entità dal database.
     * 
     * @return Una lista contenente tutte le entità
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<T> findAll() throws SQLException;
}
