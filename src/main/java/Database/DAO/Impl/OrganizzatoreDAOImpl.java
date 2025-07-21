package Database.DAO.Impl;

import Database.DAO.OrganizzatoreDAO;
import Database.ConnessioneDatabase;
import model.Organizzatore;
import Database.DAO.GenericDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizzatoreDAOImpl implements OrganizzatoreDAO {

    private Connection connection;

    /**
     * Costruttore che inizializza la connessione al database.
     *
     * @throws SQLException Se si verifica un errore nella connessione
     */
    public OrganizzatoreDAOImpl() throws SQLException {
        this.connection = ConnessioneDatabase.getInstance().connection;
    }

    @Override
    public Organizzatore login(String username, String password) throws SQLException {
        String sql = "SELECT Username_org, Password FROM ORGANIZZATORE WHERE Username_org = ? AND Password = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Organizzatore(
                        rs.getString("Username_org"),
                        rs.getString("Password")
                );
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il login dell'organizzatore: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Organizzatore findByNome(String nome) throws SQLException {
        return findByKey(nome);
    }

    @Override
    public Organizzatore findByHackathon(String hackathonTitolo) throws SQLException {
        String sql = """
                SELECT o.Username_org, o.Password
                FROM ORGANIZZATORE o
                JOIN HACKATHON h ON o.Username_org = h.Organizzatore
                WHERE h.Titolo_identificativo = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hackathonTitolo);

            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Organizzatore(
                            rs.getString("Username_org"),
                            rs.getString("Password")
                    );
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la ricerca dell'organizzatore per hackathon: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public boolean save(Organizzatore entity) throws SQLException {
        String sql = "INSERT INTO ORGANIZZATORE (Username_org, Password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getPassword());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante il salvataggio dell'organizzatore: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Organizzatore entity) throws SQLException {
        String sql = "UPDATE ORGANIZZATORE SET Password = ? WHERE Username_org = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getPassword());
            stmt.setString(2, entity.getName());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'aggiornamento dell'organizzatore: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String key) throws SQLException {
        String sql = "DELETE FROM ORGANIZZATORE WHERE Username_org = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, key);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Errore durante l'eliminazione dell'organizzatore: " + e.getMessage(), e);
        }
    }

    @Override
    public Organizzatore findByKey(String key) throws SQLException {
        String sql = "SELECT Username_org, Password FROM ORGANIZZATORE WHERE Username_org = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Organizzatore(
                        rs.getString("Username_org"),
                        rs.getString("Password")
                );
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante la ricerca dell'organizzatore: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Organizzatore> findAll() throws SQLException {
        List<Organizzatore> organizzatori = new ArrayList<>();
        String sql = "SELECT Username_org, Password FROM ORGANIZZATORE";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                organizzatori.add(
                        new Organizzatore(
                            rs.getString("Username_org"),
                            rs.getString("Password")
                ));
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero degli organizzatori: " + e.getMessage(), e);
        }
        return organizzatori;
    }
}
