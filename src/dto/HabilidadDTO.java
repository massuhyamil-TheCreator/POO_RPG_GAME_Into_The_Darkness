package dto;

// Una habilidad lista para mostrar en el menú de combate.
public class HabilidadDTO {

    private final String nombre;
    private final int costo;
    private final String descripcion;
    private final boolean disponible;
    private final boolean eligeEnemigo;
    private final boolean eligeAliado;

    public HabilidadDTO(String nombre, int costo, String descripcion, boolean disponible, boolean eligeEnemigo, boolean eligeAliado) {
        this.nombre = nombre;
        this.costo = costo;
        this.descripcion = descripcion;
        this.disponible = disponible;
        this.eligeEnemigo = eligeEnemigo;
        this.eligeAliado = eligeAliado;
    }

    public String getNombre() { return nombre; }
    public int getCosto() { return costo; }
    public String getDescripcion() { return descripcion; }
    public boolean estaDisponible() { return disponible; }
    public boolean eligeEnemigo() { return eligeEnemigo; }
    public boolean eligeAliado() { return eligeAliado; }
}
