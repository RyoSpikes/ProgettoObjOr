import model.*;

import javax.print.Doc;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Organizzatore org1 = new Organizzatore("Gioele", "4316");
        org1.getLoginOrganizzatore("4316");

        Utente user1 = new Utente("Giulia", "4312");
        user1.getLogin("4312");

        org1.registrazioneHackathon(
                "1",
                "Napoli",
                LocalDateTime.of(2025, 4, 24, 0, 0),
                LocalDateTime.of(2026, 12, 30, 0, 0),
                LocalDateTime.of(2025, 4, 10, 0, 0),
                "Forza napoli",
                3,
                12
        );

        org1.getHackathonOrganizzata(
                org1.getIndexHackathonOrganizzata("1")
        ).printInfoEvento();

        Team team1 = user1.creaTeam(org1.getHackathonOrganizzata(org1.getIndexHackathonOrganizzata("1")),
                "Napoli");

        team1.aggiungiMembro(new Utente("Carlo", "0103"));
        team1.aggiungiMembro(new Utente("Stefania", "0209"));

        user1.abbandonaTeam();

        user1.entrataTeam(team1);
        team1.getMembro().get(1).abbandonaTeam();

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

        user1.getTeam().uploadDocumento("Prova documento", "Sarebbe inutile testare questo metodo perchè il documento dovrebbe essere effettivamente un file e non una stringa ma non abbiamo ancora un database.");

        // Stampa manuale informazioni documento
        System.out.println("Documento caricato:");
        // Da utente vorrei stampare un documento del mio team basandomi su ricerca tramite confronto di sottostringa (DA RIVEDERE)
        user1.getTeam().stampaDocumento(user1.getTeam().cercaDocumento("prova"));


        // Giudice che vuole visualizzare l'ultimo documento caricato dal team e decidere se valutarlo
        giudice1.visualizzaValutaUltimoDocumento(user1.getTeam());
        // Inseriamo un commento casuale per testare il metodo

        // Da utente vorrei visualizzare le valutazioni di un qualsiasi documento caricato da un team
        user1.getTeam().getDocumentazione().get(0).stampaValutazioni();

        // Da utente voglio abbandonare il team
        user1.abbandonaTeam();
        if (user1.getTeam() == null) {
            System.out.println("L'utente ha abbandonato correttamente il team");
        } else {
            System.out.println("Utente non abbandonato il team");
        }

        // Testing su un hackaton con data di registrazione non valida (WORKING)
        org1.registrazioneHackathon(
                "2",
                "Napoli",
                LocalDateTime.of(2025, 4, 28, 0, 0),
                LocalDateTime.of(2025, 4, 20, 0, 0),
                LocalDateTime.of(2025, 4, 5, 0, 0),
                "Forza napoli 2",
                3,
                12
        );

        Utente user3 = new Utente("Mimmo", "11037");

        user3.creaTeam(org1.getHackathonOrganizzata(org1.getIndexHackathonOrganizzata("2")),
                "Salerno");

        Utente user2 = new Utente("Marco", "7489");
        user2.entrataTeam(user3.getTeam());
        // L'eccezione viene chiamata correttamente
    }
}