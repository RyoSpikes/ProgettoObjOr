package model;

/**
 * Classe che rappresenta un voto assegnato da un giudice a un team partecipante.
 * Il voto viene automaticamente aggiunto al punteggio totale del team.
 */
public class Voto {
    /**
     * Team a cui è stato assegnato il voto.
     */
    private Team teamGiudicato;

    /**
     * Giudice che ha assegnato il voto.
     */
    private Giudice giudiceVotante;

    /**
     * Valore numerico del voto assegnato.
     */
    private int voto;

    /**
     * Costruttore che crea un nuovo voto e lo aggiunge automaticamente al team.
     *
     * @param teamGiudicato  Team destinatario del voto (non nullo)
     * @param giudiceVotante Giudice che assegna il voto (non nullo)
     * @param voto           Valore numerico del voto
     */
    public Voto(Team teamGiudicato, Giudice giudiceVotante, int voto) {
        this.teamGiudicato = teamGiudicato;
        this.giudiceVotante = giudiceVotante;
        teamGiudicato.setVotoFinale(this.voto = voto);
    }

    /**
     * Restituisce il valore del voto.
     *
     * @return Valore del voto (intero)
     */
    public int getVoto() {return voto;}
    // Forse non serve
}