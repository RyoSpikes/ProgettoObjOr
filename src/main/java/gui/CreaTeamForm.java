package gui;

import controller.HackathonController;
import model.Hackathon;
import model.Team;
import model.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * La classe CreaTeamForm rappresenta un'interfaccia grafica per la creazione di un nuovo team.
 * Consente all'utente loggato di selezionare un hackathon e creare un team associato.
 */
public class CreaTeamForm {
    private JPanel panelCreaTeam; // Pannello principale della finestra.
    private JTextField nomeTeamField; // Campo di testo per il nome del team.
    private JComboBox<Hackathon> hackathonComboBox; // ComboBox per selezionare un hackathon.
    private JButton creaTeamButton; // Pulsante per creare il team.

    /**
     * Costruttore della classe CreaTeamForm.
     * Inizializza l'interfaccia grafica e gestisce le azioni degli elementi.
     *
     * @param userLogged L'utente attualmente loggato.
     * @param frameCalling Il frame chiamante che ha aperto questa finestra.
     * @param hackathonController Il controller utilizzato per gestire gli hackathon.
     */
    public CreaTeamForm(Utente userLogged, JFrame frameCalling, HackathonController hackathonController) {
        
        JFrame frame = new JFrame("Creazione Team");
        frame.setContentPane(panelCreaTeam);
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
        for(Hackathon h : hackathonController.getListaHackathon()) {
            hackathonComboBox.addItem(h);
        }

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

        // Listener per il pulsante "Crea Team".
        creaTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Validazione input
                    String nomeTeam = nomeTeamField.getText();
                    if (nomeTeam == null || nomeTeam.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Il nome del team è obbligatorio!", 
                            "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    Hackathon hackathonSelezionato = (Hackathon) hackathonComboBox.getSelectedItem();
                    if (hackathonSelezionato == null) {
                        JOptionPane.showMessageDialog(frame, "Seleziona un hackathon!", 
                            "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Crea il team utilizzando il TeamController
                    Team nuovoTeam = hackathonController.creaTeam(userLogged, hackathonSelezionato, nomeTeam.trim());
                    
                    // Notifica successo
                    JOptionPane.showMessageDialog(frame, 
                        "Team '" + nuovoTeam.getNomeTeam() + "' creato con successo!\n" +
                        "Sei stato automaticamente aggiunto come primo membro.", 
                        "Team creato", JOptionPane.INFORMATION_MESSAGE);
                    
                    frameCalling.setVisible(true);
                    frame.dispose();
                }
                catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, 
                        "Errore nella creazione del team:\n" + ex.getMessage(), 
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