package controller;

import model.*;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ControllerOrganizzatore extends Controller{

    ArrayList<Organizzatore> listaOrganizzatori;

    public ControllerOrganizzatore() {
        listaOrganizzatori = new ArrayList<>();
    }

    public ArrayList<Organizzatore> getListaOrganizzatori() {
        return listaOrganizzatori;
    }

    public ArrayList<Hackathon> getListaHackathon()
    {
        ArrayList<Hackathon> hList = new ArrayList<>();
        for(Organizzatore o : listaOrganizzatori)
        {
            hList.addAll(o.getHackathonOrganizzate());
        }
        return hList;
    }

    //TODO
    /*public ArrayList<Hackathon> getListaHackathon(Organizzatore org) {
        return org.getHackathonOrganizzata;
    }*/

    @Override
    public void aggiungiUtente(String username) throws IllegalArgumentException {
        try {
            listaOrganizzatori.add(new Organizzatore(username, "admin"));
        }
        catch (IllegalArgumentException ex)
        {
            throw ex;
        }
    }

    public void aggiungiHackathon(Organizzatore org, String idNum, String sede, LocalDateTime dataInizio, LocalDateTime dataFine,
                                  LocalDateTime dataInizioRegistrazioni, String titolo,
                                  int maxMembriTeam, int maxNumIscritti)
    {
        try {
            org.registrazioneHackathon(idNum, sede, dataInizio, dataFine, dataInizioRegistrazioni, titolo, maxMembriTeam, maxNumIscritti);
        }
        catch (DateTimeException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }


}
