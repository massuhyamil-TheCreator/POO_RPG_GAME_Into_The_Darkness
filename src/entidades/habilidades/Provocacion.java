package entidades.habilidades;
import combate.Batalla;
import entidades.Entidad;
import entidades.efectos.Provocado;
import java.util.ArrayList;
import java.util.List;

public class Provocacion extends Habilidad {

    private static final int DURACION = 2;
    private static final int ENFRIAMIENTO = 2;
    private static final int ENFRIAMIENTO_JEFES = 4;

    public Provocacion() {
        super("Provocación", 15, Objetivo.UN_ENEMIGO, 0, 0);
    }

    @Override
    public List<String> ejecutar(Entidad usuario, List<Entidad> objetivos, Batalla batalla) {
        List<String> mensajes = new ArrayList<>();
        for (Entidad objetivo : objetivos) {
            if (objetivo.aplicarEfecto(new Provocado(usuario, DURACION))) {
                mensajes.add("  " + objetivo.getNombre() + " solo tiene ojos para " + usuario.getNombre() + ".");
            }
            iniciarEnfriamiento(objetivo.esJefe() ? ENFRIAMIENTO_JEFES : ENFRIAMIENTO);
        }
        return mensajes;
    }
}
