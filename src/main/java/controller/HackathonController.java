package controller;

import model.*;

import javax.swing.*;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import Database.DAO.Impl.OrganizzatoreDAOImpl;
import Database.DAO.Impl.HackathonDAOImpl;
import Database.DAO.Impl.UtenteDAOImpl;
import Database.DAO.Impl.InvitoGiudiceDAOImpl;
import Database.DAO.Impl.DocumentoDAOImpl;
import Database.DAO.Impl.MembershipDAOImpl;
import Database.DAO.Impl.VotoDAOImpl;
import Database.DAO.Impl.ValutazioneDAOImpl;

/**
 * La classe HackathonController è il controller principale dell'applicazione.
 * Gestisce tutte le operazioni relative a hackathon, organizzatori, utenti, team,
 * inviti, voti e valutazioni. Fornisce un'interfaccia unificata per l'accesso
 * ai dati e coordina le operazioni tra le diverse entità del sistema.
 */
public class HackathonController extends UserController {

    /**
     * Lista degli organizzatori gestiti dal controller.
     */
    ArrayList<Organizzatore> listaOrganizzatori;

    /**
     * DAO per l'accesso agli organizzatori nel database.
     * Può essere utilizzato per operazioni di login e gestione degli organizzatori.
     */
    private OrganizzatoreDAOImpl organizzatoreDAO;

    /**
     * DAO per l'accesso agli hackathon nel database.
     * Utilizzato per operazioni CRUD sugli hackathon.
     */
    private HackathonDAOImpl hackathonDAO;

    /**
     * DAO per l'accesso agli utenti nel database.
     */
    private UtenteDAOImpl utenteDAO;

    /**
     * DAO per l'accesso agli inviti giudice nel database.
     */
    private InvitoGiudiceDAOImpl invitoGiudiceDAO;

    /**
     * DAO per l'accesso ai documenti nel database.
     */
    private DocumentoDAOImpl documentoDAO;

    /**
     * DAO per l'accesso alle membership nel database.
     */
    private MembershipDAOImpl membershipDAO;

    /**
     * DAO per l'accesso ai voti nel database.
     */
    private VotoDAOImpl votoDAO;

    /**
     * DAO per l'accesso alle valutazioni nel database.
     */
    private ValutazioneDAOImpl valutazioneDAO;

    /**
     * Costruttore della classe HackathonController.
     * Inizializza la lista degli organizzatori e le DAO.
     */
    public HackathonController() {
        listaOrganizzatori = new ArrayList<>();
        try {
            organizzatoreDAO = new OrganizzatoreDAOImpl();
            hackathonDAO = new HackathonDAOImpl();
            utenteDAO = new UtenteDAOImpl();
            invitoGiudiceDAO = new InvitoGiudiceDAOImpl();
            documentoDAO = new DocumentoDAOImpl();
            membershipDAO = new MembershipDAOImpl();
            votoDAO = new VotoDAOImpl();
            valutazioneDAO = new ValutazioneDAOImpl();
            
            // Carica tutti gli organizzatori dal database all'avvio
            listaOrganizzatori.addAll(organizzatoreDAO.findAll());
            System.out.println("HackathonController inizializzato con " + listaOrganizzatori.size() + " organizzatori dal database.");
        } catch (SQLException e) {
            System.err.println("Errore durante l'inizializzazione del HackathonController: " + e.getMessage());
            organizzatoreDAO = null; // Modalità offline
            try {
                hackathonDAO = new HackathonDAOImpl();
            } catch (SQLException ex) {
                System.err.println("Errore critico nell'inizializzazione HackathonDAO: " + ex.getMessage());
                hackathonDAO = null;
            }
        }
    }

    /**
     * Recupera la lista degli organizzatori gestiti dal controller.
     *
     * @return Un ArrayList di oggetti Organizzatore.
     */
    public ArrayList<Organizzatore> getListaOrganizzatori() {
        return listaOrganizzatori;
    }

    /**
     * Recupera la lista di tutti gli hackathon organizzati dagli organizzatori.
     *
     * @return Un ArrayList di oggetti Hackathon.
     */
    public ArrayList<Hackathon> getListaHackathon() {
        ArrayList<Hackathon> hList = new ArrayList<>();

        // Prova prima a recuperare gli hackathon dal database
        if (hackathonDAO != null) {
            try {
                hList.addAll(hackathonDAO.getAllHackathon());
                return hList;
            } catch (SQLException e) {
                System.err.println("Errore durante il recupero degli hackathon dal database: " + e.getMessage());
                // In caso di errore, fallback al metodo originale
            }
        }

        // Fallback: recupera gli hackathon dagli oggetti organizzatore in memoria
        for (Organizzatore o : listaOrganizzatori) {
            hList.addAll(o.getHackathonOrganizzate());
        }
        return hList;
    }

    /**
     * Aggiunge un nuovo organizzatore alla lista.
     *
     * @param username Il nome utente del nuovo organizzatore.
     * @param password La password del nuovo organizzatore.
     * @throws IllegalArgumentException se l'organizzatore non può essere creato (ad esempio, input non valido).
     */
    @Override
    public void aggiungiUtente(String username, String password) throws IllegalArgumentException {
        try {
            listaOrganizzatori.add(new Organizzatore(username, password));
        } catch (IllegalArgumentException ex) {
            throw ex;
        }
    }

    /**
     * Effettua il login di un organizzatore.
     *
     * @param username Il nome utente dell'organizzatore.
     * @param password La password dell'organizzatore.
     * @return L'oggetto Organizzatore se il login è riuscito, null altrimenti.
     */
    public Organizzatore loginOrganizzatore(String username, String password) {
        try {
            if (organizzatoreDAO != null) {
                return organizzatoreDAO.login(username, password);
            } else {
                // Modalità offline
                for (Organizzatore o : listaOrganizzatori) {
                    if (o.getName().equals(username) && o.getLogin(password)) {
                        return o;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il login: " + e.getMessage());
            // Fallback alla verifica locale
            for (Organizzatore o : listaOrganizzatori) {
                try {
                    if (o.getName().equals(username) && o.getLogin(password)) {
                        return o;
                    }
                } catch (IllegalArgumentException ex) {
                    // Continua con il prossimo utente
                }
            }
        }
        return null;
    }

    //TODO: Eliminare idNum
    /**
     * Aggiunge un nuovo hackathon per un organizzatore specifico (metodo legacy).
     *
     * @param org                     L'organizzatore che registra l'hackathon.
     * @param idNum                   L'ID dell'hackathon.
     * @param sede                    La sede dell'hackathon.
     * @param dataInizio              La data di inizio dell'hackathon.
     * @param dataFine                La data di fine dell'hackathon.
     * @param dataInizioRegistrazioni La data di inizio delle registrazioni.
     * @param titolo                  Il titolo dell'hackathon.
     * @param maxMembriTeam           Il numero massimo di membri per team.
     * @param maxNumIscritti          Il numero massimo di iscritti.
     */
    public void aggiungiHackathon(Organizzatore org, String idNum, String sede, LocalDateTime dataInizio, LocalDateTime dataFine,
                                  LocalDateTime dataInizioRegistrazioni, String titolo,
                                  int maxMembriTeam, int maxNumIscritti) {
        try {
            org.registrazioneHackathon(idNum, sede, dataInizio, dataFine, dataInizioRegistrazioni, titolo, maxMembriTeam, maxNumIscritti);
        } catch (DateTimeException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    /**
     * Risultato dell'operazione di creazione hackathon.
     */
    public enum CreazioneHackathonRisultato {
        SUCCESSO,
        ERRORE_TITOLO_DUPLICATO,
        ERRORE_ORGANIZZATORE_NON_VALIDO,
        ERRORE_DATE_NON_VALIDE,
        ERRORE_DAO_NON_DISPONIBILE,
        ERRORE_GENERICO
    }

    /**
     * Crea un nuovo hackathon con i dati forniti.
     * Versione sicura che non lancia SQLException verso la GUI.
     *
     * @param titoloIdentificativo Il titolo identificativo dell'hackathon
     * @param organizzatore L'organizzatore dell'hackathon
     * @param sede La sede dell'hackathon
     * @param dataInizioRegistrazione Data di inizio registrazioni
     * @param dataFineRegistrazione Data di fine registrazioni
     * @param dataInizioEvento Data di inizio evento
     * @param dataFineEvento Data di fine evento
     * @param descrizioneProblema Descrizione del problema da risolvere
     * @param maxNumIscritti Numero massimo di iscritti
     * @param maxNumMembriTeam Numero massimo di membri per team
     * @return Risultato dell'operazione
     */
    public CreazioneHackathonRisultato creaHackathonSicuro(String titoloIdentificativo, Organizzatore organizzatore, String sede,
                                LocalDateTime dataInizioRegistrazione, LocalDateTime dataFineRegistrazione,
                                LocalDateTime dataInizioEvento, LocalDateTime dataFineEvento,
                                String descrizioneProblema, int maxNumIscritti, int maxNumMembriTeam) {

        if (hackathonDAO == null) {
            return CreazioneHackathonRisultato.ERRORE_DAO_NON_DISPONIBILE;
        }

        try {
            // Creo l'oggetto Hackathon basado sullo schema del database
            Hackathon hackathon = new Hackathon(
                titoloIdentificativo,           
                organizzatore.getName(),        
                sede,                          
                dataInizioEvento,              
                dataFineEvento,                
                dataInizioRegistrazione,       
                maxNumMembriTeam,              
                maxNumIscritti,               
                descrizioneProblema            
            );

            boolean risultato = hackathonDAO.creaHackathon(hackathon);
            
            if (risultato) {
                System.out.println("✅ Hackathon '" + titoloIdentificativo + "' salvato.");
                System.out.println("📅 Data fine registrazioni calcolata automaticamente: " +
                                 hackathon.getDataFineRegistrazioni());
            }
            
            return risultato ? CreazioneHackathonRisultato.SUCCESSO : CreazioneHackathonRisultato.ERRORE_GENERICO;

        } catch (DateTimeException e) {
            return CreazioneHackathonRisultato.ERRORE_DATE_NON_VALIDE;
        } catch (SQLException e) {
            // Gestisce gli errori specifici del database
            String sqlState = e.getSQLState();
            if ("23505".equals(sqlState)) {
                return CreazioneHackathonRisultato.ERRORE_TITOLO_DUPLICATO;
            } else if ("23503".equals(sqlState)) {
                return CreazioneHackathonRisultato.ERRORE_ORGANIZZATORE_NON_VALIDO;
            } else {
                return CreazioneHackathonRisultato.ERRORE_GENERICO;
            }
        } catch (Exception e) {
            System.err.println("Errore durante la creazione dell'hackathon: " + e.getMessage());
            return CreazioneHackathonRisultato.ERRORE_GENERICO;
        }
    }

    //TODO: Correggere la logica sulla descrizioneProblema, per traccia la descrizione è decisa dai giudici
    /**
     * Crea un nuovo hackathon e lo salva nel database PostgreSQL.
     *
     * @param titoloIdentificativo Titolo identificativo dell'hackathon (chiave primaria)
     * @param organizzatore L'organizzatore che crea l'hackathon
     * @param sede Sede dell'evento
     * @param dataInizioRegistrazione Data inizio registrazioni
     * @param dataFineRegistrazione Data fine registrazioni (calcolata automaticamente: 2 giorni prima dell'evento)
     * @param dataInizioEvento Data inizio evento
     * @param dataFineEvento Data fine evento
     * @param descrizioneProblema Descrizione del problema da risolvere
     * @param maxNumIscritti Numero massimo di iscritti
     * @param maxNumMembriTeam Numero massimo di membri per team
     * @return true se la creazione è riuscita
     * @throws SQLException in caso di errore nel database o validazione
     */
    public boolean creaHackathon(String titoloIdentificativo, Organizzatore organizzatore, String sede,
                                LocalDateTime dataInizioRegistrazione, LocalDateTime dataFineRegistrazione,
                                LocalDateTime dataInizioEvento, LocalDateTime dataFineEvento,
                                String descrizioneProblema, int maxNumIscritti, int maxNumMembriTeam) throws SQLException {

        if (hackathonDAO == null) {
            throw new SQLException("DAO non disponibile. Impossibile creare l'hackathon.");
        }

        try {
            // Creo l'oggetto Hackathon basato sullo schema del database
            // NOTA: La dataFineRegistrazione viene calcolata automaticamente nel costruttore
            // della classe Hackathon come dataInizioEvento.minusDays(2)
            Hackathon hackathon = new Hackathon(
                titoloIdentificativo,           // Titolo_identificativo (PRIMARY KEY)
                organizzatore.getName(),        // Organizzatore (FOREIGN KEY → ORGANIZZATORE.Username_org)
                sede,                          // Sede
                dataInizioEvento,              // DataInizio_evento
                dataFineEvento,                // DataFine_evento
                dataInizioRegistrazione,       // DataInizio_registrazione
                maxNumMembriTeam,              // MaxNum_membriTeam
                maxNumIscritti,                // MaxNum_iscritti
                descrizioneProblema            // Descrizione_problema
            );

            // Salva nel database PostgreSQL tramite DAO
            boolean risultato = hackathonDAO.creaHackathon(hackathon);

            if (risultato) {
                System.out.println("✅ Hackathon '" + titoloIdentificativo + "' salvato.");
                System.out.println("📅 Data fine registrazioni calcolata automaticamente: " +
                                 hackathon.getDataFineRegistrazioni());
            }

            return risultato;

        } catch (DateTimeException e) {
            throw new SQLException("Date non valide: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Errore durante la creazione dell'hackathon: " + e.getMessage());
            throw new SQLException("Impossibile creare l'hackathon: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera tutti gli hackathon organizzati da un organizzatore specifico dal database.
     *
     * @param organizzatore L'organizzatore di cui recuperare gli hackathon
     * @return Lista degli hackathon dell'organizzatore
     */
    public List<Hackathon> getHackathonDiOrganizzatore(Organizzatore organizzatore) {
        try {
            if (hackathonDAO == null) {
                System.err.println("DAO non disponibile.");
                return new ArrayList<>();
            }
            return hackathonDAO.getHackathonByOrganizzatore(organizzatore.getName());
        } catch (SQLException e) {
            System.err.println("Errore nel recupero degli hackathon: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Recupera tutti gli hackathon disponibili dal database.
     *
     * @return Lista di tutti gli hackathon
     */
    public List<Hackathon> getTuttiGliHackathon() {
        try {
            if (hackathonDAO == null) {
                System.err.println("DAO non disponibile.");
                return new ArrayList<>();
            }
            return hackathonDAO.getAllHackathon();
        } catch (SQLException e) {
            System.err.println("Errore nel recupero di tutti gli hackathon: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Recupera gli hackathon con registrazioni attualmente aperte.
     *
     * @return Lista degli hackathon con registrazioni aperte
     */
    public List<Hackathon> getHackathonConRegistrazioniAperte() {
        try {
            if (hackathonDAO == null) {
                System.err.println("DAO non disponibile.");
                return new ArrayList<>();
            }
            return hackathonDAO.getHackathonConRegistrazioniAperte();
        } catch (SQLException e) {
            System.err.println("Errore nel recupero degli hackathon con registrazioni aperte: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Recupera un hackathon specifico per titolo identificativo.
     *
     * @param titoloIdentificativo Il titolo identificativo dell'hackathon
     * @return L'hackathon trovato, null se non esiste
     */
    public Hackathon getHackathonPerTitolo(String titoloIdentificativo) {
        try {
            if (hackathonDAO == null) {
                System.err.println("DAO non disponibile.");
                return null;
            }
            return hackathonDAO.getHackathonByTitolo(titoloIdentificativo);
        } catch (SQLException e) {
            System.err.println("Errore nel recupero dell'hackathon: " + e.getMessage());
            return null;
        }
    }

    // ===== METODI PER GESTIONE UTENTI =====

    /**
     * Recupera tutti gli utenti dal database.
     *
     * @return Lista di tutti gli utenti
     */
    public List<Utente> getTuttiUtenti() {
        try {
            return utenteDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Errore nel recupero degli utenti: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ===== METODI PER GESTIONE INVITI GIUDICE =====

    /**
     * Invia un invito a un giudice per un hackathon.
     *
     * @param hackathonTitolo Il titolo dell'hackathon
     * @param giudiceUsername Il nome utente del giudice da invitare
     * @param organizzatore L'organizzatore che invia l'invito
     * @return true se l'invito è stato inviato con successo, false altrimenti
     */
    public boolean invitaGiudice(String hackathonTitolo, String giudiceUsername, Organizzatore organizzatore) {
        try {
            return invitoGiudiceDAO.creaInvitoConVerifica(organizzatore.getName(), giudiceUsername, hackathonTitolo);
        } catch (SQLException | SecurityException e) {
            System.err.println("Errore nell'invio dell'invito: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica se un utente ha inviti come giudice.
     *
     * @param utente L'utente da verificare
     * @return true se ha inviti, false altrimenti
     */
    public boolean hasInvitiGiudice(Utente utente) {
        try {
            List<String> inviti = invitoGiudiceDAO.getInvitiByUser(utente.getName());
            return !inviti.isEmpty();
        } catch (SQLException e) {
            System.err.println("Errore nella verifica degli inviti: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera gli inviti giudice per un utente.
     *
     * @param username Il nome utente
     * @return Lista degli hackathon per cui l'utente è invitato come giudice
     */
    public List<String> getInvitiGiudice(String username) {
        try {
            return invitoGiudiceDAO.getInvitiByUser(username);
        } catch (SQLException e) {
            System.err.println("Errore nel recupero degli inviti: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ===== METODI PER GESTIONE DOCUMENTI =====

    /**
     * Recupera i documenti caricati per un hackathon.
     *
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Lista dei documenti
     */
    public List<Documento> getDocumentiHackathon(String titoloHackathon) {
        try {
            return documentoDAO.getDocumentiByHackathon(titoloHackathon);
        } catch (SQLException e) {
            System.err.println("Errore nel recupero dei documenti: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ===== METODI PER GESTIONE MEMBERSHIP =====

    /**
     * Recupera i nomi dei team per un hackathon.
     *
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Lista dei nomi dei team
     */
    public List<String> getNomiTeamHackathon(String titoloHackathon) {
        try {
            return membershipDAO.getTeamsForHackathon(titoloHackathon);
        } catch (SQLException e) {
            System.err.println("Errore nel recupero dei team: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Recupera i membri di un team.
     *
     * @param nomeTeam Il nome del team
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Lista dei membri del team
     */
    public List<Utente> getMembriTeam(String nomeTeam, String titoloHackathon) {
        try {
            return membershipDAO.getTeamMembers(nomeTeam, titoloHackathon);
        } catch (SQLException e) {
            System.err.println("Errore nel recupero dei membri: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ===== METODI PER GESTIONE VOTI =====

    /**
     * Assegna un voto finale a un team.
     *
     * @param titoloHackathon Il titolo dell'hackathon
     * @param nomeTeam Il nome del team
     * @param votoFinale Il voto da assegnare
     * @param giudice Il giudice che assegna il voto
     * @return true se il voto è stato assegnato con successo, false altrimenti
     */
    public boolean assegnaVotoFinale(String titoloHackathon, String nomeTeam, int votoFinale, Utente giudice) {
        try {
            return votoDAO.save(giudice.getName(), titoloHackathon, nomeTeam, votoFinale);
        } catch (SQLException e) {
            System.err.println("Errore nell'assegnazione del voto: " + e.getMessage());
            return false;
        }
    }

    // ===== METODI PER GESTIONE VALUTAZIONI =====

    /**
     * Salva una valutazione per un documento.
     *
     * @param valutazione La valutazione da salvare
     * @param idDocumento L'ID del documento valutato
     * @param nomeTeam Il nome del team
     * @param titoloHackathon Il titolo dell'hackathon
     * @return true se la valutazione è stata salvata con successo, false altrimenti
     */
    public boolean salvaValutazione(Valutazione valutazione, int idDocumento, String nomeTeam, String titoloHackathon) {
        try {
            return valutazioneDAO.save(valutazione, idDocumento, nomeTeam, titoloHackathon);
        } catch (SQLException e) {
            System.err.println("Errore nel salvataggio della valutazione: " + e.getMessage());
            return false;
        }
    }

    /**
     * Salva una valutazione testuale per un documento.
     *
     * @param valutazioneTestuale Il testo della valutazione
     * @param idDocumento L'ID del documento valutato
     * @param usernameGiudice Il nome utente del giudice
     * @param titoloHackathon Il titolo dell'hackathon
     * @return true se la valutazione è stata salvata con successo, false altrimenti
     */
    public boolean salvaValutazioneTestuale(String valutazioneTestuale, int idDocumento, String usernameGiudice, String titoloHackathon) {
        try {
            return valutazioneDAO.save(valutazioneTestuale, idDocumento, usernameGiudice, titoloHackathon);
        } catch (SQLException e) {
            System.err.println("Errore nel salvataggio della valutazione: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica se un giudice ha già valutato un documento specifico.
     * 
     * @param usernameGiudice Il nome utente del giudice
     * @param idDocumento L'ID del documento da controllare
     * @return true se il giudice ha già valutato il documento, false altrimenti
     */
    public boolean hasGiudiceValutatoDocumento(String usernameGiudice, int idDocumento) {
        if (valutazioneDAO == null) {
            return false;
        }
        
        try {
            return valutazioneDAO.hasGiudiceValutatoDocumento(usernameGiudice, idDocumento);
        } catch (SQLException e) {
            System.err.println("Errore durante il controllo della valutazione: " + e.getMessage());
            return false;
        }
    }

    // ===== METODI AGGIUNTIVI PER GESTIONE INVITI =====

    /**
     * Accetta un invito per un hackathon.
     *
     * @param username Il nome utente che accetta l'invito
     * @param titoloHackathon Il titolo dell'hackathon
     * @return true se l'invito è stato accettato con successo, false altrimenti
     */
    public boolean accettaInvito(String username, String titoloHackathon) {
        try {
            return invitoGiudiceDAO.accettaInvito(username, titoloHackathon);
        } catch (SQLException e) {
            System.err.println("Errore nell'accettazione dell'invito: " + e.getMessage());
            return false;
        }
    }

    /**
     * Rifiuta un invito per un hackathon.
     *
     * @param username Il nome utente che rifiuta l'invito
     * @param titoloHackathon Il titolo dell'hackathon
     * @return true se l'invito è stato rifiutato con successo, false altrimenti
     */
    public boolean rifiutaInvito(String username, String titoloHackathon) {
        try {
            return invitoGiudiceDAO.rifiutaInvito(username, titoloHackathon);
        } catch (SQLException e) {
            System.err.println("Errore nel rifiuto dell'invito: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera la lista degli hackathon per cui un utente è giudice.
     *
     * @param username Il nome utente del giudice
     * @return Lista degli hackathon per cui l'utente è giudice
     */
    public List<String> getHackathonAsGiudice(String username) {
        try {
            return invitoGiudiceDAO.getHackathonAsGiudice(username);
        } catch (SQLException e) {
            System.err.println("Errore nel recupero degli hackathon come giudice: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Salva un documento nel database.
     *
     * @param documento Il documento da salvare
     * @return true se il salvataggio è riuscito, false altrimenti
     */
    public boolean salvaDocumento(Documento documento) {
        try {
            return documentoDAO.save(documento);
        } catch (SQLException e) {
            System.err.println("Errore nel salvataggio del documento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Genera la classifica finale di un hackathon.
     *
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Stringa con la classifica generata
     */
    public String generaClassificaHackathon(String titoloHackathon) {
        try {
            return hackathonDAO.generaClassificaHackathon(titoloHackathon);
        } catch (SQLException e) {
            System.err.println("Errore nella generazione della classifica: " + e.getMessage());
            return "Errore nella generazione della classifica: " + e.getMessage();
        }
    }

    /**
     * Ottiene tutti i documenti caricati da un team specifico.
     *
     * @param nomeTeam Il nome del team
     * @param titoloHackathon Il titolo dell'hackathon
     * @return Lista dei documenti del team
     */
    public List<Documento> getDocumentiByTeam(String nomeTeam, String titoloHackathon) {
        try {
            return documentoDAO.getDocumentiByTeam(nomeTeam, titoloHackathon);
        } catch (SQLException e) {
            System.err.println("Errore nel recupero documenti del team: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Verifica se un hackathon è terminato.
     *
     * @param titoloHackathon Il titolo dell'hackathon da verificare
     * @return true se l'hackathon è terminato, false altrimenti
     */
    public boolean isHackathonTerminato(String titoloHackathon) {
        try {
            return hackathonDAO.isHackathonTerminato(titoloHackathon);
        } catch (SQLException e) {
            System.err.println("Errore nella verifica dello stato hackathon: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica se un giudice ha già votato un team specifico.
     *
     * @param usernameGiudice Il nome utente del giudice
     * @param titoloHackathon Il titolo dell'hackathon
     * @param nomeTeam Il nome del team
     * @return true se il giudice ha già votato il team, false altrimenti
     */
    public boolean hasGiudiceVotatoTeam(String usernameGiudice, String titoloHackathon, String nomeTeam) {
        try {
            return votoDAO.hasGiudiceVotatoTeam(usernameGiudice, titoloHackathon, nomeTeam);
        } catch (SQLException e) {
            System.err.println("Errore nella verifica del voto: " + e.getMessage());
            return false;
        }
    }
}
