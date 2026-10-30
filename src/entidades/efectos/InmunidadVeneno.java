package entidades.efectos;

public class InmunidadVeneno extends Efecto {

    public InmunidadVeneno(int duracion) {
        super("Inmune al veneno", duracion);
    }

    @Override
    public boolean bloquea(Efecto nuevo) {
        return nuevo.getNombre().equals(DanioPorTurno.VENENO);
    }
}
