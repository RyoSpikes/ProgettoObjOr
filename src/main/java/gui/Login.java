package gui;

import javax.swing.*;
import model.*;
import controller.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * La classe Login rappresenta l'interfaccia grafica per il login degli utenti e degli organizzatori.
 * Consente di autenticarsi o registrarsi come nuovo utente o organizzatore.
 */
public class Login {
    private JPanel loginPanel; // Pannello principale della finestra di login.
    private JTextField fieldUsername; // Campo di testo per l'inserimento del nome utente.
    private JPasswordField fieldPassword; // Campo di testo per l'inserimento della password.
    private JButton getLoginBtn; // Pulsante per effettuare il login.
    public JFrame logFrame; // Finestra principale della vista di login.

    /**
     * Metodo per trovare un utente nella lista degli utenti in base alle credenziali fornite.
     *
     * @param utenti La lista degli utenti registrati.
     * @param username Il nome utente da cercare.
     * @param password La password da verificare.
     * @return L'utente trovato, o null se non esiste.
     * @throws IllegalArgumentException se il nome utente è vuoto o la password è errata.
     */
    private Utente foundLogin(ArrayList<Utente> utenti, String username, String password) throws IllegalArgumentException {
        if(!utenti.isEmpty()) {
            for(Utente u : utenti) {
                if(username == null || username.isEmpty())
                    throw new IllegalArgumentException("Non è stato inserito alcun nome utente.");

                if(username.equals(u.getName())) {
                    if(!u.getLogin(password))
                        throw new IllegalArgumentException("Password errata.");
                    else {
                        return u;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Metodo per trovare un organizzatore nella lista degli organizzatori in base alle credenziali fornite.
     *
     * @param utenti La lista degli organizzatori registrati.
     * @param username Il nome utente da cercare.
     * @param password La password da verificare.
     * @return L'organizzatore trovato, o null se non esiste.
     * @throws IllegalArgumentException se il nome utente è vuoto o la password è errata.
     */
    private Organizzatore foundLoginOrg(ArrayList<Organizzatore> utenti, String username, String password) throws IllegalArgumentException {
        if(!utenti.isEmpty()) {
            for(Organizzatore u : utenti) {
                if(username == null || username.isEmpty())
                    throw new IllegalArgumentException("Non è stato inserito alcun nome utente.");

                if(username.equals(u.getName())) {
                    if(!u.getLogin(password))
                        throw new IllegalArgumentException("Password errata.");
                    else {
                        return u;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Costruttore della classe Login per gli utenti.
     * Inizializza l'interfaccia grafica e gestisce le azioni per il login degli utenti.
     *
     * @param controllerOrganizzatore Il controller per la gestione degli organizzatori.
     * @param controller Il controller per la gestione degli utenti.
     * @param frameCalling Il frame chiamante che ha aperto questa finestra.
     */
    public Login(ControllerOrganizzatore controllerOrganizzatore, Controller controller, JFrame frameCalling) {
        logFrame = new JFrame("Login");
        logFrame.setContentPane(loginPanel);

        logFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameCalling.setVisible(true);
                logFrame.dispose();
            }
        });

        logFrame.pack();
        logFrame.setVisible(true);
        logFrame.setSize(600, 300);
        logFrame.setResizable(false);
        logFrame.setLocationRelativeTo(null);

        getLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = fieldUsername.getText();
                String password = new String(fieldPassword.getPassword());

                try
                {
                    Utente u = foundLogin(controller.getListaUtenti(), username, password);
                    if(u == null)
                    {
                        controller.aggiungiUtente(username, password);
                        new UserView(controller.getListaUtenti().getLast(), frameCalling, controllerOrganizzatore, controller);
                        logFrame.dispose();
                    }
                    else
                    {
                        new UserView(u,frameCalling, controllerOrganizzatore, controller);
                        logFrame.dispose();
                    }
                }
                catch (IllegalArgumentException ex)
                {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });
    }

    /**
     * Costruttore della classe Login per gli organizzatori.
     * Inizializza l'interfaccia grafica e gestisce le azioni per il login degli organizzatori.
     *
     * @param controller Il controller per la gestione degli organizzatori.
     * @param frameCalling Il frame chiamante che ha aperto questa finestra.
     */
    public Login(ControllerOrganizzatore controller, JFrame frameCalling) {
        logFrame = new JFrame("Login");
        logFrame.setContentPane(loginPanel);

        logFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameCalling.setVisible(true);
                logFrame.dispose();
            }
        });

        logFrame.pack();
        logFrame.setVisible(true);
        logFrame.setSize(600, 300);
        logFrame.setResizable(false);
        logFrame.setLocationRelativeTo(null);

        getLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = fieldUsername.getText();
                String password = new String(fieldPassword.getPassword());

                try
                {
                    Organizzatore org = foundLoginOrg(controller.getListaOrganizzatori(), username, password);
                    if(org == null)
                    {
                        controller.aggiungiUtente(username, password);
                        new AdminView(controller.getListaOrganizzatori().getLast(), frameCalling);
                        logFrame.dispose();
                    }
                    else
                    {
                        new AdminView(org,frameCalling);
                        logFrame.dispose();
                    }
                }
                catch (IllegalArgumentException ex)
                {
                    JOptionPane.showMessageDialog(null, ex);
                }

            }
        });
    }
}