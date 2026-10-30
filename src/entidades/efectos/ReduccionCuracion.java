package entidades.efectos;

public class ReduccionCuracion extends Efecto {

    private int porcentaje;

    public ReduccionCuracion(String nombre, int porcentaje, int duracion) {
        super(nombre, duracion);
        this.porcentaje = porcentaje;
    }

    @Override
    public int modificarCuracionRecibida(int curacion) {
        return curacion * (100 - porcentaje) / 100;
    }

    @Override
    public boolean esNegativo() {
        return true;
    }
}
