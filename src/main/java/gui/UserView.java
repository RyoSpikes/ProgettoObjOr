package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserView {
    public JFrame userViewFrame;
    private JPanel userPanel;
    private JTextArea userTextArea;
    private JButton creaTeam;
    private JButton mostraInvitiButton;
    private JButton scegliTeamButton;
    private Utente user;

    public UserView(Utente userLogged, JFrame frameCalling) {
        user = userLogged;
        userViewFrame = new JFrame("User View");
        userViewFrame.setContentPane(userPanel);
        userViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userViewFrame.pack();
        userViewFrame.setVisible(true);
        userViewFrame.setSize(800, 800);
        userViewFrame.setResizable(false);
        userTextArea.setEditable(false);

        creaTeam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
