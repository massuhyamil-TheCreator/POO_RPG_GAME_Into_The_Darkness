package entidades.habilidades;
import combate.Batalla;
import entidades.Entidad;
import java.util.List;

public class Bastonazo extends Habilidad {

    private static final int PROBABILIDAD_CURAR = 20;
    private static final int CURACION = 5;

    public Bastonazo() {
        super("Bastonazo", 0, Objetivo.UN_ENEMIGO, 3, 0);
    }

    @Override
    public List<String> ejecutar(Entidad usuario, List<Entidad> objetivos, Batalla batalla) {
        List<String> mensajes = super.ejecutar(usuario, objetivos, batalla);
        if (AZAR.nextInt(100) < PROBABILIDAD_CURAR) {
            List<Entidad> aliados = batalla.aliadosDe(usuario);
            Entidad aliado = aliados.get(AZAR.nextInt(aliados.size()));
            int curado = aliado.curar(CURACION);
            mensajes.add("  El bastón brilla: " + aliado.getNombre() + " recupera " + curado + " de vida.");
        }
        return mensajes;
    }
}
