package entidades.personajes;
import entidades.efectos.DanioPorTurno;
import entidades.efectos.Invisibilidad;
import entidades.habilidades.GolpeDelLadron;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;
import entidades.pasivas.CazadorSigiloso;

public class Asesino extends Personaje {

    private int letalidad;

    public Asesino(String nombre) {
        super(nombre, "Letalidad", 40, 15, 90, 5, 1, 2, 1);
        this.letalidad = 10;
        aplicarEfecto(new CazadorSigiloso());
        setBasico(new Habilidad("Apuñalar", 0, Objetivo.UN_ENEMIGO, 7, 0));
        agregarHabilidad(new Habilidad("Desvanecimiento", 15, Objetivo.SI_MISMO, 0, 0)
                .conEfecto(() -> new Invisibilidad(2), 100));
        agregarHabilidad(new GolpeDelLadron());
        agregarHabilidad(new Habilidad("Daga Serruchada", 15, Objetivo.UN_ENEMIGO, 9, 0)
                .conEfecto(() -> new DanioPorTurno("Sangrado", 2, 2), 100));
    }

    public int getLetalidad() {
        return letalidad;
    }

    public void setLetalidad(int letalidad) {
        this.letalidad = letalidad;
    }

    @Override
    protected void subirNivel() {
        subirNivel(4, 2, 0, 0, 0, 1);
        letalidad++;
    }

    @Override
    public int getCapacidadInventario() {
        return 3;
    }
}
