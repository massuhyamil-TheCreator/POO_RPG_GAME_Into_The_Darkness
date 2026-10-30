package entidades.personajes;
import entidades.Estadistica;
import entidades.efectos.Aturdimiento;
import entidades.efectos.DanioPorTurno;
import entidades.efectos.Invulnerabilidad;
import entidades.efectos.ModificadorEstadistica;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;
import entidades.pasivas.ConocimientosArcanos;

public class Mago extends Personaje {

    private int inteligencia;

    public Mago(String nombre) {
        super(nombre, "Inteligencia", 40, 9, 90, 1, 5, 1, 4);
        this.inteligencia = 10;
        aplicarEfecto(new ConocimientosArcanos());
        setBasico(new Habilidad("Proyectil Etéreo", 0, Objetivo.UN_ENEMIGO, 0, 7));
        agregarHabilidad(new Habilidad("Bola de Fuego", 20, Objetivo.VARIOS_ENEMIGOS, 0, 9)
                .conEfecto(() -> new DanioPorTurno("Quemadura", 2, 2), 100));
        agregarHabilidad(new Habilidad("Lluvia de Escarcha", 25, Objetivo.TODOS_LOS_ENEMIGOS, 0, 10)
                .conEfecto(() -> new ModificadorEstadistica("Ralentizado", 2).porcentaje(Estadistica.VELOCIDAD, -30), 100)
                .conEfecto(() -> new Aturdimiento("Congelado", 1), 20));
        agregarHabilidad(new Habilidad("Escudo Mágico", 15, Objetivo.SI_MISMO, 0, 0)
                .conEfecto(() -> new Invulnerabilidad(1), 100));
    }

    public int getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = inteligencia;
    }

    @Override
    protected void subirNivel() {
        subirNivel(4, 0, 2, 0, 1, 0);
        inteligencia++;
    }

    @Override
    public int getCapacidadInventario() {
        return 3;
    }
}
