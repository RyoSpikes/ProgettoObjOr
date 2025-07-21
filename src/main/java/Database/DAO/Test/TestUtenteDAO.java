package Database.DAO.Test;

import Database.DAO.Impl.UtenteDAOImpl;
import Database.DAO.UtenteDAO;
import model.Utente;

import java.sql.SQLException;
import java.util.List;

/**
 * Classe di test per verificare il funzionamento della UtenteDAO.
 * Questa classe è utilizzata per testare le operazioni CRUD sulla tabella UTENTE.
 */
public class TestUtenteDAO {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== TEST UTENTE DAO ===");
            
            // Inizializza la DAO
            UtenteDAO utenteDAO = new UtenteDAOImpl();
            
            // Test 1: Salvataggio di un nuovo utente
            System.out.println("\n1. Test salvataggio utente...");
            Utente nuovoUtente = new Utente("testUser123", "password123!");
            boolean salvato = utenteDAO.save(nuovoUtente);
            System.out.println("Utente salvato: " + salvato);
            
            // Test 2: Ricerca utente per username
            System.out.println("\n2. Test ricerca utente per username...");
            Utente utenteRicercato = utenteDAO.findByUsername("testUser123");
            if (utenteRicercato != null) {
                System.out.println("Utente trovato: " + utenteRicercato.getName());
            } else {
                System.out.println("Utente non trovato");
            }
            
            // Test 3: Login
            System.out.println("\n3. Test login...");
            Utente utenteLogin = utenteDAO.login("testUser123", "password123!");
            if (utenteLogin != null) {
                System.out.println("Login riuscito per: " + utenteLogin.getName());
            } else {
                System.out.println("Login fallito");
            }
            
            // Test 4: Login con password sbagliata
            System.out.println("\n4. Test login con password sbagliata...");
            Utente loginFallito = utenteDAO.login("testUser123", "passwordSbagliata");
            if (loginFallito != null) {
                System.out.println("Login riuscito (ERRORE!)");
            } else {
                System.out.println("Login fallito correttamente");
            }
            
            // Test 5: Aggiornamento password
            System.out.println("\n5. Test aggiornamento password...");
            if (utenteRicercato != null) {
                // Creiamo un nuovo oggetto Utente con la password aggiornata
                Utente utenteAggiornato = new Utente("testUser123", "nuovaPassword456!");
                boolean aggiornato = utenteDAO.update(utenteAggiornato);
                System.out.println("Password aggiornata: " + aggiornato);
                
                // Verifichiamo che il login funzioni con la nuova password
                Utente loginNuovaPassword = utenteDAO.login("testUser123", "nuovaPassword456!");
                if (loginNuovaPassword != null) {
                    System.out.println("Login con nuova password riuscito");
                } else {
                    System.out.println("Login con nuova password fallito");
                }
            }
            
            // Test 6: Recupero di tutti gli utenti
            System.out.println("\n6. Test recupero tutti gli utenti...");
            List<Utente> tuttiUtenti = utenteDAO.findAll();
            System.out.println("Numero totale di utenti nel database: " + tuttiUtenti.size());
            for (Utente u : tuttiUtenti) {
                System.out.println("- " + u.getName());
            }
            
            // Test 7: Eliminazione utente (opzionale - decommentare se si vuole testare)
            /*
            System.out.println("\n7. Test eliminazione utente...");
            boolean eliminato = utenteDAO.delete("testUser123");
            System.out.println("Utente eliminato: " + eliminato);
            
            // Verifichiamo che l'utente sia stato effettivamente eliminato
            Utente utenteEliminato = utenteDAO.findByUsername("testUser123");
            if (utenteEliminato == null) {
                System.out.println("Utente effettivamente eliminato dal database");
            } else {
                System.out.println("ERRORE: Utente non eliminato");
            }
            */
            
            System.out.println("\n=== FINE TEST ===");
            
        } catch (SQLException e) {
            System.err.println("Errore SQL durante i test: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Errore generale durante i test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
