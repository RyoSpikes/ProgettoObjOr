package model;

/**
 * Classe base che rappresenta un utente del sistema, con funzionalità di autenticazione.
 * Può essere estesa per creare tipi specifici di utenti (Organizzatore, Giudice).
 */
public class Utente {
    /**
     * Nome dell'utente.
     * Definito come costante per aumentare la sicurezza della struttura.
     */
    private final String login;
    /**
     * Password dell'utente.
     */
    private String password;
    //private Giudice statusGiudice;

    /**
     * Costruttore per creare un nuovo utente.
     *
     * @param login    Il nome univoco dell'utente.
     * @param password La password in chiaro.
     * @throws IllegalArgumentException Se login o password sono null/vuoti.
     */
    public Utente(String login, String password) {
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
    public boolean getLogin(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La password non può essere null o vuota.");
        }
        return this.password.equals(password);
    }

    /**
     * Stampa il nome dell'utente.
     * @return Restituisce il nome dell'utente
     */
    public String getName()
    {
        return this.login;
    }

    //TO DO
    public Giudice getInvite(String idEvento) {
        /*System.out.println("Invito come giudice per l'Hackathon con ID: " + idEvento);
        return statusGiudice = new Giudice(login, password);*/
        return null;
    }
}