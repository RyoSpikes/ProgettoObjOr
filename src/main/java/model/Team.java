package model;

import java.util.ArrayList;

public class Team {

    //Lista dei membri appartenenti al team
    Utente[] membro;
    Hackathon eventoPartecipazione;
    int index = 0;


    public Team(Hackathon evento)
    {
        membro = new Utente[evento.getMaxMembriTeam()];
        eventoPartecipazione = evento;
    }

    public void aggiungiMembro(Utente user)
    {
        if(index < eventoPartecipazione.getMaxMembriTeam())
            membro[index++] = user;
    }

    public String uploadDocumento()
    {
        return "documento";
    }
}
