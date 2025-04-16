package model;

import java.util.ArrayList;

public class Team {

    //Lista dei membri appartenenti al team
    private Utente[] membro;
    private String nomeTeam;
    private Hackathon eventoPartecipazione;
    private ArrayList<Documento> documentazione;
    private int votoFinale = 0;
    int index = 0;


    public Team(Hackathon evento, String nomeTeam)
    {
        membro = new Utente[evento.getMaxMembriTeam()];
        eventoPartecipazione = evento;
        this.nomeTeam = nomeTeam;
    }

    public void aggiungiMembro(Utente user)
    {
        if(index < eventoPartecipazione.getMaxMembriTeam())
            membro[index++] = user;
        else System.out.println("Attenzione, il team è pieno secondo i canoni di questa Hackathon.");
    }

    public void uploadDocumento(String doc)
    {
        documentazione.add(new Documento(this, doc));
    }

    public void setVotoFinale(int votoFinale) {
        this.votoFinale += votoFinale;
    }
}
