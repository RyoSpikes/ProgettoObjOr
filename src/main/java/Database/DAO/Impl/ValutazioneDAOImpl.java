package Database.DAO.Impl;

import Database.DAO.ValutazioneDAO;
import Database.ConnessioneDatabase;
import model.Valutazione;
import model.Giudice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione del DAO per la gestione delle valutazioni nel database PostgreSQL.
 */
public class ValutazioneDAOImpl implements ValutazioneDAO {

    private Connection connection;

    /**
     * Costruttore che inizializza la connessione al database.
     *
     * @throws SQLException Se si verifica un errore nella connessione
     */
    public ValutazioneDAOImpl() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    @Override
    public boolean save(Valutazione valutazione, int idDocumento, String nomeTeam, String titoloHackathon) throws SQLException {
        String sql = """
                INSERT INTO VALUTAZIONE (
                    ID_documento, Username_giudice, Titolo_hackathon, 
                    Team_valutato, Valutazione_giudice
                ) VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idDocumento);
            stmt.setString(2, valutazione.getGiudiceValutante().getName());
            stmt.setString(3, titoloHackathon);
            stmt.setString(4, nomeTeam);
            stmt.setString(5, valutazione.getGiudizio());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Errore durante il salvataggio della valutazione: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean save(String valutazioneTestuale, int idDocumento, String usernameGiudice, String titoloHackathon) throws SQLException {
        // Recupera il nome del team dal documento
        String sqlTeam = """
                SELECT Nome_team FROM DOCUMENTO WHERE ID_documento = ?
                """;
        
        String nomeTeam = null;
        try (PreparedStatement stmt = connection.prepareStatement(sqlTeam)) {
            stmt.setInt(1, idDocumento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nomeTeam = rs.getString("Nome_team");
                }
            }
        }
        
        if (nomeTeam == null) {
            throw new SQLException("Documento non trovato con ID: " + idDocumento);
        }
        
        String sql = """
                INSERT INTO VALUTAZIONE (
                    ID_documento, Username_giudice, Titolo_hackathon, 
                    Team_valutato, Valutazione_giudice
                ) VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idDocumento);
            stmt.setString(2, usernameGiudice);
            stmt.setString(3, titoloHackathon);
            stmt.setString(4, nomeTeam);
            stmt.setString(5, valutazioneTestuale);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new SQLException("Errore durante il salvataggio della valutazione: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Valutazione> getValutazioniByDocumento(int idDocumento) throws SQLException {
        List<Valutazione> valutazioni = new ArrayList<>();
        String sql = """
                SELECT v.Username_giudice, v.Valutazione_giudice, v.Titolo_hackathon
                FROM VALUTAZIONE v
                WHERE v.ID_documento = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idDocumento);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Crea un oggetto Giudice semplificato per la valutazione
                    // Nota: creiamo un Giudice con password vuota per questo scopo
                    Giudice giudice = new Giudice(rs.getString("Username_giudice"), "", null);
                    
                    // Crea la valutazione (senza documento per evitare riferimenti circolari)
                    Valutazione valutazione = new Valutazione(null, giudice, rs.getString("Valutazione_giudice"));
                    valutazioni.add(valutazione);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero delle valutazioni: " + e.getMessage(), e);
        }
        
        return valutazioni;
    }

    @Override
    public boolean hasGiudiceValutatoDocumento(String usernameGiudice, int idDocumento, String titoloHackathon) throws SQLException {
        String sql = """
                SELECT COUNT(*) 
                FROM VALUTAZIONE 
                WHERE Username_giudice = ? AND ID_documento = ? AND Titolo_hackathon = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usernameGiudice);
            stmt.setInt(2, idDocumento);
            stmt.setString(3, titoloHackathon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la verifica della valutazione: " + e.getMessage(), e);
        }
        
        return false;
    }

    @Override
    public boolean hasGiudiceValutatoDocumento(String usernameGiudice, int idDocumento) throws SQLException {
        String sql = """
                SELECT COUNT(*) 
                FROM VALUTAZIONE 
                WHERE Username_giudice = ? AND ID_documento = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usernameGiudice);
            stmt.setInt(2, idDocumento);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la verifica della valutazione: " + e.getMessage(), e);
        }
        
        return false;
    }

    @Override
    public List<Valutazione> getValutazioniByGiudice(String usernameGiudice, String titoloHackathon) throws SQLException {
        List<Valutazione> valutazioni = new ArrayList<>();
        String sql = """
                SELECT v.ID_documento, v.Valutazione_giudice, v.Team_valutato
                FROM VALUTAZIONE v
                WHERE v.Username_giudice = ? AND v.Titolo_hackathon = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usernameGiudice);
            stmt.setString(2, titoloHackathon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Crea un oggetto Giudice
                    Giudice giudice = new Giudice(usernameGiudice, "", null);
                    
                    // Crea la valutazione
                    Valutazione valutazione = new Valutazione(null, giudice, rs.getString("Valutazione_giudice"));
                    valutazioni.add(valutazione);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero delle valutazioni del giudice: " + e.getMessage(), e);
        }
        
        return valutazioni;
    }
}
