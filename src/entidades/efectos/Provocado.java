package entidades.efectos;
import entidades.Entidad;

public class Provocado extends Efecto {

    private Entidad provocador;

    public Provocado(Entidad provocador, int duracion) {
        super("Provocado", duracion);
        this.provocador = provocador;
    }

    @Override
    public Entidad objetivoForzado() {
        return provocador;
    }

    @Override
    public boolean esNegativo() {
        return true;
    }
}
