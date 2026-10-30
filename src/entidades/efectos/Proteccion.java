package entidades.efectos;

public class Proteccion extends Efecto {

    private int porcentajeMitigado;

    public Proteccion(String nombre, int porcentajeMitigado, int duracion) {
        super(nombre, duracion);
        this.porcentajeMitigado = porcentajeMitigado;
    }

    @Override
    public int modificarDanioRecibido(int danio) {
        terminar();
        return danio * (100 - porcentajeMitigado) / 100;
    }
}

