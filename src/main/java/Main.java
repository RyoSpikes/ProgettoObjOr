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
                2,
                3
        );

        org1.getHackathonOrganizzata(
                org1.getIndexHackathonOrganizzata("1")
        ).printInfoEvento();

        try {
            // User crea un nuovo team
            user1.creaTeam(org1.getHackathonOrganizzata(0), "team1");


            // Utente aggiunge altri utenti
            user1.getTeam().aggiungiMembro(new Utente("Carlo", "0103"));
            // user1.getTeam().aggiungiMembro(new Utente("Stefania", "0209"));
            // L'eccezione viene chiamata correttamente perchè il team è pieno


            Utente user2 = new Utente("Stefania", "0209");
            Team team1 = user1.getTeam();

            // Eccezioni per numero massimo di membri team e numero massimo di iscritti all'hackathon funzionanti
            Team team2 = user2.creaTeam(org1.getHackathonOrganizzata(0), "team2");


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
            // Da utente vorrei stampare un documento del mio team basandomi su ricerca tramite confronto di sottostringa (DA RIVEDERE)
            user1.getTeam().stampaDocumento(team1.cercaDocumento("prova"));


            // Giudice che vuole visualizzare l'ultimo documento caricato dal team e decidere se valutarlo
            giudice1.visualizzaValutaUltimoDocumento(team1);
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
            // Genererà l'eccezione se eseguito
            org1.registrazioneHackathon(
                    "2",
                    "Napoli",
                    LocalDateTime.of(2025, 4, 28, 0, 0),
                    LocalDateTime.of(2025, 4, 29, 0, 0),
                    LocalDateTime.of(2025, 4, 5, 0, 0),
                    "Forza napoli 2",
                    3,
                    12
            );

            user2.entrataTeam(team2);
            // L'eccezione viene chiamata correttamente

            Utente user3 = new Utente("Mario", "3543");
            Utente user4 = new Utente("Luigi", "0024");
            Utente user5 = new Utente("Anna", "2503");
            Utente user6 = new Utente("Sara", "4735");

            Giudice giudice2 = new Giudice("Paolo", "4367",
                    org1.getHackathonOrganizzata(org1.getIndexHackathonOrganizzata("1"))
            );

            // Disabilitato temporaneamente il controllo su dataFine hackathon per
            // testare il metodo per l'ordinamento e stampa della classifica (WORKING)
            System.out.println(org1.getHackathonOrganizzata(0).getGiudiciEvento().size());

            team1.printMembers();
            team2.printMembers();

            giudice1.assegnaVoto(team1, 9);
            giudice1.assegnaVoto(team2, 6);

            giudice2.assegnaVoto(team1, 8);
            giudice2.assegnaVoto(team2, 5);
        }
        catch (Exception e)
        {
            System.out.println("No");
        }
    }
}