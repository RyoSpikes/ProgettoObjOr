package model;

public class Giudice extends Utente{
    Utente userGiudice;
    public Giudice(String login, String password) {
        super(login, password);
    }

    public void pubblicaProblema(Hackathon evento, String descrizioneProblema)
    {
        evento.setDescrizioneProblema(descrizioneProblema);
    }
}
