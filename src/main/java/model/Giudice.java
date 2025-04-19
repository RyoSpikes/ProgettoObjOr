package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
     *
     * @param login    Credenziale di accesso (ereditata da Utente)
     * @param password Credenziale di accesso (ereditata da Utente)
     * @param evento   Hackathon di competenza del giudice
     */
    public Giudice(String login, String password, Hackathon evento) {
        super(login, password);
        this.eventoGiudicato = evento;
        eventoGiudicato.getGiudiciEvento().add(this);
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
     * Pubblica la descrizione del problema da risolvere durante l'hackathon.
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
        Scanner scanner = new Scanner(System.in);
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
        } else {
            // Se il giudice non vuole valutare il documento, restituisci null
            return null;
        }
    }

    /**
     * Assegna un voto a un team e verifica se la votazione è terminata.
     * Se tutti i giudici hanno votato per tutti i team, ordina e stampa la classifica.
     *
     * @param team Il team a cui assegnare il voto.
     * @param voto Il valore numerico del voto (compreso tra 0 e 10).
     * @return L'istanza di Voto creata.
     * @throws IllegalArgumentException Se l'evento non è ancora terminato o il voto non è valido.
     */
    public Voto assegnaVoto(Team team, int voto) {
        // Controllo validità del voto e della data
        if (eventoGiudicato.getDataFine().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Non puoi votare finché l'evento non è finito.");
        }
        if (voto < 0 || voto > 10) {
            throw new IllegalArgumentException("Il voto deve essere compreso tra 0 e 10.");
        }

        // Incrementa il numero di voti del team e crea il voto
        team.incrementaNumeroVoti();
        Voto nuovoVoto = new Voto(team, this, voto);

        // Controlla se la votazione è finita e stampa la classifica
        if (team.getNumeroVoti() == eventoGiudicato.getGiudiciEvento().size() &&
                eventoGiudicato.getClassifica().stream().allMatch(t -> t.getNumeroVoti() == eventoGiudicato.getGiudiciEvento().size())) {
            eventoGiudicato.ordinaStampaClassifica();
        }

        return nuovoVoto;
    }
}