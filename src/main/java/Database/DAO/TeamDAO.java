package Database.DAO;

import model.Team;
import model.Utente;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DAO specifica per l'entità Team.
 * Estende l'interfaccia generica GenericDAO e aggiunge metodi specifici per i team.
 * 
 * La chiave primaria è composta da Nome_team e Titolo_hackathon, 
 * quindi viene usata una classe TeamKey come chiave primaria composita.
 */
public interface TeamDAO extends GenericDAO<Team, TeamDAO.TeamKey> {
    
    /**
     * Classe per rappresentare la chiave primaria composita di Team.
     */
    public static class TeamKey {
        private final String nomeTeam;
        private final String titoloHackathon;
        
        public TeamKey(String nomeTeam, String titoloHackathon) {
            this.nomeTeam = nomeTeam;
            this.titoloHackathon = titoloHackathon;
        }
        
        public String getNomeTeam() {
            return nomeTeam;
        }
        
        public String getTitoloHackathon() {
            return titoloHackathon;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TeamKey teamKey = (TeamKey) obj;
            return nomeTeam.equals(teamKey.nomeTeam) && titoloHackathon.equals(teamKey.titoloHackathon);
        }
        
        @Override
        public int hashCode() {
            return nomeTeam.hashCode() + titoloHackathon.hashCode();
        }
    }
    
    /**
     * Trova tutti i team partecipanti a un determinato hackathon.
     *
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Lista dei team partecipanti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<Team> findByHackathon(String titoloHackathon) throws SQLException;
    
    /**
     * Trova il team a cui appartiene un determinato utente per un hackathon specifico.
     *
     * @param username Il nome utente
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Il team dell'utente, null se non appartiene a nessun team
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    Team findByUserAndHackathon(String username, String titoloHackathon) throws SQLException;
    
    /**
     * Conta il numero di membri di un team.
     *
     * @param nomeTeam Il nome del team
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Il numero di membri del team
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    int countMembers(String nomeTeam, String titoloHackathon) throws SQLException;
    
    /**
     * Recupera tutti i membri di un team.
     *
     * @param nomeTeam Il nome del team
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Lista degli utenti membri del team
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    List<Utente> getTeamMembers(String nomeTeam, String titoloHackathon) throws SQLException;
    
    /**
     * Aggiorna il punteggio finale di un team.
     *
     * @param nomeTeam Il nome del team
     * @param titoloHackathon Il titolo dell'hackathon
     * @param punteggioFinale Il punteggio finale da assegnare
     * @return true se l'operazione è andata a buon fine, false altrimenti
     * @throws SQLException Se si verifica un errore durante l'operazione
     */
    boolean updatePunteggioFinale(String nomeTeam, String titoloHackathon, int punteggioFinale) throws SQLException;
}
