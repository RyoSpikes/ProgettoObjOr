package gui;

import controller.ControllerOrganizzatore;
import model.Hackathon;
import model.Utente;

import javax.naming.ldap.Control;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreaTeamForm {
    private JPanel panelCreaTeam;
    private JTextField nomeTeamField;
    private JComboBox<Hackathon> hackathonComboBox;
    private JButton creaTeamButton;

    public CreaTeamForm(Utente userLogged, JFrame frameCalling, ControllerOrganizzatore controllerOrganizzatore) {
        JFrame frame = new JFrame("Creazione Team");
        frame.setContentPane(panelCreaTeam);
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

        //TODO
        creaTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    userLogged.creaTeam((Hackathon) hackathonComboBox.getSelectedItem(), nomeTeamField.getText());
                    frameCalling.setVisible(true);
                    frame.dispose();
                }
                catch (IllegalArgumentException ex)
                {
                    JOptionPane.showMessageDialog(null, ex);
                }
                catch (IllegalStateException ex)
                {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });
    }
}
