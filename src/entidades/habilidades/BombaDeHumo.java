package entidades.habilidades;
import combate.Batalla;
import entidades.Entidad;
import entidades.Estadistica;
import entidades.efectos.ModificadorEstadistica;
import java.util.ArrayList;
import java.util.List;

public class BombaDeHumo extends Habilidad {

    private static final int DURACION = 2;
    private static final int REDUCCION = -30;
    private static final int REDUCCION_JEFES = -15;

    public BombaDeHumo() {
        super("Bomba de Humo", 15, Objetivo.TODOS_LOS_ENEMIGOS, 0, 0);
    }

    @Override
    public List<String> ejecutar(Entidad usuario, List<Entidad> objetivos, Batalla batalla) {
        List<String> mensajes = new ArrayList<>();
        for (Entidad objetivo : objetivos) {
            int reduccion = objetivo.esJefe() ? REDUCCION_JEFES : REDUCCION;
            ModificadorEstadistica cegado = new ModificadorEstadistica("Cegado", DURACION)
                    .porcentaje(Estadistica.PRECISION, reduccion);
            if (objetivo.aplicarEfecto(cegado)) {
                mensajes.add("  " + objetivo.getNombre() + " recibe el efecto Cegado.");
            }
        }
        return mensajes;
    }
}
