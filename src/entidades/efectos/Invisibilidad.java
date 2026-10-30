package entidades.efectos;
import entidades.Estadistica;

public class Invisibilidad extends Efecto {

    public static final String NOMBRE = "Invisible";
    private static final int ESQUIVA_EXTRA = 80;

    public Invisibilidad(int duracion) {
        super(NOMBRE, duracion);
    }

    @Override
    public int modificar(Estadistica estadistica, int valor) {
        if (estadistica == Estadistica.ESQUIVA) return valor + ESQUIVA_EXTRA;
        return valor;
    }

    @Override
    public int modificarDanioRecibido(int danio) {
        terminar();
        return danio;
    }
}
