package Database.DAO.Impl;

import Database.DAO.TeamDAO;
import Database.ConnessioneDatabase;
import model.Team;
import model.Utente;
import model.Hackathon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'interfaccia TeamDAO per la gestione dei team nel database PostgreSQL.
 */
public class TeamDAOImpl implements TeamDAO {
    
    private Connection connection;
    
    /**
     * Costruttore che inizializza la connessione al database.
     * 
     * @throws SQLException Se si verifica un errore nella connessione
     */
    public TeamDAOImpl() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    @Override
    public boolean save(Team entity) throws SQLException {
        String sql = "INSERT INTO TEAM (Nome_team, Titolo_hackathon, Punteggio_finale) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getNomeTeam());
            stmt.setString(2, entity.getHackathon().getTitoloIdentificativo());
            stmt.setObject(3, entity.getVotoFinale(), Types.INTEGER);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante il salvataggio del team: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Team entity) throws SQLException {
        String sql = "UPDATE TEAM SET Punteggio_finale = ? WHERE Nome_team = ? AND Titolo_hackathon = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, entity.getVotoFinale(), Types.INTEGER);
            stmt.setString(2, entity.getNomeTeam());
            stmt.setString(3, entity.getHackathon().getTitoloIdentificativo());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'aggiornamento del team: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(TeamKey key) throws SQLException {
        String sql = "DELETE FROM TEAM WHERE Nome_team = ? AND Titolo_hackathon = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, key.getNomeTeam());
            stmt.setString(2, key.getTitoloHackathon());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'eliminazione del team: " + e.getMessage(), e);
        }
    }

    @Override
    public Team findByKey(TeamKey key) throws SQLException {
        String sql = "SELECT Nome_team, Titolo_hackathon, Punteggio_finale FROM TEAM WHERE Nome_team = ? AND Titolo_hackathon = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, key.getNomeTeam());
            stmt.setString(2, key.getTitoloHackathon());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createTeamFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la ricerca del team: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Team> findAll() throws SQLException {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT Nome_team, Titolo_hackathon, Punteggio_finale FROM TEAM ORDER BY Nome_team";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                teams.add(createTeamFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero di tutti i team: " + e.getMessage(), e);
        }
        return teams;
    }

    @Override
    public List<Team> findByHackathon(String titoloHackathon) throws SQLException {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT Nome_team, Titolo_hackathon, Punteggio_finale FROM TEAM WHERE Titolo_hackathon = ? ORDER BY Nome_team";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titoloHackathon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teams.add(createTeamFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la ricerca dei team per hackathon: " + e.getMessage(), e);
        }
        return teams;
    }

    @Override
    public Team findByUserAndHackathon(String username, String titoloHackathon) throws SQLException {
        String sql = """
            SELECT t.Nome_team, t.Titolo_hackathon, t.Punteggio_finale 
            FROM TEAM t 
            JOIN MEMBERSHIP m ON t.Nome_team = m.Team_appartenenza AND t.Titolo_hackathon = m.Titolo_hackathon 
            WHERE m.Username_utente = ? AND t.Titolo_hackathon = ?
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, titoloHackathon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createTeamFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la ricerca del team dell'utente: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public int countMembers(String nomeTeam, String titoloHackathon) throws SQLException {
        String sql = "SELECT COUNT(*) FROM MEMBERSHIP WHERE Team_appartenenza = ? AND Titolo_hackathon = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeTeam);
            stmt.setString(2, titoloHackathon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il conteggio dei membri del team: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public List<Utente> getTeamMembers(String nomeTeam, String titoloHackathon) throws SQLException {
        List<Utente> members = new ArrayList<>();
        String sql = """
            SELECT u.Username, u.Password 
            FROM UTENTE u 
            JOIN MEMBERSHIP m ON u.Username = m.Username_utente 
            WHERE m.Team_appartenenza = ? AND m.Titolo_hackathon = ?
            ORDER BY m.Data_adesione
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeTeam);
            stmt.setString(2, titoloHackathon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Utente utente = new Utente(
                        rs.getString("Username"),
                        rs.getString("Password")
                    );
                    members.add(utente);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero dei membri del team: " + e.getMessage(), e);
        }
        return members;
    }

    @Override
    public boolean updatePunteggioFinale(String nomeTeam, String titoloHackathon, int punteggioFinale) throws SQLException {
        String sql = "UPDATE TEAM SET Punteggio_finale = ? WHERE Nome_team = ? AND Titolo_hackathon = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, punteggioFinale);
            stmt.setString(2, nomeTeam);
            stmt.setString(3, titoloHackathon);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'aggiornamento del punteggio del team: " + e.getMessage(), e);
        }
    }

    /**
     * Cerca team il cui nome contiene il testo specificato (ricerca LIKE).
     * 
     * @param titoloHackathon Il titolo dell'hackathon in cui cercare
     * @param nomeTeamParziale Il testo da cercare nel nome del team
     * @return Lista dei team che contengono il testo nel nome
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public List<Team> searchTeamsByName(String titoloHackathon, String nomeTeamParziale) throws SQLException {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT Nome_team, Titolo_hackathon, Punteggio_finale FROM TEAM " +
                     "WHERE Titolo_hackathon = ? AND Nome_team ILIKE ? " +
                     "ORDER BY Nome_team";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titoloHackathon);
            stmt.setString(2, "%" + nomeTeamParziale + "%"); // Cerca ovunque nel nome
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teams.add(createTeamFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la ricerca dei team: " + e.getMessage(), e);
        }
        
        return teams;
    }

    /**
     * Metodo helper per creare un oggetto Team da un ResultSet.
     * Nota: Per creare un Team completo, serve l'oggetto Hackathon.
     * Questa implementazione crea un Team parziale. 
     * In un'implementazione più completa, si dovrebbe recuperare anche l'oggetto Hackathon.
     */
    private Team createTeamFromResultSet(ResultSet rs) throws SQLException {
        String nomeTeam = rs.getString("Nome_team");
        String titoloHackathon = rs.getString("Titolo_hackathon");
        Integer punteggioFinale = rs.getObject("Punteggio_finale", Integer.class);
        
        // Per ora creiamo un Hackathon "placeholder" con solo il titolo
        // In un'implementazione più completa, si dovrebbe recuperare l'oggetto Hackathon completo
        Hackathon hackathon = createHackathonPlaceholder(titoloHackathon);
        
        Team team = new Team(hackathon, nomeTeam);
        if (punteggioFinale != null) {
            team.setVotoFinale(punteggioFinale);
        }
        
        return team;
    }
    
    /**
     * Crea un oggetto Hackathon placeholder con solo il titolo.
     * Usa il costruttore di test di Hackathon.
     */
    private Hackathon createHackathonPlaceholder(String titolo) {
        return new Hackathon(titolo);
    }
}
