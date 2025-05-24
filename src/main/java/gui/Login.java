package gui;

import javax.swing.*;
import model.*;
import controller.*;
import utilities.RandomStringGenerator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Arrays;

public class Login {
    private JPanel loginPanel;
    private JTextField fieldUsername;
    private JPasswordField fieldPassword;
    private JButton getLoginBtn;
    public JFrame logFrame;

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
                        controller.aggiungiUtente(username);
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
                        controller.aggiungiUtente(username);
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
