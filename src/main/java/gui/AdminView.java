package gui;

import model.Hackathon;
import model.Organizzatore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AdminView {
    private JFrame frameAdminView;
    private JPanel panelAdmin;
    private JTextArea adminTextArea;
    private JButton creaHackathonButton;
    private JButton mostraHackathonButton;

    public AdminView(Organizzatore adminLogged, JFrame frameCalling) {

        frameAdminView = new JFrame("User View");
        frameAdminView.setContentPane(panelAdmin);
        frameAdminView.pack();

        frameAdminView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameCalling.setVisible(true);
                frameAdminView.dispose();
            }
        });

        frameAdminView.setVisible(true);
        frameAdminView.setSize(800, 800);
        frameAdminView.setResizable(false);
        frameAdminView.setLocationRelativeTo(null);

        adminTextArea.setLineWrap(true);
        adminTextArea.setWrapStyleWord(true);
        adminTextArea.setEditable(false);


        creaHackathonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreaHackathonForm(adminLogged, frameAdminView, frameCalling);
                frameAdminView.setVisible(false);
            }
        });
        mostraHackathonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminTextArea.setText(adminLogged.printListaHackathon());
            }
        });
    }
}
