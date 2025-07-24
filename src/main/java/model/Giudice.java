package model;

import java.util.Scanner;

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
     * NOTA: L'aggiunta dei giudici agli hackathon è ora gestita tramite database.
     * Utilizzare il sistema di inviti (INVITO_GIUDICE) per assegnare giudici.
     *
     * @param login    Credenziale di accesso (ereditata da Utente)
     * @param password Credenziale di accesso (ereditata da Utente)
     * @param evento   Hackathon di competenza del giudice
     */
    public Giudice(String login, String password, Hackathon evento) {
        super(login, password);
        this.eventoGiudicato = evento;

        // RIMOSSO: eventoGiudicato.getGiudiciEvento().add(this);
        // La gestione dei giudici è ora delegata al database tramite:
        // 1. Tabella INVITO_GIUDICE per gestire gli inviti
        // 2. Trigger aggiungi_giudice() per l'aggiunta automatica alla tabella GIUDICE
        // 3. Utilizzo delle funzionalità del database
    }

    /**
     * Stampa le informazioni del giudice.
     */
    public void printGiudice() {
        System.out.println(this.getName());
        this.eventoGiudicato.printInfoEvento();
    }

    /**
     * Pubblica la descrizione del problema da risolvere durante l'hackathon.
     * @param descrizioneProblema Descrizione del problema da pubblicare.
     */
    public void pubblicazioneProblema(String descrizioneProblema) {
        eventoGiudicato.setDescrizioneProblema(descrizioneProblema);
    }

    /**
     * Visualizza l'ultimo documento caricato da un team e consente al giudice di valutarlo.
     *
     * @param team Team dal quale ricavare il documento da valutare.
     * @return valutazione restituisce una valutazione su assenso del giudice.
     *                     Restituisce null altrimenti.
     */
    public Valutazione visualizzaValutaUltimoDocumento(Team team) {
        // Controlla se il team ha caricato documenti
        if (team.getDocumentazione().isEmpty()) {
            System.out.println("Il team non ha caricato alcun documento.");
            return null;
        }

        // Recupera l'ultimo documento caricato dal team
        // Ignora il fatto che il team potrebbe avere più documenti ancora non valutati da un giudice, basta valutare l'ultimo
        // Recupera l'ultimo documento caricato dal team recuperando la size dell'arraylist dei documenti e sottraendole 1
        Documento documento = team.getDocumentazione().getLast();

        System.out.println("Titolo: " + documento.getTitle() +
                "\nTesto documento: " +
                "\t" + documento.getText());

        // Chiedi al giudice se vuole valutare il documento
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Vuoi commentare il documento (si/no)? ");
            String risposta = scanner.nextLine();

            if (risposta.equalsIgnoreCase("si")) {
                // Se il giudice decide di valutare il documento, restituisci Valutazione
                System.out.println("Inserisci commento: ");
                String commento = scanner.nextLine();

                // Creo la valutazione, aggiorno l'arraylist delle valutazioni del documento e ritorno la nuova istanza di valutazione creata
                Valutazione valutazione = new Valutazione(documento, this, commento);
                documento.getValutazioni().add(valutazione);

                return valutazione;
            }
            // Se il giudice non vuole valutare il documento, restituisci null
            return null;
        }
    }

    /**
     * Assegna un voto a un team.
     * NOTA: La logica di controllo e generazione classifica è ora gestita dal database.
     *
     * @param team Il team a cui assegnare il voto.
     * @param voto Il valore numerico del voto (compreso tra 0 e 10).
     * @return L'istanza di Voto creata.
     * @throws IllegalArgumentException Se il voto non è valido.
     */
    public Voto assegnaVoto(Team team, int voto) {
        // Il controllo sulla data dell'evento è ora gestito dal database
        if (voto < 0 || voto > 10) {
            throw new IllegalArgumentException("Il voto deve essere compreso tra 0 e 10.");
        }

        // La logica di controllo per la fine della votazione e la generazione
        // della classifica è ora gestita dalla funzione del database:
        // genera_classifica_hackathon(titolo_hack)

        return new Voto(team, this, voto);
    }

    public Team getTeam() {
        // Non implementato, l'organizzatore non ha un team
        throw new UnsupportedOperationException("L'organizzatore non ha un team.");
    }

    public void entrataTeam(Team team) throws UnsupportedOperationException {
        // Non implementato, l'organizzatore non entra nei team
        throw new UnsupportedOperationException("L'organizzatore non può entrare in un team.");
    }

    public Team creaTeam(Hackathon hackathon, String nomeTeam) throws UnsupportedOperationException {
        // Non implementato, l'organizzatore non crea team
        throw new UnsupportedOperationException("L'organizzatore non può creare un team.");
    }
}
