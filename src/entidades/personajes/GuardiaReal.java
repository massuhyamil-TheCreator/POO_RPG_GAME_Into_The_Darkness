package entidades.personajes;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;

public class GuardiaReal extends Personaje {

    private static final int DURACION = 3;

    private int turnosRestantes = DURACION;

    public GuardiaReal() {
        super("Guardia Real", "Lealtad", 25, 7, 80, 3, 0, 3, 2);
        setBasico(new Habilidad("Lanzazo", 0, Objetivo.UN_ENEMIGO, 4, 0));
    }

    @Override
    public void finalizarTurno() {
        super.finalizarTurno();
        turnosRestantes--;
        if (turnosRestantes == 0) desaparecer();
    }

    @Override
    public int getCapacidadInventario() {
        return 0;
    }
}
