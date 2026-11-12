package excepciones;

public class PartidaException extends Exception {

    public PartidaException(String mensaje) {
        super(mensaje);
    }

    public PartidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
