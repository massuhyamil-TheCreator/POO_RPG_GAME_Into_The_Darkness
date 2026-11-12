package excepciones;

public class PartidaDanadaException extends PartidaException {

    public PartidaDanadaException(int ranura, Throwable causa) {
        super("El archivo de la ranura " + ranura + " está dañado.", causa);
    }
}
