package entidades.enemigos;
import entidades.Categoria;
import entidades.Estadistica;
import entidades.efectos.ModificadorEstadistica;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;

public class HombreLobo extends Enemigo {

    public HombreLobo(String nombre) {
        super(nombre, Categoria.BESTIA, 45, 11, 85, 4, 0, 3, 1, 25, 15);
        setBasico(new Habilidad("Rasguño Licántropo", 0, Objetivo.UN_ENEMIGO, 7, 0));
        agregarHabilidad(new Habilidad("Zarpazo", 0, Objetivo.UN_ENEMIGO, 10, 0)
                .retrasandoTurno());
        agregarHabilidad(new Habilidad("Aullido del Alfa", 0, Objetivo.TODOS_LOS_ALIADOS, 0, 0)
                .conEfecto(() -> new ModificadorEstadistica("Aullido del Alfa", 3)
                        .sumar(Estadistica.ATAQUE_FISICO, 3)
                        .sumar(Estadistica.ATAQUE_MAGICO, 3)
                        .sumar(Estadistica.DEFENSA_FISICA, 3)
                        .sumar(Estadistica.DEFENSA_MAGICA, 3), 100));
    }
}
