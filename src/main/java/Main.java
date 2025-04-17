import model.*;

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
    }
}