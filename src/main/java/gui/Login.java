package gui;

import javax.swing.*;
import model.*;
import controller.*;
import utilities.RandomStringGenerator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.util.Arrays;

public class Login {
    private JPanel loginPanel;
    private JTextField fieldUsername;
    private JPasswordField fieldPassword;
    private JButton getLoginBtn;
    public JFrame logFrame;

    public Login(Controller controller, JFrame frameCalling) {
        logFrame = new JFrame("Login");
        logFrame.setContentPane(loginPanel);
        logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logFrame.pack();
        logFrame.setVisible(true);
        logFrame.setSize(600, 300);
        logFrame.setResizable(false);

        getLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = fieldUsername.getText();
                String password = new String(fieldPassword.getPassword());
                boolean trovato = false;

                if(controller.getListaUtenti().isEmpty())
                {
                    controller.aggiungiUtente(username);
                    //frameCalling.setVisible(true);
                    new UserView(controller.getListaUtenti().getLast(), frameCalling);
                    logFrame.dispose();
                }
                else {
                    for (Utente u : controller.getListaUtenti()) {
                        if (u.getName().equals(username)) {
                            trovato = true;
                            try {
                                if (!u.getLogin(password)) {
                                    JOptionPane.showMessageDialog(null, "Password Errata.");
                                } else {
                                    frameCalling.setVisible(true);
                                    logFrame.dispose();
                                }
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(null, ex);
                            }
                        }
                    }
                    if(!trovato) {
                        controller.aggiungiUtente(username);
                        frameCalling.setVisible(true);
                        logFrame.dispose();
                    }
                }
            }
        });
    }

    public Login(ControllerOrganizzatore controller, JFrame frameCalling) {
        logFrame = new JFrame("Login");
        logFrame.setContentPane(loginPanel);
        logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logFrame.pack();
        logFrame.setVisible(true);
        logFrame.setSize(600, 300);
        logFrame.setResizable(false);

        getLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean trovato = false;
                String username = fieldUsername.getText();
                String password = new String(fieldPassword.getPassword());

                if(controller.getListaOrganizzatori().isEmpty())
                {
                    controller.aggiungiUtente(username);
                    frameCalling.setVisible(true);
                    logFrame.dispose();
                }
                else {
                    for (Organizzatore u : controller.getListaOrganizzatori()) {
                        if (u.getName().equals(username)) {
                            trovato = true;
                            try {
                                if (!u.getLogin(password)) {
                                    JOptionPane.showMessageDialog(null, "Password Errata.");
                                }
                                else {
                                    frameCalling.setVisible(true);
                                    logFrame.dispose();
                                }
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(null, ex);
                            }
                        }
                    }
                    if(!trovato) {
                        controller.aggiungiUtente(username);
                        frameCalling.setVisible(true);
                        logFrame.dispose();
                    }
                }
            }
        });
    }


}
