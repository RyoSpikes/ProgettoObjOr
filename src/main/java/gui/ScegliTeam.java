package gui;

import controller.Controller;
import controller.ControllerOrganizzatore;
import controller.TeamController;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

/**
 * La classe ScegliTeam rappresenta un'interfaccia grafica per consentire a un utente di selezionare un team
 * associato a un hackathon. L'utente può scegliere un hackathon e successivamente un team da una lista.
 */
public class ScegliTeam {
    private JPanel panelScegliTeam; // Pannello principale della finestra.
    private JComboBox<Hackathon> hackathonComboBox; // ComboBox per selezionare un hackathon.
    private JComboBox<Team> teamComboBox; // ComboBox per selezionare un team.
    private JButton btnInvio; // Pulsante per confermare la selezione del team.

    /**
     * Costruttore della classe ScegliTeam.
     *
     * @param userLogged L'utente attualmente loggato.
     * @param frameCalling Il frame chiamante che ha aperto questa finestra.
     * @param controllerOrganizzatore Il controller per la gestione degli organizzatori e degli hackathon.
     * @param controllerUtente Il controller per la gestione degli utenti.
     */
    public ScegliTeam(Utente userLogged, JFrame frameCalling, ControllerOrganizzatore controllerOrganizzatore, Controller controllerUtente) {
        // Inizializza il TeamController per gestire le operazioni sui team
        TeamController teamController = new TeamController();
        
        JFrame frame = new JFrame("Scelta Team");
        frame.setContentPane(panelScegliTeam);
        frame.pack();

        // Listener per gestire la chiusura della finestra.
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

        // Popola il ComboBox con la lista degli hackathon disponibili.
        for(Hackathon h : controllerOrganizzatore.getListaHackathon()) {
            hackathonComboBox.addItem(h);
        }

        // Listener per aggiornare i team disponibili in base all'hackathon selezionato.
        hackathonComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teamComboBox.removeAllItems();

                Hackathon hackathonSelezionato = (Hackathon) hackathonComboBox.getSelectedItem();
                if (hackathonSelezionato != null) {
                    try {
                        // Usa direttamente TeamDAO per operazioni semplici di query
                        for (Team team : teamController.getTeamDAO().findByHackathon(hackathonSelezionato.getTitoloIdentificativo())) {
                            teamComboBox.addItem(team);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(frame, 
                            "Errore nel caricamento dei team:\n" + ex.getMessage(), 
                            "Errore database", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, 
                            "Errore imprevisto nel caricamento dei team:\n" + ex.getMessage(), 
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Imposta il renderer per visualizzare i titoli degli hackathon nel ComboBox.
        hackathonComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Hackathon) {
                    setText(((Hackathon) value).getTitoloIdentificativo());
                }
                return this;
            }
        });

        // Imposta il renderer per visualizzare i nomi dei team nel ComboBox.
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

        // Listener per il pulsante "Invio".
        btnInvio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Team teamSelezionato = (Team) teamComboBox.getSelectedItem();
                    if (teamSelezionato == null) {
                        JOptionPane.showMessageDialog(frame, "Seleziona un team!", 
                            "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Aggiunge l'utente al team utilizzando il TeamController
                    teamController.aggiungiUtenteATeam(userLogged, teamSelezionato);
                    
                    // Notifica successo
                    JOptionPane.showMessageDialog(frame, 
                        "Sei stato aggiunto con successo al team '" + teamSelezionato.getNomeTeam() + "'!", 
                        "Adesione completata", JOptionPane.INFORMATION_MESSAGE);
                    
                    frameCalling.setVisible(true);
                    frame.dispose();
                }
                catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, 
                        "Errore nell'adesione al team:\n" + ex.getMessage(), 
                        "Errore", JOptionPane.ERROR_MESSAGE);
                }
                catch (IllegalStateException ex) {
                    JOptionPane.showMessageDialog(frame, 
                        "Operazione non consentita:\n" + ex.getMessage(), 
                        "Avviso", JOptionPane.WARNING_MESSAGE);
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, 
                        "Errore imprevisto:\n" + ex.getMessage(), 
                        "Errore", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

    }
}