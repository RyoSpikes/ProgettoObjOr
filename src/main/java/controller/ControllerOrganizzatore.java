package controller;
import controller.Controller;

import model.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ControllerOrganizzatore extends Controller{

    ArrayList<Organizzatore> listaOrganizzatori;

    public ControllerOrganizzatore() {
        listaOrganizzatori = new ArrayList<>();
        aggiungiUtente("admin");
    }

    public List<Organizzatore> getListaOrganizzatori() {
        return listaOrganizzatori;
    }

    @Override
    public void aggiungiUtente(String username) {
        try {
            listaOrganizzatori.add(new Organizzatore(username, "admin"));
        }
        catch (IllegalArgumentException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }


}
