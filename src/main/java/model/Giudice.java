package model;

/**
 * Classe che rappresenta un giudice di hackathon, specializzazione di Utente.
 * Gestisce l'associazione con un evento specifico e la pubblicazione dei problemi.
 */
public class Giudice extends Utente {

    /**
     * Hackathon a cui il giudice è assegnato per la valutazione.
     */
    private Hackathon eventoGiudicato;

    /**
     * Costruttore del giudice.
     *
     * @param login    Credenziale di accesso (ereditata da Utente)
     * @param password Credenziale di accesso (ereditata da Utente)
     * @param evento   Hackathon di competenza del giudice
     */
    public Giudice(String login, String password, Hackathon evento) {
        super(login, password);
        this.eventoGiudicato = evento;
    }

    /**
     * Stampa le informazioni del giudice.
     */
    public void printGiudice()
    {
        System.out.println(this.getName());
        this.eventoGiudicato.printInfoEvento();
    }

    //TO DO
    public void pubblicazioneProblema(String descrizioneProblema)
    {
        eventoGiudicato.setDescrizioneProblema(descrizioneProblema);
    }
}
