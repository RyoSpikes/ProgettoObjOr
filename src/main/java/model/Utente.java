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
    public String getLogin() {
        return login;
    }

    public Giudice getInvite(String idEvento)
    {
        System.out.println("Invito come giudice per l'Hackaton da ID: " +
                idEvento);
        return statusGiudice = new Giudice(login, password);
    }
}
