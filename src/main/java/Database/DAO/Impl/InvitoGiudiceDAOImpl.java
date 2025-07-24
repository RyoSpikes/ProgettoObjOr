package Database.DAO.Impl;

import Database.ConnessioneDatabase;
import Database.DAO.InvitoGiudiceDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'interfaccia InvitoGiudiceDAO per PostgreSQL.
 */
public class InvitoGiudiceDAOImpl implements InvitoGiudiceDAO {
    
    private Connection connection;
    
    /**
     * Costruttore che inizializza la connessione al database.
     */
    public InvitoGiudiceDAOImpl() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
        System.out.println("InvitoGiudiceDAO inizializzato correttamente.");
    }
    
    @Override
    public List<String> getInvitiByUser(String username) throws SQLException {
        List<String> inviti = new ArrayList<>();
        String sql = """
            SELECT ig.Titolo_hackathon, ig.Data_invito, ig.Stato_invito,
                   h.DataInizio_evento, h.DataFine_evento, h.Sede
            FROM INVITO_GIUDICE ig
            JOIN HACKATHON h ON ig.Titolo_hackathon = h.Titolo_identificativo
            WHERE ig.Username_utente = ? AND ig.Stato_invito = 'Inviato'
            ORDER BY ig.Data_invito DESC
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String invito = String.format("Invito a giudicare '%s' - Sede: %s - Dal %s al %s",
                        rs.getString("Titolo_hackathon"),
                        rs.getString("Sede"),
                        rs.getDate("DataInizio_evento"),
                        rs.getDate("DataFine_evento")
                    );
                    inviti.add(invito);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero degli inviti: " + e.getMessage(), e);
        }
        return inviti;
    }
    
    @Override
    public boolean accettaInvito(String username, String titoloHackathon) throws SQLException {
        String sql = """
            UPDATE INVITO_GIUDICE 
            SET Stato_invito = 'Accettato' 
            WHERE Username_utente = ? AND Titolo_hackathon = ? AND Stato_invito = 'Inviato'
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, titoloHackathon);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'accettazione dell'invito: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean rifiutaInvito(String username, String titoloHackathon) throws SQLException {
        String sql = """
            UPDATE INVITO_GIUDICE 
            SET Stato_invito = 'Rifiutato' 
            WHERE Username_utente = ? AND Titolo_hackathon = ? AND Stato_invito = 'Inviato'
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, titoloHackathon);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante il rifiuto dell'invito: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<String> getHackathonAsGiudice(String username) throws SQLException {
        List<String> hackathons = new ArrayList<>();
        String sql = """
            SELECT DISTINCT g.Titolo_hackathon, h.DataInizio_evento, h.DataFine_evento, h.Sede
            FROM GIUDICE g
            JOIN HACKATHON h ON g.Titolo_hackathon = h.Titolo_identificativo
            WHERE g.Username_utente = ?
            ORDER BY h.DataInizio_evento DESC
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String hackathon = String.format("%s - %s (%s - %s)",
                        rs.getString("Titolo_hackathon"),
                        rs.getString("Sede"),
                        rs.getDate("DataInizio_evento"),
                        rs.getDate("DataFine_evento")
                    );
                    hackathons.add(hackathon);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero delle hackathon come giudice: " + e.getMessage(), e);
        }
        return hackathons;
    }
    
    @Override
    public boolean creaInvito(String username, String titoloHackathon) throws SQLException {
        // Prima recuperiamo l'organizzatore dell'hackathon
        String sqlOrganizzatore = "SELECT Organizzatore FROM HACKATHON WHERE Titolo_identificativo = ?";
        String organizzatore = null;
        
        try (PreparedStatement stmtOrg = connection.prepareStatement(sqlOrganizzatore)) {
            stmtOrg.setString(1, titoloHackathon);
            try (ResultSet rs = stmtOrg.executeQuery()) {
                if (rs.next()) {
                    organizzatore = rs.getString("Organizzatore");
                } else {
                    throw new SQLException("Hackathon non trovato: " + titoloHackathon);
                }
            }
        }
        
        // Ora inseriamo l'invito con tutti i campi richiesti
        String sql = "INSERT INTO INVITO_GIUDICE (Username_organizzatore, Username_utente, Titolo_hackathon, Data_invito, Stato_invito) VALUES (?, ?, ?, CURRENT_DATE, 'Inviato')";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, organizzatore);
            stmt.setString(2, username);
            stmt.setString(3, titoloHackathon);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Invito creato con successo per " + username + " nell'hackathon " + titoloHackathon + " dall'organizzatore " + organizzatore);
                return true;
            }
            
        } catch (SQLException e) {
            // Se è un errore di chiave duplicata, significa che l'utente è già stato invitato
            if (e.getSQLState().equals("23505")) { // PostgreSQL unique constraint violation
                System.out.println("L'utente " + username + " è già stato invitato per l'hackathon " + titoloHackathon);
                return false;
            }
            throw new SQLException("Errore nella creazione dell'invito: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean creaInvitoConVerifica(String usernameOrganizzatore, String username, String titoloHackathon) throws SQLException, SecurityException {
        // Prima verifichiamo che l'organizzatore sia effettivamente il proprietario dell'hackathon
        String sqlVerifica = "SELECT Organizzatore FROM HACKATHON WHERE Titolo_identificativo = ?";
        String organizzatoreEffettivo = null;
        
        try (PreparedStatement stmtVerifica = connection.prepareStatement(sqlVerifica)) {
            stmtVerifica.setString(1, titoloHackathon);
            try (ResultSet rs = stmtVerifica.executeQuery()) {
                if (rs.next()) {
                    organizzatoreEffettivo = rs.getString("Organizzatore");
                } else {
                    throw new SQLException("Hackathon non trovato: " + titoloHackathon);
                }
            }
        }
        
        // Verifica che l'organizzatore che fa la richiesta sia quello autorizzato
        if (!usernameOrganizzatore.equals(organizzatoreEffettivo)) {
            throw new SecurityException("L'organizzatore " + usernameOrganizzatore + 
                " non è autorizzato a invitare giudici per l'hackathon '" + titoloHackathon + 
                "'. Solo l'organizzatore " + organizzatoreEffettivo + " può farlo.");
        }
        
        // Se la verifica è passata, procediamo con la creazione dell'invito
        String sql = "INSERT INTO INVITO_GIUDICE (Username_organizzatore, Username_utente, Titolo_hackathon, Data_invito, Stato_invito) VALUES (?, ?, ?, CURRENT_DATE, 'Inviato')";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usernameOrganizzatore);
            stmt.setString(2, username);
            stmt.setString(3, titoloHackathon);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Invito creato con successo per " + username + 
                    " nell'hackathon " + titoloHackathon + " dall'organizzatore autorizzato " + usernameOrganizzatore);
                return true;
            }
            
        } catch (SQLException e) {
            // Se è un errore di chiave duplicata, significa che l'utente è già stato invitato
            if (e.getSQLState().equals("23505")) { // PostgreSQL unique constraint violation
                System.out.println("L'utente " + username + " è già stato invitato per l'hackathon " + titoloHackathon);
                return false;
            }
            throw new SQLException("Errore nella creazione dell'invito: " + e.getMessage(), e);
        }
        
        return false;
    }
}
