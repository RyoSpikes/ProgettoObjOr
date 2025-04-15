package model;

import java.util.ArrayList;

public class Organizzatore extends Utente {
    private ArrayList<Hackathon> hackathonOrganizzate;

    /**
     * Instantiates a new Utente.
     *
     * @param login    the login
     * @param password the password
     */
    public Organizzatore(String login, String password) {
        super(login, password);
        hackathonOrganizzate = new ArrayList<Hackathon>();
    }

    public void registrazioneHackathon(
            String idNum,
            String sede,
            String dataInizio,
            String dataFine,
            String dataInizioRegistrazioni,
            String dataFineRegistrazioni,
            String titolo,
            int maxMembriTeam,
            int maxNumIscritti
    )
    {
        hackathonOrganizzate.add(new Hackathon(
                idNum, sede, dataInizio,
                dataFine, dataInizioRegistrazioni, dataFineRegistrazioni,
                titolo, maxMembriTeam, maxNumIscritti
                ));
    }

    public void invitoGiudice(Utente giudiceAggiunto, String idNum)
    {
        boolean flag = false;
        for (Hackathon hack:
             hackathonOrganizzate) {
            if(hack.getIdNum().equals(idNum))
            {
                hack.aggiungiGiudice(giudiceAggiunto.getInvite(idNum));
                flag = true;
            }
        }
        if(!flag)
            System.out.println("Non è stato trovato alcun evento con ID: " + idNum);
    }
}
