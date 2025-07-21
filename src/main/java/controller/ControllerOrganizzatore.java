package controller;

import model.*;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.*;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import Database.DAO.Impl.OrganizzatoreDAOImpl;
import Database.DAO.Impl.HackathonDAOImpl;

/**
 * La classe ControllerOrganizzatore estende la classe Controller e gestisce una lista di organizzatori.
 * Fornisce metodi per la gestione degli organizzatori e degli hackathon associati.
 */
public class ControllerOrganizzatore extends Controller {

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
     * Costruttore della classe ControllerOrganizzatore.
     * Inizializza la lista degli organizzatori e le DAO.
     */
    public ControllerOrganizzatore() {
        listaOrganizzatori = new ArrayList<>();
        try {
            organizzatoreDAO = new OrganizzatoreDAOImpl();
            hackathonDAO = new HackathonDAOImpl();
            // Carica tutti gli organizzatori dal database all'avvio
            listaOrganizzatori.addAll(organizzatoreDAO.findAll());
            System.out.println("ControllerOrganizzatore inizializzato con " + listaOrganizzatori.size() + " organizzatori dal database.");
        } catch (SQLException e) {
            System.err.println("Errore durante l'inizializzazione del ControllerOrganizzatore: " + e.getMessage());
            organizzatoreDAO = null; // Modalità offline
            hackathonDAO = new HackathonDAOImpl();
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
        for (Organizzatore o : listaOrganizzatori) {
            hList.addAll(o.getHackathonOrganizzate());
        }
        return hList;
    }

    // TODO: Implementare il metodo per recuperare la lista di hackathon di un singolo organizzatore.
    /*public ArrayList<Hackathon> getListaHackathon(Organizzatore org) {
        return org.getHackathonOrganizzata;
    }*/

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
     * Aggiunge un nuovo organizzatore alla lista e lo salva nel database.
     *
     * @param username Il nome utente del nuovo organizzatore.
     * @param password La password del nuovo organizzatore.
     * @throws IllegalArgumentException se l'organizzatore non può essere creato (ad esempio, input non valido).
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

    /**
     * Aggiunge un nuovo hackathon per un organizzatore specifico.
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
     * Crea un nuovo hackathon e lo salva nel database.
     *
     * @param titoloIdentificativo Titolo identificativo dell'hackathon
     * @param organizzatore L'organizzatore che crea l'hackathon
     * @param sede Sede dell'evento
     * @param dataInizioRegistrazione Data inizio registrazioni
     * @param dataFineRegistrazione Data fine registrazioni
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

        return hackathonDAO.creaHackathon(titoloIdentificativo, organizzatore.getName(), sede,
                                        dataInizioRegistrazione, dataFineRegistrazione,
                                        dataInizioEvento, dataFineEvento,
                                        descrizioneProblema, maxNumIscritti, maxNumMembriTeam);
    }

    /**
     * Recupera tutti gli hackathon di un organizzatore specifico dal database.
     *
     * @param organizzatore L'organizzatore di cui recuperare gli hackathon
     * @return Lista degli hackathon dell'organizzatore
     */
    public List<Hackathon> getHackathonDiOrganizzatore(Organizzatore organizzatore) {
        try {
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
            return hackathonDAO.getHackathonByTitolo(titoloIdentificativo);
        } catch (SQLException e) {
            System.err.println("Errore nel recupero dell'hackathon: " + e.getMessage());
            return null;
        }
    }
}
