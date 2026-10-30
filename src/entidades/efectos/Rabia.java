package entidades.efectos;
import java.util.Random;

public class Rabia extends Efecto {

    private static final Random AZAR = new Random();
    private static final int PROBABILIDAD = 25;

    public Rabia(int duracion) {
        super("Rabia", duracion);
    }

    @Override
    public boolean descontrola() {
        return AZAR.nextInt(100) < PROBABILIDAD;
    }

    @Override
    public boolean esNegativo() {
        return true;
    }
}
