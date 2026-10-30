package entidades.enemigos;
import entidades.Categoria;
import entidades.Estadistica;
import entidades.efectos.ModificadorEstadistica;
import entidades.efectos.Rabia;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;

public class LoboSalvaje extends Enemigo {

    public LoboSalvaje(String nombre) {
        super(nombre, Categoria.BESTIA, 28, 14, 85, 3, 0, 1, 1, 12, 5);
        setBasico(new Habilidad("Mordisco", 0, Objetivo.UN_ENEMIGO, 4, 0));
        agregarHabilidad(new Habilidad("Garra Rabiosa", 0, Objetivo.UN_ENEMIGO, 5, 0)
                .conEfecto(() -> new Rabia(2), 100));
        agregarHabilidad(new Habilidad("Aullido de la Manada", 0, Objetivo.TODOS_LOS_ALIADOS, 0, 0)
                .conEfecto(() -> new ModificadorEstadistica("Aullido de la Manada", 3)
                        .sumar(Estadistica.ATAQUE_FISICO, 1)
                        .sumar(Estadistica.ATAQUE_MAGICO, 1)
                        .sumar(Estadistica.DEFENSA_FISICA, 1)
                        .sumar(Estadistica.DEFENSA_MAGICA, 1), 100));
    }
}
