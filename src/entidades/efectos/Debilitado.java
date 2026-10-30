package entidades.efectos;
import entidades.Estadistica;

public class Debilitado extends ModificadorEstadistica {

    public Debilitado(int duracion) {
        super("Debilitado", duracion);
        porcentaje(Estadistica.ATAQUE_FISICO, -30);
        porcentaje(Estadistica.ATAQUE_MAGICO, -30);
        porcentaje(Estadistica.DEFENSA_FISICA, -20);
        porcentaje(Estadistica.DEFENSA_MAGICA, -20);
    }
}
