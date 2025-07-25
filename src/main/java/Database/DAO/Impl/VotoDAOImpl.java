package Database.DAO.Impl;

import Database.ConnessioneDatabase;
import Database.DAO.VotoDAO;
import model.Giudice;
import model.Team;
import model.Voto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione del DAO per la gestione dei voti nel database PostgreSQL.
 */
public class VotoDAOImpl implements VotoDAO {

    private Connection connection;

    /**
     * Costruttore che inizializza la connessione al database.
     *
     * @throws SQLException Se si verifica un errore nella connessione
     */
    public VotoDAOImpl() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    @Override
    public boolean save(String usernameGiudice, String titoloHackathon, String nomeTeam, int punteggio) throws SQLException {
        String sql = """
                INSERT INTO VOTO (
                    Username_giudice, Titolo_hackathon, Team_votato, Punteggio
                ) VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usernameGiudice);
            stmt.setString(2, titoloHackathon);
            stmt.setString(3, nomeTeam);
            stmt.setInt(4, punteggio);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Errore durante il salvataggio del voto: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasGiudiceVotatoTeam(String usernameGiudice, String titoloHackathon, String nomeTeam) throws SQLException {
        String sql = """
                SELECT COUNT(*) 
                FROM VOTO 
                WHERE Username_giudice = ? AND Titolo_hackathon = ? AND Team_votato = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usernameGiudice);
            stmt.setString(2, titoloHackathon);
            stmt.setString(3, nomeTeam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la verifica del voto: " + e.getMessage(), e);
        }
        
        return false;
    }

    @Override
    public List<Voto> getVotiByGiudice(String usernameGiudice, String titoloHackathon) throws SQLException {
        List<Voto> voti = new ArrayList<>();
        String sql = """
                SELECT v.Team_votato, v.Punteggio
                FROM VOTO v
                WHERE v.Username_giudice = ? AND v.Titolo_hackathon = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usernameGiudice);
            stmt.setString(2, titoloHackathon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Crea oggetti semplificati per il voto
                    Giudice giudice = new Giudice(usernameGiudice, "", null);
                    Team team = new Team(rs.getString("Team_votato"));
                    
                    Voto voto = new Voto(team, giudice, rs.getInt("Punteggio"));
                    voti.add(voto);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero dei voti del giudice: " + e.getMessage(), e);
        }
        
        return voti;
    }

    @Override
    public List<Voto> getVotiByTeam(String nomeTeam, String titoloHackathon) throws SQLException {
        List<Voto> voti = new ArrayList<>();
        String sql = """
                SELECT v.Username_giudice, v.Punteggio
                FROM VOTO v
                WHERE v.Team_votato = ? AND v.Titolo_hackathon = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeTeam);
            stmt.setString(2, titoloHackathon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Crea oggetti semplificati per il voto
                    Giudice giudice = new Giudice(rs.getString("Username_giudice"), "", null);
                    Team team = new Team(nomeTeam);
                    
                    Voto voto = new Voto(team, giudice, rs.getInt("Punteggio"));
                    voti.add(voto);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero dei voti del team: " + e.getMessage(), e);
        }
        
        return voti;
    }
}
