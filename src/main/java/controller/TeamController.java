package controller;

import Database.DAO.Impl.TeamDAOImpl;
import Database.DAO.Impl.MembershipDAOImpl;
import model.*;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Controller per la gestione dei team e delle membership.
 * Coordina le operazioni tra i modelli e le DAO.
 */
public class TeamController {
    
    private TeamDAOImpl teamDAO;
    private MembershipDAOImpl membershipDAO;
    
    /**
     * Costruttore che inizializza le DAO necessarie.
     */
    public TeamController() {
        try {
            this.teamDAO = new TeamDAOImpl();
            this.membershipDAO = new MembershipDAOImpl();
            System.out.println("TeamController inizializzato correttamente.");
        } catch (SQLException e) {
            System.err.println("Errore durante l'inizializzazione del TeamController: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Errore di connessione al database per la gestione dei team.\n" + 
                "Errore: " + e.getMessage(), 
                "Avviso Database", 
                JOptionPane.WARNING_MESSAGE);
            this.teamDAO = null;
            this.membershipDAO = null;
        }
    }
    
    /**
     * Crea un nuovo team per un hackathon e aggiunge automaticamente l'utente come primo membro.
     * 
     * @param utente L'utente che crea il team
     * @param hackathon L'hackathon per cui creare il team
     * @param nomeTeam Il nome del team da creare
     * @return Il team appena creato
     * @throws IllegalArgumentException Se la creazione non è possibile
     * @throws IllegalStateException Se l'utente è già in un team per questo hackathon
     */
    public Team creaTeam(Utente utente, Hackathon hackathon, String nomeTeam) 
            throws IllegalArgumentException, IllegalStateException {
        
        // Validazione parametri
        if (utente == null || hackathon == null || nomeTeam == null || nomeTeam.trim().isEmpty()) {
            throw new IllegalArgumentException("Tutti i parametri sono obbligatori");
        }
        
        if (teamDAO == null || membershipDAO == null) {
            throw new IllegalStateException("Servizio non disponibile - errore di connessione al database");
        }
        
        try {
            // Verifica se l'utente è già in un team per questo hackathon
            if (membershipDAO.isUserInTeamForHackathon(utente.getName(), hackathon.getTitoloIdentificativo())) {
                throw new IllegalStateException("L'utente è già membro di un team per questo hackathon");
            }
            
            // 1. Crea il team
            Team nuovoTeam = new Team(hackathon, nomeTeam);
            boolean teamCreato = teamDAO.save(nuovoTeam);
            
            if (!teamCreato) {
                throw new IllegalArgumentException("Impossibile creare il team nel database");
            }
            
            // 2. Aggiunge l'utente al team
            boolean membershipCreata = membershipDAO.addUserToTeam(
                utente.getName(), 
                nomeTeam, 
                hackathon.getTitoloIdentificativo(), 
                LocalDate.now()
            );
            
            if (!membershipCreata) {
                throw new IllegalArgumentException("Impossibile aggiungere l'utente al team");
            }
            
            System.out.println("Team '" + nomeTeam + "' creato con successo per l'hackathon '" + 
                             hackathon.getTitoloIdentificativo() + "' con l'utente '" + utente.getName() + "'");
            
            return nuovoTeam;
            
        } catch (SQLException e) {
            // I trigger del database gestiscono automaticamente molti controlli
            String message = e.getMessage().toLowerCase();
            if (message.contains("duplicate") || message.contains("unique")) {
                throw new IllegalArgumentException("Nome team già esistente per questo hackathon");
            } else if (message.contains("registrazione") || message.contains("chiuse")) {
                throw new IllegalStateException("Le registrazioni per questo hackathon sono chiuse");
            } else if (message.contains("numero massimo")) {
                throw new IllegalStateException("Hackathon al completo - numero massimo di partecipanti raggiunto");
            }
            throw new IllegalArgumentException("Errore durante la creazione del team: " + e.getMessage());
        }
    }
    
    /**
     * Aggiunge un utente a un team esistente.
     * 
     * @param utente L'utente da aggiungere al team
     * @param team Il team a cui aggiungere l'utente
     * @throws IllegalArgumentException Se l'aggiunta non è possibile
     * @throws IllegalStateException Se l'utente è già in un team o il team è pieno
     */
    public void aggiungiUtenteATeam(Utente utente, Team team) 
            throws IllegalArgumentException, IllegalStateException {
        
        // Validazione parametri
        if (utente == null || team == null) {
            throw new IllegalArgumentException("Utente e team sono obbligatori");
        }
        
        if (membershipDAO == null) {
            throw new IllegalStateException("Servizio non disponibile - errore di connessione al database");
        }
        
        try {
            // Verifica se l'utente è già in un team per questo hackathon
            if (membershipDAO.isUserInTeamForHackathon(utente.getName(), team.getHackathon().getTitoloIdentificativo())) {
                throw new IllegalStateException("L'utente è già membro di un team per questo hackathon");
            }
            
            // Aggiunge l'utente al team
            boolean membershipCreata = membershipDAO.addUserToTeam(
                utente.getName(), 
                team.getNomeTeam(), 
                team.getHackathon().getTitoloIdentificativo(), 
                LocalDate.now()
            );
            
            if (!membershipCreata) {
                throw new IllegalArgumentException("Impossibile aggiungere l'utente al team");
            }
            
            System.out.println("Utente '" + utente.getName() + "' aggiunto al team '" + team.getNomeTeam() + "'");
            
        } catch (SQLException e) {
            // I trigger del database gestiscono molti controlli
            String message = e.getMessage().toLowerCase();
            if (message.contains("numero massimo") || message.contains("team.*completo")) {
                throw new IllegalStateException("Il team ha raggiunto il numero massimo di membri");
            } else if (message.contains("registrazione") || message.contains("chiuse")) {
                throw new IllegalStateException("Le registrazioni per questo hackathon sono chiuse");
            }
            throw new IllegalArgumentException("Errore durante l'aggiunta dell'utente al team: " + e.getMessage());
        }
    }
    
    // NOTA: I metodi di semplice query sono stati rimossi dal controller.
    // Per operazioni semplici, utilizzare direttamente le DAO:
    // - teamDAO.findByHackathon() per ottenere team di un hackathon
    // - membershipDAO.getTeamForUserAndHackathon() per team di un utente
    // - membershipDAO.getTeamMembers() per membri di un team
    // - membershipDAO.countTeamMembers() per contare membri
    // - membershipDAO.isUserInTeamForHackathon() per verifiche membership
    
    /**
     * Ottiene le DAO per uso diretto nelle GUI o altri controller.
     * Espone le DAO per operazioni semplici che non richiedono coordinamento.
     */
    public TeamDAOImpl getTeamDAO() {
        return teamDAO;
    }
    
    public MembershipDAOImpl getMembershipDAO() {
        return membershipDAO;
    }
}
