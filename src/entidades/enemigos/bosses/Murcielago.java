package entidades.enemigos.bosses;
import entidades.Categoria;
import entidades.Estadistica;
import entidades.enemigos.Enemigo;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;

public class Murcielago extends Enemigo {

    private static final int ESQUIVA = 60;

    public Murcielago() {
        super("Murciélago", Categoria.CRIATURA_NOCTURNA, 1, 14, 80, 2, 0, 0, 0, 0, 0);
        setBasico(new Habilidad("Mordisco de Murciélago", 0, Objetivo.UN_ENEMIGO, 1, 0)
                .conVariacion(2));
    }

    @Override
    protected int getEstadisticaBase(Estadistica estadistica) {
        if (estadistica == Estadistica.ESQUIVA) return ESQUIVA;
        return super.getEstadisticaBase(estadistica);
    }
}
