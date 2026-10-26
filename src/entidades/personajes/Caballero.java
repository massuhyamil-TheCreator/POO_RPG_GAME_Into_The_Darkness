package entidades.personajes;

public class Caballero extends Personaje {
    public Caballero() { super("CABALLERO_SAGRADO"); }

    private int constitucion;

    public Caballero(String nombre, String categoria, int puntosDeSalud, int puntosDeSaludMaximos, int recurso, int recursoMaximo, int velocidad, int precision, int nivel, int experiencia, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int inventario, int habilidades, String atributoDistintivo, int constitucion) {
        super(nombre, categoria, puntosDeSalud, puntosDeSaludMaximos, recurso, recursoMaximo, velocidad, precision, nivel, experiencia, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica, inventario, habilidades, atributoDistintivo);
        this.constitucion = constitucion;
        asignarClase("CABALLERO_SAGRADO");
    }

    public int getConstitucion() {
        return constitucion;
    }

    public void setConstitucion(int constitucion) {
        this.constitucion = constitucion;
    }
}
