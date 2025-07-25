package Database.DAO;

import model.Hackathon;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DAO per la gestione degli hackathon nel database.
 * Fornisce metodi per creare, recuperare e gestire gli hackathon.
 */
public interface HackathonDAO extends GenericDAO<Hackathon, String> {
    
    /**
     * Crea un nuovo hackathon nel database.
     *
     * @param hackathon L'oggetto hackathon da creare
     * @return true se la creazione è avvenuta con successo
     * @throws SQLException in caso di errore nel database
     */
    boolean creaHackathon(Hackathon hackathon) throws SQLException;

    /**
     * Recupera tutti gli hackathon organizzati da un organizzatore specifico.
     *
     * @param usernameOrganizzatore Username dell'organizzatore
     * @return Lista degli hackathon dell'organizzatore
     * @throws SQLException in caso di errore nel database
     */
    List<Hackathon> getHackathonByOrganizzatore(String usernameOrganizzatore) throws SQLException;

    /**
     * Recupera tutti gli hackathon dal database.
     *
     * @return Lista di tutti gli hackathon
     * @throws SQLException in caso di errore nel database
     */
    List<Hackathon> getAllHackathon() throws SQLException;

    /**
     * Recupera gli hackathon con registrazioni attualmente aperte.
     *
     * @return Lista degli hackathon con registrazioni aperte
     * @throws SQLException in caso di errore nel database
     */
    List<Hackathon> getHackathonConRegistrazioniAperte() throws SQLException;

    /**
     * Recupera un hackathon specifico per titolo identificativo.
     *
     * @param titoloIdentificativo Il titolo identificativo dell'hackathon
     * @return L'hackathon trovato, null se non esiste
     * @throws SQLException in caso di errore nel database
     */
    Hackathon getHackathonByTitolo(String titoloIdentificativo) throws SQLException;

    /**
     * Recupera gli hackathon terminati per cui è possibile generare la classifica.
     *
     * @return Lista degli hackathon terminati
     * @throws SQLException in caso di errore nel database
     */
    List<Hackathon> getHackathonTerminati() throws SQLException;

    /**
     * Aggiorna la classifica di un hackathon.
     *
     * @param titoloIdentificativo Titolo dell'hackathon
     * @param classifica La stringa della classifica
     * @return true se l'aggiornamento è avvenuto con successo
     * @throws SQLException in caso di errore nel database
     */
    boolean aggiornaClassifica(String titoloIdentificativo, String classifica) throws SQLException;
    
    /**
     * Genera la classifica di un hackathon utilizzando la funzione del database.
     * Questa funzione verifica che l'hackathon sia terminato e che tutti i giudici
     * abbiano espresso i loro voti prima di generare la classifica finale.
     *
     * @param titoloIdentificativo Titolo dell'hackathon
     * @return La stringa della classifica generata, o un messaggio di errore
     * @throws SQLException in caso di errore nel database
     */
    String generaClassificaHackathon(String titoloIdentificativo) throws SQLException;
    
    /**
     * Verifica se un hackathon è terminato (data fine evento superata).
     *
     * @param titoloIdentificativo Titolo dell'hackathon
     * @return true se l'hackathon è terminato, false altrimenti
     * @throws SQLException in caso di errore nel database
     */
    boolean isHackathonTerminato(String titoloIdentificativo) throws SQLException;
}
