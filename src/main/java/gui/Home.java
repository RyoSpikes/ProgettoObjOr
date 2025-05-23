package gui;

import controller.Controller;
import controller.ControllerOrganizzatore;
import model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home {
    private JPanel mainPanel;
    private JTextArea homeTextArea;
    private JButton stampaUtentiBtn;
    private JScrollPane textAreaScrollPane;
    private JButton aggiungiUtenteBtn;
    private JButton aggiungiOrganizzatoreBtn;
    private JButton stampaOrganizzatoriBtn;
    public static JFrame frame;
    private Controller userController;
    private ControllerOrganizzatore orgController;

    public static void main(String[] args) {
        frame = new JFrame("Home");
        frame.setContentPane(new Home().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800, 800);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public Home() {
        userController = new Controller();
        orgController = new ControllerOrganizzatore();
        homeTextArea.setEditable(false);

        stampaUtentiBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeTextArea.setText("");
                if(userController.getListaUtenti().isEmpty())
                    homeTextArea.append("Lista Utenti vuota.");
                else {
                    for (Utente u : userController.getListaUtenti()) {
                        homeTextArea.append(
                                "Utente: " + u.getName() + "\n"
                        );
                    }
                }
            }
        });

        aggiungiUtenteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeTextArea.setText("");
                frame.setVisible(false);
                new Login(userController, frame);
            }
        });

        aggiungiOrganizzatoreBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeTextArea.setText("");
                frame.setVisible(false);
                new Login(orgController, frame);
            }
        });

        stampaOrganizzatoriBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeTextArea.setText("");

                if(orgController.getListaOrganizzatori().isEmpty())
                    homeTextArea.append("Lista Organizzatori vuota.");
                else {
                    for (Organizzatore org : orgController.getListaOrganizzatori()) {
                        homeTextArea.append(
                                "Organizzatore: " + org.getName() + "\n"
                        );
                    }
                }
            }
        });
    }


}