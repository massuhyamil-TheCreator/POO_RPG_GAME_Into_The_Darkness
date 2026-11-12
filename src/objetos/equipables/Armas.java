package objetos.equipables;
import objetos.Inventario;

public class Armas extends Equipables {

    private int danioFisico;
    private int danioMagico;
    // (merge) efecto especial de las armas del catálogo por clase; por ahora es solo dato
    private String efectoEspecial = "";
    private String condicionEfecto = "";
    private int valorEfecto, segundoValorEfecto, probabilidadEfecto, duracionEfecto;

    public Armas(int id, String nombre, String descripcion, double precio, String tipo, int danioFisico, int danioMagico) {
        super(id, nombre, descripcion, precio, tipo);
        this.danioFisico = danioFisico;
        this.danioMagico = danioMagico;
    }

    // Para las armas del catálogo por clase, sin precio hasta que se diseñe la economía.
    public Armas(int id, String nombre, String clase, int danioFisico, int danioMagico) {
        this(id, nombre, "Arma exclusiva de " + clase, -1, "Arma", danioFisico, danioMagico);
        setClasePermitida(clase);
        definirBonos(0, danioFisico, danioMagico, 0, 0, 0, 0, 0);
    }

    public void definirEfecto(String descripcion, String condicion, int valor,
                              int otroValor, int probabilidad, int duracion) {
        this.efectoEspecial = descripcion;
        this.condicionEfecto = condicion;
        this.valorEfecto = valor;
        this.segundoValorEfecto = otroValor;
        this.probabilidadEfecto = probabilidad;
        this.duracionEfecto = duracion;
    }

    public String getEfectoEspecial() { return efectoEspecial; }
    public String getCondicionEfecto() { return condicionEfecto; }
    public int getValorEfecto() { return valorEfecto; }
    public int getSegundoValorEfecto() { return segundoValorEfecto; }
    public int getProbabilidadEfecto() { return probabilidadEfecto; }
    public int getDuracionEfecto() { return duracionEfecto; }

    public int getDanioFisico() {
        return danioFisico;
    }

    public int getDanioMagico() {
        return danioMagico;
    }


    @Override
    public void guardarEn(Inventario inventario) { inventario.guardarArma(this); }

    @Override
    public void sacarDe(Inventario inventario) { inventario.sacarArma(this); }

    @Override
    public boolean estaEn(Inventario inventario) { return inventario.tieneArma(this); }

    @Override
    public String resumen() {
        return getNombre() + " (+" + danioFisico + " fís, +" + danioMagico + " mág)";
    }
}
