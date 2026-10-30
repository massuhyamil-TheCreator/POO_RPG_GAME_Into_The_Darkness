package objetos.consumibles;
import entidades.Entidad;
import objetos.Item;

public abstract class Consumibles extends Item {

    private int espacios;

    public Consumibles(int id, String nombre, String descripcion, double precio, int espacios) {
        super(id, nombre, descripcion, precio);
        this.espacios = espacios;
    }

    public abstract String usar(Entidad objetivo);

    public int getEspacios() {
        return espacios;
    }
}
