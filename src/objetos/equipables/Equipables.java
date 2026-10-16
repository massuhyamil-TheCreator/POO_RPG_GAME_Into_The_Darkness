public abstract class Equipables extends Item {

    private String tipo;

    public Equipables(int id, String nombre, String descripcion, double precio, String tipo) {
        super(id, nombre, descripcion, precio);
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
