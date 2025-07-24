package Database.DAO.Impl;

import Database.DAO.MembershipDAO;
import Database.ConnessioneDatabase;
import model.Utente;
import model.Team;
import model.Hackathon;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'interfaccia MembershipDAO per la gestione delle membership nel database PostgreSQL.
 * Gestisce la tabella associativa MEMBERSHIP senza bisogno di un modello specifico.
 */
public class MembershipDAOImpl implements MembershipDAO {
    
    private Connection connection;
    
    /**
     * Costruttore che inizializza la connessione al database.
     * 
     * @throws SQLException Se si verifica un errore nella connessione
     */
    public MembershipDAOImpl() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    @Override
    public boolean addUserToTeam(String username, String nomeTeam, String titoloHackathon, LocalDate dataAdesione) throws SQLException {
        String sql = "INSERT INTO MEMBERSHIP (Username_utente, Team_appartenenza, Titolo_hackathon, Data_adesione) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, nomeTeam);
            stmt.setString(3, titoloHackathon);
            stmt.setDate(4, Date.valueOf(dataAdesione));
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'aggiunta dell'utente al team: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean removeUserFromTeam(String username, String nomeTeam, String titoloHackathon) throws SQLException {
        String sql = "DELETE FROM MEMBERSHIP WHERE Username_utente = ? AND Team_appartenenza = ? AND Titolo_hackathon = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, nomeTeam);
            stmt.setString(3, titoloHackathon);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante la rimozione dell'utente dal team: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isUserInTeamForHackathon(String username, String titoloHackathon) throws SQLException {
        String sql = "SELECT COUNT(*) FROM MEMBERSHIP WHERE Username_utente = ? AND Titolo_hackathon = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, titoloHackathon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la verifica della membership dell'utente: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public int countTeamMembers(String nomeTeam, String titoloHackathon) throws SQLException {
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
    public String getUserTeamForHackathon(String username, String titoloHackathon) throws SQLException {
        String sql = "SELECT Team_appartenenza FROM MEMBERSHIP WHERE Username_utente = ? AND Titolo_hackathon = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, titoloHackathon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Team_appartenenza");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la ricerca del team dell'utente: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<String> getTeamsForHackathon(String titoloHackathon) throws SQLException {
        List<String> teams = new ArrayList<>();
        String sql = "SELECT DISTINCT Team_appartenenza FROM MEMBERSHIP WHERE Titolo_hackathon = ? ORDER BY Team_appartenenza";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titoloHackathon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teams.add(rs.getString("Team_appartenenza"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero dei team per hackathon: " + e.getMessage(), e);
        }
        return teams;
    }

    @Override
    public List<Team> getTeamsByUser(String username) throws SQLException {
        List<Team> teams = new ArrayList<>();
        String sql = """
            SELECT DISTINCT t.Nome_team, t.Titolo_hackathon, t.Punteggio_finale
            FROM TEAM t 
            JOIN MEMBERSHIP m ON t.Nome_team = m.Team_appartenenza AND t.Titolo_hackathon = m.Titolo_hackathon 
            WHERE m.Username_utente = ?
            ORDER BY t.Titolo_hackathon, t.Nome_team
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Crea un oggetto Hackathon placeholder per il Team
                    Hackathon hackathon = new Hackathon(rs.getString("Titolo_hackathon"));
                    Team team = new Team(hackathon, rs.getString("Nome_team"));
                    Integer punteggio = rs.getObject("Punteggio_finale", Integer.class);
                    if (punteggio != null) {
                        team.setVotoFinale(punteggio);
                    }
                    teams.add(team);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero dei team dell'utente: " + e.getMessage(), e);
        }
        return teams;
    }

    @Override
    public Team getTeamForUserAndHackathon(String username, String titoloHackathon) throws SQLException {
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
                    // Crea un oggetto Hackathon placeholder per il Team
                    Hackathon hackathon = new Hackathon(rs.getString("Titolo_hackathon"));
                    Team team = new Team(hackathon, rs.getString("Nome_team"));
                    Integer punteggio = rs.getObject("Punteggio_finale", Integer.class);
                    if (punteggio != null) {
                        team.setVotoFinale(punteggio);
                    }
                    return team;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero del team dell'utente per l'hackathon: " + e.getMessage(), e);
        }
        return null;
    }
}
