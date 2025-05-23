package controller;
import model.*;
import utilities.RandomStringGenerator;

import javax.swing.*;
import java.util.ArrayList;

public class Controller {
    private ArrayList<Utente> listaUtenti;

    public Controller()
    {
        listaUtenti = new ArrayList<>();
    }

    public void aggiungiUtente(String username)
    {
        try {
            listaUtenti.add(new Utente(username, "admin"));
        }
        catch (IllegalArgumentException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public boolean checkLogin(String user, String password)
    {
        for(Utente u : listaUtenti)
        {
            try
            {
                if(u.getName().equals(user) && u.getLogin(password))
                {
                    return true;
                }
            }
            catch(IllegalArgumentException e)
            {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), e);
            }
        }
        return false;
    }

    public ArrayList<Utente> getListaUtenti()
    {
        return listaUtenti;
    }
}
