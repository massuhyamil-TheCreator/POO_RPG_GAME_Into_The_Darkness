package entidades.efectos;
import entidades.Entidad;

public class Encantado extends Efecto {

    private Entidad encantador;

    public Encantado(Entidad encantador, int duracion) {
        super("Encantado", duracion);
        this.encantador = encantador;
    }

    @Override
    public boolean permiteAtacarA(Entidad objetivo) {
        return objetivo != encantador;
    }

    @Override
    public boolean esNegativo() {
        return true;
    }
}
