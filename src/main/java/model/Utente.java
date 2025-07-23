package model;

import Database.ConnessioneDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Classe base che rappresenta un utente del sistema, con funzionalità di autenticazione.
 * Può essere estesa per creare tipi specifici di utenti (Organizzatore, Giudice).
 */
public class Utente {

    //TODO: Cambiare in Username
    /**
     * Username dell'utente.
     * Definito come costante per aumentare la sicurezza della struttura.
     */
    private final String login;

    /**
     * Password dell'utente.
     */
    private String password;

    /**
     * Team di appartenenza dell'utente.
     */
    private Team teamCorrente;

    /**
     * Costruttore per creare un nuovo utente.
     *
     * @param login    L'username univoco dell'utente.
     * @param password La password in chiaro.
     * @throws IllegalArgumentException Se login o password sono null/vuoti.
     */
    public Utente(String login, String password) throws IllegalArgumentException {
        if (login == null || login.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Login e password non possono essere null o vuoti.");
        }
        this.login = login;
        this.password = password;
    }

    /**
     * Verifica se la password fornita corrisponde a quella dell'utente.
     *
     * @param password La password da verificare (case-sensitive).
     * @return true se la password è corretta, false altrimenti.
     * @throws IllegalArgumentException Se la password fornita è null o vuota.
     */
    public boolean getLogin(String password) throws IllegalArgumentException {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La password non può essere null o vuota.");
        }
        return this.password.equals(password);
    }

    /**
     * Ritorna l'username dell'utente.
     *
     * @return Restituisce il nome dell'utente
     */
    public String getName() {
        return this.login;
    }

    /**
     * Ritorna la password dell'utente.
     * Necessario per le operazioni di persistenza nel database.
     *
     * @return La password dell'utente
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Ritorna il team dell'utente.
     *
     * @return Restituisce il team dell'utente
     */
    public Team getTeam() {
        return this.teamCorrente;
    }

    /**
     * Aggiorna il team di appartenenza quando l'utente cambia team.
     *
     * @param team Il nuovo team di appartenenza.
     */
    public void setNewTeam(Team team) {
        this.teamCorrente = team;
    }

    /**
     * Permette all'utente di aderire a un team esistente.
     * Inserisce una nuova riga nella tabella MEMBERSHIP del database.
     * I trigger del database gestiranno automaticamente le validazioni:
     * - Verifica che le registrazioni siano ancora aperte
     * - Verifica che il team non sia al completo
     * - Verifica che l'hackathon non abbia raggiunto il limite di partecipanti
     *
     * @param team Il team a cui l'utente vuole aderire
     * @throws IllegalArgumentException Se l'adesione non è possibile per i vincoli del database
     */
    public void entrataTeam(Team team) throws IllegalArgumentException {
        if (team == null) {
            throw new IllegalArgumentException("Il team non può essere null");
        }

        String sql = "INSERT INTO MEMBERSHIP (Username_utente, Team_appartenenza, Titolo_hackathon, Data_adesione) VALUES (?, ?, ?, CURRENT_DATE)";

        try (Connection conn = ConnessioneDatabase.getInstance().connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.login);
            stmt.setString(2, team.getNomeTeam());
            stmt.setString(3, team.getHackathon().getTitoloIdentificativo());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Aggiorna il team corrente dell'utente
                this.teamCorrente = team;
                System.out.println("Utente " + this.login + " è entrato nel team " + team.getNomeTeam());
            } else {
                throw new IllegalArgumentException("Non è stato possibile aderire al team");
            }

        } catch (SQLException e) {
            // Il database restituisce errori specifici tramite i trigger
            throw new IllegalArgumentException("Errore nell'adesione al team: " + e.getMessage());
        }
    }

    /**
     * Permette all'utente di creare un nuovo team per un hackathon specifico.
     * Inserisce il team nella tabella TEAM e l'utente nella tabella MEMBERSHIP.
     *
     * @param hackathon L'hackathon per cui creare il team
     * @param nomeTeam Il nome del team da creare
     * @return Il team appena creato
     * @throws IllegalArgumentException Se la creazione non è possibile
     * @throws IllegalStateException Se l'utente è già in un team per questo hackathon
     */
    public Team creaTeam(Hackathon hackathon, String nomeTeam) throws IllegalArgumentException, IllegalStateException {
        if (hackathon == null || nomeTeam == null || nomeTeam.trim().isEmpty()) {
            throw new IllegalArgumentException("Hackathon e nome team non possono essere null o vuoti");
        }

        String sqlTeam = "INSERT INTO TEAM (Nome_team, Titolo_hackathon) VALUES (?, ?)";
        String sqlMembership = "INSERT INTO MEMBERSHIP (Username_utente, Team_appartenenza, Titolo_hackathon, Data_adesione) VALUES (?, ?, ?, CURRENT_DATE)";

        try (Connection conn = ConnessioneDatabase.getInstance().connection) {
            conn.setAutoCommit(false); // Inizia transazione

            try (PreparedStatement stmtTeam = conn.prepareStatement(sqlTeam);
                 PreparedStatement stmtMembership = conn.prepareStatement(sqlMembership)) {

                // Inserisci il team
                stmtTeam.setString(1, nomeTeam);
                stmtTeam.setString(2, hackathon.getTitoloIdentificativo());
                stmtTeam.executeUpdate();

                // Inserisci l'utente nel team
                stmtMembership.setString(1, this.login);
                stmtMembership.setString(2, nomeTeam);
                stmtMembership.setString(3, hackathon.getTitoloIdentificativo());
                stmtMembership.executeUpdate();

                conn.commit(); // Conferma transazione

                // Crea e restituisce l'oggetto Team
                Team nuovoTeam = new Team(hackathon, nomeTeam);
                this.teamCorrente = nuovoTeam;

                System.out.println("Team " + nomeTeam + " creato con successo per l'hackathon " + hackathon.getTitoloIdentificativo());
                return nuovoTeam;

            } catch (SQLException e) {
                conn.rollback(); // Annulla transazione in caso di errore
                throw e;
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException("Errore nella creazione del team: " + e.getMessage());
        }
    }

    /**
     * Metodo placeholder per gestire inviti come giudice.
     *
     * @param idEvento ID dell'evento per cui si riceve l'invito.
     * @return Oggetto Giudice (attualmente null).
     */
    public Giudice getInvite(String idEvento) {
        // TODO Implementazione futura per gestire gli inviti come giudice
        return null;
    }
}
