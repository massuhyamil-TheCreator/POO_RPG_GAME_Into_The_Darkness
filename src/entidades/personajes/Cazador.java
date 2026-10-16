public class Cazador extends Personaje {

    private int astucia;

    public Cazador(String nombre, String categoria, int puntosDeSalud, int puntosDeSaludMaximos, int recurso, int recursoMaximo, int velocidad, int precision, int nivel, int experiencia, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int inventario, int habilidades, String atributoDistintivo, int astucia) {
        super(nombre, categoria, puntosDeSalud, puntosDeSaludMaximos, recurso, recursoMaximo, velocidad, precision, nivel, experiencia, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica, inventario, habilidades, atributoDistintivo);
        this.astucia = astucia;
    }

    public int getAstucia() {
        return astucia;
    }

    public void setAstucia(int astucia) {
        this.astucia = astucia;
    }
}
