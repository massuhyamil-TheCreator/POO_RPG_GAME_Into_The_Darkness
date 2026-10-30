package objetos.consumibles;
import entidades.Entidad;

public class PocionCuracion extends Consumibles {

    private int curacion;

    public PocionCuracion(int id, String nombre, String descripcion, double precio, int espacios, int curacion) {
        super(id, nombre, descripcion, precio, espacios);
        this.curacion = curacion;
    }

    @Override
    public String usar(Entidad objetivo) {
        objetivo.curar(curacion);
        return objetivo.getNombre() + " recupera " + curacion + " de vida.";
    }

    public int getCuracion() {
        return curacion;
    }
}
