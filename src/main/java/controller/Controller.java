package controller;
import model.*;
import utilities.RandomStringGenerator;

import java.util.ArrayList;

public class Controller {

    private ArrayList<Hackathon> listaHackathon;
    private ArrayList<Utente> listaUtenti;

    public Controller()
    {
        listaHackathon = new ArrayList<>();
        listaUtenti = new ArrayList<>();
    }

    public void aggiungiHackathon()
    {
        listaHackathon.add(new Hackathon());
    }

    public void aggiungiUtente(String userType)
    {
        if(userType.equalsIgnoreCase(Utente.class.getName().toLowerCase()))
        {
            listaUtenti.add(new Utente(RandomStringGenerator.generateRandomString(10), "admin"));
        }
        else if(userType.equalsIgnoreCase(Organizzatore.class.getName().toLowerCase()))
        {
            listaUtenti.add(new Organizzatore(RandomStringGenerator.generateRandomString(10), "admin"));
        }
    }


}
