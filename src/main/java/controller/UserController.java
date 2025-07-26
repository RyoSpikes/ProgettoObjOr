package controller;
import model.*;
import Database.DAO.Impl.UtenteDAOImpl;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * La classe UserController gestisce una lista di utenti e fornisce metodi per la gestione degli utenti,
 * come l'aggiunta di nuovi utenti e la verifica delle credenziali di accesso.
 * Ora integrata con il database PostgreSQL.
 */
public class UserController {
    private ArrayList<Utente> listaUtenti; // Lista degli utenti gestiti dal controller (cache locale).
    private UtenteDAOImpl utenteDAO; // DAO per l'accesso al database.

    /**
     * Costruttore della classe UserController.
     * Inizializza la lista degli utenti e la connessione al database.
     */
    public UserController()
    {
        listaUtenti = new ArrayList<>();
        try {
            utenteDAO = new UtenteDAOImpl();
            // Carica tutti gli utenti dal database all'avvio
            listaUtenti.addAll(utenteDAO.findAll());
            System.out.println("Controller inizializzato con " + listaUtenti.size() + " utenti dal database.");
        } catch (SQLException e) {
            System.err.println("Errore durante l'inizializzazione del Controller: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Errore di connessione al database. L'applicazione funzionerà in modalità offline.\n" + 
                "Errore: " + e.getMessage(), 
                "Avviso Database", 
                JOptionPane.WARNING_MESSAGE);
            utenteDAO = null; // Modalità offline
        }
    }

    /**
     * Aggiunge un nuovo utente alla lista e lo salva nel database.
     *
     * @param username Il nome utente del nuovo utente.
     * @param password La password del nuovo utente.
     * @throws IllegalArgumentException se l'utente non può essere creato (ad esempio, input non valido).
     */
    public void aggiungiUtente(String username, String password) throws IllegalArgumentException {
        try {
            Utente nuovoUtente = new Utente(username, password);
            
            // Salva nel database se disponibile
            if (utenteDAO != null) {
                boolean salvato = utenteDAO.save(nuovoUtente);
                if (salvato) {
                    listaUtenti.add(nuovoUtente);
                    System.out.println("Utente salvato nel database: " + username);
                    JOptionPane.showMessageDialog(null, 
                        "Utente creato e salvato con successo nel database!", 
                        "Successo", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new IllegalArgumentException("Errore durante il salvataggio nel database");
                }
            } else {
                // Modalità offline - salva solo in memoria
                listaUtenti.add(nuovoUtente);
                System.out.println("Utente salvato solo in memoria (modalità offline): " + username);
                JOptionPane.showMessageDialog(null, 
                    "Utente creato in modalità offline (database non disponibile)", 
                    "Avviso", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }
        catch (IllegalArgumentException ex) {
            throw ex;
        }
        catch (SQLException ex) {
            System.err.println("Errore SQL durante il salvataggio: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Errore durante il salvataggio nel database: " + ex.getMessage(), 
                "Errore Database", 
                JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException("Errore database: " + ex.getMessage());
        }
    }

    /**
     * Verifica se le credenziali di accesso fornite corrispondono a un utente nel database.
     *
     * @param user Il nome utente da verificare.
     * @param password La password da verificare.
     * @return true se le credenziali sono valide, false altrimenti.
     */
    public boolean checkLogin(String user, String password)
    {
        try {
            // Prima prova a verificare nel database se disponibile
            if (utenteDAO != null) {
                Utente utenteTrovato = utenteDAO.login(user, password);
                if (utenteTrovato != null) {
                    System.out.println("Login riuscito dal database per: " + user);
                    return true;
                }
            } else {
                // Modalità offline - verifica nella cache locale
                for(Utente u : listaUtenti) {
                    try {
                        if(u.getName().equals(user) && u.getLogin(password)) {
                            System.out.println("Login riuscito dalla cache locale per: " + user);
                            return true;
                        }
                    }
                    catch(IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), e);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il login dal database: " + e.getMessage());
            // Fallback alla verifica locale
            for(Utente u : listaUtenti) {
                try {
                    if(u.getName().equals(user) && u.getLogin(password)) {
                        System.out.println("Login riuscito dalla cache locale (fallback) per: " + user);
                        return true;
                    }
                }
                catch(IllegalArgumentException e2) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), e2);
                }
            }
        }
        
        System.out.println("Login fallito per: " + user);
        return false;
    }

    /**
     * Recupera la lista degli utenti gestiti dal controller.
     * Sincronizza con il database se disponibile.
     *
     * @return Un ArrayList di oggetti Utente.
     */
    public ArrayList<Utente> getListaUtenti()
    {
        try {
            // Aggiorna la cache locale dal database se disponibile
            if (utenteDAO != null) {
                listaUtenti.clear();
                listaUtenti.addAll(utenteDAO.findAll());
                System.out.println("Lista utenti sincronizzata dal database: " + listaUtenti.size() + " utenti");
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la sincronizzazione degli utenti: " + e.getMessage());
            // Continua con la cache locale
        }
        
        return listaUtenti;
    }
    
    /**
     * Effettua il login e restituisce l'utente se le credenziali sono corrette.
     *
     * @param username Il nome utente
     * @param password La password
     * @return L'utente se il login è riuscito, null altrimenti
     */
    public Utente loginUtente(String username, String password) {
        try {
            if (utenteDAO != null) {
                return utenteDAO.login(username, password);
            } else {
                // Modalità offline
                for (Utente u : listaUtenti) {
                    if (u.getName().equals(username) && u.getLogin(password)) {
                        return u;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il login: " + e.getMessage());
            // Fallback alla verifica locale
            for (Utente u : listaUtenti) {
                try {
                    if (u.getName().equals(username) && u.getLogin(password)) {
                        return u;
                    }
                } catch (IllegalArgumentException ex) {
                    // Continua con il prossimo utente
                }
            }
        }
        return null;
    }
}