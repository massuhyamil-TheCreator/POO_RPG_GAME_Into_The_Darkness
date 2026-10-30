package entidades.pasivas;
import entidades.efectos.Efecto;
import entidades.efectos.Pasiva;

public class Valiente extends Pasiva {

    public static final String ASUSTADO = "Asustado";

    public Valiente() {
        super("Valiente");
    }

    @Override
    public boolean bloquea(Efecto nuevo) {
        return nuevo.getNombre().equals(ASUSTADO);
    }
}
