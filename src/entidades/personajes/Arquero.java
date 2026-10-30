package entidades.personajes;
import entidades.Categoria;
import entidades.Estadistica;
import entidades.efectos.DanioPorTurno;
import entidades.efectos.ModificadorEstadistica;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;
import entidades.pasivas.LigeroComoPluma;

public class Arquero extends Personaje {

    private int destreza;

    public Arquero(String nombre) {
        super(nombre, "Destreza", 45, 13, 95, 4, 1, 2, 2);
        this.destreza = 10;
        aplicarEfecto(new LigeroComoPluma());
        setBasico(new Habilidad("Flechazo Veloz", 0, Objetivo.UN_ENEMIGO, 6, 0));
        agregarHabilidad(new Habilidad("Lluvia de Flechas", 20, Objetivo.VARIOS_ENEMIGOS, 7, 0)
                .conVariacion(3)
                .conEfecto(() -> new DanioPorTurno("Sangrado", 1, 3), 20));
        agregarHabilidad(new Habilidad("Flechazo Divino", 15, Objetivo.UN_ENEMIGO, 5, 2)
                .conMultiplicadorContra(Categoria.NO_MUERTO, 150));
        agregarHabilidad(new Habilidad("Capa del Bosque", 15, Objetivo.SI_MISMO, 0, 0)
                .conEfecto(() -> new ModificadorEstadistica("Capa del Bosque", 2)
                        .sumar(Estadistica.ESQUIVA, 55), 100));
    }

    @Override
    public boolean actuaPrimero() {
        return true;
    }

    public int getDestreza() {
        return destreza;
    }

    public void setDestreza(int destreza) {
        this.destreza = destreza;
    }

    @Override
    protected void subirNivel() {
        subirNivel(5, 2, 0, 0, 1, 0);
        destreza++;
    }

    @Override
    public int getCapacidadInventario() {
        return 4;
    }
}

