package entidades.habilidades;
import combate.Batalla;
import entidades.Entidad;
import entidades.enemigos.Enemigo;
import objetos.Item;
import java.util.List;

public class GolpeDelLadron extends Habilidad {

    private static final int PROBABILIDAD_ORO = 95;
    private static final int ORO_MAXIMO = 10;

    public GolpeDelLadron() {
        super("Golpe del Ladrón", 10, Objetivo.UN_ENEMIGO, 6, 0);
    }

    @Override
    public List<String> ejecutar(Entidad usuario, List<Entidad> objetivos, Batalla batalla) {
        Entidad objetivo = objetivos.get(0);
        int vidaAntes = objetivo.getPuntosDeSalud();
        List<String> mensajes = super.ejecutar(usuario, objetivos, batalla);
        boolean pego = objetivo.getPuntosDeSalud() < vidaAntes;
        if (pego && objetivo instanceof Enemigo) {
            Enemigo enemigo = (Enemigo) objetivo;
            if (AZAR.nextInt(100) < PROBABILIDAD_ORO) {
                int robado = enemigo.robarOro(ORO_MAXIMO);
                batalla.getPartyAliada().agregarOro(robado);
                mensajes.add("  " + usuario.getNombre() + " le roba " + robado + " de oro.");
            } else {
                Item robado = enemigo.robarObjeto();
                if (robado != null) {
                    batalla.getPartyAliada().getInventario().agregar(robado);
                    mensajes.add("  " + usuario.getNombre() + " le roba " + robado.getNombre() + ".");
                } else {
                    mensajes.add("  " + usuario.getNombre() + " intenta robar un objeto, pero no encuentra nada.");
                }
            }
        }
        return mensajes;
    }
}

