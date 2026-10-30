package entidades.enemigos;
import entidades.Categoria;
import entidades.Estadistica;
import entidades.efectos.DanioPorTurno;
import entidades.efectos.ModificadorEstadistica;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;

public class Bruja extends Enemigo {

    public Bruja(String nombre) {
        super(nombre, Categoria.CRIATURA_NOCTURNA, 28, 10, 90, 1, 4, 1, 3, 20, 15);
        setBasico(new Habilidad("Poción de Envenenamiento", 0, Objetivo.VARIOS_ENEMIGOS, 0, 0)
                .conCantidadDeObjetivos(2, 2)
                .conEfecto(() -> DanioPorTurno.veneno(), 100));
        agregarHabilidad(new Habilidad("Curación Siniestra", 0, Objetivo.ALIADOS_CON_MENOS_VIDA, 0, 0)
                .conCantidadDeObjetivos(2, 2)
                .conCuracion(8));
        agregarHabilidad(new Habilidad("Protección Nocturna", 0, Objetivo.ALIADOS_CON_MENOS_VIDA, 0, 0)
                .conCantidadDeObjetivos(2, 2)
                .conEnfriamiento(3)
                .conEfecto(() -> new ModificadorEstadistica("Protección Nocturna", 2)
                        .porcentaje(Estadistica.DEFENSA_FISICA, 30)
                        .porcentaje(Estadistica.DEFENSA_MAGICA, 30), 100));
    }
}
