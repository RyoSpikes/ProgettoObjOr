package Database.DAO.Impl;

import Database.DAO.UtenteDAO;
import Database.ConnessioneDatabase;
import model.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'interfaccia UtenteDAO per la gestione degli utenti nel database PostgreSQL.
 */
public class UtenteDAOImpl implements UtenteDAO {
    
    private Connection connection;
    
    /**
     * Costruttore che inizializza la connessione al database.
     * 
     * @throws SQLException Se si verifica un errore nella connessione
     */
    public UtenteDAOImpl() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    @Override
    public boolean save(Utente entity) throws SQLException {
        String sql = "INSERT INTO UTENTE (Username, Password) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getPassword());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante il salvataggio dell'utente: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Utente entity) throws SQLException {
        String sql = "UPDATE UTENTE SET Password = ? WHERE Username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getPassword());
            stmt.setString(2, entity.getName());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'aggiornamento dell'utente: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String username) throws SQLException {
        String sql = "DELETE FROM UTENTE WHERE Username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'eliminazione dell'utente: " + e.getMessage(), e);
        }
    }

    @Override
    public Utente findByKey(String username) throws SQLException {
        String sql = "SELECT Username, Password FROM UTENTE WHERE Username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Utente(
                        rs.getString("Username"),
                        rs.getString("Password")
                    );
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la ricerca dell'utente: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Utente> findAll() throws SQLException {
        List<Utente> utenti = new ArrayList<>();
        String sql = "SELECT Username, Password FROM UTENTE ORDER BY Username";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()
        ) {
            
            while (rs.next()) {
                Utente utente = new Utente(
                    rs.getString("Username"),
                    rs.getString("Password")
                );
                utenti.add(utente);
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero di tutti gli utenti: " + e.getMessage(), e);
        }
        
        return utenti;
    }

    @Override
    public Utente findByUsername(String username) throws SQLException {
        // Questo metodo è identico a findByKey, ma lo mantengo per chiarezza dell'interfaccia
        return findByKey(username);
    }

    @Override
    public Utente login(String username, String password) throws SQLException {
        String sql = "SELECT Username, Password FROM UTENTE WHERE Username = ? AND Password = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Utente(
                        rs.getString("Username"),
                        rs.getString("Password")
                    );
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il login dell'utente: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Utente> findByHackathon(int hackathonId) throws SQLException {
        List<Utente> utenti = new ArrayList<>();
        String sql = """
            SELECT DISTINCT u.Username, u.Password 
            FROM UTENTE u 
            JOIN MEMBERSHIP m ON u.Username = m.Username_utente 
            WHERE m.Titolo_hackathon = ?
            ORDER BY u.Username
        """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(hackathonId));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Utente utente = new Utente(
                        rs.getString("Username"),
                        rs.getString("Password")
                    );
                    utenti.add(utente);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la ricerca degli utenti per hackathon: " + e.getMessage(), e);
        }
        
        return utenti;
    }
}
