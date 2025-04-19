package model;

/**
 * Classe base che rappresenta un utente del sistema, con funzionalità di autenticazione.
 * Può essere estesa per creare tipi specifici di utenti (Organizzatore, Giudice).
 */
public class Utente {
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
     * Ritorna l'username dell'utente.
     *
     * @return Restituisce il nome dell'utente
     */
    public String getName() {
        return this.login;
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
     * Permette all'utente di unirsi a un nuovo team.
     *
     * @param team Il team a cui unirsi.
     */
    public void entrataTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Il team non può essere null.");
        }

        team.aggiungiMembro(this);
    }

    /**
     * Permette all'utente di abbandonare il team corrente di cui fa parte.
     */
    public void abbandonaTeam() {
        if (teamCorrente == null) {
            throw new IllegalArgumentException("L'utente non appartiene a nessun team.");
        }
        // Controllo se l'utente è ancora in tempo per abbandonare il team
        if (teamCorrente.getHackathon().controlloValiditaDataReg()) {
            // Rimuovo l'utente dalla lista dei membri del team
            teamCorrente.getMembro().remove(this);
            // Decremento il numero di iscritti
            teamCorrente.getHackathon().decrementaNumIscritti();
            // Se il team è vuoto, lo rimuovo dalla lista dei team
            if (teamCorrente.getMembro().isEmpty()) {
                for (int i = 0; i < teamCorrente.getHackathon().getClassifica().size(); i++) {
                    if (teamCorrente.getHackathon().getClassifica().get(i).equals(teamCorrente)) {
                        teamCorrente.getHackathon().getClassifica().remove(i);
                        break;
                    }
                }
            }
            // Setto a null il team corrente dell'utente
            this.setNewTeam(null);
        } else {
            throw new IllegalStateException("Non puoi abbandonare il team dopo la scadenza delle registrazioni.");
        }
    }

    /**
     * Metodo placeholder per gestire inviti come giudice.
     *
     * @param idEvento ID dell'evento per cui si riceve l'invito.
     * @return Oggetto Giudice (attualmente null).
     */
    public Giudice getInvite(String idEvento) {
        // TODO Placeholder per implementazione futura
        return null;
    }

    /**
     * Permette all'utente di creare un nuovo team.
     *
     * @param hackathon Evento a cui il team parteciperà.
     */
    public Team creaTeam(Hackathon hackathon, String nomeTeam) {
        if (hackathon == null) {
            throw new IllegalArgumentException("L'hackathon non può essere null.");
        }
        hackathon.controlloValiditaDataReg();
        // Il seguente controllo non sarà necessario poi perché un utente già registrato a un team non avrà
        // accesso a questa funzione nell'interfaccia
        if (this.teamCorrente != null) {
            throw new IllegalStateException("Sei già registrato a un team, abbandona il team corrente per crearne uno nuovo.");
        }
        if (nomeTeam == null || nomeTeam.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del team non può essere null o vuoto.");
        }

        Team nuovoTeam = new Team(hackathon, nomeTeam);
        this.entrataTeam(nuovoTeam);
        this.getTeam().getHackathon().getClassifica().add(this.getTeam());

        return nuovoTeam;
    }
}