package objetos.consumibles;
import entidades.Entidad;
import entidades.efectos.DanioPorTurno;
import entidades.efectos.InmunidadVeneno;

public class Antidoto extends Consumibles {

    public static final int TURNOS_INMUNIDAD = 2;

    public Antidoto(int id, String nombre, String descripcion, double precio) {
        super(id, nombre, descripcion, precio, 1);
    }

    @Override
    public String usar(Entidad objetivo) {
        objetivo.quitarEfecto(DanioPorTurno.VENENO);
        objetivo.aplicarEfecto(new InmunidadVeneno(TURNOS_INMUNIDAD));
        return objetivo.getNombre() + " se cura del veneno y queda inmune por " + TURNOS_INMUNIDAD + " turnos.";
    }
}
