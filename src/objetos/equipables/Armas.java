package objetos.equipables;
public class Armas extends Equipables {
    private int danioFisico;
    private int danioMagico;
    private String efectoEspecial = "";
    private String condicionEfecto = "";
    private int valorEfecto, segundoValorEfecto, probabilidadEfecto, duracionEfecto;

    // Constructor original sin restricciones de clase.
    public Armas(int id, String nombre, String descripcion, double precio,
                 String tipo, int danioFisico, int danioMagico) {
        super(id, nombre, descripcion, precio, tipo);
        this.danioFisico=danioFisico;
        this.danioMagico=danioMagico;
    }
    // Para definir armas del catálogo, sin precio hasta que se diseñe la economía.
    public Armas(int id, String nombre, String clase, int danioFisico, int danioMagico) {
        this(id, nombre, "Arma exclusiva de " + clase, -1, "Arma", danioFisico, danioMagico);
        setClasePermitida(clase);
        definirBonos(0, danioFisico, danioMagico, 0, 0, 0, 0, 0);
    }
    public void definirEfecto(String descripcion, String condicion, int valor,
                              int otroValor, int probabilidad, int duracion) {
        this.efectoEspecial=descripcion;
        this.condicionEfecto=condicion;
        this.valorEfecto=valor;
        this.segundoValorEfecto=otroValor;
        this.probabilidadEfecto=probabilidad;
        this.duracionEfecto=duracion;
    }
    public String getEfectoEspecial(){return efectoEspecial;}
    public String getCondicionEfecto(){return condicionEfecto;}
    public int getValorEfecto(){return valorEfecto;}
    public int getSegundoValorEfecto(){return segundoValorEfecto;}
    public int getProbabilidadEfecto(){return probabilidadEfecto;}
    public int getDuracionEfecto(){return duracionEfecto;}
    public int getDanioFisico(){return danioFisico;}
    public void setDanioFisico(int danioFisico){this.danioFisico=danioFisico;}
    public int getDanioMagico(){return danioMagico;}
    public void setDanioMagico(int danioMagico){this.danioMagico=danioMagico;}
}
