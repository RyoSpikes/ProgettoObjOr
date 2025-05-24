package gui;

import controller.Controller;
import controller.ControllerOrganizzatore;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ScegliTeam {
    private JPanel panelScegliTeam;
    private JComboBox<Hackathon> hackathonComboBox;
    private JComboBox<Team> teamComboBox;
    private JButton btnInvio;

    public ScegliTeam(Utente userLogged, JFrame frameCalling, ControllerOrganizzatore controllerOrganizzatore, Controller controllerUtente) {
        JFrame frame = new JFrame("Scelta Team");
        frame.setContentPane(panelScegliTeam);
        frame.pack();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frameCalling.setVisible(true);
                frame.dispose();
            }
        });

        frame.setVisible(true);
        frame.setSize(600, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        for(Hackathon h : controllerOrganizzatore.getListaHackathon()) {
            hackathonComboBox.addItem(h);
        }

        hackathonComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teamComboBox.removeAllItems();

                Hackathon h = (Hackathon) hackathonComboBox.getSelectedItem();
                try
                {
                    for(Team t : h.getClassifica())
                    {
                        teamComboBox.addItem(t);
                    }
                }
                catch (NullPointerException ex)
                {
                    JOptionPane.showMessageDialog(null,"Non ci sono Hackathon!");
                }
            }
        });

        hackathonComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Hackathon) {
                    setText(((Hackathon) value).getTitolo());
                }
                return this;
            }
        });

        teamComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Team) {
                    setText(((Team) value).getNomeTeam());
                }
                return this;
            }
        });


        btnInvio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userLogged.entrataTeam((Team) teamComboBox.getSelectedItem());
                frameCalling.setVisible(true);
                frame.dispose();
            }
        });

    }
}
