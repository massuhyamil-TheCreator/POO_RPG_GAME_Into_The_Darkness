package entidades.habilidades;
import combate.Batalla;
import entidades.Categoria;
import entidades.Entidad;
import entidades.efectos.Debilitado;
import java.util.ArrayList;
import java.util.List;

public class CrucifijoConAjo extends Habilidad {

    private static final int DURACION = 2;

    public CrucifijoConAjo() {
        super("Crucifijo con Ajo", 15, Objetivo.TODOS_LOS_ENEMIGOS, 0, 0);
    }

    @Override
    public List<String> ejecutar(Entidad usuario, List<Entidad> objetivos, Batalla batalla) {
        List<String> mensajes = new ArrayList<>();
        for (Entidad objetivo : objetivos) {
            if (objetivo.getCategoria() != Categoria.CRIATURA_NOCTURNA) continue;
            if (batalla.retrasarTurno(objetivo)) {
                mensajes.add("  " + objetivo.getNombre() + " retrocede espantado.");
            }
            if (objetivo.aplicarEfecto(new Debilitado(DURACION))) {
                mensajes.add("  " + objetivo.getNombre() + " recibe el efecto Debilitado.");
            }
        }
        if (mensajes.isEmpty()) {
            mensajes.add("  Ninguna criatura de la noche le teme.");
        }
        return mensajes;
    }
}

