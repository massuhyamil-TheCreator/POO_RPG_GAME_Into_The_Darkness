public class Armaduras extends Equipables {

    private int defensaFisica;
    private int defensaMagica;

    public Armaduras(int id, String nombre, String descripcion, double precio, String tipo, int defensaFisica, int defensaMagica) {
        super(id, nombre, descripcion, precio, tipo);
        this.defensaFisica = defensaFisica;
        this.defensaMagica = defensaMagica;
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
