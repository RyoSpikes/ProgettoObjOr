package model;

import javax.swing.event.DocumentEvent;
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
     * @param team Team dal quale ricavare il documento da valutare.
     * @return valutazione restituisce una valutazione su assenso del giudice.
     *                     restituisce null altrimenti.
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
        Documento documento = team.getDocumentazione().get(team.getDocumentazione().size() - 1);

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
}