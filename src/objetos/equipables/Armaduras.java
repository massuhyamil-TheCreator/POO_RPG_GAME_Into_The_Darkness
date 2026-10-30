package objetos.equipables;
public class Armaduras extends Equipables {

    private int defensaFisica;
    private int defensaMagica;

    public Armaduras(int id, String nombre, String descripcion, double precio, String tipo, int defensaFisica, int defensaMagica) {
        super(id, nombre, descripcion, precio, tipo);
        this.defensaFisica = defensaFisica;
        this.defensaMagica = defensaMagica;
    }

    // Para las armaduras del catálogo por clase (CatalogoArmaduras).
    public Armaduras(int id, String nombre, String clase, int vida, int fisico,
                     int magico, int defensaFisica, int defensaMagica, int curacionPorcentaje) {
        this(id, nombre, "Armadura exclusiva de " + clase, -1, "Armadura", defensaFisica, defensaMagica);
        setClasePermitida(clase);
        definirBonos(vida, fisico, magico, defensaFisica, defensaMagica, 0, curacionPorcentaje, 0);
    }

    public int getDefensaFisica() {
        return defensaFisica;
    }

    public void setDefensaFisica(int defensaFisica) {
        this.defensaFisica = defensaFisica;
    }

    public int getDefensaMagica() {
        return defensaMagica;
    }

    public void setDefensaMagica(int defensaMagica) {
        this.defensaMagica = defensaMagica;
    }
}
