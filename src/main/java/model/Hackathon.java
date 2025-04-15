package model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Hackathon {
    private String idNum;
    private String sede;
    private String dataInizio;
    private String dataFine;
    private String dataInizioRegistrazioni;
    private String dataFineRegistrazioni;
    private String titolo;
    private int maxMembriTeam;
    private int maxNumIscritti;
    private String descrizioneProblema;
    private ArrayList<Giudice> giudiciEvento;

    public Hackathon(String idNum, String sede, String dataInizio, String dataFine, String dataInizioRegistrazioni, String dataFineRegistrazioni, String titolo, int maxMembriTeam, int maxNumIscritti) {
        this.idNum = idNum;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.dataInizioRegistrazioni = dataInizioRegistrazioni;
        this.dataFineRegistrazioni = dataFineRegistrazioni;
        this.titolo = titolo;
        this.maxMembriTeam = maxMembriTeam;
        this.maxNumIscritti = maxNumIscritti;
        giudiciEvento = new ArrayList<>();
    }

    public void setDescrizioneProblema(String descrizioneProblema) {
        this.descrizioneProblema = descrizioneProblema;
    }

    public int getMaxMembriTeam() {
        return maxMembriTeam;
    }

    public void printInfoEvento()
    {
        System.out.println("- Titolo: " + titolo +
                "\n- Sede dell'evento: " + sede +
                "\n- Data Inizio dell'evento: " + dataInizio +
                "\n- Data Fine dell'evento: " + dataFine +
                "\n- Data Inizio iscrizioni: " + dataInizioRegistrazioni +
                "\n- Data Fine iscrizioni: " + dataFineRegistrazioni +
                "\n- Descrizione del problema: " +
                "\t" + descrizioneProblema);
    }

    public void aggiungiGiudice(Giudice newGiudice)
    {
        giudiciEvento.add(newGiudice);
    }

    public String getIdNum() {
        return idNum;
    }
}
