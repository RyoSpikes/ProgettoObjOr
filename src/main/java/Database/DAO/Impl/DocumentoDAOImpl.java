package Database.DAO.Impl;

import Database.ConnessioneDatabase;
import Database.DAO.DocumentoDAO;
import model.Documento;
import model.Team;
import model.Hackathon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'interfaccia DocumentoDAO per PostgreSQL.
 */
public class DocumentoDAOImpl implements DocumentoDAO {
    
    private Connection connection;
    
    /**
     * Costruttore che inizializza la connessione al database.
     */
    public DocumentoDAOImpl() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
        System.out.println("DocumentoDAO inizializzato correttamente.");
    }
    
    @Override
    public boolean save(Documento documento) throws SQLException {
        String sql = """
            INSERT INTO DOCUMENTO (Nome_team, Titolo_hackathon, Titolo_doc, Contenuto, Data_stesura)
            VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, documento.getSource().getNomeTeam());
            stmt.setString(2, documento.getSource().getHackathon().getTitoloIdentificativo());
            stmt.setString(3, documento.getTitle());
            stmt.setString(4, documento.getText());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante il salvataggio del documento: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Documento> getDocumentiByTeam(String nomeTeam, String titoloHackathon) throws SQLException {
        List<Documento> documenti = new ArrayList<>();
        String sql = """
            SELECT d.ID_documento, d.Titolo_doc, d.Contenuto, d.Data_stesura
            FROM DOCUMENTO d
            WHERE d.Nome_team = ? AND d.Titolo_hackathon = ?
            ORDER BY d.Data_stesura DESC
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeTeam);
            stmt.setString(2, titoloHackathon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Crea oggetti placeholder per Team e Hackathon
                    Hackathon hackathon = new Hackathon(titoloHackathon);
                    Team team = new Team(hackathon, nomeTeam);
                    
                    Documento documento = new Documento(
                        rs.getInt("ID_documento"),
                        team,
                        rs.getString("Titolo_doc"),
                        rs.getString("Contenuto")
                    );
                    documenti.add(documento);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero dei documenti del team: " + e.getMessage(), e);
        }
        return documenti;
    }
    
    @Override
    public List<Documento> getDocumentiByHackathon(String titoloHackathon) throws SQLException {
        List<Documento> documenti = new ArrayList<>();
        String sql = """
            SELECT d.ID_documento, d.Nome_team, d.Titolo_doc, d.Contenuto, d.Data_stesura
            FROM DOCUMENTO d
            WHERE d.Titolo_hackathon = ?
            ORDER BY d.Nome_team, d.Data_stesura DESC
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titoloHackathon);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Crea oggetti placeholder per Team e Hackathon
                    Hackathon hackathon = new Hackathon(titoloHackathon);
                    Team team = new Team(hackathon, rs.getString("Nome_team"));
                    
                    Documento documento = new Documento(
                        rs.getInt("ID_documento"),
                        team,
                        rs.getString("Titolo_doc"),
                        rs.getString("Contenuto")
                    );
                    documenti.add(documento);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero dei documenti dell'hackathon: " + e.getMessage(), e);
        }
        return documenti;
    }
    
    @Override
    public boolean delete(int idDocumento) throws SQLException {
        String sql = "DELETE FROM DOCUMENTO WHERE ID_documento = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idDocumento);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'eliminazione del documento: " + e.getMessage(), e);
        }
    }
}
