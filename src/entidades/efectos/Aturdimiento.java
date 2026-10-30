package entidades.efectos;

public class Aturdimiento extends Efecto {

    public Aturdimiento(String nombre, int duracion) {
        super(nombre, duracion);
    }

    @Override
    public boolean puedeActuar() {
        return false;
    }

    @Override
    public boolean esNegativo() {
        return true;
    }
}
