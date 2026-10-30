package entidades.pasivas;
import combate.Batalla;
import entidades.Entidad;
import entidades.Estadistica;
import entidades.efectos.ModificadorEstadistica;
import entidades.efectos.Pasiva;

public class Intimidante extends Pasiva {

    private static final int REDUCCION = -2;

    public Intimidante() {
        super("Intimidante");
    }

    @Override
    public void alComenzarBatalla(Batalla batalla) {
        for (Entidad enemigo : batalla.enemigosDe(getPortador())) {
            enemigo.aplicarEfecto(new ModificadorEstadistica("Intimidado", PERMANENTE)
                    .sumar(Estadistica.ATAQUE_FISICO, REDUCCION)
                    .sumar(Estadistica.ATAQUE_MAGICO, REDUCCION));
        }
    }
}
