public class Caballero extends Personaje {

    private int constitucion;

    public Caballero(String nombre, String categoria, int puntosDeSalud, int puntosDeSaludMaximos, int recurso, int recursoMaximo, int velocidad, int precision, int nivel, int experiencia, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int inventario, int habilidades, String atributoDistintivo, int constitucion) {
        super(nombre, categoria, puntosDeSalud, puntosDeSaludMaximos, recurso, recursoMaximo, velocidad, precision, nivel, experiencia, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica, inventario, habilidades, atributoDistintivo);
        this.constitucion = constitucion;
    }

    public int getConstitucion() {
        return constitucion;
    }

    public void setConstitucion(int constitucion) {
        this.constitucion = constitucion;
    }
}
