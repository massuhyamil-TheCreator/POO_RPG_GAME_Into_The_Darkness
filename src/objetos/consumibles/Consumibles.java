package objetos.consumibles;
import entidades.Entidad;
import objetos.Inventario;
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


    @Override
    public void guardarEn(Inventario inventario) { inventario.guardarConsumible(this); }

    @Override
    public void sacarDe(Inventario inventario) { inventario.sacarConsumible(this); }

    @Override
    public boolean estaEn(Inventario inventario) { return inventario.tieneConsumible(this); }
}
