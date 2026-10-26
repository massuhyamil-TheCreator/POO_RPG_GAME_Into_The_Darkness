package entidades;
/** Definición de un efecto: el motor futuro decide cuándo aplicarlo. */
public class Efecto {
    private String tipo;
    private double magnitud;
    private int duracion;
    private int probabilidad;
    private String detalle;

    public Efecto(String tipo, double magnitud, int duracion, int probabilidad, String detalle) {
        this.tipo = tipo;
        this.magnitud = magnitud;
        this.duracion = duracion;
        this.probabilidad = probabilidad;
        this.detalle = detalle;
    }
    public String getTipo() { return tipo; }
    public double getMagnitud() { return magnitud; }
    public int getDuracion() { return duracion; }
    public int getProbabilidad() { return probabilidad; }
    public String getDetalle() { return detalle; }
}
