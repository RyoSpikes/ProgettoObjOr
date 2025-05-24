package controller;
import model.*;
import utilities.RandomStringGenerator;

import javax.swing.*;
import java.util.ArrayList;

/**
 * La classe Controller gestisce una lista di utenti e fornisce metodi per la gestione degli utenti,
 * come l'aggiunta di nuovi utenti e la verifica delle credenziali di accesso.
 */
public class Controller {
    private ArrayList<Utente> listaUtenti; // Lista degli utenti gestiti dal controller.

    /**
     * Costruttore della classe Controller.
     * Inizializza la lista degli utenti.
     */
    public Controller()
    {
        listaUtenti = new ArrayList<>();
    }

    /**
     * Aggiunge un nuovo utente alla lista.
     *
     * @param username Il nome utente del nuovo utente.
     * @param password La password del nuovo utente.
     * @throws IllegalArgumentException se l'utente non può essere creato (ad esempio, input non valido).
     */
    public void aggiungiUtente(String username, String password) throws IllegalArgumentException {
        try {
            listaUtenti.add(new Utente(username, password));
        }
        catch (IllegalArgumentException ex)
        {
            throw ex;
        }
    }

    /**
     * Verifica se le credenziali di accesso fornite corrispondono a un utente nella lista.
     *
     * @param user Il nome utente da verificare.
     * @param password La password da verificare.
     * @return true se le credenziali sono valide, false altrimenti.
     */
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

    /**
     * Recupera la lista degli utenti gestiti dal controller.
     *
     * @return Un ArrayList di oggetti Utente.
     */
    public ArrayList<Utente> getListaUtenti()
    {
        return listaUtenti;
    }
}