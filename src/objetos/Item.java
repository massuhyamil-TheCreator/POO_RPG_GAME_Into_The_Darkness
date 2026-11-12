package objetos;
public abstract class Item {

    private int id;
    private String nombre;
    private String descripcion;
    private double precio;

    public Item(int id, String nombre, String descripcion, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }


    // Cada tipo de objeto sabe en qué lista del Inventario va ("doble despacho"):
    // así el Inventario no necesita preguntar de qué tipo es cada objeto con instanceof.
    public abstract void guardarEn(Inventario inventario);
    public abstract void sacarDe(Inventario inventario);
    public abstract boolean estaEn(Inventario inventario);

    // espacios de mochila que ocupa (solo los consumibles ocupan)
    public int getEspacios() {
        return 0;
    }

    // texto corto para las listas; los que tienen valores propios lo amplían
    public String resumen() {
        return nombre;
    }
}
