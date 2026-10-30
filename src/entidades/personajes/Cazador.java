package entidades.personajes;
import entidades.Estadistica;
import entidades.efectos.DanioPorTurno;
import entidades.efectos.ModificadorEstadistica;
import entidades.habilidades.BombaDeHumo;
import entidades.habilidades.CrucifijoConAjo;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;
import entidades.pasivas.VigilanteDeLaNoche;

public class Cazador extends Personaje {

    private int astucia;

    public Cazador(String nombre) {
        super(nombre, "Astucia", 50, 11, 90, 4, 1, 3, 2);
        this.astucia = 10;
        aplicarEfecto(new VigilanteDeLaNoche());
        setBasico(new Habilidad("Escopetazo", 0, Objetivo.UN_ENEMIGO, 7, 0));
        agregarHabilidad(new BombaDeHumo());
        agregarHabilidad(new Habilidad("Trampa para Bobos", 15, Objetivo.UN_ENEMIGO, 4, 0)
                .conEfecto(() -> new ModificadorEstadistica("Atrapado", 2)
                        .porcentaje(Estadistica.VELOCIDAD, -30), 100)
                .conEfecto(() -> new DanioPorTurno("Sangrado", 2, 1), 100));
        agregarHabilidad(new CrucifijoConAjo());
    }

    public int getAstucia() {
        return astucia;
    }

    public void setAstucia(int astucia) {
        this.astucia = astucia;
    }

    @Override
    protected void subirNivel() {
        subirNivel(5, 2, 0, 1, 0, 0);
        astucia++;
    }

    @Override
    public int getCapacidadInventario() {
        return 3;
    }
}
