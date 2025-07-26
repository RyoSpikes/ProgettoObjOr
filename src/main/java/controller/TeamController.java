package controller;

import Database.DAO.Impl.TeamDAOImpl;
import Database.DAO.Impl.MembershipDAOImpl;
import model.*;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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
    
    /**
     * Ottiene tutti i team per un hackathon specifico.
     * 
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Lista di team per l'hackathon o null in caso di errore
     */
    public List<Team> getTeamsByHackathon(String titoloHackathon) {
        if (teamDAO == null) {
            return null;
        }
        
        try {
            return teamDAO.findByHackathon(titoloHackathon);
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei team per l'hackathon '" + titoloHackathon + "': " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Ottiene il numero di membri di un team.
     * 
     * @param nomeTeam Nome del team
     * @param titoloHackathon Titolo dell'hackathon
     * @return Numero di membri del team
     */
    public int getNumeroMembriTeam(String nomeTeam, String titoloHackathon) {
        if (membershipDAO == null) {
            return 0;
        }
        
        try {
            return membershipDAO.countTeamMembers(nomeTeam, titoloHackathon);
        } catch (SQLException e) {
            System.err.println("Errore durante il conteggio dei membri del team '" + nomeTeam + "': " + e.getMessage());
            return 0;
        }
    }
    
    // NOTA: I metodi di semplice query sono stati rimossi dal controller.
    /**
     * Conta il numero di membri di un team.
     * Versione sicura che non lancia SQLException.
     *
     * @param nomeTeam Il nome del team
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Il numero di membri del team, 0 in caso di errore
     */
    public int contaMembriTeam(String nomeTeam, String titoloHackathon) {
        try {
            return teamDAO.countMembers(nomeTeam, titoloHackathon);
        } catch (SQLException e) {
            System.err.println("Errore nel conteggio membri team: " + e.getMessage());
            return 0; // Ritorna 0 in caso di errore
        }
    }

    /**
     * Verifica se un utente può unirsi a un team specifico.
     * Versione sicura che non lancia SQLException.
     *
     * @param team Il team da verificare
     * @param utente L'utente che vuole unirsi
     * @return true se l'utente può unirsi al team, false altrimenti
     */
    public boolean puoUtenterUnirsiAlTeam(Team team, Utente utente) {
        try {
            // Verifica se il periodo di registrazione è ancora aperto
            LocalDate dataFineRegistrazione = team.getHackathon().getDataFineRegistrazioni().toLocalDate();
            if (LocalDate.now().isAfter(dataFineRegistrazione)) {
                return false; // Registrazioni chiuse
            }

            // Verifica se l'utente è già in un team per questo hackathon
            String teamCorrente = membershipDAO.getUserTeamForHackathon(
                utente.getName(), team.getHackathon().getTitoloIdentificativo());

            if (teamCorrente != null) {
                return false; // Già in un team
            }

            // Verifica se il team è al completo
            int membri = teamDAO.countMembers(
                team.getNomeTeam(),
                team.getHackathon().getTitoloIdentificativo()
            );

            int maxMembri = team.getHackathon().getMaxMembriTeam();
            if (membri >= maxMembri) {
                return false; // Team pieno
            }

            return true; // Può unirsi

        } catch (SQLException e) {
            System.err.println("Errore nella verifica unione team: " + e.getMessage());
            return false; // In caso di errore, non permettere l'unione per sicurezza
        }
    }

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

    /**
     * Restituisce il nome del team corrente dell'utente per un hackathon specifico
     * (metodo sicuro per la GUI)
     */
    public String getTeamCorrenteUtente(String nomeUtente, String titoloHackathon) {
        try {
            return membershipDAO.getUserTeamForHackathon(nomeUtente, titoloHackathon);
        } catch (SQLException e) {
            return null; // Ritorna null se non ha team o in caso di errore
        }
    }

    /**
     * Metodo per ottenere tutti i team in modo sicuro per la GUI
     * (restituisce lista vuota in caso di errore invece di lanciare eccezione)
     */
    public List<Team> getAllTeamsSafe() {
        try {
            return teamDAO.findAll();
        } catch (SQLException e) {
            return new java.util.ArrayList<>(); // Restituisce lista vuota in caso di errore
        }
    }
}
