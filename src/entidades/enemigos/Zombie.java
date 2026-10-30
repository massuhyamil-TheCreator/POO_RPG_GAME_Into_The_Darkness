package entidades.enemigos;
import entidades.Categoria;
import entidades.Estadistica;
import entidades.efectos.DanioPorTurno;
import entidades.efectos.ModificadorEstadistica;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;

public class Zombie extends Enemigo {

    public Zombie(String nombre) {
        super(nombre, Categoria.NO_MUERTO, 35, 5, 80, 3, 1, 2, 1, 15, 8);
        setBasico(new Habilidad("Mordida", 0, Objetivo.UN_ENEMIGO, 5, 0));
        agregarHabilidad(new Habilidad("Rasguño Pútrido", 0, Objetivo.UN_ENEMIGO, 3, 1)
                .conEfecto(() -> new DanioPorTurno("Putrefacción", 1, 3), 100));
        agregarHabilidad(new Habilidad("Vómito", 0, Objetivo.VARIOS_ENEMIGOS, 0, 0)
                .conCantidadDeObjetivos(1, 2)
                .conEfecto(() -> new ModificadorEstadistica("Ralentizado", 2).porcentaje(Estadistica.VELOCIDAD, -30), 100));
    }
}
