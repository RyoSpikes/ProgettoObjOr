package controller;

import model.*;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
     * Costruttore della classe ControllerOrganizzatore.
     * Inizializza la lista degli organizzatori.
     */
    public ControllerOrganizzatore() {
        listaOrganizzatori = new ArrayList<>();
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
}