package entidades.enemigos.bosses;
import entidades.Categoria;
import entidades.Estadistica;
import entidades.efectos.DanioPorTurno;
import entidades.efectos.Debilitado;
import entidades.efectos.ModificadorEstadistica;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Invocacion;
import entidades.habilidades.Objetivo;
import entidades.pasivas.RejuvenecimientoGeneracional;

public class ReyVampiro extends Jefe {

    public ReyVampiro() {
        super("Rey Vampiro", Categoria.CRIATURA_NOCTURNA, 200, 12, 90, 8, 7, 5, 5, 100, 100);
        aplicarEfecto(new RejuvenecimientoGeneracional());
        setBasico(new Habilidad("Garras del Rey de la Noche", 0, Objetivo.UN_ENEMIGO, 5, 4)
                .conEfecto(() -> DanioPorTurno.sangrado(1, 2), 10));
        agregarHabilidad(new Invocacion("Secuaces Nocturnos", 0, () -> new Murcielago(), 2)
                .conEnfriamiento(3));
        agregarHabilidad(new Habilidad("Visión Vampírica", 0, Objetivo.UN_ENEMIGO, 0, 3)
                .conEfectoSobreSiMismo(() -> new ModificadorEstadistica("Visión Vampírica", 3)
                        .sumar(Estadistica.CRITICO, 30)));
        agregarHabilidad(new Habilidad("Succión de Sangre", 0, Objetivo.UN_ENEMIGO, 0, 5)
                .conEfecto(() -> DanioPorTurno.sangrado(1, 2), 100)
                .conEfecto(() -> new Debilitado(2), 100));
    }
}
