package utilities;

/**
 * Utilità per tradurre i messaggi di errore del database in messaggi user-friendly.
 */
public class ErrorMessageTranslator {
    
    /**
     * Traduce un messaggio di errore tecnico del database in un messaggio comprensibile all'utente.
     * 
     * @param technicalError Il messaggio di errore originale dal database o dal sistema
     * @return Un messaggio di errore user-friendly
     */
    public static String translateError(String technicalError) {
        if (technicalError == null || technicalError.trim().isEmpty()) {
            return "Si è verificato un errore imprevisto. Riprova più tardi.";
        }
        
        String error = technicalError.toLowerCase();
        
        // Errori di password complexity
        if (error.contains("chk_password_complexity") || 
            error.contains("password") && error.contains("check")) {
            return "La password non rispetta i requisiti di sicurezza.\n\n" +
                   "Requisiti password:\n" +
                   "- Almeno 8 caratteri di lunghezza\n" +
                   "- Almeno una lettera maiuscola (A-Z)\n" +
                   "- Almeno una lettera minuscola (a-z)\n" +
                   "- Almeno un numero (0-9)\n" +
                   "- Almeno un carattere speciale (!@#$%^&*()-_=+{};:,<.>/?)";
        }
        
        // Errori di username duplicato
        if (error.contains("duplicate key") && error.contains("username")) {
            return "Questo nome utente è già stato utilizzato.\nScegli un nome utente diverso.";
        }
        
        // Errori di chiave primaria duplicata per organizzatori
        if (error.contains("duplicate key") && error.contains("organizzatore")) {
            return "Questo nome organizzatore è già stato utilizzato.\nScegli un nome diverso.";
        }
        
        // Errori di team duplicato
        if (error.contains("duplicate key") && error.contains("team")) {
            return "Esiste già un team con questo nome.\nScegli un nome diverso per il tuo team.";
        }
        
        // Errori di hackathon duplicato
        if (error.contains("duplicate key") && error.contains("hackathon")) {
            return "Esiste già un hackathon con questo titolo.\nScegli un titolo diverso.";
        }
        
        // Errori di data non valida
        if (error.contains("datainizio_evento") || error.contains("datafine_evento") ||
            error.contains("date") && error.contains("check")) {
            return "Le date inserite non sono valide.\n\n" +
                   "Verifica che:\n" +
                   "- La data di inizio sia precedente alla data di fine\n" +
                   "- Le registrazioni terminino almeno 2 giorni prima dell'evento\n" +
                   "- Tutte le date siano nel formato corretto";
        }
        
        // Errori di numero massimo partecipanti
        if (error.contains("maxnum_iscritti") && error.contains("check")) {
            return "Il numero massimo di partecipanti deve essere maggiore di zero.";
        }
        
        // Errori di numero massimo membri team
        if (error.contains("maxnum_membriteam") && error.contains("check")) {
            return "Il numero massimo di membri per team deve essere maggiore di zero.";
        }
        
        // Errori di voto fuori range
        if (error.contains("punteggio") && error.contains("check")) {
            return "Il voto deve essere compreso tra 0 e 10.";
        }
        
        // Errori di registrazione chiusa
        if (error.contains("registrazioni") && error.contains("chiuse")) {
            return "Le registrazioni per questo hackathon sono già chiuse.\nNon è più possibile iscriversi.";
        }
        
        // Errori di team al completo
        if (error.contains("team") && error.contains("numero massimo di membri")) {
            return "Il team ha già raggiunto il numero massimo di membri.\nCerca un altro team o creane uno nuovo.";
        }
        
        // Errori di hackathon al completo
        if (error.contains("hackathon") && error.contains("numero massimo di partecipanti")) {
            return "L'hackathon ha raggiunto il numero massimo di partecipanti.\nNon è possibile iscriversi.";
        }
        
        // Errori di documento mancante per il voto
        if (error.contains("documento") && error.contains("votare")) {
            return "Non è possibile votare questo team perché non ha caricato alcun documento.";
        }
        
        // Errori di giudice con date sovrapposte
        if (error.contains("giudice") && error.contains("sovrapposte")) {
            return "Non puoi essere giudice per questo hackathon perché sei già giudice\n" +
                   "per un altro hackathon con date sovrapposte.";
        }
        
        // Errori di connessione al database
        if (error.contains("connection") || error.contains("database")) {
            return "Problemi di connessione al database.\nRiprova tra qualche minuto.";
        }
        
        // Errori generici di SQL
        if (error.contains("sql") || error.contains("constraint")) {
            return "Si è verificato un errore di validazione dei dati.\nVerifica le informazioni inserite e riprova.";
        }
        
        // Se non riconosco l'errore, restituisco un messaggio generico ma rimuovo i dettagli tecnici
        return "Si è verificato un errore durante l'operazione.\nVerifica i dati inseriti e riprova.";
    }
    
    /**
     * Verifica se un errore è relativo alla complessità della password.
     */
    public static boolean isPasswordComplexityError(String error) {
        if (error == null) return false;
        String lowerError = error.toLowerCase();
        return lowerError.contains("chk_password_complexity") || 
               (lowerError.contains("password") && lowerError.contains("check"));
    }
    
    /**
     * Verifica se un errore è relativo a un username duplicato.
     */
    public static boolean isDuplicateUsernameError(String error) {
        if (error == null) return false;
        String lowerError = error.toLowerCase();
        return lowerError.contains("duplicate key") && lowerError.contains("username");
    }
}
