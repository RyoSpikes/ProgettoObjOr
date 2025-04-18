import model.*;

import javax.print.Doc;

public class Main {
    public static void main(String[] args) {
        Organizzatore org1 = new Organizzatore("Gioele", "4316");
        org1.getLoginOrganizzatore("4316");

        Utente user1 = new Utente("Giulia", "4312");
        user1.getLogin("4312");

        org1.registrazioneHackathon(
                "1",
                "Napoli",
                "16/07/2001",
                "18/07/2001",
                "16/07/2001",
                "18/07/2001",
                "Forza napoli",
                3,
                12
        );

        org1.getHackathonOrganizzata(
                org1.getIndexHackathonOrganizzata("1")
        ).printInfoEvento();

        Team team1 = new Team(
                org1.getHackathonOrganizzata(org1.getIndexHackathonOrganizzata("1")),
                "Napoli"
        );

        team1.aggiungiMembro(new Utente("Carlo", "0103"));
        team1.aggiungiMembro(new Utente("Stefania", "0209"));

        team1.printMembers();

        Giudice giudice1 = new Giudice(
                "Dario", "4313",
                org1.getHackathonOrganizzata(org1.getIndexHackathonOrganizzata("1"))
        );

        giudice1.printGiudice();

        // Ulteriore testing
        giudice1.pubblicazioneProblema("I partecipanti dovranno tifare Napoli!");
        org1.getHackathonOrganizzata(
                org1.getIndexHackathonOrganizzata("1")
        ).printInfoEvento();

        team1.uploadDocumento("Prova documento", "Sarebbe inutile testare questo metodo perchè il documento dovrebbe essere effettivamente un file e non una stringa ma non abbiamo ancora un database.");

        // Stampa manuale informazioni documento
        System.out.println("Documento caricato:");
        //Stampa documento basandosi su ricerca tramite confronto di sottostringa (DA RIVEDERE)
        team1.stampaDocumento(team1.cercaDocumento("Prova"));


        // Giudice che vuole visualizzare l'ultimo documento caricato dal team e decidere se valutarlo
        giudice1.visualizzaValutaUltimoDocumento(team1);
        // Inseriamo un commento casuale per testare il metodo

        // Da utente ora vorrei visualizzare le valutazioni di un qualsiasi documento caricato da un team
        team1.getDocumentazione().get(0).stampaValutazioni();
    }
}