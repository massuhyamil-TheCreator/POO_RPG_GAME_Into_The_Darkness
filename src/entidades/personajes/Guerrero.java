package entidades.personajes;
import entidades.Estadistica;
import entidades.efectos.Aturdimiento;
import entidades.efectos.ModificadorEstadistica;
import entidades.efectos.Proteccion;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;
import entidades.pasivas.Valiente;

public class Guerrero extends Personaje {

    private int fuerza;

    public Guerrero(String nombre) {
        super(nombre, "Fuerza", 60, 8, 85, 5, 0, 4, 1);
        this.fuerza = 10;
        aplicarEfecto(new Valiente());
        setBasico(new Habilidad("Espadazo", 0, Objetivo.UN_ENEMIGO, 6, 0));
        agregarHabilidad(new Habilidad("Grito de Guerra", 20, Objetivo.TODOS_LOS_ALIADOS, 0, 0)
                .conEfecto(() -> new ModificadorEstadistica("Grito de Guerra", 3)
                        .sumar(Estadistica.ATAQUE_FISICO, 3)
                        .sumar(Estadistica.ATAQUE_MAGICO, 3), 100));
        agregarHabilidad(new Habilidad("Escudazo", 15, Objetivo.UN_ENEMIGO, 4, 0)
                .conEfecto(() -> new Aturdimiento("Aturdido", 1), 100));
        agregarHabilidad(new Habilidad("Protector", 15, Objetivo.UN_ALIADO, 0, 0)
                .conEfecto(() -> new Proteccion("Protegido", 80, 1), 100));
    }

    public int getFuerza() {
        return fuerza;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    @Override
    protected void subirNivel() {
        subirNivel(7, 2, 0, 1, 0, 0);
        fuerza++;
    }

    @Override
    public int getCapacidadInventario() {
        return 6;
    }
}
