package Database.DAO.Impl;

import Database.DAO.HackathonDAO;
import Database.ConnessioneDatabase;
import model.Hackathon;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione del DAO per la gestione degli hackathon nel database PostgreSQL.
 */
public class HackathonDAOImpl implements HackathonDAO {

    private Connection connection;

    /**
     * Costruttore che inizializza la connessione al database.
     *
     * @throws SQLException Se si verifica un errore nella connessione
     */
    public HackathonDAOImpl() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    @Override
    public boolean creaHackathon(Hackathon hackathon) throws SQLException {
        
        String sql = """
                INSERT INTO HACKATHON (
                    Titolo_identificativo, Organizzatore, Sede, 
                    DataInizio_registrazione, DataFine_registrazione,
                    DataInizio_evento, DataFine_evento,
                    Descrizione_problema, MaxNum_iscritti, MaxNum_membriTeam
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathon.getTitoloIdentificativo());
            stmt.setString(2, hackathon.getOrganizzatore());
            stmt.setString(3, hackathon.getSede());
            stmt.setDate(4, Date.valueOf(hackathon.getDataInizioRegistrazioni().toLocalDate()));
            stmt.setDate(5, Date.valueOf(hackathon.getDataFineRegistrazioni().toLocalDate()));
            stmt.setDate(6, Date.valueOf(hackathon.getDataInizio().toLocalDate()));
            stmt.setDate(7, Date.valueOf(hackathon.getDataFine().toLocalDate()));
            stmt.setString(8, hackathon.getDescrizioneProblema());
            stmt.setInt(9, hackathon.getMaxNumIscritti());
            stmt.setInt(10, hackathon.getMaxMembriTeam());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            // Gestione errori specifici del database
            if (e.getSQLState().equals("23505")) { // Violazione unique constraint
                throw new SQLException("Esiste già un hackathon con questo titolo: " + hackathon.getTitoloIdentificativo(), e);
            } else if (e.getSQLState().equals("23503")) { // Violazione foreign key
                throw new SQLException("Organizzatore non valido: " + hackathon.getOrganizzatore(), e);
            } else if (e.getSQLState().equals("23514")) { // Violazione check constraint
                throw new SQLException("Date non valide. Verificare che:\n" +
                        "- La registrazione termini almeno 2 giorni prima dell'evento\n" +
                        "- Le date siano coerenti tra loro", e);
            }
            throw new SQLException("Errore durante la creazione dell'hackathon: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Hackathon> getHackathonByOrganizzatore(String usernameOrganizzatore) throws SQLException {
        String sql = """
                SELECT Titolo_identificativo, Organizzatore, Sede, Classifica,
                       DataInizio_registrazione, DataFine_registrazione,
                       DataInizio_evento, DataFine_evento,
                       Descrizione_problema, NumIscritti_corrente,
                       MaxNum_iscritti, MaxNum_membriTeam
                FROM HACKATHON
                WHERE Organizzatore = ?
                ORDER BY DataInizio_evento DESC
                """;

        List<Hackathon> hackathons = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usernameOrganizzatore);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    hackathons.add(createHackathonFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore nel recupero degli hackathon dell'organizzatore: " + e.getMessage(), e);
        }

        return hackathons;
    }

    @Override
    public List<Hackathon> getAllHackathon() throws SQLException {
        String sql = """
                SELECT Titolo_identificativo, Organizzatore, Sede, Classifica,
                       DataInizio_registrazione, DataFine_registrazione,
                       DataInizio_evento, DataFine_evento,
                       Descrizione_problema, NumIscritti_corrente,
                       MaxNum_iscritti, MaxNum_membriTeam
                FROM HACKATHON
                ORDER BY DataInizio_evento DESC
                """;

        List<Hackathon> hackathons = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                hackathons.add(createHackathonFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Errore nel recupero di tutti gli hackathon: " + e.getMessage(), e);
        }

        return hackathons;
    }

    @Override
    public List<Hackathon> getHackathonConRegistrazioniAperte() throws SQLException {
        String sql = """
                SELECT Titolo_identificativo, Organizzatore, Sede, Classifica,
                       DataInizio_registrazione, DataFine_registrazione,
                       DataInizio_evento, DataFine_evento,
                       Descrizione_problema, NumIscritti_corrente,
                       MaxNum_iscritti, MaxNum_membriTeam
                FROM HACKATHON
                WHERE DataInizio_registrazione <= CURRENT_DATE 
                  AND DataFine_registrazione >= CURRENT_DATE
                  AND COALESCE(NumIscritti_corrente, 0) < MaxNum_iscritti
                ORDER BY DataFine_registrazione ASC
                """;

        List<Hackathon> hackathons = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                hackathons.add(createHackathonFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Errore nel recupero degli hackathon con registrazioni aperte: " + e.getMessage(), e);
        }

        return hackathons;
    }

    @Override
    public Hackathon getHackathonByTitolo(String titoloIdentificativo) throws SQLException {
        return findByKey(titoloIdentificativo);
    }

    @Override
    public List<Hackathon> getHackathonTerminati() throws SQLException {
        String sql = """
                SELECT Titolo_identificativo, Organizzatore, Sede, Classifica,
                       DataInizio_registrazione, DataFine_registrazione,
                       DataInizio_evento, DataFine_evento,
                       Descrizione_problema, NumIscritti_corrente,
                       MaxNum_iscritti, MaxNum_membriTeam
                FROM HACKATHON
                WHERE DataFine_evento < CURRENT_DATE
                ORDER BY DataFine_evento DESC
                """;

        List<Hackathon> hackathons = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                hackathons.add(createHackathonFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Errore nel recupero degli hackathon terminati: " + e.getMessage(), e);
        }

        return hackathons;
    }

    @Override
    public boolean aggiornaClassifica(String titoloIdentificativo, String classifica) throws SQLException {
        String sql = "UPDATE HACKATHON SET Classifica = ? WHERE Titolo_identificativo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, classifica);
            stmt.setString(2, titoloIdentificativo);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore nell'aggiornamento della classifica: " + e.getMessage(), e);
        }
    }

    // Metodi dell'interfaccia GenericDAO

    @Override
    public boolean save(Hackathon entity) throws SQLException {
        return creaHackathon(entity);
    }

    @Override
    public boolean update(Hackathon entity) throws SQLException {
        String sql = """
                UPDATE HACKATHON SET 
                    Sede = ?, 
                    DataInizio_registrazione = ?, DataFine_registrazione = ?,
                    DataInizio_evento = ?, DataFine_evento = ?,
                    Descrizione_problema = ?, MaxNum_iscritti = ?, MaxNum_membriTeam = ?
                WHERE Titolo_identificativo = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getSede());
            stmt.setDate(2, Date.valueOf(entity.getDataInizioRegistrazioni().toLocalDate()));
            stmt.setDate(3, Date.valueOf(entity.getDataFineRegistrazioni().toLocalDate()));
            stmt.setDate(4, Date.valueOf(entity.getDataInizio().toLocalDate()));
            stmt.setDate(5, Date.valueOf(entity.getDataFine().toLocalDate()));
            stmt.setString(6, entity.getDescrizioneProblema());
            stmt.setInt(7, entity.getMaxNumIscritti());
            stmt.setInt(8, entity.getMaxMembriTeam());
            stmt.setString(9, entity.getTitoloIdentificativo());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore nell'aggiornamento dell'hackathon: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String titoloIdentificativo) throws SQLException {
        String sql = "DELETE FROM HACKATHON WHERE Titolo_identificativo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titoloIdentificativo);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore nell'eliminazione dell'hackathon: " + e.getMessage(), e);
        }
    }

    @Override
    public Hackathon findByKey(String titoloIdentificativo) throws SQLException {
        String sql = """
                SELECT Titolo_identificativo, Organizzatore, Sede, Classifica,
                       DataInizio_registrazione, DataFine_registrazione,
                       DataInizio_evento, DataFine_evento,
                       Descrizione_problema, NumIscritti_corrente,
                       MaxNum_iscritti, MaxNum_membriTeam
                FROM HACKATHON
                WHERE Titolo_identificativo = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titoloIdentificativo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createHackathonFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore nella ricerca dell'hackathon: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<Hackathon> findAll() throws SQLException {
        return getAllHackathon();
    }

    /**
     * Metodo di utilità per creare un oggetto Hackathon da un ResultSet.
     *
     * @param rs Il ResultSet contenente i dati dell'hackathon
     * @return L'oggetto Hackathon creato
     * @throws SQLException in caso di errore nella lettura dei dati
     */
    private Hackathon createHackathonFromResultSet(ResultSet rs) throws SQLException {
        String titoloIdentificativo = rs.getString("Titolo_identificativo");
        String organizzatore = rs.getString("Organizzatore");
        String sede = rs.getString("Sede");
        
        // Conversione date da SQL Date a LocalDateTime
        LocalDateTime dataInizioEvento = rs.getDate("DataInizio_evento").toLocalDate().atStartOfDay();
        LocalDateTime dataFineEvento = rs.getDate("DataFine_evento").toLocalDate().atTime(23, 59, 59);
        LocalDateTime dataInizioRegistrazione = rs.getDate("DataInizio_registrazione").toLocalDate().atStartOfDay();
        
        int maxMembriTeam = rs.getInt("MaxNum_membriTeam");
        int maxNumIscritti = rs.getInt("MaxNum_iscritti");

        try {
            // Creo l'hackathon usando il costruttore principale
            Hackathon hackathon = new Hackathon(
                    titoloIdentificativo,  // titolo identificativo (chiave primaria)
                    organizzatore,         // organizzatore
                    sede,                  // sede
                    dataInizioEvento,      // data inizio evento
                    dataFineEvento,        // data fine evento
                    dataInizioRegistrazione, // data inizio registrazioni
                    maxMembriTeam,         // max membri team
                    maxNumIscritti,        // max num iscritti
                    rs.getString("Descrizione_problema") // descrizione problema
            );

            // Imposto i dati aggiuntivi
            hackathon.setNumIscritti(rs.getInt("NumIscritti_corrente"));

            return hackathon;
            
        } catch (Exception e) {
            throw new SQLException("Errore nella creazione dell'oggetto Hackathon: " + e.getMessage(), e);
        }
    }
}
