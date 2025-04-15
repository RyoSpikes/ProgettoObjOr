package model;

public class Documento {
    private String text;
    private Team source;

    public Documento(Team source, String text) {
        this.text = text;
        this.source = source;
    }

    public String getText() {
        return text;
    }
}
