package entidades.pasivas;
import entidades.efectos.Pasiva;
import java.util.Random;

public class ConocimientosArcanos extends Pasiva {

    private static final Random AZAR = new Random();
    private static final int PROBABILIDAD = 20;

    public ConocimientosArcanos() {
        super("Conocimientos Arcanos");
    }

    @Override
    public boolean ahorraCosto() {
        return AZAR.nextInt(100) < PROBABILIDAD;
    }
}
