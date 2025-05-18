package utilities;
import java.time.DateTimeException;
import java.util.Random;

public class RandomStringGenerator {

    /**
     * Metodo per la generazione casuale di stringhe.
     *
     * @param length                   Lunghezza della stringa da generare
     * @return sb.toString()           Stringa costruita tramite selezione casuale dei caratteri listati
     *
     */
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

}
