package entidades.efectos;

public class Invulnerabilidad extends Efecto {

    public Invulnerabilidad(int duracion) {
        super("Invulnerable", duracion);
    }

    @Override
    public int modificarDanioRecibido(int danio) {
        return 0;
    }
}
