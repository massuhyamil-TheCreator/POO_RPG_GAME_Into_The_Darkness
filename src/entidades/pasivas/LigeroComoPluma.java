package entidades.pasivas;
import entidades.Estadistica;
import entidades.efectos.Pasiva;

public class LigeroComoPluma extends Pasiva {

    private static final int ESQUIVA = 20;

    public LigeroComoPluma() {
        super("Ligero como Pluma");
    }

    @Override
    public int modificar(Estadistica estadistica, int valor) {
        if (estadistica == Estadistica.ESQUIVA) return valor + ESQUIVA;
        return valor;
    }
}
