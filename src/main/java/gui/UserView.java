package gui;

import controller.Controller;
import controller.ControllerOrganizzatore;
import model.Utente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserView {
    public JFrame userViewFrame;
    private JPanel userPanel;
    private JTextArea userTextArea;
    private JButton creaTeam;
    private JButton mostraInvitiButton;
    private JButton scegliTeamButton;
    private JButton visualizzaTeamButton;

    public UserView(Utente userLogged, JFrame frameHome, ControllerOrganizzatore controllerOrganizzatore, Controller controllerUtente) {
        userViewFrame = new JFrame("User View");
        userViewFrame.setContentPane(userPanel);
        userViewFrame.pack();

        userViewFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameHome.setVisible(true);
                userViewFrame.dispose();
            }
        });

        userViewFrame.setVisible(true);
        userViewFrame.setSize(800, 800);
        userViewFrame.setResizable(false);
        userViewFrame.setLocationRelativeTo(null);
        userTextArea.setEditable(false);

        creaTeam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userViewFrame.setVisible(false);
                new CreaTeamForm(userLogged, userViewFrame, controllerOrganizzatore);
            }
        });

        //TODO
        mostraInvitiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userTextArea.append("");
                userTextArea.append("Non ci sono inviti da mostrare.");
            }
        });
        scegliTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ScegliTeam(userLogged, userViewFrame, controllerOrganizzatore, controllerUtente);
                userViewFrame.setVisible(false);
            }
        });
        visualizzaTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userTextArea.setText("");
                try{
                    userTextArea.append(
                            "Nome Team: " + userLogged.getTeam().getNomeTeam() +
                                    "\nHackathon: \n" + userLogged.getTeam().getHackathon().printInfoEvento()
                    );
                }
                catch(NullPointerException ex)
                {
                    userTextArea.append("Non sei in nessun team.");
                }
            }
        });
    }
}
