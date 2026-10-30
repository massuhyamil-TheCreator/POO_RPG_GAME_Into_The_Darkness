package entidades.efectos;
import entidades.Entidad;

public class DanioPorTurno extends Efecto {

    public static final String VENENO = "Envenenado";
    public static final String SANGRADO = "Sangrado";

    private int danioPorTurno;

    public DanioPorTurno(String nombre, int danioPorTurno, int duracion) {
        super(nombre, duracion);
        this.danioPorTurno = danioPorTurno;
    }

    public static DanioPorTurno veneno() {
        return new DanioPorTurno(VENENO, 1, PERMANENTE);
    }

    public static DanioPorTurno sangrado(int danioPorTurno, int duracion) {
        return new DanioPorTurno(SANGRADO, danioPorTurno, duracion);
    }

    @Override
    public String alInicioDelTurno(Entidad portador) {
        portador.recibirDanio(danioPorTurno);
        return portador.getNombre() + " sufre " + danioPorTurno + " de daño por " + getNombre() + ".";
    }

    public int getDanioPorTurno() { return danioPorTurno; }

    @Override
    public boolean esNegativo() {
        return true;
    }
}
