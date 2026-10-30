package entidades.habilidades;
import combate.Batalla;
import entidades.Entidad;
import entidades.efectos.Fragmentacion;
import entidades.enemigos.Enemigo;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PiedraViviente extends Habilidad {

    private static final int DURACION = 2;

    private Supplier<Enemigo> creadorDeCopia;

    public PiedraViviente(Supplier<Enemigo> creadorDeCopia) {
        super("¡Piedra Viviente!", 0, Objetivo.SI_MISMO, 0, 0);
        this.creadorDeCopia = creadorDeCopia;
    }

    @Override
    public boolean convieneUsar(Entidad usuario, Batalla batalla) {
        return !usuario.tieneEfecto(Fragmentacion.NOMBRE);
    }

    @Override
    public List<String> ejecutar(Entidad usuario, List<Entidad> objetivos, Batalla batalla) {
        List<String> mensajes = new ArrayList<>();
        if (usuario.aplicarEfecto(new Fragmentacion(batalla, creadorDeCopia, DURACION))) {
            mensajes.add("  La piel de " + usuario.getNombre() + " empieza a resquebrajarse...");
        }
        return mensajes;
    }
}
