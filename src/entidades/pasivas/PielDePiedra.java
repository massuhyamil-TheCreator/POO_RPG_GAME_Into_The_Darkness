package entidades.pasivas;
import entidades.Estadistica;
import entidades.efectos.Pasiva;
import java.util.Random;

public class PielDePiedra extends Pasiva {

    private static final Random AZAR = new Random();
    private static final int DEFENSA_FISICA_EXTRA = 3;
    private static final int PROBABILIDAD_ANULAR_CRITICO = 50;

    public PielDePiedra() {
        super("Piel de Piedra");
    }

    @Override
    public int modificar(Estadistica estadistica, int valor) {
        if (estadistica == Estadistica.DEFENSA_FISICA) return valor + DEFENSA_FISICA_EXTRA;
        return valor;
    }

    @Override
    public boolean anulaCritico() {
        return AZAR.nextInt(100) < PROBABILIDAD_ANULAR_CRITICO;
    }
}
