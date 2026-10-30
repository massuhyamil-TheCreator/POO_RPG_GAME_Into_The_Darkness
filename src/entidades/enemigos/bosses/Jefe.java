package entidades.enemigos.bosses;
import entidades.Categoria;
import entidades.enemigos.Enemigo;
import java.util.Random;

public abstract class Jefe extends Enemigo {

    private static final Random AZAR = new Random();

    public Jefe(String nombre, Categoria categoria, int vida, int velocidad, int precision, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int experienciaOtorgada, int oroOtorgado) {
        super(nombre, categoria, vida, velocidad, precision, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica, experienciaOtorgada, oroOtorgado);
    }

    @Override
    public boolean esJefe() {
        return true;
    }

    public static Jefe crearAlAzar() {
        switch (AZAR.nextInt(3)) {
            case 0: return new Dragon();
            case 1: return new Golem();
            default: return new ReyVampiro();
        }
    }
}
