package model;

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


}
