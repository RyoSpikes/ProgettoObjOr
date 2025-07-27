package gui.views;

import controller.HackathonController;
import model.Team;
import model.Utente;

import javax.swing.*;

/**
 * Wrapper per mantenere la compatibilità con la vecchia TeamView.
 * Reindirizza alla nuova ModernTeamView.
 */
public class TeamView {
    private ModernTeamView modernView;
    
    /**
     * Costruttore che crea la nuova vista moderna.
     */
    public TeamView(Team team, Utente userLogged, JFrame parentFrame, HackathonController hackathonController) {
        // Delega alla nuova vista moderna
        modernView = new ModernTeamView(team, userLogged, parentFrame, hackathonController);
    }
    
    /**
     * Getter per il frame (per compatibilità con codice esistente).
     */
    public JFrame getFrame() {
        return modernView.getFrame();
    }
}
