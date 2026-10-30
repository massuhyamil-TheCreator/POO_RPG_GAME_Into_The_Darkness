package entidades.habilidades;
import combate.Batalla;
import entidades.Entidad;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Invocacion extends Habilidad {

    private Supplier<Entidad> creadorDeInvocado;
    private int cantidad;

    public Invocacion(String nombre, int costo, Supplier<Entidad> creadorDeInvocado, int cantidad) {
        super(nombre, costo, Objetivo.SI_MISMO, 0, 0);
        this.creadorDeInvocado = creadorDeInvocado;
        this.cantidad = cantidad;
    }

    @Override
    public boolean convieneUsar(Entidad usuario, Batalla batalla) {
        return batalla.aliadosDe(usuario).size() < Batalla.MAXIMO_POR_BANDO;
    }

    @Override
    public List<String> ejecutar(Entidad usuario, List<Entidad> objetivos, Batalla batalla) {
        List<String> mensajes = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            Entidad invocado = creadorDeInvocado.get();
            if (!batalla.invocar(invocado, usuario)) {
                mensajes.add("  No hay lugar para más invocaciones.");
                break;
            }
            mensajes.add("  Aparece " + invocado.getNombre() + ".");
        }
        return mensajes;
    }
}
