package model;

public class Voto {
    private Team teamGiudicato;
    private Giudice giudiceVotante;
    private int voto;

    public Voto(Team teamGiudicato, Giudice giudiceVotante, int voto) {
        this.teamGiudicato = teamGiudicato;
        this.giudiceVotante = giudiceVotante;
        teamGiudicato.setVotoFinale(this.voto = voto);
    }
}
