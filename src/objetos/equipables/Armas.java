public class Armas extends Equipables {

    private int danioFisico;
    private int danioMagico;

    public Armas(int id, String nombre, String descripcion, double precio, String tipo, int danioFisico, int danioMagico) {
        super(id, nombre, descripcion, precio, tipo);
        this.danioFisico = danioFisico;
        this.danioMagico = danioMagico;
    }

    public int getDanioFisico() {
        return danioFisico;
    }

    public void setDanioFisico(int danioFisico) {
        this.danioFisico = danioFisico;
    }

    public int getDanioMagico() {
        return danioMagico;
    }

    public void setDanioMagico(int danioMagico) {
        this.danioMagico = danioMagico;
    }
}
