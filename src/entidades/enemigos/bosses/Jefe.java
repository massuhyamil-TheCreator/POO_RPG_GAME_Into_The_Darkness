package entidades.enemigos.bosses;
import entidades.Categoria;
import entidades.enemigos.Enemigo;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public abstract class Jefe extends Enemigo {

    private static final Random AZAR = new Random();
    private static final List<Supplier<Jefe>> JEFES = List.of(Dragon::new, Golem::new, ReyVampiro::new);

    public Jefe(String nombre, Categoria categoria, int vida, int velocidad, int precision, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int experienciaOtorgada, int oroOtorgado) {
        super(nombre, categoria, vida, velocidad, precision, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica, experienciaOtorgada, oroOtorgado);
    }

    @Override
    public boolean esJefe() {
        return true;
    }

    public static Jefe crearAlAzar() {
        return JEFES.get(AZAR.nextInt(JEFES.size())).get();
    }
}
