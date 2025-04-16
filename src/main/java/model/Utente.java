package model;

/**
 * The type Utente.
 */
public class Utente {
    private final String login;
    private String password;
    private Giudice statusGiudice;

    /**
     * Instantiates a new Utente.
     *
     * @param login    the login
     * @param password the password
     */
    public Utente(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Gets login.
     *
     * @return the login
     */
    public boolean getLogin(String password) {
        return this.password.equals(password);
    }

    public Giudice getInvite(String idEvento)
    {
        System.out.println("Invito come giudice per l'Hackaton da ID: " +
                idEvento);
        return statusGiudice = new Giudice(login, password);
    }
}
