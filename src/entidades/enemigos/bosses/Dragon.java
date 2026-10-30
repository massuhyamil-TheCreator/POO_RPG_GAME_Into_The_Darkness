package entidades.enemigos.bosses;
import entidades.Categoria;
import entidades.Estadistica;
import entidades.efectos.Aturdimiento;
import entidades.efectos.DanioPorTurno;
import entidades.efectos.ModificadorEstadistica;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;
import entidades.pasivas.Intimidante;
import entidades.pasivas.Valiente;

public class Dragon extends Jefe {

    public Dragon() {
        super("Dragón", Categoria.JEFE, 200, 9, 90, 9, 8, 6, 5, 100, 100);
        aplicarEfecto(new Intimidante());
        setBasico(new Habilidad("Garras Draconianas", 0, Objetivo.UN_ENEMIGO, 8, 0));
        agregarHabilidad(new Habilidad("Aliento de Fuego", 0, Objetivo.VARIOS_ENEMIGOS, 0, 5)
                .conCantidadDeObjetivos(2, 2)
                .conEfecto(() -> new DanioPorTurno("Quemadura", 2, 3), 100));
        agregarHabilidad(new Habilidad("Rugido Escamado", 0, Objetivo.VARIOS_ENEMIGOS, 0, 0)
                .conCantidadDeObjetivos(2, 2)
                .conEfecto(() -> new Aturdimiento(Valiente.ASUSTADO, 1), 100));
        agregarHabilidad(new Habilidad("Piel Draconiana", 0, Objetivo.SI_MISMO, 0, 0)
                .conEfecto(() -> new ModificadorEstadistica("Piel Draconiana", 3)
                        .sumar(Estadistica.DEFENSA_MAGICA, 3)
                        .sumar(Estadistica.DEFENSA_FISICA, 4), 100));
        agregarHabilidad(new Habilidad("Embestida Alada", 0, Objetivo.TODOS_LOS_ENEMIGOS, 3, 0));
    }
}
