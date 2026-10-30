package entidades.efectos;
import combate.Batalla;
import entidades.enemigos.Enemigo;
import java.util.function.Supplier;

public class Fragmentacion extends Efecto {

    public static final String NOMBRE = "Piedra Viviente";

    private Batalla batalla;
    private Supplier<Enemigo> creadorDeCopia;

    public Fragmentacion(Batalla batalla, Supplier<Enemigo> creadorDeCopia, int duracion) {
        super(NOMBRE, duracion);
        this.batalla = batalla;
        this.creadorDeCopia = creadorDeCopia;
    }

    @Override
    public int modificarDanioRecibido(int danio) {
        if (danio > 0) batalla.invocar(creadorDeCopia.get(), getPortador());
        return danio;
    }
}
