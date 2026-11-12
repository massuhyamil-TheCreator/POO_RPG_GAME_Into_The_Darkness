package dto;

// Un objeto del inventario o de la mochila. "indice" es su posición en su lista: así se lo nombra
// cuando se le pide una acción al controlador.
public class ItemDTO {

    private final int indice;
    private final String nombre;
    private final String descripcion;
    private final String resumen;
    private final int espacios;

    public ItemDTO(int indice, String nombre, String descripcion, String resumen, int espacios) {
        this.indice = indice;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.resumen = resumen;
        this.espacios = espacios;
    }

    public int getIndice() { return indice; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    // nombre con sus valores ("Espada larga (+2 fís, +0 mág)")
    public String getResumen() { return resumen; }
    public int getEspacios() { return espacios; }
}
