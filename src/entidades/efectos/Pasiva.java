package entidades.efectos;

public abstract class Pasiva extends Efecto {

    public Pasiva(String nombre) {
        super(nombre, PERMANENTE);
    }

    @Override
    public boolean esPasiva() {
        return true;
    }
}
