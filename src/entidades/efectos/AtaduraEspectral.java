package entidades.efectos;

public class AtaduraEspectral extends Efecto {

    public AtaduraEspectral(int duracion) {
        super("Atadura Espectral", duracion);
    }

    @Override
    public boolean puedeUsarItems() {
        return false;
    }

    @Override
    public boolean esNegativo() {
        return true;
    }
}
